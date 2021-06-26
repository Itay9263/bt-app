package com.syu.bt.page;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.act.ActBt;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JScrollView;
import com.syu.ctrl.JText;
import com.syu.ctrl.JView;
import com.syu.ipcself.module.main.Main;
import com.syu.page.Page;
import com.syu.util.FuncUtils;

public class Page_Menu extends Page {
    ActBt actBt;
    boolean bHideScroll;
    public JButton bShowScroll;
    Animation endAni;
    public Runnable mRunnable_HideMenuButton = new Runnable() {
        public void run() {
            if (Page_Menu.this.mScroll != null && Page_Menu.this.mScroll.getVisibility() == 0) {
                Page_Menu.this.mScroll.setVisibility(8);
                if (Page_Menu.this.vLine_one != null) {
                    Page_Menu.this.vLine_one.setVisibility(8);
                }
                Page_Menu.this.mScroll.startAnimation(Page_Menu.this.endAni);
                if (Page_Menu.this.vLine_one != null) {
                    Page_Menu.this.vLine_one.startAnimation(Page_Menu.this.endAni);
                }
            }
        }
    };
    public JScrollView mScroll;
    Animation startAni;
    public JText tvTxtName;
    public JText tvTxtNumber;
    public JView vLine_one;
    public JView vLine_three;
    public JView vLine_two;

    public Page_Menu(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    private boolean btnClick(View view) {
        switch (view.getId()) {
            case 41:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                this.actBt.startActivity(intent);
                return true;
            case 42:
                if (App.getApp().mIpcObj.isCalling()) {
                    cb.a().a(App.getApp().getString("bt_state_using"));
                    return true;
                }
                Intent intent2 = new Intent();
                intent2.setAction("com.syu.music.btav");
                intent2.setPackage("com.syu.music");
                this.actBt.startActivity(intent2);
                return true;
            case 43:
                this.actBt.dispatchKeyEvent(new KeyEvent(0, 4));
                return true;
            case 47:
                App.getApp().mIpcObj.setStandBy();
                return true;
            case 50:
                ShowMenuButton();
                return true;
            case 101:
                if (FuncUtils.isFastDoubleClick()) {
                    return true;
                }
                App.getApp().mIpcObj.FuncDial();
                return true;
            case 102:
                if (FuncUtils.isFastDoubleClick() || !IpcObj.isInCall()) {
                    return true;
                }
                IpcObj.hang();
                return true;
            case 120:
                if (!bv.a()) {
                    return true;
                }
                IpcObj.clearKey(false);
                return true;
            default:
                return false;
        }
    }

    public void ResponseClick(View view) {
        if (bv.j()) {
            Main.removeRunnable_Ui(this.mRunnable_HideMenuButton);
            Main.postRunnable_Ui(false, this.mRunnable_HideMenuButton, 10000);
        }
        if (bt.a().d() || btnClick(view)) {
            return;
        }
        if (App.getApp().mIpcObj.isCalling()) {
            cb.a().a(App.getApp().getString("bt_state_using"));
        } else {
            this.actBt.MenuClick(view);
        }
    }

    public boolean ResponseLongClick(View view) {
        switch (view.getId()) {
            case 120:
                if (!bv.a()) {
                    return false;
                }
                IpcObj.clearKey(true);
                return false;
            default:
                return false;
        }
    }

    public void ShowMenuButton() {
        if (this.bShowScroll != null) {
            this.bShowScroll.setVisibility(8);
        }
        if (this.vLine_three != null) {
            this.vLine_three.setVisibility(0);
        }
        if (this.vLine_two != null) {
            this.vLine_two.setVisibility(8);
        }
        if (this.mScroll.getVisibility() == 8) {
            this.mScroll.setVisibility(0);
            if (this.vLine_one != null) {
                this.vLine_one.setVisibility(0);
            }
            this.mScroll.startAnimation(this.startAni);
            if (this.vLine_one != null) {
                this.vLine_one.startAnimation(this.startAni);
            }
        }
    }

    public void init() {
        this.mScroll = (JScrollView) getPage().getChildViewByid(49);
        this.bShowScroll = (JButton) getPage().getChildViewByid(50);
        this.vLine_one = (JView) getPage().getChildViewByid(51);
        this.vLine_two = (JView) getPage().getChildViewByid(52);
        this.vLine_three = (JView) getPage().getChildViewByid(53);
        if (this.vLine_two != null) {
            this.vLine_two.setVisibility(8);
        }
        if (this.mScroll != null) {
            this.mScroll.setVisibility(0);
            this.bShowScroll.setVisibility(8);
        }
        if (getPage().getChildViewByid(38) == null) {
            App.bDelMenuFromBtAv = true;
        }
        setAnimation();
        setButtonColor(App.color);
        initTypeface();
    }

    public void initTypeface() {
    }

    public void resetMenuButton() {
        if (bv.j()) {
            ShowMenuButton();
            Main.removeRunnable_Ui(this.mRunnable_HideMenuButton);
            Main.postRunnable_Ui(false, this.mRunnable_HideMenuButton, 10000);
        }
    }

    public void setAnimation() {
        if (bv.j()) {
            this.startAni = new TranslateAnimation(1, -0.64f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
            this.startAni.setDuration(500);
            this.endAni = new TranslateAnimation(1, 0.0f, 1, -0.64f, 1, 0.0f, 1, 0.0f);
            this.endAni.setDuration(500);
            this.endAni.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    if (Page_Menu.this.mScroll != null) {
                        Page_Menu.this.mScroll.setVisibility(8);
                    }
                    if (Page_Menu.this.vLine_one != null) {
                        Page_Menu.this.vLine_one.setVisibility(8);
                    }
                    if (Page_Menu.this.bShowScroll != null) {
                        Page_Menu.this.bShowScroll.setVisibility(0);
                    }
                    if (Page_Menu.this.vLine_three != null) {
                        Page_Menu.this.vLine_three.setVisibility(8);
                    }
                    if (Page_Menu.this.vLine_two != null) {
                        Page_Menu.this.vLine_two.setVisibility(0);
                    }
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            });
            if (bv.j()) {
                Main.removeRunnable_Ui(this.mRunnable_HideMenuButton);
                Main.postRunnable_Ui(false, this.mRunnable_HideMenuButton, 10000);
            }
        }
    }

    public void setButtonColor(int i) {
        if (App.mStrCustomer.equalsIgnoreCase("changecolor")) {
            int[] iArr = {i, i};
            int[][] iArr2 = {new int[]{16842919}, new int[]{16842913}};
            JButton jButton = (JButton) getPage().getChildViewByid(35);
            if (jButton != null) {
                jButton.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton2 = (JButton) getPage().getChildViewByid(37);
            if (jButton2 != null) {
                jButton2.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton2.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton3 = (JButton) getPage().getChildViewByid(36);
            if (jButton3 != null) {
                jButton3.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton3.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton4 = (JButton) getPage().getChildViewByid(38);
            if (jButton4 != null) {
                jButton4.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton4.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton5 = (JButton) getPage().getChildViewByid(40);
            if (jButton5 != null) {
                jButton5.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton5.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton6 = (JButton) getPage().getChildViewByid(39);
            if (jButton6 != null) {
                jButton6.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton6.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            ImageView imageView = (ImageView) getPage().getChildViewByid(48);
            if (imageView != null) {
                imageView.setColorFilter(i, PorterDuff.Mode.SRC_ATOP);
                imageView.setImageResource(App.getApp().getIdDrawable("bt_menu_bk"));
            }
        }
    }

    public void setPressed(JButton jButton, boolean z) {
        updateSelected(jButton.getId(), !z);
    }

    public boolean updateSelected(int i, boolean z) {
        int id;
        int fixPage = this.actBt.getFixPage(i);
        if (fixPage == -1) {
            return false;
        }
        if (!(this.actBt.mPageCurrent == null || (id = this.actBt.mPageCurrent.getId()) == fixPage)) {
            if (z) {
                this.actBt.updateMenuSelected(2, id, 0, z);
            } else {
                this.actBt.updateMenuSelected_New(2, id, fixPage, 0, z);
            }
        }
        return true;
    }
}
