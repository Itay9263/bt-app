package com.syu.bt.page;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import com.syu.app.App;
import com.syu.app.MyUiItem;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.act.ActBt;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JSeekBar;
import com.syu.ctrl.JText;
import com.syu.ctrl.JView;
import com.syu.ipcself.module.main.Main;
import com.syu.page.Page;
import com.syu.util.FuncUtils;
import com.syu.util.Runnable_AnimDrawable;

public class Page_Av extends Page {
    public ActBt actBt;
    ValueAnimator animMin;
    JText currentTime;
    int i = 0;
    public int iAvFocusSel = -1;
    private AnimationDrawable mAnimationDrawable;
    public JButton mBtnPlayPause;
    public JButton mBtnSoundAmp;
    public JButton mBtnSoundLoud;
    View mDynamicView1;
    View mDynamicView2;
    public int mFullSwitch = 0;
    public View mProgBtAv;
    private Runnable_AnimDrawable mRunnable_AnimDrawable = null;
    View mViewBtAvCtrl;
    View mViewBtAvFullScreen;
    View mViewBtAvHalfScreen;
    View mViewBtConnectTip;
    View mViewShowGps;
    public boolean misAvStop = false;
    JSeekBar seekBar;
    JText totalTime;
    public JText tvTitle;

    public class RunnableSwitchFull implements Runnable {
        public RunnableSwitchFull() {
        }

        public void run() {
            if (Page_Av.this.actBt.mPageCurrent != Page_Av.this.getPage()) {
                return;
            }
            if (Page_Av.this.actBt.isHalf()) {
                if (Page_Av.this.mViewBtAvFullScreen != null) {
                    Page_Av.this.mViewBtAvFullScreen.setVisibility(8);
                }
                if (Page_Av.this.mViewBtAvHalfScreen != null) {
                    Page_Av.this.mViewBtAvHalfScreen.setVisibility(0);
                    return;
                }
                return;
            }
            if (Page_Av.this.mViewBtAvHalfScreen != null) {
                Page_Av.this.mViewBtAvHalfScreen.setVisibility(8);
            }
            if (Page_Av.this.mViewBtAvFullScreen != null) {
                Page_Av.this.mViewBtAvFullScreen.setVisibility(0);
            }
        }
    }

    public Page_Av(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    public void ResponseClick(View view) {
        if (!bt.a().d()) {
            switch (view.getId()) {
                case 97:
                    if (App.getApp().isHalfScreenAble()) {
                        App.getApp().setFullScreenMode(0);
                        break;
                    }
                    break;
                case 209:
                    if (!FuncUtils.isFastDoubleClick()) {
                        IpcObj.avPrev();
                        this.misAvStop = false;
                        break;
                    }
                    break;
                case 210:
                case 211:
                    if (!FuncUtils.isFastDoubleClick()) {
                        IpcObj.avPlayPause();
                        this.misAvStop = false;
                        break;
                    }
                    break;
                case 212:
                    if (!FuncUtils.isFastDoubleClick()) {
                        IpcObj.avPlay();
                        this.misAvStop = false;
                        break;
                    }
                    break;
                case 213:
                    if (!FuncUtils.isFastDoubleClick()) {
                        IpcObj.avPause();
                        this.misAvStop = false;
                        break;
                    }
                    break;
                case 214:
                    if (!FuncUtils.isFastDoubleClick()) {
                        IpcObj.avStop();
                        this.misAvStop = true;
                        break;
                    }
                    break;
                case 215:
                    if (!FuncUtils.isFastDoubleClick()) {
                        IpcObj.avNext();
                        this.misAvStop = false;
                        break;
                    }
                    break;
                case 216:
                    if (!FuncUtils.isFastDoubleClick()) {
                        IpcObj.avLink();
                        break;
                    }
                    break;
                case 217:
                    if (!FuncUtils.isFastDoubleClick()) {
                        IpcObj.avCut();
                        break;
                    }
                    break;
                case 268:
                    App.getApp().popBtAvMicSet(true);
                    break;
                case 345:
                    if (!FuncUtils.isFastDoubleClick()) {
                        if (App.bSoundLoud != 0) {
                            App.getApp().mIpcObj.sendCmdLoud(0);
                            break;
                        } else {
                            App.getApp().mIpcObj.sendCmdLoud(1);
                            break;
                        }
                    }
                    break;
                case 346:
                    if (!FuncUtils.isFastDoubleClick()) {
                        if (App.bSoundAMP != 0) {
                            App.getApp().mIpcObj.sendCmdAmp(0);
                            break;
                        } else {
                            App.getApp().mIpcObj.sendCmdAmp(1);
                            break;
                        }
                    }
                    break;
                case 347:
                    Intent intent = new Intent();
                    intent.setAction("com.syu.eq");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setFlags(268435456);
                    App.getApp().startActivity(intent);
                    break;
                case 348:
                    this.actBt.Func_Back();
                    break;
            }
            if (this.actBt.MenuClick(view)) {
            }
        }
    }

    public void handleSeekPos(int i2) {
        int i3 = i2 / 1000;
        if (this.seekBar != null) {
            this.seekBar.setProgress(i3);
        }
        if (this.currentTime != null) {
            this.currentTime.setText(String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(i3 / 3600), Integer.valueOf((i3 / 60) % 60), Integer.valueOf(i3 % 60)}));
        }
    }

    public void init() {
        this.mProgBtAv = getPage().getChildViewByid(226);
        this.mViewBtAvFullScreen = getPage().getChildViewByid(236);
        this.mViewBtAvHalfScreen = getPage().getChildViewByid(235);
        this.mViewShowGps = getPage().getChildViewByid(97);
        this.mBtnPlayPause = (JButton) getPage().getChildViewByid(211);
        this.mBtnSoundLoud = (JButton) getPage().getChildViewByid(345);
        this.mBtnSoundAmp = (JButton) getPage().getChildViewByid(346);
        this.mViewBtAvCtrl = getPage().getChildViewByid(227);
        this.mViewBtConnectTip = getPage().getChildViewByid(228);
        this.seekBar = (JSeekBar) getPage().getChildViewByid(407);
        this.currentTime = (JText) getPage().getChildViewByid(408);
        this.totalTime = (JText) getPage().getChildViewByid(409);
        this.mDynamicView2 = getPage().getChildViewByid(230);
        if (this.mDynamicView2 != null) {
            this.mDynamicView2.setAlpha(1.0f);
        }
        initViewData();
        setButtonColor(App.color);
        updatePhoneBattery(App.mPhoneBatterty);
        updatePhoneAptx(App.mPhoneCoding);
        updateSeekbar(App.iBtAv_TotalTime, false);
    }

    public void initViewData() {
        if (this.mProgBtAv != null) {
            String[] strDrawableExtra = ((MyUiItem) this.mProgBtAv.getTag()).getStrDrawableExtra();
            if (strDrawableExtra != null && strDrawableExtra.length > 0) {
                this.mAnimationDrawable = (AnimationDrawable) this.actBt.ui.getDrawableFromPath(strDrawableExtra[0]);
            }
            this.mProgBtAv.setBackground(this.mAnimationDrawable);
        }
    }

    public void onNotify(int i2, int[] iArr, float[] fArr, String[] strArr) {
        App.getApp().mIpcObj.onNotify(this, i2, iArr, fArr, strArr);
    }

    public void pause() {
        App.getApp().DisableGainSpec();
        App.bResumeByDial = false;
    }

    public void resume() {
        JPage jPage;
        if (this.actBt.bTop) {
            App.getApp().mIpcObj.updateNotify_BtAv();
            App.getApp().mIpcObj.requestAppIdRight(10);
            App.getApp().EnableGainSpec();
            if (App.bDelMenuFromBtAv && (jPage = this.actBt.ui.mPages.get(2)) != null) {
                jPage.setVisibility(8);
            }
            if (App.getApp().bPopBtRingSet) {
                App.getApp().popBtRingSet(false);
            }
            if (App.mEnableHalfScreen) {
                switchFull();
                if (this.mViewShowGps != null) {
                    this.mViewShowGps.setVisibility(App.getApp().isHalfScreenAble() ? 0 : 8);
                }
            }
            updateBtConnectTip();
            updateSoundMode();
        }
    }

    public void setButtonColor(int i2) {
        if (App.mStrCustomer.equalsIgnoreCase("changecolor")) {
            int[] iArr = {i2};
            int[][] iArr2 = {new int[]{16842919}};
            ImageView imageView = (ImageView) getPage().getChildViewByid(406);
            if (imageView != null) {
                imageView.setColorFilter(i2, PorterDuff.Mode.SRC_ATOP);
                imageView.setImageResource(App.getApp().getIdDrawable("bk0"));
            }
            ImageView imageView2 = (ImageView) getPage().getChildViewByid(234);
            if (imageView2 != null) {
                imageView2.setColorFilter(i2, PorterDuff.Mode.SRC_ATOP);
                imageView2.setImageResource(App.getApp().getIdDrawable("btav_icopinpu"));
            }
            JButton jButton = (JButton) getPage().getChildViewByid(209);
            if (jButton != null) {
                jButton.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton2 = (JButton) getPage().getChildViewByid(210);
            if (jButton2 != null) {
                jButton2.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton2.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton3 = (JButton) getPage().getChildViewByid(214);
            if (jButton3 != null) {
                jButton3.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton3.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton4 = (JButton) getPage().getChildViewByid(215);
            if (jButton4 != null) {
                jButton4.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton4.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public void startAnim() {
        if (this.mDynamicView2 != null) {
            if (this.animMin == null) {
                this.animMin = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
                this.animMin.setDuration(4000);
                this.animMin.setRepeatMode(2);
                this.animMin.setRepeatCount(-1);
                this.animMin.setInterpolator(new TimeInterpolator() {
                    public float getInterpolation(float f) {
                        return f;
                    }
                });
                this.animMin.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        Page_Av.this.mDynamicView2.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
                    }
                });
            }
            if (this.animMin.isPaused()) {
                this.animMin.resume();
            } else if (!this.animMin.isRunning()) {
                this.animMin.start();
            }
        } else {
            stopAnim();
            if (this.mProgBtAv != null) {
                this.mRunnable_AnimDrawable = new Runnable_AnimDrawable(this.mProgBtAv, this.mAnimationDrawable, 300);
                Main.postRunnable_Ui(false, this.mRunnable_AnimDrawable);
            }
        }
    }

    public void stopAnim() {
        if (this.animMin != null && this.animMin.isRunning()) {
            this.animMin.pause();
        }
        if (this.mRunnable_AnimDrawable != null) {
            Main.removeRunnable_Ui(this.mRunnable_AnimDrawable);
            this.mRunnable_AnimDrawable.stopRun();
            this.mRunnable_AnimDrawable = null;
        }
    }

    public void switchFull() {
        if (App.getApp().fytGetState != null) {
            Main.postRunnable_Ui(false, new RunnableSwitchFull());
        }
    }

    public void updateAvSelected(int i2, boolean z) {
        if (z && this.iAvFocusSel != i2) {
            updateAvSelected(this.iAvFocusSel, false);
            this.iAvFocusSel = i2;
        }
        JButton jButton = (JButton) getPage().getChildViewByid(i2);
        if (jButton != null) {
            jButton.setFocus(z);
        }
    }

    public void updateBtConnectTip() {
        int i2 = 0;
        if (this.mViewBtAvCtrl != null) {
            this.mViewBtAvCtrl.setVisibility(IpcObj.isDisConnect() ? 8 : 0);
        }
        if (this.mViewBtConnectTip != null) {
            View view = this.mViewBtConnectTip;
            if (!IpcObj.isDisConnect()) {
                i2 = 8;
            }
            view.setVisibility(i2);
        }
        JText jText = (JText) getPage().getChildViewByid(232);
        if (jText != null) {
            if (bv.e()) {
                jText.setText(App.getApp().getString("bt_connetc_name").replace("Teyes", App.getDiyName()));
            } else {
                jText.setText(App.getApp().getString("bt_connetc_name").replace("NEWMSY", App.getDiyName()));
            }
        }
        JText jText2 = (JText) getPage().getChildViewByid(233);
        if (jText2 == null) {
            return;
        }
        if (bv.e()) {
            jText2.setText(App.getApp().getString("bt_connetc_pin").replace("0000", App.getDiyPin()));
        } else {
            jText2.setText(App.getApp().getString("bt_connetc_pin").replace("5836", App.getDiyPin()));
        }
    }

    public void updateBtnPlayPause() {
        if (this.mBtnPlayPause != null) {
            MyUiItem myUiItem = (MyUiItem) this.mBtnPlayPause.getTag();
            if (myUiItem.getParaStr() != null) {
                String strDrawable = App.bBtAvPlayState ? myUiItem.getStrDrawable() : myUiItem.getParaStr() != null ? myUiItem.getParaStr()[0] : null;
                if (this.mBtnPlayPause.getStrDrawable() != strDrawable && strDrawable != null) {
                    this.mBtnPlayPause.setStrDrawable(strDrawable, true);
                }
            }
        }
    }

    public void updatePhoneAptx(int i2) {
        String str;
        int i3 = 0;
        JView jView = (JView) getPage().getChildViewByid(234);
        if (jView != null) {
            boolean z = i2 > 0 && i2 <= 9;
            if (!z) {
                i3 = 8;
            }
            jView.setVisibility(i3);
            if (z) {
                MyUiItem myUiItem = (MyUiItem) jView.getTag();
                if (myUiItem.getParaStr() != null && jView.getStrDrawable() != (str = myUiItem.getParaStr()[i2 - 1]) && str != null) {
                    jView.setStrDrawable(str, true);
                }
            }
        }
    }

    public void updatePhoneBattery(int i2) {
        JView jView;
        String str;
        View childViewByid = getPage().getChildViewByid(231);
        if (childViewByid != null) {
            childViewByid.setVisibility(i2 > 0 ? 0 : 8);
        }
        if (i2 > 0 && (jView = (JView) getPage().getChildViewByid(405)) != null) {
            MyUiItem myUiItem = (MyUiItem) jView.getTag();
            if (myUiItem.getParaStr() != null && jView.getStrDrawable() != (str = myUiItem.getParaStr()[i2 - 1]) && str != null) {
                jView.setStrDrawable(str, true);
            }
        }
    }

    public void updateSeekbar(int i2, boolean z) {
        int i3 = 8;
        int i4 = i2 / 1000;
        if (this.seekBar != null) {
            this.seekBar.setProgressMax(i4);
        }
        if (this.totalTime != null) {
            this.totalTime.setText(String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(i4 / 3600), Integer.valueOf((i4 / 60) % 60), Integer.valueOf(i4 % 60)}));
        }
        if (this.seekBar != null) {
            if (!z || this.seekBar.getVisibility() != 0) {
                this.seekBar.setVisibility(i4 <= 0 ? 8 : 0);
            } else {
                return;
            }
        }
        if (this.currentTime != null) {
            this.currentTime.setVisibility(i4 <= 0 ? 8 : 0);
        }
        if (this.totalTime != null) {
            JText jText = this.totalTime;
            if (i4 > 0) {
                i3 = 0;
            }
            jText.setVisibility(i3);
        }
    }

    public void updateSelected(int i2, boolean z) {
        switch (i2) {
            case 209:
            case 210:
            case 214:
            case 215:
                break;
            default:
                i2 = -1;
                break;
        }
        if (i2 != -1 && this.iAvFocusSel != i2 && this.iAvFocusSel > 0) {
            updateAvSelected(this.iAvFocusSel, z);
        }
    }

    public void updateSoundAmp() {
        boolean z = true;
        if (this.mBtnSoundAmp != null) {
            JButton jButton = this.mBtnSoundAmp;
            if (App.bSoundAMP != 1) {
                z = false;
            }
            jButton.setFocus(z);
        }
    }

    public void updateSoundLoud() {
        boolean z = true;
        if (this.mBtnSoundLoud != null) {
            JButton jButton = this.mBtnSoundLoud;
            if (App.bSoundLoud != 1) {
                z = false;
            }
            jButton.setFocus(z);
        }
    }

    public void updateSoundMode() {
        JText jText = (JText) getPage().getChildViewByid(221);
        if (jText != null) {
            switch (App.bSoundMode) {
                case 1:
                    jText.setText(App.getApp().getString("standard"));
                    return;
                case 2:
                    jText.setText(App.getApp().getString("rock"));
                    return;
                case 3:
                    jText.setText(App.getApp().getString("soft"));
                    return;
                case 4:
                    jText.setText(App.getApp().getString("classic"));
                    return;
                case 5:
                    jText.setText(App.getApp().getString("pop"));
                    return;
                case 6:
                    jText.setText(App.getApp().getString("hall"));
                    return;
                case 7:
                    jText.setText(App.getApp().getString("jazz"));
                    return;
                case 8:
                    jText.setText(App.getApp().getString("cinema"));
                    return;
                default:
                    jText.setText(App.getApp().getString("user"));
                    return;
            }
        }
    }
}
