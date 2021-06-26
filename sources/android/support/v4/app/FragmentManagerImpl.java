package android.support.v4.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class FragmentManagerImpl extends FragmentManager {
    static final Interpolator ACCELERATE_CUBIC = new AccelerateInterpolator(1.5f);
    static final Interpolator ACCELERATE_QUINT = new AccelerateInterpolator(2.5f);
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = HONEYCOMB;
    static final Interpolator DECELERATE_CUBIC = new DecelerateInterpolator(1.5f);
    static final Interpolator DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
    static final boolean HONEYCOMB;
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    ArrayList<Fragment> mActive;
    FragmentActivity mActivity;
    ArrayList<Fragment> mAdded;
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<Integer> mAvailIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState = 0;
    boolean mDestroyed;
    Runnable mExecCommit = new Runnable() {
        public void run() {
            FragmentManagerImpl.this.execPendingActions();
        }
    };
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    boolean mNeedMenuInvalidate;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList<Runnable> mPendingActions;
    SparseArray<Parcelable> mStateArray = null;
    Bundle mStateBundle = null;
    boolean mStateSaved;
    Runnable[] mTmpActions;

    static {
        boolean z = HONEYCOMB;
        if (Build.VERSION.SDK_INT >= 11) {
            z = true;
        }
        HONEYCOMB = z;
    }

    FragmentManagerImpl() {
    }

    private void checkStateLoss() {
        if (this.mStateSaved) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        } else if (this.mNoTransactionsBecause != null) {
            throw new IllegalStateException("Can not perform this action inside of " + this.mNoTransactionsBecause);
        }
    }

    static Animation makeFadeAnimation(Context context, float f, float f2) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(f, f2);
        alphaAnimation.setInterpolator(DECELERATE_CUBIC);
        alphaAnimation.setDuration(220);
        return alphaAnimation;
    }

    static Animation makeOpenCloseAnimation(Context context, float f, float f2, float f3, float f4) {
        AnimationSet animationSet = new AnimationSet(HONEYCOMB);
        ScaleAnimation scaleAnimation = new ScaleAnimation(f, f2, f, f2, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setInterpolator(DECELERATE_QUINT);
        scaleAnimation.setDuration(220);
        animationSet.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(f3, f4);
        alphaAnimation.setInterpolator(DECELERATE_CUBIC);
        alphaAnimation.setDuration(220);
        animationSet.addAnimation(alphaAnimation);
        return animationSet;
    }

    public static int reverseTransit(int i) {
        switch (i) {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN /*4097*/:
                return FragmentTransaction.TRANSIT_FRAGMENT_CLOSE;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE /*4099*/:
                return FragmentTransaction.TRANSIT_FRAGMENT_FADE;
            case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE /*8194*/:
                return FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
            default:
                return 0;
        }
    }

    private void throwException(RuntimeException runtimeException) {
        Log.e(TAG, runtimeException.getMessage());
        Log.e(TAG, "Activity state:");
        PrintWriter printWriter = new PrintWriter(new LogWriter(TAG));
        if (this.mActivity != null) {
            try {
                this.mActivity.dump("  ", (FileDescriptor) null, printWriter, new String[0]);
            } catch (Exception e) {
                Log.e(TAG, "Failed dumping state", e);
            }
        } else {
            try {
                dump("  ", (FileDescriptor) null, printWriter, new String[0]);
            } catch (Exception e2) {
                Log.e(TAG, "Failed dumping state", e2);
            }
        }
        throw runtimeException;
    }

    public static int transitToStyleIndex(int i, boolean z) {
        switch (i) {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN /*4097*/:
                return z ? 1 : 2;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE /*4099*/:
                return z ? 5 : 6;
            case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE /*8194*/:
                return z ? 3 : 4;
            default:
                return -1;
        }
    }

    /* access modifiers changed from: package-private */
    public void addBackStackState(BackStackRecord backStackRecord) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList<>();
        }
        this.mBackStack.add(backStackRecord);
        reportBackStackChanged();
    }

    public void addFragment(Fragment fragment, boolean z) {
        if (this.mAdded == null) {
            this.mAdded = new ArrayList<>();
        }
        if (DEBUG) {
            Log.v(TAG, "add: " + fragment);
        }
        makeActive(fragment);
        if (fragment.mDetached) {
            return;
        }
        if (this.mAdded.contains(fragment)) {
            throw new IllegalStateException("Fragment already added: " + fragment);
        }
        this.mAdded.add(fragment);
        fragment.mAdded = true;
        fragment.mRemoving = HONEYCOMB;
        if (fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        if (z) {
            moveToState(fragment);
        }
    }

    public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener onBackStackChangedListener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList<>();
        }
        this.mBackStackChangeListeners.add(onBackStackChangedListener);
    }

    public int allocBackStackIndex(BackStackRecord backStackRecord) {
        int i;
        synchronized (this) {
            if (this.mAvailBackStackIndices == null || this.mAvailBackStackIndices.size() <= 0) {
                if (this.mBackStackIndices == null) {
                    this.mBackStackIndices = new ArrayList<>();
                }
                i = this.mBackStackIndices.size();
                if (DEBUG) {
                    Log.v(TAG, "Setting back stack index " + i + " to " + backStackRecord);
                }
                this.mBackStackIndices.add(backStackRecord);
            } else {
                i = this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1).intValue();
                if (DEBUG) {
                    Log.v(TAG, "Adding back stack index " + i + " with " + backStackRecord);
                }
                this.mBackStackIndices.set(i, backStackRecord);
            }
        }
        return i;
    }

    public void attachActivity(FragmentActivity fragmentActivity, FragmentContainer fragmentContainer, Fragment fragment) {
        if (this.mActivity != null) {
            throw new IllegalStateException("Already attached");
        }
        this.mActivity = fragmentActivity;
        this.mContainer = fragmentContainer;
        this.mParent = fragment;
    }

    public void attachFragment(Fragment fragment, int i, int i2) {
        if (DEBUG) {
            Log.v(TAG, "attach: " + fragment);
        }
        if (fragment.mDetached) {
            fragment.mDetached = HONEYCOMB;
            if (!fragment.mAdded) {
                if (this.mAdded == null) {
                    this.mAdded = new ArrayList<>();
                }
                if (this.mAdded.contains(fragment)) {
                    throw new IllegalStateException("Fragment already added: " + fragment);
                }
                if (DEBUG) {
                    Log.v(TAG, "add from attach: " + fragment);
                }
                this.mAdded.add(fragment);
                fragment.mAdded = true;
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                moveToState(fragment, this.mCurState, i, i2, HONEYCOMB);
            }
        }
    }

    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    public void detachFragment(Fragment fragment, int i, int i2) {
        if (DEBUG) {
            Log.v(TAG, "detach: " + fragment);
        }
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                if (this.mAdded != null) {
                    if (DEBUG) {
                        Log.v(TAG, "remove from detach: " + fragment);
                    }
                    this.mAdded.remove(fragment);
                }
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                fragment.mAdded = HONEYCOMB;
                moveToState(fragment, 1, i, i2, HONEYCOMB);
            }
        }
    }

    public void dispatchActivityCreated() {
        this.mStateSaved = HONEYCOMB;
        moveToState(2, HONEYCOMB);
    }

    public void dispatchConfigurationChanged(Configuration configuration) {
        if (this.mAdded != null) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < this.mAdded.size()) {
                    Fragment fragment = this.mAdded.get(i2);
                    if (fragment != null) {
                        fragment.performConfigurationChanged(configuration);
                    }
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }
    }

    public boolean dispatchContextItemSelected(MenuItem menuItem) {
        if (this.mAdded == null) {
            return HONEYCOMB;
        }
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment != null && fragment.performContextItemSelected(menuItem)) {
                return true;
            }
        }
        return HONEYCOMB;
    }

    public void dispatchCreate() {
        this.mStateSaved = HONEYCOMB;
        moveToState(1, HONEYCOMB);
    }

    public boolean dispatchCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        boolean z;
        ArrayList<Fragment> arrayList = null;
        if (this.mAdded != null) {
            int i = 0;
            z = false;
            while (i < this.mAdded.size()) {
                Fragment fragment = this.mAdded.get(i);
                if (fragment != null && fragment.performCreateOptionsMenu(menu, menuInflater)) {
                    z = true;
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                    }
                    arrayList.add(fragment);
                }
                i++;
                z = z;
            }
        } else {
            z = false;
        }
        if (this.mCreatedMenus != null) {
            for (int i2 = 0; i2 < this.mCreatedMenus.size(); i2++) {
                Fragment fragment2 = this.mCreatedMenus.get(i2);
                if (arrayList == null || !arrayList.contains(fragment2)) {
                    fragment2.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = arrayList;
        return z;
    }

    public void dispatchDestroy() {
        this.mDestroyed = true;
        execPendingActions();
        moveToState(0, HONEYCOMB);
        this.mActivity = null;
        this.mContainer = null;
        this.mParent = null;
    }

    public void dispatchDestroyView() {
        moveToState(1, HONEYCOMB);
    }

    public void dispatchLowMemory() {
        if (this.mAdded != null) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < this.mAdded.size()) {
                    Fragment fragment = this.mAdded.get(i2);
                    if (fragment != null) {
                        fragment.performLowMemory();
                    }
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }
    }

    public boolean dispatchOptionsItemSelected(MenuItem menuItem) {
        if (this.mAdded == null) {
            return HONEYCOMB;
        }
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment != null && fragment.performOptionsItemSelected(menuItem)) {
                return true;
            }
        }
        return HONEYCOMB;
    }

    public void dispatchOptionsMenuClosed(Menu menu) {
        if (this.mAdded != null) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < this.mAdded.size()) {
                    Fragment fragment = this.mAdded.get(i2);
                    if (fragment != null) {
                        fragment.performOptionsMenuClosed(menu);
                    }
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }
    }

    public void dispatchPause() {
        moveToState(4, HONEYCOMB);
    }

    public boolean dispatchPrepareOptionsMenu(Menu menu) {
        if (this.mAdded == null) {
            return HONEYCOMB;
        }
        boolean z = false;
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment != null && fragment.performPrepareOptionsMenu(menu)) {
                z = true;
            }
        }
        return z;
    }

    public void dispatchReallyStop() {
        moveToState(2, HONEYCOMB);
    }

    public void dispatchResume() {
        this.mStateSaved = HONEYCOMB;
        moveToState(5, HONEYCOMB);
    }

    public void dispatchStart() {
        this.mStateSaved = HONEYCOMB;
        moveToState(4, HONEYCOMB);
    }

    public void dispatchStop() {
        this.mStateSaved = true;
        moveToState(3, HONEYCOMB);
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        int size;
        int size2;
        int size3;
        int size4;
        int size5;
        int size6;
        String str2 = str + "    ";
        if (this.mActive != null && (size6 = this.mActive.size()) > 0) {
            printWriter.print(str);
            printWriter.print("Active Fragments in ");
            printWriter.print(Integer.toHexString(System.identityHashCode(this)));
            printWriter.println(":");
            for (int i = 0; i < size6; i++) {
                Fragment fragment = this.mActive.get(i);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i);
                printWriter.print(": ");
                printWriter.println(fragment);
                if (fragment != null) {
                    fragment.dump(str2, fileDescriptor, printWriter, strArr);
                }
            }
        }
        if (this.mAdded != null && (size5 = this.mAdded.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Added Fragments:");
            for (int i2 = 0; i2 < size5; i2++) {
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i2);
                printWriter.print(": ");
                printWriter.println(this.mAdded.get(i2).toString());
            }
        }
        if (this.mCreatedMenus != null && (size4 = this.mCreatedMenus.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Fragments Created Menus:");
            for (int i3 = 0; i3 < size4; i3++) {
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i3);
                printWriter.print(": ");
                printWriter.println(this.mCreatedMenus.get(i3).toString());
            }
        }
        if (this.mBackStack != null && (size3 = this.mBackStack.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Back Stack:");
            for (int i4 = 0; i4 < size3; i4++) {
                BackStackRecord backStackRecord = this.mBackStack.get(i4);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i4);
                printWriter.print(": ");
                printWriter.println(backStackRecord.toString());
                backStackRecord.dump(str2, fileDescriptor, printWriter, strArr);
            }
        }
        synchronized (this) {
            if (this.mBackStackIndices != null && (size2 = this.mBackStackIndices.size()) > 0) {
                printWriter.print(str);
                printWriter.println("Back Stack Indices:");
                for (int i5 = 0; i5 < size2; i5++) {
                    printWriter.print(str);
                    printWriter.print("  #");
                    printWriter.print(i5);
                    printWriter.print(": ");
                    printWriter.println(this.mBackStackIndices.get(i5));
                }
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                printWriter.print(str);
                printWriter.print("mAvailBackStackIndices: ");
                printWriter.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
        }
        if (this.mPendingActions != null && (size = this.mPendingActions.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Pending Actions:");
            for (int i6 = 0; i6 < size; i6++) {
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i6);
                printWriter.print(": ");
                printWriter.println(this.mPendingActions.get(i6));
            }
        }
        printWriter.print(str);
        printWriter.println("FragmentManager misc state:");
        printWriter.print(str);
        printWriter.print("  mActivity=");
        printWriter.println(this.mActivity);
        printWriter.print(str);
        printWriter.print("  mContainer=");
        printWriter.println(this.mContainer);
        if (this.mParent != null) {
            printWriter.print(str);
            printWriter.print("  mParent=");
            printWriter.println(this.mParent);
        }
        printWriter.print(str);
        printWriter.print("  mCurState=");
        printWriter.print(this.mCurState);
        printWriter.print(" mStateSaved=");
        printWriter.print(this.mStateSaved);
        printWriter.print(" mDestroyed=");
        printWriter.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            printWriter.print(str);
            printWriter.print("  mNeedMenuInvalidate=");
            printWriter.println(this.mNeedMenuInvalidate);
        }
        if (this.mNoTransactionsBecause != null) {
            printWriter.print(str);
            printWriter.print("  mNoTransactionsBecause=");
            printWriter.println(this.mNoTransactionsBecause);
        }
        if (this.mAvailIndices != null && this.mAvailIndices.size() > 0) {
            printWriter.print(str);
            printWriter.print("  mAvailIndices: ");
            printWriter.println(Arrays.toString(this.mAvailIndices.toArray()));
        }
    }

    public void enqueueAction(Runnable runnable, boolean z) {
        if (!z) {
            checkStateLoss();
        }
        synchronized (this) {
            if (this.mDestroyed || this.mActivity == null) {
                throw new IllegalStateException("Activity has been destroyed");
            }
            if (this.mPendingActions == null) {
                this.mPendingActions = new ArrayList<>();
            }
            this.mPendingActions.add(runnable);
            if (this.mPendingActions.size() == 1) {
                this.mActivity.mHandler.removeCallbacks(this.mExecCommit);
                this.mActivity.mHandler.post(this.mExecCommit);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0083, code lost:
        r6.mExecutingActions = true;
        r1 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0086, code lost:
        if (r1 >= r3) goto L_0x009a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0088, code lost:
        r6.mTmpActions[r1].run();
        r6.mTmpActions[r1] = null;
        r1 = r1 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean execPendingActions() {
        /*
            r6 = this;
            r0 = 1
            r2 = 0
            boolean r1 = r6.mExecutingActions
            if (r1 == 0) goto L_0x000e
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "Recursive entry to executePendingTransactions"
            r0.<init>(r1)
            throw r0
        L_0x000e:
            android.os.Looper r1 = android.os.Looper.myLooper()
            android.support.v4.app.FragmentActivity r3 = r6.mActivity
            android.os.Handler r3 = r3.mHandler
            android.os.Looper r3 = r3.getLooper()
            if (r1 == r3) goto L_0x0024
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "Must be called from main thread of process"
            r0.<init>(r1)
            throw r0
        L_0x0024:
            r1 = r2
        L_0x0025:
            monitor-enter(r6)
            java.util.ArrayList<java.lang.Runnable> r3 = r6.mPendingActions     // Catch:{ all -> 0x0097 }
            if (r3 == 0) goto L_0x0032
            java.util.ArrayList<java.lang.Runnable> r3 = r6.mPendingActions     // Catch:{ all -> 0x0097 }
            int r3 = r3.size()     // Catch:{ all -> 0x0097 }
            if (r3 != 0) goto L_0x005a
        L_0x0032:
            monitor-exit(r6)     // Catch:{ all -> 0x0097 }
            boolean r0 = r6.mHavePendingDeferredStart
            if (r0 == 0) goto L_0x00a5
            r3 = r2
            r4 = r2
        L_0x0039:
            java.util.ArrayList<android.support.v4.app.Fragment> r0 = r6.mActive
            int r0 = r0.size()
            if (r3 >= r0) goto L_0x009e
            java.util.ArrayList<android.support.v4.app.Fragment> r0 = r6.mActive
            java.lang.Object r0 = r0.get(r3)
            android.support.v4.app.Fragment r0 = (android.support.v4.app.Fragment) r0
            if (r0 == 0) goto L_0x0056
            android.support.v4.app.LoaderManagerImpl r5 = r0.mLoaderManager
            if (r5 == 0) goto L_0x0056
            android.support.v4.app.LoaderManagerImpl r0 = r0.mLoaderManager
            boolean r0 = r0.hasRunningLoaders()
            r4 = r4 | r0
        L_0x0056:
            int r0 = r3 + 1
            r3 = r0
            goto L_0x0039
        L_0x005a:
            java.util.ArrayList<java.lang.Runnable> r1 = r6.mPendingActions     // Catch:{ all -> 0x0097 }
            int r3 = r1.size()     // Catch:{ all -> 0x0097 }
            java.lang.Runnable[] r1 = r6.mTmpActions     // Catch:{ all -> 0x0097 }
            if (r1 == 0) goto L_0x0069
            java.lang.Runnable[] r1 = r6.mTmpActions     // Catch:{ all -> 0x0097 }
            int r1 = r1.length     // Catch:{ all -> 0x0097 }
            if (r1 >= r3) goto L_0x006d
        L_0x0069:
            java.lang.Runnable[] r1 = new java.lang.Runnable[r3]     // Catch:{ all -> 0x0097 }
            r6.mTmpActions = r1     // Catch:{ all -> 0x0097 }
        L_0x006d:
            java.util.ArrayList<java.lang.Runnable> r1 = r6.mPendingActions     // Catch:{ all -> 0x0097 }
            java.lang.Runnable[] r4 = r6.mTmpActions     // Catch:{ all -> 0x0097 }
            r1.toArray(r4)     // Catch:{ all -> 0x0097 }
            java.util.ArrayList<java.lang.Runnable> r1 = r6.mPendingActions     // Catch:{ all -> 0x0097 }
            r1.clear()     // Catch:{ all -> 0x0097 }
            android.support.v4.app.FragmentActivity r1 = r6.mActivity     // Catch:{ all -> 0x0097 }
            android.os.Handler r1 = r1.mHandler     // Catch:{ all -> 0x0097 }
            java.lang.Runnable r4 = r6.mExecCommit     // Catch:{ all -> 0x0097 }
            r1.removeCallbacks(r4)     // Catch:{ all -> 0x0097 }
            monitor-exit(r6)     // Catch:{ all -> 0x0097 }
            r6.mExecutingActions = r0
            r1 = r2
        L_0x0086:
            if (r1 >= r3) goto L_0x009a
            java.lang.Runnable[] r4 = r6.mTmpActions
            r4 = r4[r1]
            r4.run()
            java.lang.Runnable[] r4 = r6.mTmpActions
            r5 = 0
            r4[r1] = r5
            int r1 = r1 + 1
            goto L_0x0086
        L_0x0097:
            r0 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0097 }
            throw r0
        L_0x009a:
            r6.mExecutingActions = r2
            r1 = r0
            goto L_0x0025
        L_0x009e:
            if (r4 != 0) goto L_0x00a5
            r6.mHavePendingDeferredStart = r2
            r6.startPendingDeferredFragments()
        L_0x00a5:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.execPendingActions():boolean");
    }

    public boolean executePendingTransactions() {
        return execPendingActions();
    }

    public Fragment findFragmentById(int i) {
        if (this.mAdded != null) {
            for (int size = this.mAdded.size() - 1; size >= 0; size--) {
                Fragment fragment = this.mAdded.get(size);
                if (fragment != null && fragment.mFragmentId == i) {
                    return fragment;
                }
            }
        }
        if (this.mActive != null) {
            for (int size2 = this.mActive.size() - 1; size2 >= 0; size2--) {
                Fragment fragment2 = this.mActive.get(size2);
                if (fragment2 != null && fragment2.mFragmentId == i) {
                    return fragment2;
                }
            }
        }
        return null;
    }

    public Fragment findFragmentByTag(String str) {
        if (!(this.mAdded == null || str == null)) {
            for (int size = this.mAdded.size() - 1; size >= 0; size--) {
                Fragment fragment = this.mAdded.get(size);
                if (fragment != null && str.equals(fragment.mTag)) {
                    return fragment;
                }
            }
        }
        if (!(this.mActive == null || str == null)) {
            for (int size2 = this.mActive.size() - 1; size2 >= 0; size2--) {
                Fragment fragment2 = this.mActive.get(size2);
                if (fragment2 != null && str.equals(fragment2.mTag)) {
                    return fragment2;
                }
            }
        }
        return null;
    }

    public Fragment findFragmentByWho(String str) {
        Fragment findFragmentByWho;
        if (!(this.mActive == null || str == null)) {
            for (int size = this.mActive.size() - 1; size >= 0; size--) {
                Fragment fragment = this.mActive.get(size);
                if (fragment != null && (findFragmentByWho = fragment.findFragmentByWho(str)) != null) {
                    return findFragmentByWho;
                }
            }
        }
        return null;
    }

    public void freeBackStackIndex(int i) {
        synchronized (this) {
            this.mBackStackIndices.set(i, (Object) null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList<>();
            }
            if (DEBUG) {
                Log.v(TAG, "Freeing back stack index " + i);
            }
            this.mAvailBackStackIndices.add(Integer.valueOf(i));
        }
    }

    public FragmentManager.BackStackEntry getBackStackEntryAt(int i) {
        return this.mBackStack.get(i);
    }

    public int getBackStackEntryCount() {
        if (this.mBackStack != null) {
            return this.mBackStack.size();
        }
        return 0;
    }

    public Fragment getFragment(Bundle bundle, String str) {
        int i = bundle.getInt(str, -1);
        if (i == -1) {
            return null;
        }
        if (i >= this.mActive.size()) {
            throwException(new IllegalStateException("Fragment no longer exists for key " + str + ": index " + i));
        }
        Fragment fragment = this.mActive.get(i);
        if (fragment != null) {
            return fragment;
        }
        throwException(new IllegalStateException("Fragment no longer exists for key " + str + ": index " + i));
        return fragment;
    }

    public List<Fragment> getFragments() {
        return this.mActive;
    }

    public void hideFragment(Fragment fragment, int i, int i2) {
        if (DEBUG) {
            Log.v(TAG, "hide: " + fragment);
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            if (fragment.mView != null) {
                Animation loadAnimation = loadAnimation(fragment, i, HONEYCOMB, i2);
                if (loadAnimation != null) {
                    fragment.mView.startAnimation(loadAnimation);
                }
                fragment.mView.setVisibility(8);
            }
            if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.onHiddenChanged(true);
        }
    }

    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    /* access modifiers changed from: package-private */
    public Animation loadAnimation(Fragment fragment, int i, boolean z, int i2) {
        Animation loadAnimation;
        Animation onCreateAnimation = fragment.onCreateAnimation(i, z, fragment.mNextAnim);
        if (onCreateAnimation != null) {
            return onCreateAnimation;
        }
        if (fragment.mNextAnim != 0 && (loadAnimation = AnimationUtils.loadAnimation(this.mActivity, fragment.mNextAnim)) != null) {
            return loadAnimation;
        }
        if (i == 0) {
            return null;
        }
        int transitToStyleIndex = transitToStyleIndex(i, z);
        if (transitToStyleIndex < 0) {
            return null;
        }
        switch (transitToStyleIndex) {
            case 1:
                return makeOpenCloseAnimation(this.mActivity, 1.125f, 1.0f, 0.0f, 1.0f);
            case 2:
                return makeOpenCloseAnimation(this.mActivity, 1.0f, 0.975f, 1.0f, 0.0f);
            case 3:
                return makeOpenCloseAnimation(this.mActivity, 0.975f, 1.0f, 0.0f, 1.0f);
            case 4:
                return makeOpenCloseAnimation(this.mActivity, 1.0f, 1.075f, 1.0f, 0.0f);
            case 5:
                return makeFadeAnimation(this.mActivity, 0.0f, 1.0f);
            case 6:
                return makeFadeAnimation(this.mActivity, 1.0f, 0.0f);
            default:
                if (i2 == 0 && this.mActivity.getWindow() != null) {
                    i2 = this.mActivity.getWindow().getAttributes().windowAnimations;
                }
                return i2 == 0 ? null : null;
        }
    }

    /* access modifiers changed from: package-private */
    public void makeActive(Fragment fragment) {
        if (fragment.mIndex < 0) {
            if (this.mAvailIndices == null || this.mAvailIndices.size() <= 0) {
                if (this.mActive == null) {
                    this.mActive = new ArrayList<>();
                }
                fragment.setIndex(this.mActive.size(), this.mParent);
                this.mActive.add(fragment);
            } else {
                fragment.setIndex(this.mAvailIndices.remove(this.mAvailIndices.size() - 1).intValue(), this.mParent);
                this.mActive.set(fragment.mIndex, fragment);
            }
            if (DEBUG) {
                Log.v(TAG, "Allocated fragment index " + fragment);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void makeInactive(Fragment fragment) {
        if (fragment.mIndex >= 0) {
            if (DEBUG) {
                Log.v(TAG, "Freeing fragment index " + fragment);
            }
            this.mActive.set(fragment.mIndex, (Object) null);
            if (this.mAvailIndices == null) {
                this.mAvailIndices = new ArrayList<>();
            }
            this.mAvailIndices.add(Integer.valueOf(fragment.mIndex));
            this.mActivity.invalidateSupportFragment(fragment.mWho);
            fragment.initState();
        }
    }

    /* access modifiers changed from: package-private */
    public void moveToState(int i, int i2, int i3, boolean z) {
        if (this.mActivity == null && i != 0) {
            throw new IllegalStateException("No activity");
        } else if (z || this.mCurState != i) {
            this.mCurState = i;
            if (this.mActive != null) {
                int i4 = 0;
                boolean z2 = false;
                while (i4 < this.mActive.size()) {
                    Fragment fragment = this.mActive.get(i4);
                    if (fragment != null) {
                        moveToState(fragment, i, i2, i3, HONEYCOMB);
                        if (fragment.mLoaderManager != null) {
                            z2 |= fragment.mLoaderManager.hasRunningLoaders();
                        }
                    }
                    i4++;
                    z2 = z2;
                }
                if (!z2) {
                    startPendingDeferredFragments();
                }
                if (this.mNeedMenuInvalidate && this.mActivity != null && this.mCurState == 5) {
                    this.mActivity.supportInvalidateOptionsMenu();
                    this.mNeedMenuInvalidate = HONEYCOMB;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void moveToState(int i, boolean z) {
        moveToState(i, 0, 0, z);
    }

    /* access modifiers changed from: package-private */
    public void moveToState(Fragment fragment) {
        moveToState(fragment, this.mCurState, 0, 0, HONEYCOMB);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:100:0x0214, code lost:
        if (DEBUG == false) goto L_0x022e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:101:0x0216, code lost:
        android.util.Log.v(TAG, "moveto RESUMED: " + r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x022e, code lost:
        r10.mResumed = true;
        r10.performResume();
        r10.mSavedFragmentState = null;
        r10.mSavedViewState = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x023d, code lost:
        r10.mInnerView = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x024b, code lost:
        if (r11 >= 1) goto L_0x0045;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x024f, code lost:
        if (r9.mDestroyed == false) goto L_0x025c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:113:0x0253, code lost:
        if (r10.mAnimatingAway == null) goto L_0x025c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x0255, code lost:
        r0 = r10.mAnimatingAway;
        r10.mAnimatingAway = null;
        r0.clearAnimation();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:116:0x025e, code lost:
        if (r10.mAnimatingAway == null) goto L_0x0338;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:117:0x0260, code lost:
        r10.mStateAfterAnimating = r11;
        r11 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:124:0x0289, code lost:
        if (r11 >= 4) goto L_0x02aa;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:126:0x028d, code lost:
        if (DEBUG == false) goto L_0x02a7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:127:0x028f, code lost:
        android.util.Log.v(TAG, "movefrom STARTED: " + r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:128:0x02a7, code lost:
        r10.performStop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:129:0x02aa, code lost:
        if (r11 >= 3) goto L_0x02cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:131:0x02ae, code lost:
        if (DEBUG == false) goto L_0x02c8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x02b0, code lost:
        android.util.Log.v(TAG, "movefrom STOPPED: " + r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:133:0x02c8, code lost:
        r10.performReallyStop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:135:0x02cc, code lost:
        if (r11 >= 2) goto L_0x024b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:137:0x02d0, code lost:
        if (DEBUG == false) goto L_0x02ea;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:138:0x02d2, code lost:
        android.util.Log.v(TAG, "movefrom ACTIVITY_CREATED: " + r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:140:0x02ec, code lost:
        if (r10.mView == null) goto L_0x02fd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x02f4, code lost:
        if (r9.mActivity.isFinishing() != false) goto L_0x02fd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:144:0x02f8, code lost:
        if (r10.mSavedViewState != null) goto L_0x02fd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:145:0x02fa, code lost:
        saveFragmentViewState(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:146:0x02fd, code lost:
        r10.performDestroyView();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x0302, code lost:
        if (r10.mView == null) goto L_0x0330;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:149:0x0306, code lost:
        if (r10.mContainer == null) goto L_0x0330;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:0x030a, code lost:
        if (r9.mCurState <= 0) goto L_0x0398;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:153:0x030e, code lost:
        if (r9.mDestroyed != false) goto L_0x0398;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:154:0x0310, code lost:
        r0 = loadAnimation(r10, r12, HONEYCOMB, r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:155:0x0314, code lost:
        if (r0 == null) goto L_0x0329;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:156:0x0316, code lost:
        r10.mAnimatingAway = r10.mView;
        r10.mStateAfterAnimating = r11;
        r0.setAnimationListener(new android.support.v4.app.FragmentManagerImpl.AnonymousClass5(r9));
        r10.mView.startAnimation(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:157:0x0329, code lost:
        r10.mContainer.removeView(r10.mView);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:158:0x0330, code lost:
        r10.mContainer = null;
        r10.mView = null;
        r10.mInnerView = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:160:0x033a, code lost:
        if (DEBUG == false) goto L_0x0354;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:161:0x033c, code lost:
        android.util.Log.v(TAG, "movefrom CREATED: " + r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:163:0x0356, code lost:
        if (r10.mRetaining != false) goto L_0x035b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:164:0x0358, code lost:
        r10.performDestroy();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:165:0x035b, code lost:
        r10.mCalled = HONEYCOMB;
        r10.onDetach();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:166:0x0362, code lost:
        if (r10.mCalled != false) goto L_0x0383;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:168:0x0382, code lost:
        throw new android.support.v4.app.SuperNotCalledException("Fragment " + r10 + " did not call through to super.onDetach()");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:169:0x0383, code lost:
        if (r14 != false) goto L_0x0045;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:171:0x0387, code lost:
        if (r10.mRetaining != false) goto L_0x038e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:172:0x0389, code lost:
        makeInactive(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:173:0x038e, code lost:
        r10.mActivity = null;
        r10.mParentFragment = null;
        r10.mFragmentManager = null;
        r10.mChildFragmentManager = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:174:0x0398, code lost:
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:175:0x039b, code lost:
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0126, code lost:
        if (r11 <= 1) goto L_0x01ef;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x012a, code lost:
        if (DEBUG == false) goto L_0x0144;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x012c, code lost:
        android.util.Log.v(TAG, "moveto ACTIVITY_CREATED: " + r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x0146, code lost:
        if (r10.mFromLayout != false) goto L_0x01df;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x014a, code lost:
        if (r10.mContainerId == 0) goto L_0x039b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x014c, code lost:
        r0 = (android.view.ViewGroup) r9.mContainer.findViewById(r10.mContainerId);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0156, code lost:
        if (r0 != null) goto L_0x019b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x015a, code lost:
        if (r10.mRestored != false) goto L_0x019b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x015c, code lost:
        throwException(new java.lang.IllegalArgumentException("No view found for id 0x" + java.lang.Integer.toHexString(r10.mContainerId) + " (" + r10.getResources().getResourceName(r10.mContainerId) + ") for fragment " + r10));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x019b, code lost:
        r10.mContainer = r0;
        r10.mView = r10.performCreateView(r10.getLayoutInflater(r10.mSavedFragmentState), r0, r10.mSavedFragmentState);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x01ad, code lost:
        if (r10.mView == null) goto L_0x023d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x01af, code lost:
        r10.mInnerView = r10.mView;
        r10.mView = android.support.v4.app.NoSaveStateFrameLayout.wrap(r10.mView);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x01bb, code lost:
        if (r0 == null) goto L_0x01cd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x01bd, code lost:
        r1 = loadAnimation(r10, r12, true, r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x01c1, code lost:
        if (r1 == null) goto L_0x01c8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x01c3, code lost:
        r10.mView.startAnimation(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x01c8, code lost:
        r0.addView(r10.mView);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x01cf, code lost:
        if (r10.mHidden == false) goto L_0x01d8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x01d1, code lost:
        r10.mView.setVisibility(8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x01d8, code lost:
        r10.onViewCreated(r10.mView, r10.mSavedFragmentState);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x01df, code lost:
        r10.performActivityCreated(r10.mSavedFragmentState);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x01e6, code lost:
        if (r10.mView == null) goto L_0x01ed;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x01e8, code lost:
        r10.restoreViewState(r10.mSavedFragmentState);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x01ed, code lost:
        r10.mSavedFragmentState = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x01ef, code lost:
        if (r11 <= 3) goto L_0x0210;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x01f3, code lost:
        if (DEBUG == false) goto L_0x020d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x01f5, code lost:
        android.util.Log.v(TAG, "moveto STARTED: " + r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x020d, code lost:
        r10.performStart();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x0210, code lost:
        if (r11 <= 4) goto L_0x0045;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void moveToState(final android.support.v4.app.Fragment r10, int r11, int r12, int r13, boolean r14) {
        /*
            r9 = this;
            r8 = 4
            r6 = 3
            r3 = 0
            r5 = 1
            r7 = 0
            boolean r0 = r10.mAdded
            if (r0 == 0) goto L_0x000d
            boolean r0 = r10.mDetached
            if (r0 == 0) goto L_0x0010
        L_0x000d:
            if (r11 <= r5) goto L_0x0010
            r11 = r5
        L_0x0010:
            boolean r0 = r10.mRemoving
            if (r0 == 0) goto L_0x001a
            int r0 = r10.mState
            if (r11 <= r0) goto L_0x001a
            int r11 = r10.mState
        L_0x001a:
            boolean r0 = r10.mDeferStart
            if (r0 == 0) goto L_0x0025
            int r0 = r10.mState
            if (r0 >= r8) goto L_0x0025
            if (r11 <= r6) goto L_0x0025
            r11 = r6
        L_0x0025:
            int r0 = r10.mState
            if (r0 >= r11) goto L_0x0240
            boolean r0 = r10.mFromLayout
            if (r0 == 0) goto L_0x0032
            boolean r0 = r10.mInLayout
            if (r0 != 0) goto L_0x0032
        L_0x0031:
            return
        L_0x0032:
            android.view.View r0 = r10.mAnimatingAway
            if (r0 == 0) goto L_0x0040
            r10.mAnimatingAway = r7
            int r2 = r10.mStateAfterAnimating
            r0 = r9
            r1 = r10
            r4 = r3
            r0.moveToState(r1, r2, r3, r4, r5)
        L_0x0040:
            int r0 = r10.mState
            switch(r0) {
                case 0: goto L_0x0048;
                case 1: goto L_0x0126;
                case 2: goto L_0x01ef;
                case 3: goto L_0x01ef;
                case 4: goto L_0x0210;
                default: goto L_0x0045;
            }
        L_0x0045:
            r10.mState = r11
            goto L_0x0031
        L_0x0048:
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x0064
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "moveto CREATED: "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r10)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x0064:
            android.os.Bundle r0 = r10.mSavedFragmentState
            if (r0 == 0) goto L_0x009d
            android.os.Bundle r0 = r10.mSavedFragmentState
            java.lang.String r1 = "android:view_state"
            android.util.SparseArray r0 = r0.getSparseParcelableArray(r1)
            r10.mSavedViewState = r0
            android.os.Bundle r0 = r10.mSavedFragmentState
            java.lang.String r1 = "android:target_state"
            android.support.v4.app.Fragment r0 = r9.getFragment(r0, r1)
            r10.mTarget = r0
            android.support.v4.app.Fragment r0 = r10.mTarget
            if (r0 == 0) goto L_0x008a
            android.os.Bundle r0 = r10.mSavedFragmentState
            java.lang.String r1 = "android:target_req_state"
            int r0 = r0.getInt(r1, r3)
            r10.mTargetRequestCode = r0
        L_0x008a:
            android.os.Bundle r0 = r10.mSavedFragmentState
            java.lang.String r1 = "android:user_visible_hint"
            boolean r0 = r0.getBoolean(r1, r5)
            r10.mUserVisibleHint = r0
            boolean r0 = r10.mUserVisibleHint
            if (r0 != 0) goto L_0x009d
            r10.mDeferStart = r5
            if (r11 <= r6) goto L_0x009d
            r11 = r6
        L_0x009d:
            android.support.v4.app.FragmentActivity r0 = r9.mActivity
            r10.mActivity = r0
            android.support.v4.app.Fragment r0 = r9.mParent
            r10.mParentFragment = r0
            android.support.v4.app.Fragment r0 = r9.mParent
            if (r0 == 0) goto L_0x00d9
            android.support.v4.app.Fragment r0 = r9.mParent
            android.support.v4.app.FragmentManagerImpl r0 = r0.mChildFragmentManager
        L_0x00ad:
            r10.mFragmentManager = r0
            r10.mCalled = r3
            android.support.v4.app.FragmentActivity r0 = r9.mActivity
            r10.onAttach(r0)
            boolean r0 = r10.mCalled
            if (r0 != 0) goto L_0x00de
            android.support.v4.app.SuperNotCalledException r0 = new android.support.v4.app.SuperNotCalledException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Fragment "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r10)
            java.lang.String r2 = " did not call through to super.onAttach()"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x00d9:
            android.support.v4.app.FragmentActivity r0 = r9.mActivity
            android.support.v4.app.FragmentManagerImpl r0 = r0.mFragments
            goto L_0x00ad
        L_0x00de:
            android.support.v4.app.Fragment r0 = r10.mParentFragment
            if (r0 != 0) goto L_0x00e7
            android.support.v4.app.FragmentActivity r0 = r9.mActivity
            r0.onAttachFragment(r10)
        L_0x00e7:
            boolean r0 = r10.mRetaining
            if (r0 != 0) goto L_0x00f0
            android.os.Bundle r0 = r10.mSavedFragmentState
            r10.performCreate(r0)
        L_0x00f0:
            r10.mRetaining = r3
            boolean r0 = r10.mFromLayout
            if (r0 == 0) goto L_0x0126
            android.os.Bundle r0 = r10.mSavedFragmentState
            android.view.LayoutInflater r0 = r10.getLayoutInflater(r0)
            android.os.Bundle r1 = r10.mSavedFragmentState
            android.view.View r0 = r10.performCreateView(r0, r7, r1)
            r10.mView = r0
            android.view.View r0 = r10.mView
            if (r0 == 0) goto L_0x0239
            android.view.View r0 = r10.mView
            r10.mInnerView = r0
            android.view.View r0 = r10.mView
            android.view.ViewGroup r0 = android.support.v4.app.NoSaveStateFrameLayout.wrap(r0)
            r10.mView = r0
            boolean r0 = r10.mHidden
            if (r0 == 0) goto L_0x011f
            android.view.View r0 = r10.mView
            r1 = 8
            r0.setVisibility(r1)
        L_0x011f:
            android.view.View r0 = r10.mView
            android.os.Bundle r1 = r10.mSavedFragmentState
            r10.onViewCreated(r0, r1)
        L_0x0126:
            if (r11 <= r5) goto L_0x01ef
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x0144
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "moveto ACTIVITY_CREATED: "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r10)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x0144:
            boolean r0 = r10.mFromLayout
            if (r0 != 0) goto L_0x01df
            int r0 = r10.mContainerId
            if (r0 == 0) goto L_0x039b
            android.support.v4.app.FragmentContainer r0 = r9.mContainer
            int r1 = r10.mContainerId
            android.view.View r0 = r0.findViewById(r1)
            android.view.ViewGroup r0 = (android.view.ViewGroup) r0
            if (r0 != 0) goto L_0x019b
            boolean r1 = r10.mRestored
            if (r1 != 0) goto L_0x019b
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "No view found for id 0x"
            java.lang.StringBuilder r2 = r2.append(r3)
            int r3 = r10.mContainerId
            java.lang.String r3 = java.lang.Integer.toHexString(r3)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = " ("
            java.lang.StringBuilder r2 = r2.append(r3)
            android.content.res.Resources r3 = r10.getResources()
            int r4 = r10.mContainerId
            java.lang.String r3 = r3.getResourceName(r4)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = ") for fragment "
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r10)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            r9.throwException(r1)
        L_0x019b:
            r10.mContainer = r0
            android.os.Bundle r1 = r10.mSavedFragmentState
            android.view.LayoutInflater r1 = r10.getLayoutInflater(r1)
            android.os.Bundle r2 = r10.mSavedFragmentState
            android.view.View r1 = r10.performCreateView(r1, r0, r2)
            r10.mView = r1
            android.view.View r1 = r10.mView
            if (r1 == 0) goto L_0x023d
            android.view.View r1 = r10.mView
            r10.mInnerView = r1
            android.view.View r1 = r10.mView
            android.view.ViewGroup r1 = android.support.v4.app.NoSaveStateFrameLayout.wrap(r1)
            r10.mView = r1
            if (r0 == 0) goto L_0x01cd
            android.view.animation.Animation r1 = r9.loadAnimation(r10, r12, r5, r13)
            if (r1 == 0) goto L_0x01c8
            android.view.View r2 = r10.mView
            r2.startAnimation(r1)
        L_0x01c8:
            android.view.View r1 = r10.mView
            r0.addView(r1)
        L_0x01cd:
            boolean r0 = r10.mHidden
            if (r0 == 0) goto L_0x01d8
            android.view.View r0 = r10.mView
            r1 = 8
            r0.setVisibility(r1)
        L_0x01d8:
            android.view.View r0 = r10.mView
            android.os.Bundle r1 = r10.mSavedFragmentState
            r10.onViewCreated(r0, r1)
        L_0x01df:
            android.os.Bundle r0 = r10.mSavedFragmentState
            r10.performActivityCreated(r0)
            android.view.View r0 = r10.mView
            if (r0 == 0) goto L_0x01ed
            android.os.Bundle r0 = r10.mSavedFragmentState
            r10.restoreViewState(r0)
        L_0x01ed:
            r10.mSavedFragmentState = r7
        L_0x01ef:
            if (r11 <= r6) goto L_0x0210
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x020d
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "moveto STARTED: "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r10)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x020d:
            r10.performStart()
        L_0x0210:
            if (r11 <= r8) goto L_0x0045
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x022e
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "moveto RESUMED: "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r10)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x022e:
            r10.mResumed = r5
            r10.performResume()
            r10.mSavedFragmentState = r7
            r10.mSavedViewState = r7
            goto L_0x0045
        L_0x0239:
            r10.mInnerView = r7
            goto L_0x0126
        L_0x023d:
            r10.mInnerView = r7
            goto L_0x01df
        L_0x0240:
            int r0 = r10.mState
            if (r0 <= r11) goto L_0x0045
            int r0 = r10.mState
            switch(r0) {
                case 1: goto L_0x024b;
                case 2: goto L_0x02cb;
                case 3: goto L_0x02aa;
                case 4: goto L_0x0289;
                case 5: goto L_0x0265;
                default: goto L_0x0249;
            }
        L_0x0249:
            goto L_0x0045
        L_0x024b:
            if (r11 >= r5) goto L_0x0045
            boolean r0 = r9.mDestroyed
            if (r0 == 0) goto L_0x025c
            android.view.View r0 = r10.mAnimatingAway
            if (r0 == 0) goto L_0x025c
            android.view.View r0 = r10.mAnimatingAway
            r10.mAnimatingAway = r7
            r0.clearAnimation()
        L_0x025c:
            android.view.View r0 = r10.mAnimatingAway
            if (r0 == 0) goto L_0x0338
            r10.mStateAfterAnimating = r11
            r11 = r5
            goto L_0x0045
        L_0x0265:
            r0 = 5
            if (r11 >= r0) goto L_0x0289
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x0284
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "movefrom RESUMED: "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r10)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x0284:
            r10.performPause()
            r10.mResumed = r3
        L_0x0289:
            if (r11 >= r8) goto L_0x02aa
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x02a7
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "movefrom STARTED: "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r10)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x02a7:
            r10.performStop()
        L_0x02aa:
            if (r11 >= r6) goto L_0x02cb
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x02c8
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "movefrom STOPPED: "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r10)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x02c8:
            r10.performReallyStop()
        L_0x02cb:
            r0 = 2
            if (r11 >= r0) goto L_0x024b
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x02ea
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "movefrom ACTIVITY_CREATED: "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r10)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x02ea:
            android.view.View r0 = r10.mView
            if (r0 == 0) goto L_0x02fd
            android.support.v4.app.FragmentActivity r0 = r9.mActivity
            boolean r0 = r0.isFinishing()
            if (r0 != 0) goto L_0x02fd
            android.util.SparseArray<android.os.Parcelable> r0 = r10.mSavedViewState
            if (r0 != 0) goto L_0x02fd
            r9.saveFragmentViewState(r10)
        L_0x02fd:
            r10.performDestroyView()
            android.view.View r0 = r10.mView
            if (r0 == 0) goto L_0x0330
            android.view.ViewGroup r0 = r10.mContainer
            if (r0 == 0) goto L_0x0330
            int r0 = r9.mCurState
            if (r0 <= 0) goto L_0x0398
            boolean r0 = r9.mDestroyed
            if (r0 != 0) goto L_0x0398
            android.view.animation.Animation r0 = r9.loadAnimation(r10, r12, r3, r13)
        L_0x0314:
            if (r0 == 0) goto L_0x0329
            android.view.View r1 = r10.mView
            r10.mAnimatingAway = r1
            r10.mStateAfterAnimating = r11
            android.support.v4.app.FragmentManagerImpl$5 r1 = new android.support.v4.app.FragmentManagerImpl$5
            r1.<init>(r10)
            r0.setAnimationListener(r1)
            android.view.View r1 = r10.mView
            r1.startAnimation(r0)
        L_0x0329:
            android.view.ViewGroup r0 = r10.mContainer
            android.view.View r1 = r10.mView
            r0.removeView(r1)
        L_0x0330:
            r10.mContainer = r7
            r10.mView = r7
            r10.mInnerView = r7
            goto L_0x024b
        L_0x0338:
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x0354
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "movefrom CREATED: "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r10)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x0354:
            boolean r0 = r10.mRetaining
            if (r0 != 0) goto L_0x035b
            r10.performDestroy()
        L_0x035b:
            r10.mCalled = r3
            r10.onDetach()
            boolean r0 = r10.mCalled
            if (r0 != 0) goto L_0x0383
            android.support.v4.app.SuperNotCalledException r0 = new android.support.v4.app.SuperNotCalledException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Fragment "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r10)
            java.lang.String r2 = " did not call through to super.onDetach()"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x0383:
            if (r14 != 0) goto L_0x0045
            boolean r0 = r10.mRetaining
            if (r0 != 0) goto L_0x038e
            r9.makeInactive(r10)
            goto L_0x0045
        L_0x038e:
            r10.mActivity = r7
            r10.mParentFragment = r7
            r10.mFragmentManager = r7
            r10.mChildFragmentManager = r7
            goto L_0x0045
        L_0x0398:
            r0 = r7
            goto L_0x0314
        L_0x039b:
            r0 = r7
            goto L_0x019b
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.moveToState(android.support.v4.app.Fragment, int, int, int, boolean):void");
    }

    public void noteStateNotSaved() {
        this.mStateSaved = HONEYCOMB;
    }

    public void performPendingDeferredStart(Fragment fragment) {
        if (!fragment.mDeferStart) {
            return;
        }
        if (this.mExecutingActions) {
            this.mHavePendingDeferredStart = true;
            return;
        }
        fragment.mDeferStart = HONEYCOMB;
        moveToState(fragment, this.mCurState, 0, 0, HONEYCOMB);
    }

    public void popBackStack() {
        enqueueAction(new Runnable() {
            public void run() {
                FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mActivity.mHandler, (String) null, -1, 0);
            }
        }, HONEYCOMB);
    }

    public void popBackStack(final int i, final int i2) {
        if (i < 0) {
            throw new IllegalArgumentException("Bad id: " + i);
        }
        enqueueAction(new Runnable() {
            public void run() {
                FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mActivity.mHandler, (String) null, i, i2);
            }
        }, HONEYCOMB);
    }

    public void popBackStack(final String str, final int i) {
        enqueueAction(new Runnable() {
            public void run() {
                FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mActivity.mHandler, str, -1, i);
            }
        }, HONEYCOMB);
    }

    public boolean popBackStackImmediate() {
        checkStateLoss();
        executePendingTransactions();
        return popBackStackState(this.mActivity.mHandler, (String) null, -1, 0);
    }

    public boolean popBackStackImmediate(int i, int i2) {
        checkStateLoss();
        executePendingTransactions();
        if (i >= 0) {
            return popBackStackState(this.mActivity.mHandler, (String) null, i, i2);
        }
        throw new IllegalArgumentException("Bad id: " + i);
    }

    public boolean popBackStackImmediate(String str, int i) {
        checkStateLoss();
        executePendingTransactions();
        return popBackStackState(this.mActivity.mHandler, str, -1, i);
    }

    /* access modifiers changed from: package-private */
    public boolean popBackStackState(Handler handler, String str, int i, int i2) {
        int i3;
        if (this.mBackStack == null) {
            return HONEYCOMB;
        }
        if (str == null && i < 0 && (i2 & 1) == 0) {
            int size = this.mBackStack.size() - 1;
            if (size < 0) {
                return HONEYCOMB;
            }
            this.mBackStack.remove(size).popFromBackStack(true);
            reportBackStackChanged();
        } else {
            int i4 = -1;
            if (str != null || i >= 0) {
                int size2 = this.mBackStack.size() - 1;
                while (i3 >= 0) {
                    BackStackRecord backStackRecord = this.mBackStack.get(i3);
                    if ((str != null && str.equals(backStackRecord.getName())) || (i >= 0 && i == backStackRecord.mIndex)) {
                        break;
                    }
                    size2 = i3 - 1;
                }
                if (i3 < 0) {
                    return HONEYCOMB;
                }
                if ((i2 & 1) != 0) {
                    i3--;
                    while (i3 >= 0) {
                        BackStackRecord backStackRecord2 = this.mBackStack.get(i3);
                        if ((str == null || !str.equals(backStackRecord2.getName())) && (i < 0 || i != backStackRecord2.mIndex)) {
                            break;
                        }
                        i3--;
                    }
                }
                i4 = i3;
            }
            if (i4 == this.mBackStack.size() - 1) {
                return HONEYCOMB;
            }
            ArrayList arrayList = new ArrayList();
            for (int size3 = this.mBackStack.size() - 1; size3 > i4; size3--) {
                arrayList.add(this.mBackStack.remove(size3));
            }
            int size4 = arrayList.size() - 1;
            int i5 = 0;
            while (i5 <= size4) {
                if (DEBUG) {
                    Log.v(TAG, "Popping back stack state: " + arrayList.get(i5));
                }
                ((BackStackRecord) arrayList.get(i5)).popFromBackStack(i5 == size4);
                i5++;
            }
            reportBackStackChanged();
        }
        return true;
    }

    public void putFragment(Bundle bundle, String str, Fragment fragment) {
        if (fragment.mIndex < 0) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        bundle.putInt(str, fragment.mIndex);
    }

    public void removeFragment(Fragment fragment, int i, int i2) {
        if (DEBUG) {
            Log.v(TAG, "remove: " + fragment + " nesting=" + fragment.mBackStackNesting);
        }
        boolean z = !fragment.isInBackStack();
        if (!fragment.mDetached || z) {
            if (this.mAdded != null) {
                this.mAdded.remove(fragment);
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = HONEYCOMB;
            fragment.mRemoving = true;
            moveToState(fragment, z ? 0 : 1, i, i2, HONEYCOMB);
        }
    }

    public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener onBackStackChangedListener) {
        if (this.mBackStackChangeListeners != null) {
            this.mBackStackChangeListeners.remove(onBackStackChangedListener);
        }
    }

    /* access modifiers changed from: package-private */
    public void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < this.mBackStackChangeListeners.size()) {
                    this.mBackStackChangeListeners.get(i2).onBackStackChanged();
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void restoreAllState(Parcelable parcelable, ArrayList<Fragment> arrayList) {
        if (parcelable != null) {
            FragmentManagerState fragmentManagerState = (FragmentManagerState) parcelable;
            if (fragmentManagerState.mActive != null) {
                if (arrayList != null) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        Fragment fragment = arrayList.get(i);
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: re-attaching retained " + fragment);
                        }
                        FragmentState fragmentState = fragmentManagerState.mActive[fragment.mIndex];
                        fragmentState.mInstance = fragment;
                        fragment.mSavedViewState = null;
                        fragment.mBackStackNesting = 0;
                        fragment.mInLayout = HONEYCOMB;
                        fragment.mAdded = HONEYCOMB;
                        fragment.mTarget = null;
                        if (fragmentState.mSavedFragmentState != null) {
                            fragmentState.mSavedFragmentState.setClassLoader(this.mActivity.getClassLoader());
                            fragment.mSavedViewState = fragmentState.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
                        }
                    }
                }
                this.mActive = new ArrayList<>(fragmentManagerState.mActive.length);
                if (this.mAvailIndices != null) {
                    this.mAvailIndices.clear();
                }
                for (int i2 = 0; i2 < fragmentManagerState.mActive.length; i2++) {
                    FragmentState fragmentState2 = fragmentManagerState.mActive[i2];
                    if (fragmentState2 != null) {
                        Fragment instantiate = fragmentState2.instantiate(this.mActivity, this.mParent);
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: active #" + i2 + ": " + instantiate);
                        }
                        this.mActive.add(instantiate);
                        fragmentState2.mInstance = null;
                    } else {
                        this.mActive.add((Object) null);
                        if (this.mAvailIndices == null) {
                            this.mAvailIndices = new ArrayList<>();
                        }
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: avail #" + i2);
                        }
                        this.mAvailIndices.add(Integer.valueOf(i2));
                    }
                }
                if (arrayList != null) {
                    for (int i3 = 0; i3 < arrayList.size(); i3++) {
                        Fragment fragment2 = arrayList.get(i3);
                        if (fragment2.mTargetIndex >= 0) {
                            if (fragment2.mTargetIndex < this.mActive.size()) {
                                fragment2.mTarget = this.mActive.get(fragment2.mTargetIndex);
                            } else {
                                Log.w(TAG, "Re-attaching retained fragment " + fragment2 + " target no longer exists: " + fragment2.mTargetIndex);
                                fragment2.mTarget = null;
                            }
                        }
                    }
                }
                if (fragmentManagerState.mAdded != null) {
                    this.mAdded = new ArrayList<>(fragmentManagerState.mAdded.length);
                    for (int i4 = 0; i4 < fragmentManagerState.mAdded.length; i4++) {
                        Fragment fragment3 = this.mActive.get(fragmentManagerState.mAdded[i4]);
                        if (fragment3 == null) {
                            throwException(new IllegalStateException("No instantiated fragment for index #" + fragmentManagerState.mAdded[i4]));
                        }
                        fragment3.mAdded = true;
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: added #" + i4 + ": " + fragment3);
                        }
                        if (this.mAdded.contains(fragment3)) {
                            throw new IllegalStateException("Already added!");
                        }
                        this.mAdded.add(fragment3);
                    }
                } else {
                    this.mAdded = null;
                }
                if (fragmentManagerState.mBackStack != null) {
                    this.mBackStack = new ArrayList<>(fragmentManagerState.mBackStack.length);
                    for (int i5 = 0; i5 < fragmentManagerState.mBackStack.length; i5++) {
                        BackStackRecord instantiate2 = fragmentManagerState.mBackStack[i5].instantiate(this);
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: back stack #" + i5 + " (index " + instantiate2.mIndex + "): " + instantiate2);
                            instantiate2.dump("  ", new PrintWriter(new LogWriter(TAG)), HONEYCOMB);
                        }
                        this.mBackStack.add(instantiate2);
                        if (instantiate2.mIndex >= 0) {
                            setBackStackIndex(instantiate2.mIndex, instantiate2);
                        }
                    }
                    return;
                }
                this.mBackStack = null;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public ArrayList<Fragment> retainNonConfig() {
        ArrayList<Fragment> arrayList = null;
        if (this.mActive != null) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= this.mActive.size()) {
                    break;
                }
                Fragment fragment = this.mActive.get(i2);
                if (fragment != null && fragment.mRetainInstance) {
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                    }
                    arrayList.add(fragment);
                    fragment.mRetaining = true;
                    fragment.mTargetIndex = fragment.mTarget != null ? fragment.mTarget.mIndex : -1;
                    if (DEBUG) {
                        Log.v(TAG, "retainNonConfig: keeping retained " + fragment);
                    }
                }
                i = i2 + 1;
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public Parcelable saveAllState() {
        int[] iArr;
        int size;
        int size2;
        boolean z;
        BackStackState[] backStackStateArr = null;
        execPendingActions();
        if (HONEYCOMB) {
            this.mStateSaved = true;
        }
        if (this.mActive == null || this.mActive.size() <= 0) {
            return null;
        }
        int size3 = this.mActive.size();
        FragmentState[] fragmentStateArr = new FragmentState[size3];
        int i = 0;
        boolean z2 = false;
        while (i < size3) {
            Fragment fragment = this.mActive.get(i);
            if (fragment != null) {
                if (fragment.mIndex < 0) {
                    throwException(new IllegalStateException("Failure saving state: active " + fragment + " has cleared index: " + fragment.mIndex));
                }
                FragmentState fragmentState = new FragmentState(fragment);
                fragmentStateArr[i] = fragmentState;
                if (fragment.mState <= 0 || fragmentState.mSavedFragmentState != null) {
                    fragmentState.mSavedFragmentState = fragment.mSavedFragmentState;
                } else {
                    fragmentState.mSavedFragmentState = saveFragmentBasicState(fragment);
                    if (fragment.mTarget != null) {
                        if (fragment.mTarget.mIndex < 0) {
                            throwException(new IllegalStateException("Failure saving state: " + fragment + " has target not in fragment manager: " + fragment.mTarget));
                        }
                        if (fragmentState.mSavedFragmentState == null) {
                            fragmentState.mSavedFragmentState = new Bundle();
                        }
                        putFragment(fragmentState.mSavedFragmentState, TARGET_STATE_TAG, fragment.mTarget);
                        if (fragment.mTargetRequestCode != 0) {
                            fragmentState.mSavedFragmentState.putInt(TARGET_REQUEST_CODE_STATE_TAG, fragment.mTargetRequestCode);
                        }
                    }
                }
                if (DEBUG) {
                    Log.v(TAG, "Saved state of " + fragment + ": " + fragmentState.mSavedFragmentState);
                }
                z = true;
            } else {
                z = z2;
            }
            i++;
            z2 = z;
        }
        if (z2) {
            if (this.mAdded == null || (size2 = this.mAdded.size()) <= 0) {
                iArr = null;
            } else {
                iArr = new int[size2];
                for (int i2 = 0; i2 < size2; i2++) {
                    iArr[i2] = this.mAdded.get(i2).mIndex;
                    if (iArr[i2] < 0) {
                        throwException(new IllegalStateException("Failure saving state: active " + this.mAdded.get(i2) + " has cleared index: " + iArr[i2]));
                    }
                    if (DEBUG) {
                        Log.v(TAG, "saveAllState: adding fragment #" + i2 + ": " + this.mAdded.get(i2));
                    }
                }
            }
            if (this.mBackStack != null && (size = this.mBackStack.size()) > 0) {
                backStackStateArr = new BackStackState[size];
                for (int i3 = 0; i3 < size; i3++) {
                    backStackStateArr[i3] = new BackStackState(this, this.mBackStack.get(i3));
                    if (DEBUG) {
                        Log.v(TAG, "saveAllState: adding back stack #" + i3 + ": " + this.mBackStack.get(i3));
                    }
                }
            }
            FragmentManagerState fragmentManagerState = new FragmentManagerState();
            fragmentManagerState.mActive = fragmentStateArr;
            fragmentManagerState.mAdded = iArr;
            fragmentManagerState.mBackStack = backStackStateArr;
            return fragmentManagerState;
        } else if (!DEBUG) {
            return null;
        } else {
            Log.v(TAG, "saveAllState: no fragments!");
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public Bundle saveFragmentBasicState(Fragment fragment) {
        Bundle bundle;
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        fragment.performSaveInstanceState(this.mStateBundle);
        if (!this.mStateBundle.isEmpty()) {
            bundle = this.mStateBundle;
            this.mStateBundle = null;
        } else {
            bundle = null;
        }
        if (fragment.mView != null) {
            saveFragmentViewState(fragment);
        }
        if (fragment.mSavedViewState != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray(VIEW_STATE_TAG, fragment.mSavedViewState);
        }
        if (!fragment.mUserVisibleHint) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putBoolean(USER_VISIBLE_HINT_TAG, fragment.mUserVisibleHint);
        }
        return bundle;
    }

    public Fragment.SavedState saveFragmentInstanceState(Fragment fragment) {
        Bundle saveFragmentBasicState;
        if (fragment.mIndex < 0) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        if (fragment.mState <= 0 || (saveFragmentBasicState = saveFragmentBasicState(fragment)) == null) {
            return null;
        }
        return new Fragment.SavedState(saveFragmentBasicState);
    }

    /* access modifiers changed from: package-private */
    public void saveFragmentViewState(Fragment fragment) {
        if (fragment.mInnerView != null) {
            if (this.mStateArray == null) {
                this.mStateArray = new SparseArray<>();
            } else {
                this.mStateArray.clear();
            }
            fragment.mInnerView.saveHierarchyState(this.mStateArray);
            if (this.mStateArray.size() > 0) {
                fragment.mSavedViewState = this.mStateArray;
                this.mStateArray = null;
            }
        }
    }

    public void setBackStackIndex(int i, BackStackRecord backStackRecord) {
        synchronized (this) {
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<>();
            }
            int size = this.mBackStackIndices.size();
            if (i < size) {
                if (DEBUG) {
                    Log.v(TAG, "Setting back stack index " + i + " to " + backStackRecord);
                }
                this.mBackStackIndices.set(i, backStackRecord);
            } else {
                while (size < i) {
                    this.mBackStackIndices.add((Object) null);
                    if (this.mAvailBackStackIndices == null) {
                        this.mAvailBackStackIndices = new ArrayList<>();
                    }
                    if (DEBUG) {
                        Log.v(TAG, "Adding available back stack index " + size);
                    }
                    this.mAvailBackStackIndices.add(Integer.valueOf(size));
                    size++;
                }
                if (DEBUG) {
                    Log.v(TAG, "Adding back stack index " + i + " with " + backStackRecord);
                }
                this.mBackStackIndices.add(backStackRecord);
            }
        }
    }

    public void showFragment(Fragment fragment, int i, int i2) {
        if (DEBUG) {
            Log.v(TAG, "show: " + fragment);
        }
        if (fragment.mHidden) {
            fragment.mHidden = HONEYCOMB;
            if (fragment.mView != null) {
                Animation loadAnimation = loadAnimation(fragment, i, true, i2);
                if (loadAnimation != null) {
                    fragment.mView.startAnimation(loadAnimation);
                }
                fragment.mView.setVisibility(0);
            }
            if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.onHiddenChanged(HONEYCOMB);
        }
    }

    /* access modifiers changed from: package-private */
    public void startPendingDeferredFragments() {
        if (this.mActive != null) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < this.mActive.size()) {
                    Fragment fragment = this.mActive.get(i2);
                    if (fragment != null) {
                        performPendingDeferredStart(fragment);
                    }
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        if (this.mParent != null) {
            DebugUtils.buildShortClassTag(this.mParent, sb);
        } else {
            DebugUtils.buildShortClassTag(this.mActivity, sb);
        }
        sb.append("}}");
        return sb.toString();
    }
}
