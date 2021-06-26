package com.syu.ctrl;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.syu.app.MyUi;
import com.syu.app.MyUiItem;
import com.syu.page.IPageNotify;
import java.util.ArrayList;
import java.util.List;

public class JPage extends FrameLayout {
    private Dialog dlg;
    public boolean focus = false;
    public JGridView gridView;
    int iChildTag = -1;
    int iGroupTag = -1;
    int iKeyChildView = 50000;
    int iState = -1;
    public JListApp listApp;
    public JListViewEx listViewEx;
    public SparseArray<View> mChildView = new SparseArray<>();
    public List<Integer> mListParseViewId = new ArrayList();
    IPageNotify mNotify;
    public SparseArray<JPage> mPages = new SparseArray<>();
    String mStrDrawable;
    String mStrDrawableFix;
    private MyUi ui;

    public JPage(Context context, MyUi myUi) {
        super(context);
        this.ui = myUi;
    }

    @SuppressLint({"NewApi"})
    public void addChildView(View view, MyUiItem myUiItem) {
        if (view instanceof JPage) {
            this.mPages.put(view.getId(), (JPage) view);
        }
        switch (myUiItem.getType()) {
            case 6:
                String[] paraStr = myUiItem.getParaStr();
                if (paraStr != null) {
                    int length = paraStr.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            break;
                        } else if (paraStr[i].contains("click")) {
                            view.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View view) {
                                    if (JPage.this.mNotify != null) {
                                        JPage.this.mNotify.ResponseClick(view);
                                    }
                                }
                            });
                            break;
                        } else {
                            i++;
                        }
                    }
                }
                break;
            case 9:
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (JPage.this.mNotify != null) {
                            JPage.this.mNotify.ResponseClick(view);
                        }
                    }
                });
                if (myUiItem.isLongClick()) {
                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        public boolean onLongClick(View view) {
                            if (JPage.this.mNotify == null) {
                                return true;
                            }
                            JPage.this.mNotify.ResponseLongClick(view);
                            return true;
                        }
                    });
                    break;
                }
                break;
            case 17:
                if (((JSwitchButton) view).bIsCheckBox) {
                    view.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            JSwitchButton jSwitchButton = (JSwitchButton) view;
                            jSwitchButton.setChecked(!jSwitchButton.isChecked());
                            if (JPage.this.mNotify != null) {
                                JPage.this.mNotify.ResponseClick(view);
                            }
                        }
                    });
                    break;
                }
                break;
            case 18:
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (JPage.this.mNotify != null) {
                            JPage.this.mNotify.ResponseClick(view);
                        }
                    }
                });
                break;
        }
        view.setTag(myUiItem);
        if (-1 != view.getId()) {
            this.mChildView.put(view.getId(), view);
        } else {
            this.mChildView.put(this.iKeyChildView, view);
        }
        this.mListParseViewId.add(Integer.valueOf(view.getId()));
        this.iKeyChildView++;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        IPageNotify notify = getNotify();
        if (notify != null) {
            notify.dispatchTouchEvent(motionEvent);
        }
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (Exception e) {
            return false;
        }
    }

    public int getChildTag() {
        return this.iChildTag;
    }

    public View getChildViewByid(int i) {
        return this.mChildView.get(i);
    }

    public Dialog getDialog() {
        return this.dlg;
    }

    public int getGroupTag() {
        return this.iGroupTag;
    }

    public IPageNotify getNotify() {
        return this.mNotify;
    }

    public int getState() {
        return this.iState;
    }

    public String getStrDrawable() {
        return this.mStrDrawable;
    }

    public void onPress(boolean z) {
        if (this.listApp != null) {
            if (z) {
                this.listApp.viewAppSelect = this;
            }
            IPageNotify notify = this.listApp.getPage().getNotify();
            if (notify != null) {
                notify.listAppPressed(this.listApp, this, z);
            }
        } else if (this.gridView != null) {
            this.gridView.setPosition(getChildTag());
            IPageNotify notify2 = this.gridView.getPage().getNotify();
            if (notify2 != null) {
                notify2.GridPressed(this.gridView, this, z);
            }
        } else if (this.listViewEx != null) {
            this.listViewEx.setPositionGroup(getGroupTag());
            this.listViewEx.setPositionChild(getChildTag());
            IPageNotify notify3 = this.listViewEx.getPage().getNotify();
            if (notify3 != null) {
                notify3.ItemExPressed(this.listViewEx, this, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        IPageNotify notify = getNotify();
        if (notify != null) {
            notify.onSizeChanged(i, i2, i3, i4);
        }
        super.onSizeChanged(i, i2, i3, i4);
    }

    public void pause() {
        IPageNotify notify = getNotify();
        if (notify != null) {
            notify.pause();
        }
    }

    public void resume() {
        IPageNotify notify = getNotify();
        if (notify != null) {
            notify.resume();
        }
    }

    public void setChildTag(int i) {
        this.iChildTag = i;
    }

    public void setDialog(Dialog dialog) {
        this.dlg = dialog;
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        updateBackground();
    }

    public void setFocus(boolean z) {
        this.focus = z;
        updateBackground();
    }

    public void setGridView(JGridView jGridView) {
        this.gridView = jGridView;
    }

    public void setGroupTag(int i) {
        this.iGroupTag = i;
    }

    public void setListApp(JListApp jListApp) {
        this.listApp = jListApp;
    }

    public void setListViewEx(JListViewEx jListViewEx) {
        this.listViewEx = jListViewEx;
    }

    public void setNotify(IPageNotify iPageNotify) {
        this.mNotify = iPageNotify;
    }

    public void setPressed(boolean z) {
        super.setPressed(z);
        updateBackground();
        onPress(z);
    }

    public void setStrDrawable(String str, boolean z) {
        this.iState = -1;
        this.mStrDrawable = str;
        if (!z) {
            return;
        }
        if (str == null) {
            setBackground((Drawable) null);
        } else {
            updateBackground();
        }
    }

    public void updateBackground() {
        int i = this.iState;
        if (this.focus) {
            this.iState = 1;
        } else if (!isEnabled()) {
            this.iState = 2;
        } else if (isPressed()) {
            this.iState = 1;
        } else {
            this.iState = 0;
        }
        if (i != this.iState && this.mStrDrawable != null) {
            this.mStrDrawableFix = this.mStrDrawable;
            switch (this.iState) {
                case 1:
                    this.mStrDrawableFix = String.valueOf(this.mStrDrawableFix) + "_p";
                    break;
                case 2:
                    this.mStrDrawableFix = String.valueOf(this.mStrDrawableFix) + "_u";
                    break;
            }
            setBackground(this.ui.getDrawableFromPath(this.mStrDrawableFix));
        }
    }
}
