package com.syu.bt.page.pop;

import android.text.TextUtils;
import android.view.View;
import com.syu.app.App;
import com.syu.app.MyUiItem;
import com.syu.app.ipc.IpcObj;
import com.syu.app.ipc.Ipc_New;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Bt;
import com.syu.page.Page;
import com.syu.util.FuncUtils;
import java.util.ArrayList;

public class Page_PopBt extends Page {
    public Page_PopBt(JPage jPage) {
        super(jPage);
    }

    private boolean btnClick_Third(View view) {
        switch (view.getId()) {
            case 31:
                if (App.bPop3rdPhone_YF) {
                    Ipc_New.switch3rdPhone();
                    return true;
                } else if ((!App.getApp().mIpcObj.isCalling() && !Bt.sPhoneNumber.contains("1")) || FuncUtils.isFastDoubleClick()) {
                    return true;
                } else {
                    App.getApp().mIpcObj.FuncDial();
                    return true;
                }
            case 32:
                if (!App.bPop3rdPhone_YF) {
                    if (!FuncUtils.isFastDoubleClick()) {
                        IpcObj.hang();
                    }
                    if (App.getApp().mIpcObj.isCalling()) {
                        return true;
                    }
                    App.getApp().popBt(false, false);
                    return true;
                } else if (Ipc_New.isRing()) {
                    Ipc_New.hang3rdPhoneHold();
                    return true;
                } else {
                    Ipc_New.hang3rdPhoneCurrent();
                    return true;
                }
            case 33:
                if (!FuncUtils.isFastDoubleClick()) {
                    IpcObj.hang();
                }
                if (App.getApp().mIpcObj.isCalling()) {
                    return true;
                }
                App.getApp().popBt(false, false);
                return true;
            case 34:
            case 35:
                if (FuncUtils.isFastDoubleClick() || !IpcObj.isTalk()) {
                    return true;
                }
                IpcObj.hfp();
                return true;
            case 41:
                if (bv.d() && App.bPop3rdPhone_YF) {
                    App.getApp().pop3rdPhone(false);
                }
                App.getApp().popBt(false, false);
                App.getApp().showFloatBtn(true);
                return true;
            case 54:
                Ipc_New.switch3rdPhone();
                return true;
            case 55:
                if (Ipc_New.isRing()) {
                    Ipc_New.hang3rdPhoneHold();
                    return true;
                }
                Ipc_New.hang3rdPhoneCurrent();
                return true;
            default:
                return false;
        }
    }

    private void initView() {
        JText jText;
        JText jText2;
        int i = Bt.DATA[9];
        if (i >= 0 && i <= 6 && (jText2 = (JText) getPage().getChildViewByid(13)) != null) {
            jText2.setText(App.getApp().getString(App.mStrStates[i]));
        }
        JText jText3 = (JText) getPage().getChildViewByid(14);
        if (jText3 != null) {
            jText3.setText(Bt.sPhoneNumber);
        }
        JText jText4 = (JText) getPage().getChildViewByid(15);
        if (jText4 != null) {
            jText4.setText(Bt.sPhoneNumber);
        }
        JText jText5 = (JText) getPage().getChildViewByid(17);
        JText jText6 = (JText) getPage().getChildViewByid(18);
        if (jText5 != null) {
            if (TextUtils.isEmpty(Bt.sPhoneNumber)) {
                jText5.setText(FinalChip.BSP_PLATFORM_Null);
                if (jText6 != null) {
                    jText6.setText(FinalChip.BSP_PLATFORM_Null);
                }
            } else {
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(App.mBtInfo.mListContact);
                if (jText6 == null || !bv.h()) {
                    App.startThread(App.StrThreadGetNameByNumber, new br(arrayList, jText5, FinalChip.BSP_PLATFORM_Null, false), false, 5);
                } else {
                    String str = Bt.sPhoneNumber;
                    if (Ipc_New.isRing()) {
                        str = Bt.sPhoneNumberHoldOn;
                    }
                    App.startThread(App.StrThreadGetNameByNumber, new bs(arrayList, jText5, jText6, str, str, true), false, 5);
                }
            }
        }
        if (!Ipc_New.isTalk()) {
            JText jText7 = (JText) getPage().getChildViewByid(16);
            if (jText7 != null) {
                jText7.setText(FinalChip.BSP_PLATFORM_Null);
            }
            if (App.mStrCustomer.equalsIgnoreCase("TZY_NEW") && (jText = (JText) getPage().getChildViewByid(17)) != null && jText.getVisibility() != 0) {
                jText.setVisibility(0);
            }
        }
    }

    public void ResponseClick(int i) {
        switch (i) {
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
                App.getApp().mIpcObj.cmdNum(i - 19);
                return;
            case 36:
                IpcObj.downloadBook();
                return;
            case 37:
                IpcObj.clearKey(false);
                return;
            case 38:
                View childViewByid = getPage().getChildViewByid(40);
                if (childViewByid != null) {
                    if (childViewByid.getVisibility() == 0) {
                        childViewByid.setVisibility(8);
                    } else {
                        childViewByid.setVisibility(0);
                    }
                } else if (App.getApp().bPopBtShow_Num) {
                    App.getApp().popBtNum(false);
                    App.getApp().bPopBtShowBak_Num = false;
                } else {
                    App.getApp().popBtNum(true);
                    App.getApp().bPopBtShowBak_Num = true;
                }
                updateKeyBoardShow(true);
                return;
            default:
                return;
        }
    }

    public void ResponseClick(View view) {
        if (!btnClick_Third(view) && !App.bPop3rdPhone && !App.bPop3rdPhone_YF) {
            ResponseClick(view.getId());
        }
    }

    public void ResponseLongClick(int i) {
        switch (i) {
            case 19:
                IpcObj.LongClick0();
                return;
            case 37:
                IpcObj.clearKey(true);
                showKeyBorad(true);
                return;
            default:
                return;
        }
    }

    public boolean ResponseLongClick(View view) {
        if (!App.bPop3rdPhone && !App.bPop3rdPhone_YF) {
            ResponseLongClick(view.getId());
        }
        return false;
    }

    public void resume() {
        super.resume();
        View childViewByid = getPage().getChildViewByid(40);
        if (childViewByid != null) {
            childViewByid.setVisibility(8);
        }
        updateKeyBoardShow(false);
        initView();
        updateView(Bt.DATA[9]);
        if (bv.h() || bv.d()) {
            updateBtnVoiceSwitch(App.bHFP);
        }
    }

    public void showKeyBorad(boolean z) {
        View childViewByid = getPage().getChildViewByid(40);
        if (z != (childViewByid != null ? childViewByid.getVisibility() == 0 : App.getApp().bPopBtShowBak_Num)) {
            ResponseClick(38);
        }
    }

    public void updateBtnVoiceSwitch(boolean z) {
        String str = null;
        JButton jButton = (JButton) getPage().getChildViewByid(34);
        if (jButton != null) {
            MyUiItem myUiItem = (MyUiItem) jButton.getTag();
            if (myUiItem.getParaStr() != null) {
                String strDrawable = !z ? myUiItem.getStrDrawable() : myUiItem.getParaStr() != null ? myUiItem.getParaStr()[0] : null;
                if (!(jButton.getStrDrawable() == strDrawable || strDrawable == null)) {
                    jButton.setStrDrawable(strDrawable, true);
                }
            }
        }
        JButton jButton2 = (JButton) getPage().getChildViewByid(35);
        if (jButton2 != null) {
            MyUiItem myUiItem2 = (MyUiItem) jButton2.getTag();
            if (myUiItem2.getParaStr() != null) {
                if (!z) {
                    str = myUiItem2.getStrDrawable();
                } else if (myUiItem2.getParaStr() != null) {
                    str = myUiItem2.getParaStr()[0];
                }
                if (jButton2.getStrDrawable() != str && str != null) {
                    jButton2.setStrDrawable(str, true);
                }
            }
        }
    }

    public void updateKeyBoardShow(boolean z) {
        boolean z2;
        View childViewByid = getPage().getChildViewByid(40);
        if (childViewByid != null) {
            z2 = childViewByid.getVisibility() == 0;
        } else {
            z2 = App.getApp().bPopBtShowBak_Num;
        }
        JButton jButton = (JButton) getPage().getChildViewByid(38);
        if (jButton != null) {
            MyUiItem myUiItem = (MyUiItem) jButton.getTag();
            if (myUiItem.getParaStr() != null) {
                String strDrawable = z2 ? myUiItem.getParaStr()[0] : myUiItem.getStrDrawable();
                if (jButton.getStrDrawable() != strDrawable) {
                    jButton.setStrDrawable(strDrawable, true);
                }
            }
        }
        if (z) {
            App.getApp().popBtBook(z2);
        }
    }

    public void updateView(int i) {
        boolean z;
        int i2 = 0;
        if (bv.h()) {
            switch (i) {
                case 3:
                case 4:
                    z = true;
                    break;
                case 5:
                    z = true;
                    break;
                default:
                    z = false;
                    break;
            }
            if (App.bPop3rdPhone_YF && i == 5) {
                App.getApp().popBtNum(false);
                z = true;
            }
            View childViewByid = getPage().getChildViewByid(42);
            if (childViewByid != null) {
                childViewByid.setVisibility(z ? 0 : 8);
            }
            View childViewByid2 = getPage().getChildViewByid(43);
            if (childViewByid2 != null) {
                childViewByid2.setVisibility(z ? 0 : 8);
            }
            View childViewByid3 = getPage().getChildViewByid(44);
            if (childViewByid3 != null) {
                if (!z) {
                    i2 = 8;
                }
                childViewByid3.setVisibility(i2);
            }
        }
    }
}
