package com.syu.ctrl;

import android.content.Context;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.syu.app.MyUi;
import com.syu.data.FinalChip;
import com.syu.page.IPageNotify;
import com.syu.util.MyFolder;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class JGridView extends GridView {
    public GridAdapter adapter;
    public boolean bDefaultSet = true;
    public JGridView gridView;
    /* access modifiers changed from: private */
    public int idPageRow;
    /* access modifiers changed from: private */
    public List<SparseArray<String>> list = new ArrayList();
    private List<MyFolder> listFolder = new ArrayList();
    public boolean mActionDown = false;
    public int mBegin = 0;
    public int mCnt = 0;
    public boolean mInScrollView = false;
    public int mIndexFocus = 0;
    public int mMaxRow = 1;
    public ArrayList<WeakReference<JPage>> mPageInfos = new ArrayList<>();
    public int mPosition;
    /* access modifiers changed from: private */
    public int mScrollState;
    /* access modifiers changed from: private */
    public SwipeGridViewHelper mSwipeHelper;
    public JPage page;
    public String strTail = FinalChip.BSP_PLATFORM_Null;
    public String[] strs = null;
    /* access modifiers changed from: private */
    public MyUi ui;

    public class GridAdapter extends BaseAdapter {
        private JGridView mGridView;

        public GridAdapter() {
        }

        public int getCount() {
            return JGridView.this.list != null ? JGridView.this.list.size() : JGridView.this.mCnt;
        }

        public JGridView getGridView() {
            return this.mGridView;
        }

        public Object getItem(int i) {
            return null;
        }

        public long getItemId(int i) {
            return (long) i;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0076, code lost:
            r1 = r0.getStrDrawableExtra();
         */
        /* JADX WARNING: Removed duplicated region for block: B:54:0x0124  */
        /* JADX WARNING: Removed duplicated region for block: B:59:0x0166  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public android.view.View getView(int r12, android.view.View r13, android.view.ViewGroup r14) {
            /*
                r11 = this;
                r2 = 0
                r6 = 1
                r7 = 0
                com.syu.ctrl.JGridView r3 = r11.getGridView()
                if (r13 != 0) goto L_0x00d7
                com.syu.ctrl.JGridView r0 = com.syu.ctrl.JGridView.this
                com.syu.app.MyUi r0 = r0.ui
                com.syu.ctrl.JGridView r1 = com.syu.ctrl.JGridView.this
                int r1 = r1.idPageRow
                com.syu.ctrl.JPage r13 = r0.loadPage(r7, r1)
                java.lang.Object r0 = r13.getTag()
                com.syu.app.MyUiItem r0 = (com.syu.app.MyUiItem) r0
                android.widget.FrameLayout$LayoutParams r1 = r0.mLayoutParams
                if (r1 == 0) goto L_0x00c5
                android.widget.AbsListView$LayoutParams r1 = new android.widget.AbsListView$LayoutParams
                android.widget.FrameLayout$LayoutParams r4 = r0.mLayoutParams
                int r4 = r4.width
                android.widget.FrameLayout$LayoutParams r0 = r0.mLayoutParams
                int r0 = r0.height
                r1.<init>(r4, r0)
                r13.setLayoutParams(r1)
            L_0x0033:
                com.syu.ctrl.JGridView r0 = com.syu.ctrl.JGridView.this
                com.syu.ctrl.SwipeGridViewHelper r0 = r0.mSwipeHelper
                if (r0 == 0) goto L_0x0040
                if (r13 == 0) goto L_0x0040
                r13.setScrollX(r7)
            L_0x0040:
                java.util.ArrayList r1 = new java.util.ArrayList
                r1.<init>()
                com.syu.ctrl.JGridView r0 = com.syu.ctrl.JGridView.this
                java.util.ArrayList<java.lang.ref.WeakReference<com.syu.ctrl.JPage>> r0 = r0.mPageInfos
                java.util.Iterator r4 = r0.iterator()
            L_0x004d:
                boolean r0 = r4.hasNext()
                if (r0 != 0) goto L_0x00db
                r0 = r7
            L_0x0054:
                if (r0 != 0) goto L_0x006c
                r13.setGridView(r3)
                com.syu.ctrl.JGridView r0 = com.syu.ctrl.JGridView.this
                java.util.ArrayList<java.lang.ref.WeakReference<com.syu.ctrl.JPage>> r0 = r0.mPageInfos
                r0.removeAll(r1)
                java.lang.ref.WeakReference r0 = new java.lang.ref.WeakReference
                r0.<init>(r13)
                com.syu.ctrl.JGridView r1 = com.syu.ctrl.JGridView.this
                java.util.ArrayList<java.lang.ref.WeakReference<com.syu.ctrl.JPage>> r1 = r1.mPageInfos
                r1.add(r0)
            L_0x006c:
                java.lang.Object r0 = r13.getTag()
                com.syu.app.MyUiItem r0 = (com.syu.app.MyUiItem) r0
                r1 = r12 & 1
                if (r1 == 0) goto L_0x0199
                java.lang.String[] r1 = r0.getStrDrawableExtra()
                if (r1 == 0) goto L_0x0199
                r1 = r1[r7]
            L_0x007e:
                if (r1 != 0) goto L_0x0196
                java.lang.String r0 = r0.getStrDrawable()
            L_0x0084:
                r13.setChildTag(r12)
                java.lang.String r1 = r13.getStrDrawable()
                if (r1 == r0) goto L_0x0092
                if (r0 == 0) goto L_0x0092
                r13.setStrDrawable(r0, r6)
            L_0x0092:
                com.syu.ctrl.JGridView r0 = com.syu.ctrl.JGridView.this
                java.util.List r0 = r0.list
                if (r0 == 0) goto L_0x0181
                com.syu.ctrl.JGridView r0 = com.syu.ctrl.JGridView.this
                java.util.List r0 = r0.list
                java.lang.Object r0 = r0.get(r12)
                android.util.SparseArray r0 = (android.util.SparseArray) r0
                com.syu.ctrl.JPage r1 = r3.getPage()
                com.syu.page.IPageNotify r1 = r1.getNotify()
                if (r1 == 0) goto L_0x00b7
                com.syu.ctrl.JGridView r2 = r11.getGridView()
                r1.InitGridItem(r2, r13, r0, r12)
            L_0x00b7:
                com.syu.ctrl.JGridView r1 = com.syu.ctrl.JGridView.this
                boolean r1 = r1.bDefaultSet
                if (r1 == 0) goto L_0x00c4
                int r9 = r0.size()
                r8 = r7
            L_0x00c2:
                if (r8 < r9) goto L_0x00f5
            L_0x00c4:
                return r13
            L_0x00c5:
                android.widget.AbsListView$LayoutParams r1 = new android.widget.AbsListView$LayoutParams
                int r4 = r0.getWidth()
                int r0 = r0.getHeight()
                r1.<init>(r4, r0)
                r13.setLayoutParams(r1)
                goto L_0x0033
            L_0x00d7:
                com.syu.ctrl.JPage r13 = (com.syu.ctrl.JPage) r13
                goto L_0x0033
            L_0x00db:
                java.lang.Object r0 = r4.next()
                java.lang.ref.WeakReference r0 = (java.lang.ref.WeakReference) r0
                java.lang.Object r5 = r0.get()
                if (r5 != r13) goto L_0x00ea
                r0 = r6
                goto L_0x0054
            L_0x00ea:
                java.lang.Object r5 = r0.get()
                if (r5 != 0) goto L_0x004d
                r1.add(r0)
                goto L_0x004d
            L_0x00f5:
                int r10 = r0.keyAt(r8)
                android.view.View r1 = r13.getChildViewByid(r10)
                com.syu.ctrl.JText r1 = (com.syu.ctrl.JText) r1
                if (r1 == 0) goto L_0x0151
                int r2 = r1.getVisibility()
                if (r2 != 0) goto L_0x0151
                java.lang.String r4 = ". "
                com.syu.ctrl.JGridView r2 = com.syu.ctrl.JGridView.this
                java.lang.String r3 = r2.strTail
                java.lang.Object r2 = r1.getTag()
                com.syu.app.MyUiItem r2 = (com.syu.app.MyUiItem) r2
                int[] r5 = r2.getPara()
                if (r5 == 0) goto L_0x0194
                int[] r5 = r2.getPara()
                r5 = r5[r7]
                if (r5 != r6) goto L_0x0156
                r5 = r6
            L_0x0122:
                if (r5 == 0) goto L_0x0166
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                com.syu.ctrl.JGridView r5 = com.syu.ctrl.JGridView.this
                int r5 = r5.mBegin
                int r5 = r5 + r12
                int r5 = r5 + 1
                java.lang.String r5 = java.lang.String.valueOf(r5)
                java.lang.String r5 = java.lang.String.valueOf(r5)
                r2.<init>(r5)
                java.lang.StringBuilder r4 = r2.append(r4)
                java.lang.Object r2 = r0.get(r10)
                java.lang.String r2 = (java.lang.String) r2
                java.lang.StringBuilder r2 = r4.append(r2)
                java.lang.StringBuilder r2 = r2.append(r3)
                java.lang.String r2 = r2.toString()
                r1.setText(r2)
            L_0x0151:
                int r1 = r8 + 1
                r8 = r1
                goto L_0x00c2
            L_0x0156:
                int[] r2 = r2.getPara()
                r2 = r2[r7]
                r5 = 2
                if (r2 != r5) goto L_0x0194
                java.lang.String r4 = ""
                java.lang.String r2 = ""
                r3 = r2
                r5 = r6
                goto L_0x0122
            L_0x0166:
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                java.lang.Object r2 = r0.get(r10)
                java.lang.String r2 = (java.lang.String) r2
                java.lang.String r2 = java.lang.String.valueOf(r2)
                r4.<init>(r2)
                java.lang.StringBuilder r2 = r4.append(r3)
                java.lang.String r2 = r2.toString()
                r1.setText(r2)
                goto L_0x0151
            L_0x0181:
                com.syu.ctrl.JPage r0 = r3.getPage()
                com.syu.page.IPageNotify r0 = r0.getNotify()
                if (r0 == 0) goto L_0x00c4
                com.syu.ctrl.JGridView r1 = r11.getGridView()
                r0.InitGridItem(r1, r13, r2, r12)
                goto L_0x00c4
            L_0x0194:
                r5 = r7
                goto L_0x0122
            L_0x0196:
                r0 = r1
                goto L_0x0084
            L_0x0199:
                r1 = r2
                goto L_0x007e
            */
            throw new UnsupportedOperationException("Method not decompiled: com.syu.ctrl.JGridView.GridAdapter.getView(int, android.view.View, android.view.ViewGroup):android.view.View");
        }

        public void setGridView(JGridView jGridView) {
            this.mGridView = jGridView;
        }
    }

    public JGridView(Context context, JPage jPage, MyUi myUi) {
        super(context);
        this.page = jPage;
        this.ui = myUi;
        this.gridView = this;
        this.adapter = new GridAdapter();
        this.adapter.setGridView(this);
        setAdapter(this.adapter);
        setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            }

            public void onScrollStateChanged(AbsListView absListView, int i) {
                IPageNotify notify;
                if (JGridView.this.mScrollState != 1 && i == 1) {
                    JGridView.this.notifyDataSetChanged();
                }
                JGridView.this.mScrollState = i;
                switch (JGridView.this.mScrollState) {
                    case 0:
                        if (absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                            IPageNotify notify2 = JGridView.this.getPage().getNotify();
                            if (notify2 != null) {
                                notify2.LoadGridItemWhenPullDown(JGridView.this.gridView);
                                return;
                            }
                            return;
                        } else if (absListView.getFirstVisiblePosition() == 0 && (notify = JGridView.this.getPage().getNotify()) != null) {
                            notify.LoadGridItemWhenPullUp(JGridView.this.gridView);
                            return;
                        } else {
                            return;
                        }
                    default:
                        return;
                }
            }
        });
    }

    public int getCount() {
        return this.list != null ? this.list.size() : this.mCnt;
    }

    public List<SparseArray<String>> getList() {
        return this.list;
    }

    public List<MyFolder> getListFolder() {
        return this.listFolder;
    }

    public int getMaxRowDisp() {
        return this.mMaxRow;
    }

    public JPage getPage() {
        return this.page;
    }

    public int getPosition() {
        return this.mPosition;
    }

    public void initSwipePara(int i) {
        if (i > 0) {
            this.mSwipeHelper = new SwipeGridViewHelper(this, i);
        }
    }

    public void notifyDataSetChanged() {
        if (this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.mSwipeHelper == null || !this.mSwipeHelper.onInterceptTouchEvent(motionEvent)) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (this.mInScrollView) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(536870911, Integer.MIN_VALUE));
        } else {
            super.onMeasure(i, i2);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                this.mActionDown = true;
                break;
            case 1:
            case 3:
                this.mActionDown = false;
                break;
        }
        if (this.mSwipeHelper == null || !this.mSwipeHelper.onTouchEvent(motionEvent)) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }

    public void scrollToPosition(int i) {
        if (!this.mActionDown && i >= 0) {
            setAdapter(this.adapter);
            this.adapter.notifyDataSetChanged();
            setSelection(i);
        }
    }

    public void setMaxRowDisp(int i) {
        this.mMaxRow = i;
    }

    public void setPageRow(int i) {
        this.idPageRow = i;
    }

    public void setPosition(int i) {
        this.mPosition = i;
        this.mIndexFocus = i;
    }

    public void setStrs(String[] strArr, int i, int i2) {
        this.list = null;
        this.strs = strArr;
        this.mBegin = i;
        this.mCnt = i2 - i;
        if (this.mCnt < 0) {
            this.mCnt = 0;
        }
        notifyDataSetChanged();
    }

    public void superTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
    }
}
