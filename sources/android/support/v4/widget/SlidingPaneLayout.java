package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import com.syu.data.FinalSound;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SlidingPaneLayout extends ViewGroup {
    private static final int DEFAULT_FADE_COLOR = -858993460;
    private static final int DEFAULT_OVERHANG_SIZE = 32;
    static final SlidingPanelLayoutImpl IMPL;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final String TAG = "SlidingPaneLayout";
    private boolean mCanSlide;
    private int mCoveredFadeColor;
    /* access modifiers changed from: private */
    public final ViewDragHelper mDragHelper;
    private boolean mFirstLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    /* access modifiers changed from: private */
    public boolean mIsUnableToDrag;
    private final int mOverhangSize;
    private PanelSlideListener mPanelSlideListener;
    private int mParallaxBy;
    private float mParallaxOffset;
    /* access modifiers changed from: private */
    public final ArrayList<DisableLayerRunnable> mPostedRunnables;
    /* access modifiers changed from: private */
    public boolean mPreservedOpenState;
    private Drawable mShadowDrawableLeft;
    private Drawable mShadowDrawableRight;
    /* access modifiers changed from: private */
    public float mSlideOffset;
    /* access modifiers changed from: private */
    public int mSlideRange;
    /* access modifiers changed from: private */
    public View mSlideableView;
    private int mSliderFadeColor;
    private final Rect mTmpRect;

    class AccessibilityDelegate extends AccessibilityDelegateCompat {
        private final Rect mTmpRect = new Rect();

        AccessibilityDelegate() {
        }

        private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2) {
            Rect rect = this.mTmpRect;
            accessibilityNodeInfoCompat2.getBoundsInParent(rect);
            accessibilityNodeInfoCompat.setBoundsInParent(rect);
            accessibilityNodeInfoCompat2.getBoundsInScreen(rect);
            accessibilityNodeInfoCompat.setBoundsInScreen(rect);
            accessibilityNodeInfoCompat.setVisibleToUser(accessibilityNodeInfoCompat2.isVisibleToUser());
            accessibilityNodeInfoCompat.setPackageName(accessibilityNodeInfoCompat2.getPackageName());
            accessibilityNodeInfoCompat.setClassName(accessibilityNodeInfoCompat2.getClassName());
            accessibilityNodeInfoCompat.setContentDescription(accessibilityNodeInfoCompat2.getContentDescription());
            accessibilityNodeInfoCompat.setEnabled(accessibilityNodeInfoCompat2.isEnabled());
            accessibilityNodeInfoCompat.setClickable(accessibilityNodeInfoCompat2.isClickable());
            accessibilityNodeInfoCompat.setFocusable(accessibilityNodeInfoCompat2.isFocusable());
            accessibilityNodeInfoCompat.setFocused(accessibilityNodeInfoCompat2.isFocused());
            accessibilityNodeInfoCompat.setAccessibilityFocused(accessibilityNodeInfoCompat2.isAccessibilityFocused());
            accessibilityNodeInfoCompat.setSelected(accessibilityNodeInfoCompat2.isSelected());
            accessibilityNodeInfoCompat.setLongClickable(accessibilityNodeInfoCompat2.isLongClickable());
            accessibilityNodeInfoCompat.addAction(accessibilityNodeInfoCompat2.getActions());
            accessibilityNodeInfoCompat.setMovementGranularities(accessibilityNodeInfoCompat2.getMovementGranularities());
        }

        public boolean filter(View view) {
            return SlidingPaneLayout.this.isDimmed(view);
        }

        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName(SlidingPaneLayout.class.getName());
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            AccessibilityNodeInfoCompat obtain = AccessibilityNodeInfoCompat.obtain(accessibilityNodeInfoCompat);
            super.onInitializeAccessibilityNodeInfo(view, obtain);
            copyNodeInfoNoChildren(accessibilityNodeInfoCompat, obtain);
            obtain.recycle();
            accessibilityNodeInfoCompat.setClassName(SlidingPaneLayout.class.getName());
            accessibilityNodeInfoCompat.setSource(view);
            ViewParent parentForAccessibility = ViewCompat.getParentForAccessibility(view);
            if (parentForAccessibility instanceof View) {
                accessibilityNodeInfoCompat.setParent((View) parentForAccessibility);
            }
            int childCount = SlidingPaneLayout.this.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = SlidingPaneLayout.this.getChildAt(i);
                if (!filter(childAt) && childAt.getVisibility() == 0) {
                    ViewCompat.setImportantForAccessibility(childAt, 1);
                    accessibilityNodeInfoCompat.addChild(childAt);
                }
            }
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            if (!filter(view)) {
                return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
            }
            return false;
        }
    }

    private class DisableLayerRunnable implements Runnable {
        final View mChildView;

        DisableLayerRunnable(View view) {
            this.mChildView = view;
        }

        public void run() {
            if (this.mChildView.getParent() == SlidingPaneLayout.this) {
                ViewCompat.setLayerType(this.mChildView, 0, (Paint) null);
                SlidingPaneLayout.this.invalidateChildRegion(this.mChildView);
            }
            SlidingPaneLayout.this.mPostedRunnables.remove(this);
        }
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {
        private DragHelperCallback() {
        }

        public int clampViewPositionHorizontal(View view, int i, int i2) {
            LayoutParams layoutParams = (LayoutParams) SlidingPaneLayout.this.mSlideableView.getLayoutParams();
            if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
                int width = SlidingPaneLayout.this.getWidth() - ((layoutParams.rightMargin + SlidingPaneLayout.this.getPaddingRight()) + SlidingPaneLayout.this.mSlideableView.getWidth());
                return Math.max(Math.min(i, width), width - SlidingPaneLayout.this.mSlideRange);
            }
            int paddingLeft = layoutParams.leftMargin + SlidingPaneLayout.this.getPaddingLeft();
            return Math.min(Math.max(i, paddingLeft), SlidingPaneLayout.this.mSlideRange + paddingLeft);
        }

        public int clampViewPositionVertical(View view, int i, int i2) {
            return view.getTop();
        }

        public int getViewHorizontalDragRange(View view) {
            return SlidingPaneLayout.this.mSlideRange;
        }

        public void onEdgeDragStarted(int i, int i2) {
            SlidingPaneLayout.this.mDragHelper.captureChildView(SlidingPaneLayout.this.mSlideableView, i2);
        }

        public void onViewCaptured(View view, int i) {
            SlidingPaneLayout.this.setAllChildrenVisible();
        }

        public void onViewDragStateChanged(int i) {
            if (SlidingPaneLayout.this.mDragHelper.getViewDragState() != 0) {
                return;
            }
            if (SlidingPaneLayout.this.mSlideOffset == 0.0f) {
                SlidingPaneLayout.this.updateObscuredViewsVisibility(SlidingPaneLayout.this.mSlideableView);
                SlidingPaneLayout.this.dispatchOnPanelClosed(SlidingPaneLayout.this.mSlideableView);
                boolean unused = SlidingPaneLayout.this.mPreservedOpenState = false;
                return;
            }
            SlidingPaneLayout.this.dispatchOnPanelOpened(SlidingPaneLayout.this.mSlideableView);
            boolean unused2 = SlidingPaneLayout.this.mPreservedOpenState = true;
        }

        public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) {
            SlidingPaneLayout.this.onPanelDragged(i);
            SlidingPaneLayout.this.invalidate();
        }

        public void onViewReleased(View view, float f, float f2) {
            int paddingLeft;
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
                int paddingRight = layoutParams.rightMargin + SlidingPaneLayout.this.getPaddingRight();
                if (f < 0.0f || (f == 0.0f && SlidingPaneLayout.this.mSlideOffset > 0.5f)) {
                    paddingRight += SlidingPaneLayout.this.mSlideRange;
                }
                paddingLeft = (SlidingPaneLayout.this.getWidth() - paddingRight) - SlidingPaneLayout.this.mSlideableView.getWidth();
            } else {
                paddingLeft = layoutParams.leftMargin + SlidingPaneLayout.this.getPaddingLeft();
                if (f > 0.0f || (f == 0.0f && SlidingPaneLayout.this.mSlideOffset > 0.5f)) {
                    paddingLeft += SlidingPaneLayout.this.mSlideRange;
                }
            }
            SlidingPaneLayout.this.mDragHelper.settleCapturedViewAt(paddingLeft, view.getTop());
            SlidingPaneLayout.this.invalidate();
        }

        public boolean tryCaptureView(View view, int i) {
            if (SlidingPaneLayout.this.mIsUnableToDrag) {
                return false;
            }
            return ((LayoutParams) view.getLayoutParams()).slideable;
        }
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        private static final int[] ATTRS = {16843137};
        Paint dimPaint;
        boolean dimWhenOffset;
        boolean slideable;
        public float weight = 0.0f;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, ATTRS);
            this.weight = obtainStyledAttributes.getFloat(0, 0.0f);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(LayoutParams layoutParams) {
            super(layoutParams);
            this.weight = layoutParams.weight;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }
    }

    public interface PanelSlideListener {
        void onPanelClosed(View view);

        void onPanelOpened(View view);

        void onPanelSlide(View view, float f);
    }

    static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        boolean isOpen;

        private SavedState(Parcel parcel) {
            super(parcel);
            this.isOpen = parcel.readInt() != 0;
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.isOpen ? 1 : 0);
        }
    }

    public static class SimplePanelSlideListener implements PanelSlideListener {
        public void onPanelClosed(View view) {
        }

        public void onPanelOpened(View view) {
        }

        public void onPanelSlide(View view, float f) {
        }
    }

    interface SlidingPanelLayoutImpl {
        void invalidateChildRegion(SlidingPaneLayout slidingPaneLayout, View view);
    }

    static class SlidingPanelLayoutImplBase implements SlidingPanelLayoutImpl {
        SlidingPanelLayoutImplBase() {
        }

        public void invalidateChildRegion(SlidingPaneLayout slidingPaneLayout, View view) {
            ViewCompat.postInvalidateOnAnimation(slidingPaneLayout, view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        }
    }

    static class SlidingPanelLayoutImplJB extends SlidingPanelLayoutImplBase {
        private Method mGetDisplayList;
        private Field mRecreateDisplayList;

        SlidingPanelLayoutImplJB() {
            try {
                this.mGetDisplayList = View.class.getDeclaredMethod("getDisplayList", (Class[]) null);
            } catch (NoSuchMethodException e) {
                Log.e(SlidingPaneLayout.TAG, "Couldn't fetch getDisplayList method; dimming won't work right.", e);
            }
            try {
                this.mRecreateDisplayList = View.class.getDeclaredField("mRecreateDisplayList");
                this.mRecreateDisplayList.setAccessible(true);
            } catch (NoSuchFieldException e2) {
                Log.e(SlidingPaneLayout.TAG, "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", e2);
            }
        }

        public void invalidateChildRegion(SlidingPaneLayout slidingPaneLayout, View view) {
            if (this.mGetDisplayList == null || this.mRecreateDisplayList == null) {
                view.invalidate();
                return;
            }
            try {
                this.mRecreateDisplayList.setBoolean(view, true);
                this.mGetDisplayList.invoke(view, (Object[]) null);
            } catch (Exception e) {
                Log.e(SlidingPaneLayout.TAG, "Error refreshing display list state", e);
            }
            super.invalidateChildRegion(slidingPaneLayout, view);
        }
    }

    static class SlidingPanelLayoutImplJBMR1 extends SlidingPanelLayoutImplBase {
        SlidingPanelLayoutImplJBMR1() {
        }

        public void invalidateChildRegion(SlidingPaneLayout slidingPaneLayout, View view) {
            ViewCompat.setLayerPaint(view, ((LayoutParams) view.getLayoutParams()).dimPaint);
        }
    }

    static {
        int i = Build.VERSION.SDK_INT;
        if (i >= 17) {
            IMPL = new SlidingPanelLayoutImplJBMR1();
        } else if (i >= 16) {
            IMPL = new SlidingPanelLayoutImplJB();
        } else {
            IMPL = new SlidingPanelLayoutImplBase();
        }
    }

    public SlidingPaneLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public SlidingPaneLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SlidingPaneLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSliderFadeColor = DEFAULT_FADE_COLOR;
        this.mFirstLayout = true;
        this.mTmpRect = new Rect();
        this.mPostedRunnables = new ArrayList<>();
        float f = context.getResources().getDisplayMetrics().density;
        this.mOverhangSize = (int) ((32.0f * f) + 0.5f);
        ViewConfiguration.get(context);
        setWillNotDraw(false);
        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate());
        ViewCompat.setImportantForAccessibility(this, 1);
        this.mDragHelper = ViewDragHelper.create(this, 0.5f, new DragHelperCallback());
        this.mDragHelper.setMinVelocity(f * 400.0f);
    }

    private boolean closePane(View view, int i) {
        if (!this.mFirstLayout && !smoothSlideTo(0.0f, i)) {
            return false;
        }
        this.mPreservedOpenState = false;
        return true;
    }

    private void dimChildView(View view, float f, int i) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (f > 0.0f && i != 0) {
            int i2 = (((int) (((float) ((-16777216 & i) >>> 24)) * f)) << 24) | (16777215 & i);
            if (layoutParams.dimPaint == null) {
                layoutParams.dimPaint = new Paint();
            }
            layoutParams.dimPaint.setColorFilter(new PorterDuffColorFilter(i2, PorterDuff.Mode.SRC_OVER));
            if (ViewCompat.getLayerType(view) != 2) {
                ViewCompat.setLayerType(view, 2, layoutParams.dimPaint);
            }
            invalidateChildRegion(view);
        } else if (ViewCompat.getLayerType(view) != 0) {
            if (layoutParams.dimPaint != null) {
                layoutParams.dimPaint.setColorFilter((ColorFilter) null);
            }
            DisableLayerRunnable disableLayerRunnable = new DisableLayerRunnable(view);
            this.mPostedRunnables.add(disableLayerRunnable);
            ViewCompat.postOnAnimation(this, disableLayerRunnable);
        }
    }

    /* access modifiers changed from: private */
    public void invalidateChildRegion(View view) {
        IMPL.invalidateChildRegion(this, view);
    }

    /* access modifiers changed from: private */
    public boolean isLayoutRtlSupport() {
        return ViewCompat.getLayoutDirection(this) == 1;
    }

    /* access modifiers changed from: private */
    public void onPanelDragged(int i) {
        if (this.mSlideableView == null) {
            this.mSlideOffset = 0.0f;
            return;
        }
        boolean isLayoutRtlSupport = isLayoutRtlSupport();
        LayoutParams layoutParams = (LayoutParams) this.mSlideableView.getLayoutParams();
        int width = this.mSlideableView.getWidth();
        if (isLayoutRtlSupport) {
            i = (getWidth() - i) - width;
        }
        this.mSlideOffset = ((float) (i - ((isLayoutRtlSupport ? layoutParams.rightMargin : layoutParams.leftMargin) + (isLayoutRtlSupport ? getPaddingRight() : getPaddingLeft())))) / ((float) this.mSlideRange);
        if (this.mParallaxBy != 0) {
            parallaxOtherViews(this.mSlideOffset);
        }
        if (layoutParams.dimWhenOffset) {
            dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
        }
        dispatchOnPanelSlide(this.mSlideableView);
    }

    private boolean openPane(View view, int i) {
        if (!this.mFirstLayout && !smoothSlideTo(1.0f, i)) {
            return false;
        }
        this.mPreservedOpenState = true;
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:8:0x0021  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parallaxOtherViews(float r10) {
        /*
            r9 = this;
            r1 = 0
            r8 = 1065353216(0x3f800000, float:1.0)
            boolean r3 = r9.isLayoutRtlSupport()
            android.view.View r0 = r9.mSlideableView
            android.view.ViewGroup$LayoutParams r0 = r0.getLayoutParams()
            android.support.v4.widget.SlidingPaneLayout$LayoutParams r0 = (android.support.v4.widget.SlidingPaneLayout.LayoutParams) r0
            boolean r2 = r0.dimWhenOffset
            if (r2 == 0) goto L_0x0030
            if (r3 == 0) goto L_0x002d
            int r0 = r0.rightMargin
        L_0x0017:
            if (r0 > 0) goto L_0x0030
            r0 = 1
        L_0x001a:
            int r4 = r9.getChildCount()
            r2 = r1
        L_0x001f:
            if (r2 >= r4) goto L_0x005d
            android.view.View r5 = r9.getChildAt(r2)
            android.view.View r1 = r9.mSlideableView
            if (r5 != r1) goto L_0x0032
        L_0x0029:
            int r1 = r2 + 1
            r2 = r1
            goto L_0x001f
        L_0x002d:
            int r0 = r0.leftMargin
            goto L_0x0017
        L_0x0030:
            r0 = r1
            goto L_0x001a
        L_0x0032:
            float r1 = r9.mParallaxOffset
            float r1 = r8 - r1
            int r6 = r9.mParallaxBy
            float r6 = (float) r6
            float r1 = r1 * r6
            int r1 = (int) r1
            r9.mParallaxOffset = r10
            float r6 = r8 - r10
            int r7 = r9.mParallaxBy
            float r7 = (float) r7
            float r6 = r6 * r7
            int r6 = (int) r6
            int r1 = r1 - r6
            if (r3 == 0) goto L_0x0048
            int r1 = -r1
        L_0x0048:
            r5.offsetLeftAndRight(r1)
            if (r0 == 0) goto L_0x0029
            if (r3 == 0) goto L_0x0058
            float r1 = r9.mParallaxOffset
            float r1 = r1 - r8
        L_0x0052:
            int r6 = r9.mCoveredFadeColor
            r9.dimChildView(r5, r1, r6)
            goto L_0x0029
        L_0x0058:
            float r1 = r9.mParallaxOffset
            float r1 = r8 - r1
            goto L_0x0052
        L_0x005d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.widget.SlidingPaneLayout.parallaxOtherViews(float):void");
    }

    private static boolean viewIsOpaque(View view) {
        if (ViewCompat.isOpaque(view)) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= 18) {
            return false;
        }
        Drawable background = view.getBackground();
        if (background != null) {
            return background.getOpacity() == -1;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean canScroll(View view, boolean z, int i, int i2, int i3) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int scrollX = view.getScrollX();
            int scrollY = view.getScrollY();
            for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = viewGroup.getChildAt(childCount);
                if (i2 + scrollX >= childAt.getLeft() && i2 + scrollX < childAt.getRight() && i3 + scrollY >= childAt.getTop() && i3 + scrollY < childAt.getBottom()) {
                    if (canScroll(childAt, true, i, (i2 + scrollX) - childAt.getLeft(), (i3 + scrollY) - childAt.getTop())) {
                        return true;
                    }
                }
            }
        }
        if (z) {
            if (!isLayoutRtlSupport()) {
                i = -i;
            }
            return ViewCompat.canScrollHorizontally(view, i);
        }
    }

    @Deprecated
    public boolean canSlide() {
        return this.mCanSlide;
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return (layoutParams instanceof LayoutParams) && super.checkLayoutParams(layoutParams);
    }

    public boolean closePane() {
        return closePane(this.mSlideableView, 0);
    }

    public void computeScroll() {
        if (!this.mDragHelper.continueSettling(true)) {
            return;
        }
        if (!this.mCanSlide) {
            this.mDragHelper.abort();
        } else {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnPanelClosed(View view) {
        if (this.mPanelSlideListener != null) {
            this.mPanelSlideListener.onPanelClosed(view);
        }
        sendAccessibilityEvent(32);
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnPanelOpened(View view) {
        if (this.mPanelSlideListener != null) {
            this.mPanelSlideListener.onPanelOpened(view);
        }
        sendAccessibilityEvent(32);
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnPanelSlide(View view) {
        if (this.mPanelSlideListener != null) {
            this.mPanelSlideListener.onPanelSlide(view, this.mSlideOffset);
        }
    }

    public void draw(Canvas canvas) {
        int left;
        int i;
        super.draw(canvas);
        Drawable drawable = isLayoutRtlSupport() ? this.mShadowDrawableRight : this.mShadowDrawableLeft;
        View childAt = getChildCount() > 1 ? getChildAt(1) : null;
        if (childAt != null && drawable != null) {
            int top = childAt.getTop();
            int bottom = childAt.getBottom();
            int intrinsicWidth = drawable.getIntrinsicWidth();
            if (isLayoutRtlSupport()) {
                i = childAt.getRight();
                left = i + intrinsicWidth;
            } else {
                left = childAt.getLeft();
                i = left - intrinsicWidth;
            }
            drawable.setBounds(i, top, left, bottom);
            drawable.draw(canvas);
        }
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View view, long j) {
        boolean drawChild;
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int save = canvas.save(2);
        if (this.mCanSlide && !layoutParams.slideable && this.mSlideableView != null) {
            canvas.getClipBounds(this.mTmpRect);
            if (isLayoutRtlSupport()) {
                this.mTmpRect.left = Math.max(this.mTmpRect.left, this.mSlideableView.getRight());
            } else {
                this.mTmpRect.right = Math.min(this.mTmpRect.right, this.mSlideableView.getLeft());
            }
            canvas.clipRect(this.mTmpRect);
        }
        if (Build.VERSION.SDK_INT >= 11) {
            drawChild = super.drawChild(canvas, view, j);
        } else if (!layoutParams.dimWhenOffset || this.mSlideOffset <= 0.0f) {
            if (view.isDrawingCacheEnabled()) {
                view.setDrawingCacheEnabled(false);
            }
            drawChild = super.drawChild(canvas, view, j);
        } else {
            if (!view.isDrawingCacheEnabled()) {
                view.setDrawingCacheEnabled(true);
            }
            Bitmap drawingCache = view.getDrawingCache();
            if (drawingCache != null) {
                canvas.drawBitmap(drawingCache, (float) view.getLeft(), (float) view.getTop(), layoutParams.dimPaint);
                drawChild = false;
            } else {
                Log.e(TAG, "drawChild: child view " + view + " returned null drawing cache");
                drawChild = super.drawChild(canvas, view, j);
            }
        }
        canvas.restoreToCount(save);
        return drawChild;
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams) : new LayoutParams(layoutParams);
    }

    public int getCoveredFadeColor() {
        return this.mCoveredFadeColor;
    }

    public int getParallaxDistance() {
        return this.mParallaxBy;
    }

    public int getSliderFadeColor() {
        return this.mSliderFadeColor;
    }

    /* access modifiers changed from: package-private */
    public boolean isDimmed(View view) {
        if (view == null) {
            return false;
        }
        return this.mCanSlide && ((LayoutParams) view.getLayoutParams()).dimWhenOffset && this.mSlideOffset > 0.0f;
    }

    public boolean isOpen() {
        return !this.mCanSlide || this.mSlideOffset == 1.0f;
    }

    public boolean isSlideable() {
        return this.mCanSlide;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
        int size = this.mPostedRunnables.size();
        for (int i = 0; i < size; i++) {
            this.mPostedRunnables.get(i).run();
        }
        this.mPostedRunnables.clear();
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onInterceptTouchEvent(android.view.MotionEvent r8) {
        /*
            r7 = this;
            r2 = 0
            r1 = 1
            int r3 = android.support.v4.view.MotionEventCompat.getActionMasked(r8)
            boolean r0 = r7.mCanSlide
            if (r0 != 0) goto L_0x002d
            if (r3 != 0) goto L_0x002d
            int r0 = r7.getChildCount()
            if (r0 <= r1) goto L_0x002d
            android.view.View r0 = r7.getChildAt(r1)
            if (r0 == 0) goto L_0x002d
            android.support.v4.widget.ViewDragHelper r4 = r7.mDragHelper
            float r5 = r8.getX()
            int r5 = (int) r5
            float r6 = r8.getY()
            int r6 = (int) r6
            boolean r0 = r4.isViewUnder(r0, r5, r6)
            if (r0 != 0) goto L_0x0041
            r0 = r1
        L_0x002b:
            r7.mPreservedOpenState = r0
        L_0x002d:
            boolean r0 = r7.mCanSlide
            if (r0 == 0) goto L_0x0037
            boolean r0 = r7.mIsUnableToDrag
            if (r0 == 0) goto L_0x0043
            if (r3 == 0) goto L_0x0043
        L_0x0037:
            android.support.v4.widget.ViewDragHelper r0 = r7.mDragHelper
            r0.cancel()
            boolean r2 = super.onInterceptTouchEvent(r8)
        L_0x0040:
            return r2
        L_0x0041:
            r0 = r2
            goto L_0x002b
        L_0x0043:
            r0 = 3
            if (r3 == r0) goto L_0x0048
            if (r3 != r1) goto L_0x004e
        L_0x0048:
            android.support.v4.widget.ViewDragHelper r0 = r7.mDragHelper
            r0.cancel()
            goto L_0x0040
        L_0x004e:
            switch(r3) {
                case 0: goto L_0x005e;
                case 1: goto L_0x0051;
                case 2: goto L_0x0082;
                default: goto L_0x0051;
            }
        L_0x0051:
            r0 = r2
        L_0x0052:
            android.support.v4.widget.ViewDragHelper r3 = r7.mDragHelper
            boolean r3 = r3.shouldInterceptTouchEvent(r8)
            if (r3 != 0) goto L_0x005c
            if (r0 == 0) goto L_0x0040
        L_0x005c:
            r2 = r1
            goto L_0x0040
        L_0x005e:
            r7.mIsUnableToDrag = r2
            float r0 = r8.getX()
            float r3 = r8.getY()
            r7.mInitialMotionX = r0
            r7.mInitialMotionY = r3
            android.support.v4.widget.ViewDragHelper r4 = r7.mDragHelper
            android.view.View r5 = r7.mSlideableView
            int r0 = (int) r0
            int r3 = (int) r3
            boolean r0 = r4.isViewUnder(r5, r0, r3)
            if (r0 == 0) goto L_0x0051
            android.view.View r0 = r7.mSlideableView
            boolean r0 = r7.isDimmed(r0)
            if (r0 == 0) goto L_0x0051
            r0 = r1
            goto L_0x0052
        L_0x0082:
            float r0 = r8.getX()
            float r3 = r8.getY()
            float r4 = r7.mInitialMotionX
            float r0 = r0 - r4
            float r0 = java.lang.Math.abs(r0)
            float r4 = r7.mInitialMotionY
            float r3 = r3 - r4
            float r3 = java.lang.Math.abs(r3)
            android.support.v4.widget.ViewDragHelper r4 = r7.mDragHelper
            int r4 = r4.getTouchSlop()
            float r4 = (float) r4
            int r4 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r4 <= 0) goto L_0x0051
            int r0 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
            if (r0 <= 0) goto L_0x0051
            android.support.v4.widget.ViewDragHelper r0 = r7.mDragHelper
            r0.cancel()
            r7.mIsUnableToDrag = r1
            goto L_0x0040
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.widget.SlidingPaneLayout.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int width;
        int i9;
        boolean isLayoutRtlSupport = isLayoutRtlSupport();
        if (isLayoutRtlSupport) {
            this.mDragHelper.setEdgeTrackingEnabled(2);
        } else {
            this.mDragHelper.setEdgeTrackingEnabled(1);
        }
        int i10 = i3 - i;
        int paddingRight = isLayoutRtlSupport ? getPaddingRight() : getPaddingLeft();
        int paddingLeft = isLayoutRtlSupport ? getPaddingLeft() : getPaddingRight();
        int paddingTop = getPaddingTop();
        int childCount = getChildCount();
        if (this.mFirstLayout) {
            this.mSlideOffset = (!this.mCanSlide || !this.mPreservedOpenState) ? 0.0f : 1.0f;
        }
        int i11 = 0;
        int i12 = paddingRight;
        int i13 = paddingRight;
        while (i11 < childCount) {
            View childAt = getChildAt(i11);
            if (childAt.getVisibility() == 8) {
                width = i12;
                i9 = i13;
            } else {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int measuredWidth = childAt.getMeasuredWidth();
                if (layoutParams.slideable) {
                    int min = (Math.min(i12, (i10 - paddingLeft) - this.mOverhangSize) - i13) - (layoutParams.leftMargin + layoutParams.rightMargin);
                    this.mSlideRange = min;
                    int i14 = isLayoutRtlSupport ? layoutParams.rightMargin : layoutParams.leftMargin;
                    layoutParams.dimWhenOffset = ((i13 + i14) + min) + (measuredWidth / 2) > i10 - paddingLeft;
                    int i15 = (int) (((float) min) * this.mSlideOffset);
                    i6 = i13 + i14 + i15;
                    this.mSlideOffset = ((float) i15) / ((float) this.mSlideRange);
                    i5 = 0;
                } else if (!this.mCanSlide || this.mParallaxBy == 0) {
                    i5 = 0;
                    i6 = i12;
                } else {
                    i5 = (int) ((1.0f - this.mSlideOffset) * ((float) this.mParallaxBy));
                    i6 = i12;
                }
                if (isLayoutRtlSupport) {
                    i8 = (i10 - i6) + i5;
                    i7 = i8 - measuredWidth;
                } else {
                    i7 = i6 - i5;
                    i8 = i7 + measuredWidth;
                }
                childAt.layout(i7, paddingTop, i8, childAt.getMeasuredHeight() + paddingTop);
                width = childAt.getWidth() + i12;
                i9 = i6;
            }
            i11++;
            i12 = width;
            i13 = i9;
        }
        if (this.mFirstLayout) {
            if (this.mCanSlide) {
                if (this.mParallaxBy != 0) {
                    parallaxOtherViews(this.mSlideOffset);
                }
                if (((LayoutParams) this.mSlideableView.getLayoutParams()).dimWhenOffset) {
                    dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
                }
            } else {
                for (int i16 = 0; i16 < childCount; i16++) {
                    dimChildView(getChildAt(i16), 0.0f, this.mSliderFadeColor);
                }
            }
            updateObscuredViewsVisibility(this.mSlideableView);
        }
        this.mFirstLayout = false;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int paddingTop;
        float f;
        int i6;
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size2 = View.MeasureSpec.getSize(i2);
        if (mode == 1073741824) {
            if (mode2 == 0) {
                if (!isInEditMode()) {
                    throw new IllegalStateException("Height must not be UNSPECIFIED");
                } else if (mode2 == 0) {
                    i3 = 300;
                    i4 = Integer.MIN_VALUE;
                    i5 = size;
                }
            }
            i3 = size2;
            i4 = mode2;
            i5 = size;
        } else if (!isInEditMode()) {
            throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
        } else if (mode == Integer.MIN_VALUE) {
            i3 = size2;
            i4 = mode2;
            i5 = size;
        } else {
            if (mode == 0) {
                i3 = size2;
                i4 = mode2;
                i5 = 300;
            }
            i3 = size2;
            i4 = mode2;
            i5 = size;
        }
        int i7 = 0;
        switch (i4) {
            case Integer.MIN_VALUE:
                paddingTop = (i3 - getPaddingTop()) - getPaddingBottom();
                break;
            case FinalSound.Face.ALL /*1073741824*/:
                int paddingTop2 = (i3 - getPaddingTop()) - getPaddingBottom();
                paddingTop = paddingTop2;
                i7 = paddingTop2;
                break;
            default:
                paddingTop = -1;
                break;
        }
        float f2 = 0.0f;
        boolean z = false;
        int paddingLeft = (i5 - getPaddingLeft()) - getPaddingRight();
        int childCount = getChildCount();
        if (childCount > 2) {
            Log.e(TAG, "onMeasure: More than two child views are not supported.");
        }
        this.mSlideableView = null;
        int i8 = 0;
        int i9 = paddingLeft;
        int i10 = i7;
        while (i8 < childCount) {
            View childAt = getChildAt(i8);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            if (childAt.getVisibility() == 8) {
                layoutParams.dimWhenOffset = false;
                i6 = i9;
                f = f2;
            } else {
                if (layoutParams.weight > 0.0f) {
                    f = layoutParams.weight + f2;
                    if (layoutParams.width == 0) {
                        i6 = i9;
                    }
                } else {
                    f = f2;
                }
                int i11 = layoutParams.leftMargin + layoutParams.rightMargin;
                childAt.measure(layoutParams.width == -2 ? View.MeasureSpec.makeMeasureSpec(paddingLeft - i11, Integer.MIN_VALUE) : layoutParams.width == -1 ? View.MeasureSpec.makeMeasureSpec(paddingLeft - i11, FinalSound.Face.ALL) : View.MeasureSpec.makeMeasureSpec(layoutParams.width, FinalSound.Face.ALL), layoutParams.height == -2 ? View.MeasureSpec.makeMeasureSpec(paddingTop, Integer.MIN_VALUE) : layoutParams.height == -1 ? View.MeasureSpec.makeMeasureSpec(paddingTop, FinalSound.Face.ALL) : View.MeasureSpec.makeMeasureSpec(layoutParams.height, FinalSound.Face.ALL));
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                if (i4 == Integer.MIN_VALUE && measuredHeight > i10) {
                    i10 = Math.min(measuredHeight, paddingTop);
                }
                int i12 = i9 - measuredWidth;
                boolean z2 = i12 < 0;
                layoutParams.slideable = z2;
                boolean z3 = z2 | z;
                if (layoutParams.slideable) {
                    this.mSlideableView = childAt;
                }
                i6 = i12;
                z = z3;
            }
            i8++;
            i9 = i6;
            f2 = f;
        }
        if (z || f2 > 0.0f) {
            int i13 = paddingLeft - this.mOverhangSize;
            for (int i14 = 0; i14 < childCount; i14++) {
                View childAt2 = getChildAt(i14);
                if (childAt2.getVisibility() != 8) {
                    LayoutParams layoutParams2 = (LayoutParams) childAt2.getLayoutParams();
                    if (childAt2.getVisibility() != 8) {
                        boolean z4 = layoutParams2.width == 0 && layoutParams2.weight > 0.0f;
                        int measuredWidth2 = z4 ? 0 : childAt2.getMeasuredWidth();
                        if (!z || childAt2 == this.mSlideableView) {
                            if (layoutParams2.weight > 0.0f) {
                                int makeMeasureSpec = layoutParams2.width == 0 ? layoutParams2.height == -2 ? View.MeasureSpec.makeMeasureSpec(paddingTop, Integer.MIN_VALUE) : layoutParams2.height == -1 ? View.MeasureSpec.makeMeasureSpec(paddingTop, FinalSound.Face.ALL) : View.MeasureSpec.makeMeasureSpec(layoutParams2.height, FinalSound.Face.ALL) : View.MeasureSpec.makeMeasureSpec(childAt2.getMeasuredHeight(), FinalSound.Face.ALL);
                                if (z) {
                                    int i15 = paddingLeft - (layoutParams2.rightMargin + layoutParams2.leftMargin);
                                    int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(i15, FinalSound.Face.ALL);
                                    if (measuredWidth2 != i15) {
                                        childAt2.measure(makeMeasureSpec2, makeMeasureSpec);
                                    }
                                } else {
                                    childAt2.measure(View.MeasureSpec.makeMeasureSpec(((int) ((layoutParams2.weight * ((float) Math.max(0, i9))) / f2)) + measuredWidth2, FinalSound.Face.ALL), makeMeasureSpec);
                                }
                            }
                        } else if (layoutParams2.width < 0 && (measuredWidth2 > i13 || layoutParams2.weight > 0.0f)) {
                            childAt2.measure(View.MeasureSpec.makeMeasureSpec(i13, FinalSound.Face.ALL), z4 ? layoutParams2.height == -2 ? View.MeasureSpec.makeMeasureSpec(paddingTop, Integer.MIN_VALUE) : layoutParams2.height == -1 ? View.MeasureSpec.makeMeasureSpec(paddingTop, FinalSound.Face.ALL) : View.MeasureSpec.makeMeasureSpec(layoutParams2.height, FinalSound.Face.ALL) : View.MeasureSpec.makeMeasureSpec(childAt2.getMeasuredHeight(), FinalSound.Face.ALL));
                        }
                    }
                }
            }
        }
        setMeasuredDimension(i5, getPaddingTop() + i10 + getPaddingBottom());
        this.mCanSlide = z;
        if (this.mDragHelper.getViewDragState() != 0 && !z) {
            this.mDragHelper.abort();
        }
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (savedState.isOpen) {
            openPane();
        } else {
            closePane();
        }
        this.mPreservedOpenState = savedState.isOpen;
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.isOpen = isSlideable() ? isOpen() : this.mPreservedOpenState;
        return savedState;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i != i3) {
            this.mFirstLayout = true;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mCanSlide) {
            return super.onTouchEvent(motionEvent);
        }
        this.mDragHelper.processTouchEvent(motionEvent);
        switch (motionEvent.getAction() & 255) {
            case 0:
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                return true;
            case 1:
                if (!isDimmed(this.mSlideableView)) {
                    return true;
                }
                float x2 = motionEvent.getX();
                float y2 = motionEvent.getY();
                float f = x2 - this.mInitialMotionX;
                float f2 = y2 - this.mInitialMotionY;
                int touchSlop = this.mDragHelper.getTouchSlop();
                if ((f * f) + (f2 * f2) >= ((float) (touchSlop * touchSlop)) || !this.mDragHelper.isViewUnder(this.mSlideableView, (int) x2, (int) y2)) {
                    return true;
                }
                closePane(this.mSlideableView, 0);
                return true;
            default:
                return true;
        }
    }

    public boolean openPane() {
        return openPane(this.mSlideableView, 0);
    }

    public void requestChildFocus(View view, View view2) {
        super.requestChildFocus(view, view2);
        if (!isInTouchMode() && !this.mCanSlide) {
            this.mPreservedOpenState = view == this.mSlideableView;
        }
    }

    /* access modifiers changed from: package-private */
    public void setAllChildrenVisible() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 4) {
                childAt.setVisibility(0);
            }
        }
    }

    public void setCoveredFadeColor(int i) {
        this.mCoveredFadeColor = i;
    }

    public void setPanelSlideListener(PanelSlideListener panelSlideListener) {
        this.mPanelSlideListener = panelSlideListener;
    }

    public void setParallaxDistance(int i) {
        this.mParallaxBy = i;
        requestLayout();
    }

    @Deprecated
    public void setShadowDrawable(Drawable drawable) {
        setShadowDrawableLeft(drawable);
    }

    public void setShadowDrawableLeft(Drawable drawable) {
        this.mShadowDrawableLeft = drawable;
    }

    public void setShadowDrawableRight(Drawable drawable) {
        this.mShadowDrawableRight = drawable;
    }

    @Deprecated
    public void setShadowResource(int i) {
        setShadowDrawable(getResources().getDrawable(i));
    }

    public void setShadowResourceLeft(int i) {
        setShadowDrawableLeft(getResources().getDrawable(i));
    }

    public void setShadowResourceRight(int i) {
        setShadowDrawableRight(getResources().getDrawable(i));
    }

    public void setSliderFadeColor(int i) {
        this.mSliderFadeColor = i;
    }

    @Deprecated
    public void smoothSlideClosed() {
        closePane();
    }

    @Deprecated
    public void smoothSlideOpen() {
        openPane();
    }

    /* access modifiers changed from: package-private */
    public boolean smoothSlideTo(float f, int i) {
        int paddingLeft;
        if (!this.mCanSlide) {
            return false;
        }
        boolean isLayoutRtlSupport = isLayoutRtlSupport();
        LayoutParams layoutParams = (LayoutParams) this.mSlideableView.getLayoutParams();
        if (isLayoutRtlSupport) {
            paddingLeft = (int) (((float) getWidth()) - ((((float) (layoutParams.rightMargin + getPaddingRight())) + (((float) this.mSlideRange) * f)) + ((float) this.mSlideableView.getWidth())));
        } else {
            paddingLeft = (int) (((float) (layoutParams.leftMargin + getPaddingLeft())) + (((float) this.mSlideRange) * f));
        }
        if (!this.mDragHelper.smoothSlideViewTo(this.mSlideableView, paddingLeft, this.mSlideableView.getTop())) {
            return false;
        }
        setAllChildrenVisible();
        ViewCompat.postInvalidateOnAnimation(this);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void updateObscuredViewsVisibility(View view) {
        int i;
        int i2;
        int i3;
        int i4;
        boolean isLayoutRtlSupport = isLayoutRtlSupport();
        int width = isLayoutRtlSupport ? getWidth() - getPaddingRight() : getPaddingLeft();
        int paddingLeft = isLayoutRtlSupport ? getPaddingLeft() : getWidth() - getPaddingRight();
        int paddingTop = getPaddingTop();
        int height = getHeight() - getPaddingBottom();
        if (view == null || !viewIsOpaque(view)) {
            i = 0;
            i2 = 0;
            i3 = 0;
            i4 = 0;
        } else {
            i4 = view.getLeft();
            i3 = view.getRight();
            int top = view.getTop();
            i = view.getBottom();
            i2 = top;
        }
        int childCount = getChildCount();
        int i5 = 0;
        while (i5 < childCount) {
            View childAt = getChildAt(i5);
            if (childAt != view) {
                childAt.setVisibility((Math.max(isLayoutRtlSupport ? paddingLeft : width, childAt.getLeft()) < i4 || Math.max(paddingTop, childAt.getTop()) < i2 || Math.min(isLayoutRtlSupport ? width : paddingLeft, childAt.getRight()) > i3 || Math.min(height, childAt.getBottom()) > i) ? 0 : 4);
                i5++;
            } else {
                return;
            }
        }
    }
}
