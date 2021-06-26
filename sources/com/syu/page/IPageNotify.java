package com.syu.page;

import android.content.pm.ResolveInfo;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JGridView;
import com.syu.ctrl.JListApp;
import com.syu.ctrl.JListViewEx;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JViewPagerVertical;

public interface IPageNotify {
    void AppClick(JListApp jListApp, ResolveInfo resolveInfo);

    void GridClick(JGridView jGridView);

    void GridLongClick(JGridView jGridView);

    void GridPressed(JGridView jGridView, JPage jPage, boolean z);

    void InitAppGridView(GridView gridView);

    void InitAppItem(JListApp jListApp, JPage jPage, ResolveInfo resolveInfo);

    void InitChildItem(JListViewEx jListViewEx, JPage jPage, SparseArray<String> sparseArray, int i);

    void InitGridItem(JGridView jGridView, JPage jPage, SparseArray<String> sparseArray, int i);

    void InitGroupItem(JListViewEx jListViewEx, JPage jPage, SparseArray<String> sparseArray, int i);

    void ItemExPressed(JListViewEx jListViewEx, JPage jPage, boolean z);

    void ListExChildClick(JListViewEx jListViewEx);

    void LoadGridItemWhenPullDown(JGridView jGridView);

    void LoadGridItemWhenPullUp(JGridView jGridView);

    void ResponseClick(View view);

    boolean ResponseLongClick(View view);

    void dispatchTouchEvent(MotionEvent motionEvent);

    void init();

    void listAppPressed(JListApp jListApp, JPage jPage, boolean z);

    void onDismiss();

    boolean onKeyCodeBack();

    void onSizeChanged(int i, int i2, int i3, int i4);

    void pause();

    void resume();

    void setPressed(JButton jButton, boolean z);

    void viewPageNext(ViewPager viewPager);

    void viewPageNext(JViewPagerVertical jViewPagerVertical);

    void viewPagePrev(ViewPager viewPager);

    void viewPagePrev(JViewPagerVertical jViewPagerVertical);

    void viewPageScrolled(ViewPager viewPager);

    void viewPageScrolled(JViewPagerVertical jViewPagerVertical);
}
