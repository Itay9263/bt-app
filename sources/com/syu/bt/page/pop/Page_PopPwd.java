package com.syu.bt.page.pop;

import android.text.TextUtils;
import android.view.View;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.act.ActBt;
import com.syu.bt.page.Page_Pair;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.ipcself.module.main.Main;
import com.syu.page.Page;
import java.util.Locale;

public class Page_PopPwd extends Page {
    ActBt actBt;

    public Page_PopPwd(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    public void ResponseClick(View view) {
        int id = view.getId();
        Page_Pair page_Pair = null;
        JPage page = this.actBt.ui.getPage(12);
        if (page != null) {
            page_Pair = (Page_Pair) page.getNotify();
        }
        if (page_Pair != null) {
            switch (id) {
                case 308:
                    backspace();
                    return;
                case 309:
                case 310:
                case 311:
                case 312:
                case 313:
                case 314:
                case 315:
                case 316:
                case 317:
                case 318:
                    append(String.format(Locale.US, "%d", new Object[]{Integer.valueOf(id - 309)}));
                    return;
                case 319:
                    if (!TextUtils.isEmpty(IpcObj.getChoiceAddr())) {
                        getPage().getDialog().dismiss();
                        JText jText = (JText) getPage().getChildViewByid(307);
                        if (jText == null) {
                            return;
                        }
                        if (Main.mConf_PlatForm == 5 || bv.k()) {
                            bt.a().a(jText.getText().toString().getBytes());
                            return;
                        } else {
                            IpcObj.connectOBD(String.valueOf(IpcObj.getChoiceAddr()) + jText.getText());
                            return;
                        }
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void append(String str) {
        JText jText = (JText) getPage().getChildViewByid(307);
        if (jText != null) {
            String str2 = jText.getText() + str;
            if (str2.length() <= 4) {
                jText.setText(str2);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void backspace() {
        JText jText = (JText) getPage().getChildViewByid(307);
        if (jText != null) {
            String charSequence = jText.getText().toString();
            if (!TextUtils.isEmpty(charSequence)) {
                jText.setText(charSequence.substring(0, charSequence.length() - 1));
            }
        }
    }
}
