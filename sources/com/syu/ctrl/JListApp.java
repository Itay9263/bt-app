package com.syu.ctrl;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Scroller;
import com.syu.app.MyApplication;
import com.syu.app.MyUi;
import com.syu.app.MyUiItem;
import com.syu.data.FinalInfo;
import com.syu.page.IPageNotify;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JListApp extends ViewGroup implements View.OnKeyListener, AdapterView.OnItemClickListener {
    private static final int APPS_PAGE_SIZE = 10;
    private static final int SNAP_VELOCITY = 600;
    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLLING = 1;
    private int PageNum;
    /* access modifiers changed from: private */
    public int idPageRow;
    private JIndicator indicator;
    public ArrayList<ResolveInfo> mAllAppsList = new ArrayList<>();
    public AppsAdapter[] mAppsAdapter;
    private int mCurScreen;
    private float mLastMotionX;
    private Scroller mScroller;
    private int mTouchSlop;
    private int mTouchState = 0;
    private VelocityTracker mVelocityTracker;
    private JPage page;
    /* access modifiers changed from: private */
    public MyUi ui;
    public View viewAppSelect;

    public class AppsAdapter extends ArrayAdapter<ResolveInfo> {
        public ArrayList<ResolveInfo> apps = null;
        public ArrayList<WeakReference<JPage>> mPageInfos = new ArrayList<>();
        private JListApp mRelatedListApp;
        public int start = 0;

        public AppsAdapter(Context context, ArrayList<ResolveInfo> arrayList, JListApp jListApp, int i) {
            super(context, 0, arrayList);
            this.apps = arrayList;
            this.start = i;
            setRelatedListApp(jListApp);
        }

        public JListApp getRelatedListApp() {
            return this.mRelatedListApp;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            JPage jPage;
            boolean z;
            ResolveInfo resolveInfo = (ResolveInfo) getItem(i);
            if (view == null) {
                jPage = JListApp.this.ui.loadPage(false, JListApp.this.idPageRow);
                MyUiItem myUiItem = (MyUiItem) jPage.getTag();
                jPage.setLayoutParams(new AbsListView.LayoutParams(myUiItem.getWidth(), myUiItem.getHeight()));
                jPage.setStrDrawable(myUiItem.getStrDrawable(), true);
            } else {
                jPage = (JPage) view;
            }
            ArrayList arrayList = new ArrayList();
            Iterator<WeakReference<JPage>> it = this.mPageInfos.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                }
                WeakReference next = it.next();
                if (next.get() == jPage) {
                    z = true;
                    break;
                } else if (next.get() == null) {
                    arrayList.add(next);
                }
            }
            if (!z) {
                jPage.setListApp(this.mRelatedListApp);
                this.mPageInfos.removeAll(arrayList);
                this.mPageInfos.add(new WeakReference(jPage));
            }
            jPage.setChildTag(i);
            IPageNotify notify = this.mRelatedListApp.getPage().getNotify();
            if (notify != null) {
                notify.InitAppItem(this.mRelatedListApp, jPage, resolveInfo);
            }
            return jPage;
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        public void setRelatedListApp(JListApp jListApp) {
            this.mRelatedListApp = jListApp;
        }
    }

    public JListApp(Context context, JPage jPage, MyUi myUi) {
        super(context);
        this.page = jPage;
        this.ui = myUi;
        initViews();
    }

    public void ShowGridView() {
        removeAllViews();
        for (int i = 0; i < this.PageNum; i++) {
            addPage(i);
        }
        this.indicator.setTotal(this.PageNum);
        snapToScreen(this.mCurScreen);
    }

    public void addApps(ArrayList<ResolveInfo> arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.mAllAppsList.add(arrayList.get(i));
        }
        changeAdapter();
        ShowGridView();
    }

    public void addPage(int i) {
        GridView gridView = new GridView(this.ui.mContext);
        IPageNotify notify = getPage().getNotify();
        if (notify != null) {
            notify.InitAppGridView(gridView);
        }
        gridView.setNumColumns(5);
        gridView.setPadding(5, 5, 5, 5);
        gridView.setOnItemClickListener(this);
        gridView.setHorizontalSpacing(5);
        gridView.setVerticalSpacing(5);
        gridView.setGravity(17);
        gridView.setSmoothScrollbarEnabled(true);
        gridView.setAdapter(this.mAppsAdapter[i]);
        addView(gridView);
    }

    public void changeAdapter() {
        int i;
        int size = this.mAllAppsList.size();
        int i2 = size % 10;
        if (i2 == 0) {
            this.PageNum = size / 10;
        } else {
            this.PageNum = (size / 10) + 1;
        }
        this.mAppsAdapter = null;
        this.mAppsAdapter = new AppsAdapter[this.PageNum];
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 < this.PageNum) {
                ArrayList arrayList = new ArrayList();
                int i5 = i4 * 10;
                int i6 = i5 + 10;
                if (i4 == this.PageNum - 1) {
                    if (i2 != 0) {
                        i6 = i5 + i2;
                    }
                    i = i6;
                } else {
                    i = i6;
                }
                int i7 = i5;
                while (i7 >= i5 && i7 < i) {
                    arrayList.add(this.mAllAppsList.get(i7));
                    i7++;
                }
                this.mAppsAdapter[i4] = new AppsAdapter(this.ui.mContext, arrayList, this, i5);
                i3 = i4 + 1;
            } else {
                return;
            }
        }
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void getAllInstalledApps() {
        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> queryIntentActivities = MyApplication.mPkgManager.queryIntentActivities(intent, 0);
        if (queryIntentActivities != null) {
            ArrayList arrayList = new ArrayList();
            arrayList.clear();
            for (ResolveInfo next : queryIntentActivities) {
                String str = next.activityInfo.packageName;
                if (str != null && !str.startsWith(FinalInfo.COMPANY_NAME)) {
                    arrayList.add(next);
                }
            }
            setApps(arrayList);
        }
    }

    public int getCurScreen() {
        return this.mCurScreen;
    }

    public JPage getPage() {
        return this.page;
    }

    public int getPageNum() {
        return this.PageNum;
    }

    public void initViews() {
        this.mCurScreen = 0;
        this.mScroller = new Scroller(this.ui.mContext);
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public void notifyDataSetChanged() {
        if (this.mAppsAdapter != null) {
            for (AppsAdapter notifyDataSetChanged : this.mAppsAdapter) {
                notifyDataSetChanged.notifyDataSetChanged();
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 2 && this.mTouchState != 0) {
            return true;
        }
        float x = motionEvent.getX();
        switch (action) {
            case 0:
                this.mLastMotionX = x;
                this.mTouchState = this.mScroller.isFinished() ? 0 : 1;
                break;
            case 1:
            case 3:
                this.mTouchState = 0;
                break;
            case 2:
                if (((int) Math.abs(this.mLastMotionX - x)) > this.mTouchSlop) {
                    this.mTouchState = 1;
                    break;
                }
                break;
        }
        return this.mTouchState != 0;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (this.viewAppSelect != null) {
            ResolveInfo resolveInfo = (ResolveInfo) adapterView.getItemAtPosition(((JPage) this.viewAppSelect).getChildTag());
            IPageNotify notify = getPage().getNotify();
            if (notify != null) {
                notify.AppClick(this, resolveInfo);
            }
        }
    }

    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        switch (i) {
            case 4:
                return true;
            default:
                return false;
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        int i5 = 0;
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != 8) {
                int measuredWidth = childAt.getMeasuredWidth();
                childAt.layout(i5, 0, i5 + measuredWidth, childAt.getMeasuredHeight());
                i5 += measuredWidth;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int size = View.MeasureSpec.getSize(i);
        if (View.MeasureSpec.getMode(i) != 1073741824) {
            throw new IllegalStateException("AllApps only canmCurScreen run at EXACTLY mode!");
        }
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            getChildAt(i3).measure(i, i2);
        }
        scrollTo(this.mCurScreen * size, 0);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int action = motionEvent.getAction();
        float x = motionEvent.getX();
        switch (action) {
            case 0:
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                this.mLastMotionX = x;
                return true;
            case 1:
                VelocityTracker velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int xVelocity = (int) velocityTracker.getXVelocity();
                if (xVelocity > 600 && this.mCurScreen > 0) {
                    snapToScreen(this.mCurScreen - 1);
                } else if (xVelocity >= -600 || this.mCurScreen >= getChildCount() - 1) {
                    snapToDestination();
                } else {
                    snapToScreen(this.mCurScreen + 1);
                }
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                }
                this.mTouchState = 0;
                return true;
            case 2:
                this.mLastMotionX = x;
                scrollBy((int) (this.mLastMotionX - x), 0);
                return true;
            case 3:
                this.mTouchState = 0;
                return true;
            default:
                return true;
        }
    }

    public void setApps(ArrayList<ResolveInfo> arrayList) {
        this.mAllAppsList.clear();
        addApps(arrayList);
    }

    public void setIndicator(JIndicator jIndicator) {
        this.indicator = jIndicator;
    }

    public void setPageRow(int i) {
        this.idPageRow = i;
    }

    public void setToScreen(int i) {
        int max = Math.max(0, Math.min(i, getChildCount() - 1));
        this.mCurScreen = max;
        scrollTo(max * getWidth(), 0);
    }

    public void snapToDestination() {
        int width = getWidth();
        snapToScreen((getScrollX() + (width / 2)) / width);
    }

    public void snapToScreen(int i) {
        int max = Math.max(0, Math.min(i, getChildCount() - 1));
        if (getScrollX() != getWidth() * max) {
            int width = (getWidth() * max) - getScrollX();
            this.mScroller.startScroll(getScrollX(), 0, width, 0, Math.abs(width) * 2);
            this.mCurScreen = max;
            this.indicator.setCurrent(this.mCurScreen);
            invalidate();
        }
    }
}
