package com.syu.app;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import com.syu.ctrl.JPage;
import com.syu.data.FinalChip;
import com.syu.page.IPageNotify;

public abstract class MyActivity extends Activity {
    public boolean bTop = false;
    public AnimatorSet mAnimSet;
    public WindowManager.LayoutParams mAttrs;
    public JPage mPage;
    public MyUi ui;

    public void Destroy() {
    }

    public abstract void InitCtrlId();

    public void InitCtrlType() {
    }

    public abstract void InitMapPage();

    public abstract void InitPage(JPage jPage);

    public void createActivity(Bundle bundle) {
    }

    public View createUiItemOtherWay(JPage jPage, MyUiItem myUiItem) {
        return null;
    }

    public IPageNotify getPageNotify(int i) {
        JPage jPage;
        if (this.ui == null || (jPage = this.ui.mPages.get(i)) == null) {
            return null;
        }
        return jPage.getNotify();
    }

    public void handleSizeChanged() {
        View decorView = getWindow().getDecorView();
        if (decorView != null) {
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            if (rect.width() > 0 && rect.height() > 0) {
                int width = rect.width() * rect.height();
                MyApplication.myApp.getScreenWidthHeight();
                if (width * 2 > MyApplication.mDispayMetrics.widthPixels * MyApplication.mDispayMetrics.heightPixels) {
                    this.ui.mStrHeadXml = FinalChip.BSP_PLATFORM_Null;
                } else if (MyApplication.mDispayMetrics.widthPixels < MyApplication.mDispayMetrics.heightPixels) {
                    this.ui.mStrHeadXml = "half_ver_";
                } else {
                    this.ui.mStrHeadXml = "half_hor_";
                }
                if (!this.ui.mStrHeadXml.equals(this.ui.mStrHeadXmlBak)) {
                    this.ui.mStrHeadXmlBak = this.ui.mStrHeadXml;
                    onSizeChanged();
                }
            }
        }
    }

    public boolean isTop() {
        return this.bTop;
    }

    public void jumpPage(int i) {
        JPage loadPage = this.ui.loadPage(true, i);
        if (loadPage != null && loadPage != this.mPage) {
            this.mPage = loadPage;
            setContentView(this.mPage);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        handleSizeChanged();
        super.onConfigurationChanged(configuration);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        if (MyApplication.mActs != null) {
            MyApplication.mActs.add(this);
        }
        this.ui = new MyUi(this);
        InitCtrlType();
        InitCtrlId();
        InitMapPage();
        preCreateActivity(bundle);
        super.onCreate(bundle);
        createActivity(bundle);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (MyApplication.mActs != null) {
            MyApplication.mActs.remove(this);
        }
        Destroy();
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.bTop = false;
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        this.bTop = true;
        super.onResume();
    }

    public void onSizeChanged() {
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        handleSizeChanged();
    }

    public void preCreateActivity(Bundle bundle) {
        this.mAttrs = getWindow().getAttributes();
    }

    public void requestAppIdRight() {
    }
}
