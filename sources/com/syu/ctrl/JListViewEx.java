package com.syu.ctrl;

import android.content.Context;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import com.syu.app.MyUi;
import com.syu.app.MyUiItem;
import com.syu.page.IPageNotify;
import com.syu.util.MyFolder;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JListViewEx extends ExpandableListView {
    public ExtAdapter adapter;
    public int idPageRowChild;
    public int idPageRowGroup;
    /* access modifiers changed from: private */
    public List<MyFolder> listFolder = new ArrayList();
    public JListViewEx listViewEx;
    public boolean mActionDown = false;
    public ArrayList<WeakReference<JPage>> mPageInfos = new ArrayList<>();
    public int mPositionChild;
    public int mPositionGroup;
    /* access modifiers changed from: private */
    public int mScrollState;
    /* access modifiers changed from: private */
    public SwipeListViewHelper mSwipeHelper;
    private JPage page;
    /* access modifiers changed from: private */
    public MyUi ui;

    public class ExtAdapter extends BaseExpandableListAdapter {
        private JListViewEx mListViewEx;

        public ExtAdapter() {
        }

        public Object getChild(int i, int i2) {
            return ((MyFolder) JListViewEx.this.listFolder.get(i)).array.get(i2);
        }

        public long getChildId(int i, int i2) {
            return (long) i2;
        }

        public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
            JPage jPage;
            boolean z2;
            String[] strDrawableExtra;
            SparseArray sparseArray = ((MyFolder) JListViewEx.this.listFolder.get(i)).array.get(i2);
            JListViewEx listViewEx = getListViewEx();
            if (view == null) {
                jPage = JListViewEx.this.ui.loadPage(false, JListViewEx.this.idPageRowChild);
                MyUiItem myUiItem = (MyUiItem) jPage.getTag();
                if (myUiItem.mLayoutParams != null) {
                    jPage.setLayoutParams(new AbsListView.LayoutParams(myUiItem.mLayoutParams.width, myUiItem.mLayoutParams.height));
                } else {
                    jPage.setLayoutParams(new AbsListView.LayoutParams(myUiItem.getWidth(), myUiItem.getHeight()));
                }
            } else {
                jPage = (JPage) view;
            }
            if (!(JListViewEx.this.mSwipeHelper == null || jPage == null)) {
                jPage.setScrollX(0);
            }
            ArrayList arrayList = new ArrayList();
            Iterator<WeakReference<JPage>> it = JListViewEx.this.mPageInfos.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z2 = false;
                    break;
                }
                WeakReference next = it.next();
                if (next.get() == jPage) {
                    z2 = true;
                    break;
                } else if (next.get() == null) {
                    arrayList.add(next);
                }
            }
            if (!z2) {
                jPage.setListViewEx(listViewEx);
                JListViewEx.this.mPageInfos.removeAll(arrayList);
                JListViewEx.this.mPageInfos.add(new WeakReference(jPage));
            }
            MyUiItem myUiItem2 = (MyUiItem) jPage.getTag();
            String str = null;
            if (!((i2 & 1) == 0 || (strDrawableExtra = myUiItem2.getStrDrawableExtra()) == null)) {
                str = strDrawableExtra[0];
            }
            String strDrawable = str == null ? myUiItem2.getStrDrawable() : str;
            jPage.setGroupTag(i);
            jPage.setChildTag(i2);
            if (!(jPage.getStrDrawable() == strDrawable || strDrawable == null)) {
                jPage.setStrDrawable(strDrawable, true);
            }
            IPageNotify notify = listViewEx.getPage().getNotify();
            if (notify != null) {
                notify.InitChildItem(getListViewEx(), jPage, sparseArray, i2);
            }
            int size = sparseArray.size();
            for (int i3 = 0; i3 < size; i3++) {
                int keyAt = sparseArray.keyAt(i3);
                JText jText = (JText) jPage.getChildViewByid(keyAt);
                if (jText != null && jText.getVisibility() == 0) {
                    MyUiItem myUiItem3 = (MyUiItem) jText.getTag();
                    if (myUiItem3.getPara() != null && myUiItem3.getPara()[0] == 1) {
                        jText.setText(String.valueOf(String.valueOf(i2 + 1)) + ".  " + ((String) sparseArray.get(keyAt)));
                    } else {
                        jText.setText((CharSequence) sparseArray.get(keyAt));
                    }
                }
            }
            return jPage;
        }

        public int getChildrenCount(int i) {
            MyFolder myFolder = (MyFolder) JListViewEx.this.listFolder.get(i);
            if (myFolder != null) {
                return myFolder.array.size();
            }
            return 0;
        }

        public Object getGroup(int i) {
            return JListViewEx.this.listFolder.get(i);
        }

        public int getGroupCount() {
            return JListViewEx.this.listFolder.size();
        }

        public long getGroupId(int i) {
            return (long) i;
        }

        public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
            JPage jPage;
            WeakReference weakReference;
            String[] strDrawableExtra;
            String str = null;
            SparseArray<String> sparseArray = ((MyFolder) JListViewEx.this.listFolder.get(i)).map;
            JListViewEx listViewEx = getListViewEx();
            if (view == null) {
                jPage = JListViewEx.this.ui.loadPage(false, JListViewEx.this.idPageRowGroup);
                MyUiItem myUiItem = (MyUiItem) jPage.getTag();
                if (myUiItem.mLayoutParams != null) {
                    jPage.setLayoutParams(new AbsListView.LayoutParams(myUiItem.mLayoutParams.width, myUiItem.mLayoutParams.height));
                } else {
                    jPage.setLayoutParams(new AbsListView.LayoutParams(myUiItem.getWidth(), myUiItem.getHeight()));
                }
            } else {
                jPage = (JPage) view;
            }
            Iterator<WeakReference<JPage>> it = JListViewEx.this.mPageInfos.iterator();
            while (true) {
                if (it.hasNext()) {
                    weakReference = it.next();
                    if (weakReference.get() == jPage) {
                        break;
                    }
                } else {
                    weakReference = null;
                    break;
                }
            }
            if (weakReference != null) {
                jPage.setListViewEx((JListViewEx) null);
                JListViewEx.this.mPageInfos.remove(weakReference);
            }
            MyUiItem myUiItem2 = (MyUiItem) jPage.getTag();
            if (!((i & 1) == 0 || (strDrawableExtra = myUiItem2.getStrDrawableExtra()) == null)) {
                str = strDrawableExtra[0];
            }
            String strDrawable = str == null ? myUiItem2.getStrDrawable() : str;
            jPage.setChildTag(i);
            if (!(jPage.getStrDrawable() == strDrawable || strDrawable == null)) {
                jPage.setStrDrawable(strDrawable, true);
            }
            IPageNotify notify = listViewEx.getPage().getNotify();
            if (notify != null) {
                notify.InitGroupItem(getListViewEx(), jPage, sparseArray, i);
            }
            int size = sparseArray.size();
            for (int i2 = 0; i2 < size; i2++) {
                int keyAt = sparseArray.keyAt(i2);
                JText jText = (JText) jPage.getChildViewByid(keyAt);
                if (jText != null && jText.getVisibility() == 0) {
                    MyUiItem myUiItem3 = (MyUiItem) jText.getTag();
                    if (myUiItem3.getPara() != null && myUiItem3.getPara()[0] == 1) {
                        jText.setText(String.valueOf(String.valueOf(i + 1)) + ".  " + sparseArray.get(keyAt));
                    } else {
                        jText.setText(sparseArray.get(keyAt));
                    }
                }
            }
            return jPage;
        }

        public JListViewEx getListViewEx() {
            return this.mListViewEx;
        }

        public boolean hasStableIds() {
            return false;
        }

        public boolean isChildSelectable(int i, int i2) {
            return true;
        }

        public void setListViewEx(JListViewEx jListViewEx) {
            this.mListViewEx = jListViewEx;
        }
    }

    public JListViewEx(Context context, JPage jPage, MyUi myUi) {
        super(context);
        this.page = jPage;
        this.ui = myUi;
        this.listViewEx = this;
        this.adapter = new ExtAdapter();
        this.adapter.setListViewEx(this);
        setAdapter(this.adapter);
        setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            }

            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (JListViewEx.this.mScrollState != 1 && i == 1) {
                    JListViewEx.this.notifyDataSetChanged();
                }
                JListViewEx.this.mScrollState = i;
            }
        });
    }

    public int getChildrenCount(int i) {
        MyFolder myFolder;
        if (this.listFolder.size() <= i || (myFolder = this.listFolder.get(i)) == null) {
            return 0;
        }
        return myFolder.array.size();
    }

    public int getGroupCount() {
        return this.listFolder.size();
    }

    public List<MyFolder> getList() {
        return this.listFolder;
    }

    public JPage getPage() {
        return this.page;
    }

    public int getPositionChild() {
        return this.mPositionChild;
    }

    public int getPositionGroup() {
        return this.mPositionGroup;
    }

    public void initSwipePara(int i) {
        if (i > 0) {
            this.mSwipeHelper = new SwipeListViewHelper(this, i);
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
        if (!this.mActionDown && this.listFolder.size() > i) {
            for (int i2 = 0; i2 < getGroupCount(); i2++) {
                if (isGroupExpanded(i2)) {
                    collapseGroup(i2);
                }
            }
            setSelectionFromTop(i, 0);
            smoothScrollToPositionFromTop(i, 0);
        }
    }

    public void scrollToPosition(int i, int i2) {
        if (!this.mActionDown && this.listFolder.size() > i) {
            for (int i3 = 0; i3 < getGroupCount(); i3++) {
                if (i != i3 && isGroupExpanded(i3)) {
                    collapseGroup(i3);
                }
            }
            MyFolder myFolder = this.listFolder.get(i);
            if (myFolder != null && myFolder.array.size() > i2) {
                if (!isGroupExpanded(i)) {
                    expandGroup(i);
                }
                setSelectionFromTop(i + 1 + i2, 0);
                smoothScrollToPositionFromTop(i + 1 + i2, 0);
            }
        }
    }

    public void setPageRow(int i, int i2) {
        this.idPageRowGroup = i;
        this.idPageRowChild = i2;
    }

    public void setPositionChild(int i) {
        this.mPositionChild = i;
    }

    public void setPositionGroup(int i) {
        this.mPositionGroup = i;
    }

    public void superTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
    }
}
