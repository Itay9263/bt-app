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

public class Page implements IPageNotify {
    private JPage page;

    public Page(JPage jPage) {
        this.page = jPage;
    }

    public void AppClick(JListApp jListApp, ResolveInfo resolveInfo) {
    }

    public void GridClick(JGridView jGridView) {
    }

    public void GridLongClick(JGridView jGridView) {
    }

    public void GridPressed(JGridView jGridView, JPage jPage, boolean z) {
    }

    public void InitAppGridView(GridView gridView) {
    }

    public void InitAppItem(JListApp jListApp, JPage jPage, ResolveInfo resolveInfo) {
    }

    public void InitChildItem(JListViewEx jListViewEx, JPage jPage, SparseArray<String> sparseArray, int i) {
    }

    public void InitGridItem(JGridView jGridView, JPage jPage, SparseArray<String> sparseArray, int i) {
    }

    public void InitGroupItem(JListViewEx jListViewEx, JPage jPage, SparseArray<String> sparseArray, int i) {
    }

    public void ItemExPressed(JListViewEx jListViewEx, JPage jPage, boolean z) {
    }

    public void ListExChildClick(JListViewEx jListViewEx) {
    }

    public void LoadGridItemWhenPullDown(JGridView jGridView) {
    }

    public void LoadGridItemWhenPullUp(JGridView jGridView) {
    }

    public void ResponseClick(View view) {
    }

    public boolean ResponseLongClick(View view) {
        return false;
    }

    public void dispatchTouchEvent(MotionEvent motionEvent) {
    }

    public JPage getPage() {
        return this.page;
    }

    public void init() {
    }

    public void listAppPressed(JListApp jListApp, JPage jPage, boolean z) {
    }

    public void onDismiss() {
    }

    public boolean onKeyCodeBack() {
        return false;
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void setPressed(JButton jButton, boolean z) {
    }

    public void viewPageNext(ViewPager viewPager) {
    }

    public void viewPageNext(JViewPagerVertical jViewPagerVertical) {
    }

    public void viewPagePrev(ViewPager viewPager) {
    }

    public void viewPagePrev(JViewPagerVertical jViewPagerVertical) {
    }

    public void viewPageScrolled(ViewPager viewPager) {
    }

    public void viewPageScrolled(JViewPagerVertical jViewPagerVertical) {
    }
}
