package com.syu.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JGridView;
import com.syu.ctrl.JListViewEx;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JScrollView;
import com.syu.ctrl.JScrollViewHorizontal;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Main;
import com.syu.page.IPageNotify;
import com.syu.util.FuncUtils;
import com.syu.util.IpcUtil;
import com.syu.util.Markup;
import com.syu.util.MySize;
import java.util.ArrayList;
import java.util.HashMap;

public class MyUi {
    public static Interface_getDrawableFromPath mInterface_getDrawableFromPath = null;
    public static String mStrHeadDrawable = FinalChip.BSP_PLATFORM_Null;
    private MyActivity activity;
    public boolean bIsInListItem = false;
    public Context mContext;
    public HashMap<String, Integer> mCtrlId = new HashMap<>();
    public SparseArray<Dialog> mDlgs = new SparseArray<>();
    public HashMap<String, Drawable> mDrawables = new HashMap<>();
    public SparseArray<String> mMapPage = new SparseArray<>();
    public SparseArray<MyUiItem> mPageUiItems = new SparseArray<>();
    public SparseArray<JPage> mPages = new SparseArray<>();
    public String mStrHeadXml = FinalChip.BSP_PLATFORM_Null;
    public String mStrHeadXmlBak = FinalChip.BSP_PLATFORM_Null;

    public class DecodeBmp implements Runnable {
        private int iType;
        private String strDrawable;
        private String strIcon;
        private View v;

        public DecodeBmp(View view, String str, String str2, int i) {
            this.v = view;
            this.strDrawable = str;
            this.strIcon = str2;
            this.iType = i;
        }

        public void run() {
            if (this.v.getBackground() == null) {
                Main.postRunnable_Ui(false, new SetBkView(this.v, MyUi.this.getDrawableFromPath(this.strDrawable), this.strIcon, this.iType));
            }
        }
    }

    public interface Interface_getDrawableFromPath {
        Drawable getDrawableFromPath(String str);
    }

    public class MyOnChildClickListener implements ExpandableListView.OnChildClickListener {
        private JListViewEx view;

        public MyOnChildClickListener(JListViewEx jListViewEx) {
            this.view = jListViewEx;
        }

        public boolean onChildClick(ExpandableListView expandableListView, View view2, int i, int i2, long j) {
            IPageNotify notify = this.view.getPage().getNotify();
            if (notify == null) {
                return false;
            }
            notify.ListExChildClick(this.view);
            return false;
        }
    }

    public class MyOnDismissListener implements DialogInterface.OnDismissListener {
        private JPage page;

        public MyOnDismissListener(JPage jPage) {
            this.page = jPage;
        }

        public void onDismiss(DialogInterface dialogInterface) {
            IPageNotify notify = this.page.getNotify();
            if (notify != null) {
                notify.onDismiss();
            }
        }
    }

    public class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        private JGridView view;

        public MyOnItemClickListener(JGridView jGridView) {
            this.view = jGridView;
        }

        public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
            IPageNotify notify = this.view.getPage().getNotify();
            if (notify != null) {
                notify.GridClick(this.view);
            }
        }
    }

    public class MyOnItemLongClickListener implements AdapterView.OnItemLongClickListener {
        private JGridView view;

        public MyOnItemLongClickListener(JGridView jGridView) {
            this.view = jGridView;
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view2, int i, long j) {
            IPageNotify notify = this.view.getPage().getNotify();
            if (notify == null) {
                return true;
            }
            notify.GridLongClick(this.view);
            return true;
        }
    }

    public class SetBkView implements Runnable {
        private int iType;
        private Drawable normal;
        private String strIcon;
        private View v;

        public SetBkView(View view, Drawable drawable, String str, int i) {
            this.v = view;
            this.normal = drawable;
            this.strIcon = str;
            this.iType = i;
        }

        public void run() {
            if (this.v.getBackground() == null) {
                this.v.setBackground(this.normal);
                if (this.iType == 9 && this.strIcon != null) {
                    JButton jButton = (JButton) this.v;
                    if (jButton.mIcon == null) {
                        jButton.mIcon = MyUi.this.getDrawableFromPath(this.strIcon);
                        jButton.invalidate();
                    }
                }
            }
        }
    }

    public MyUi(MyActivity myActivity) {
        if (myActivity != null) {
            this.mContext = myActivity;
            this.activity = myActivity;
            return;
        }
        this.mContext = MyApplication.myApp;
    }

    private void createGroup(JPage jPage, MyUiItem myUiItem, ViewGroup viewGroup, ViewGroup viewGroup2) {
        Animation animationFromPath;
        if (viewGroup != null) {
            int childCount = myUiItem.getChildCount();
            if (myUiItem.getType() == 2) {
                for (int i = 0; i < childCount; i++) {
                    CreateViewByItem(jPage, myUiItem.getChildAt(i), ((JScrollView) viewGroup).getLayout());
                }
            } else if (myUiItem.getType() == 3) {
                for (int i2 = 0; i2 < childCount; i2++) {
                    CreateViewByItem(jPage, myUiItem.getChildAt(i2), ((JScrollViewHorizontal) viewGroup).getLayout());
                }
            } else {
                for (int i3 = 0; i3 < childCount; i3++) {
                    CreateViewByItem(jPage, myUiItem.getChildAt(i3), viewGroup);
                }
            }
            viewGroup.setTag(myUiItem);
            if (myUiItem.getId() != -1) {
                viewGroup.setId(myUiItem.getId());
            }
            if (!(myUiItem.getAnim() == null || (animationFromPath = MyApplication.myApp.getAnimationFromPath(myUiItem.getAnim())) == null)) {
                viewGroup.startAnimation(animationFromPath);
            }
            LoadImage(viewGroup, myUiItem.getStrDrawable(), myUiItem.getIcon(), myUiItem.getType());
            jPage.addChildView(viewGroup, myUiItem);
            viewGroup.setVisibility(myUiItem.getVisible());
            FrameLayout.LayoutParams layoutParams = myUiItem.getLayoutParams();
            if (layoutParams != null) {
                if (viewGroup2 instanceof LinearLayout) {
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(layoutParams.width, layoutParams.height);
                    layoutParams2.gravity = layoutParams.gravity;
                    layoutParams2.leftMargin = layoutParams.leftMargin;
                    layoutParams2.topMargin = layoutParams.topMargin;
                    if (myUiItem.getWeight() > 0) {
                        layoutParams2.weight = (float) myUiItem.getWeight();
                    }
                    viewGroup.setLayoutParams(layoutParams2);
                } else {
                    viewGroup.setLayoutParams(layoutParams);
                }
            }
            viewGroup2.addView(viewGroup);
        }
    }

    private void createLayout(JPage jPage, MyUiItem myUiItem, ViewGroup viewGroup) {
        if (myUiItem.getType() == 4) {
            LinearLayout linearLayout = new LinearLayout(this.mContext);
            linearLayout.setTag(myUiItem);
            if (myUiItem.getParaStr() != null) {
                if (myUiItem.getParaStr()[0].contains("horizontal")) {
                    linearLayout.setOrientation(0);
                } else {
                    linearLayout.setOrientation(1);
                }
            }
            createGroup(jPage, myUiItem, linearLayout, viewGroup);
            return;
        }
        createGroup(jPage, myUiItem, new FrameLayout(this.mContext), viewGroup);
    }

    private JPage createPage(MyUiItem myUiItem, JPage jPage) {
        Animation animationFromPath;
        if (jPage == null) {
            jPage = new JPage(this.mContext, this);
            jPage.setTag(myUiItem);
            if (myUiItem.getId() != -1) {
                jPage.setId(myUiItem.getId());
            }
        }
        if (jPage != null) {
            int childCount = myUiItem.getChildCount();
            for (int i = 0; i < childCount; i++) {
                CreateViewByItem(jPage, myUiItem.getChildAt(i), jPage);
            }
            if (!(myUiItem.getAnim() == null || (animationFromPath = MyApplication.myApp.getAnimationFromPath(myUiItem.getAnim())) == null)) {
                jPage.startAnimation(animationFromPath);
            }
        }
        return jPage;
    }

    private void createScrollView(JPage jPage, MyUiItem myUiItem, ViewGroup viewGroup) {
        JScrollView jScrollView = new JScrollView(this.mContext);
        LinearLayout linearLayout = new LinearLayout(this.mContext);
        jScrollView.setLayout(linearLayout);
        linearLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        linearLayout.setOrientation(1);
        jScrollView.setVerticalScrollBarEnabled(false);
        createGroup(jPage, myUiItem, jScrollView, viewGroup);
    }

    private void createScrollViewHorizontal(JPage jPage, MyUiItem myUiItem, ViewGroup viewGroup) {
        JScrollViewHorizontal jScrollViewHorizontal = new JScrollViewHorizontal(this.mContext);
        LinearLayout linearLayout = new LinearLayout(this.mContext);
        jScrollViewHorizontal.setLayout(linearLayout);
        linearLayout.setLayoutParams(new FrameLayout.LayoutParams(-2, -1));
        linearLayout.setOrientation(0);
        jScrollViewHorizontal.setHorizontalScrollBarEnabled(false);
        createGroup(jPage, myUiItem, jScrollViewHorizontal, viewGroup);
    }

    public static int getFixValue(int i, boolean z) {
        if (i == 0) {
            return 0;
        }
        int i2 = (int) (((float) i) * MyApplication.mScale);
        if (!z || i2 > 0) {
            return i2;
        }
        return 1;
    }

    private MyUiItem getItem(Markup markup, String str, MyUiItem myUiItem) {
        String[] split;
        int[] intArray;
        int id;
        int intValue = MyApplication.mCtrlType.containsKey(str) ? MyApplication.mCtrlType.get(str).intValue() : -1;
        MyUiItem myUiItem2 = new MyUiItem(intValue);
        myUiItem2.setParentItem(myUiItem);
        String GetAttr = markup.GetAttr("id");
        if (!(GetAttr == null || this.mCtrlId == null || !this.mCtrlId.containsKey(GetAttr))) {
            myUiItem2.setId(this.mCtrlId.get(GetAttr).intValue());
        }
        if (intValue == 1 && (id = myUiItem2.getId()) != -1 && this.mMapPage.indexOfKey(id) < 0 && this.mPageUiItems.indexOfKey(id) < 0) {
            this.mPageUiItems.put(id, myUiItem2);
        }
        String GetAttr2 = markup.GetAttr("visible");
        if (GetAttr2 != null && GetAttr2.equals("GONE")) {
            myUiItem2.setVisible(8);
        }
        String GetAttr3 = markup.GetAttr("anim");
        if (GetAttr3 != null) {
            myUiItem2.setAnim(GetAttr3);
        }
        String GetAttr4 = markup.GetAttr("size");
        if (GetAttr4 != null) {
            String[] split2 = GetAttr4.split(";");
            if (split2 != null) {
                int length = split2.length - 1;
                if (length >= 1) {
                    myUiItem2.mSize = new MySize[length];
                    for (int i = 0; i < length; i++) {
                        int[] intArray2 = getIntArray(split2[i + 1]);
                        myUiItem2.mSize[i] = new MySize(getFixValue(intArray2[0], true), getFixValue(intArray2[1], true));
                    }
                }
                myUiItem2.setSize(getIntArray(split2[0]));
            }
            String GetAttr5 = markup.GetAttr("pos");
            if (GetAttr5 != null) {
                myUiItem2.setPos(getIntArray(GetAttr5));
            }
        } else {
            int height = myUiItem2.getHeight();
            switch (intValue) {
                case 14:
                    height = 0;
                    break;
            }
            String GetAttr6 = markup.GetAttr("h");
            if (GetAttr6 != null) {
                height = MyApplication.myParseInt(GetAttr6);
            }
            switch (intValue) {
                case 14:
                    break;
                default:
                    height = getFixValue(height, true);
                    break;
            }
            myUiItem2.setHeight(height);
        }
        Point point = null;
        if (myUiItem != null) {
            point = myUiItem.getPtConfig();
        }
        String GetAttr7 = markup.GetAttr("paraStr");
        if (GetAttr7 != null) {
            myUiItem2.setParaStr(GetAttr7.split(","));
            if (myUiItem2.isPadding()) {
                myUiItem2.mPlusPadding = MyApplication.mStatusHeight;
            }
        }
        String GetAttr8 = markup.GetAttr("dirs");
        if (GetAttr8 != null) {
            myUiItem2.setDirStr(this, GetAttr8.split(","));
        }
        String GetAttr9 = markup.GetAttr("prevnext");
        if (GetAttr9 != null) {
            myUiItem2.setPrevNextStr(this, GetAttr9.split(","));
        }
        String GetAttr10 = markup.GetAttr("gravity");
        if (GetAttr10 != null) {
            String[] split3 = GetAttr10.split(",");
            if (split3.length >= 2) {
                myUiItem2.mLayoutParams = new FrameLayout.LayoutParams(split3[0].contains("wrap_content") ? -2 : split3[0].contains("match_parent") ? -1 : getFixValue(MyApplication.myParseInt(split3[0]), true), split3[1].contains("wrap_content") ? -2 : split3[1].contains("match_parent") ? -1 : getFixValue(MyApplication.myParseInt(split3[1]), true));
                if (split3.length >= 3) {
                    boolean z = false;
                    if (split3[2].contains("Left")) {
                        z = true;
                        if (split3[2].contains("Top")) {
                            myUiItem2.mLayoutParams.gravity = 51;
                        } else if (split3[2].contains("Bottom")) {
                            myUiItem2.mLayoutParams.gravity = 83;
                        } else {
                            myUiItem2.mLayoutParams.gravity = 19;
                        }
                    } else if (split3[2].contains("Right")) {
                        z = true;
                        if (split3[2].contains("Top")) {
                            myUiItem2.mLayoutParams.gravity = 53;
                        } else if (split3[2].contains("Bottom")) {
                            myUiItem2.mLayoutParams.gravity = 85;
                        } else {
                            myUiItem2.mLayoutParams.gravity = 21;
                        }
                    } else if (split3[2].contains("Bottom")) {
                        z = true;
                        myUiItem2.mLayoutParams.gravity = 81;
                    } else if (split3[2].contains("Top")) {
                        z = true;
                        myUiItem2.mLayoutParams.gravity = 49;
                    } else if (split3[2].contains("Center_H")) {
                        myUiItem2.mLayoutParams.gravity = 1;
                    } else if (split3[2].contains("Center_V")) {
                        myUiItem2.mLayoutParams.gravity = 16;
                    } else if (split3[2].contains("Center")) {
                        myUiItem2.mLayoutParams.gravity = 17;
                    }
                    if (!z) {
                        myUiItem2.mLayoutParams.gravity = 17;
                    }
                } else {
                    String GetAttr11 = markup.GetAttr("pos");
                    if (!(GetAttr11 == null || (intArray = getIntArray(GetAttr11)) == null || intArray.length != 2)) {
                        myUiItem2.mLayoutParams.leftMargin = getFixValue(intArray[0], false);
                        myUiItem2.mLayoutParams.topMargin = getFixValue(intArray[1], false) + myUiItem2.mPlusPadding;
                    }
                }
            }
        }
        String GetAttr12 = markup.GetAttr("rect");
        if (!(GetAttr12 == null || (split = GetAttr12.split(";")) == null)) {
            int length2 = split.length - 1;
            if (length2 >= 1) {
                myUiItem2.mRect = new Rect[length2];
                for (int i2 = 0; i2 < length2; i2++) {
                    myUiItem2.mRect[i2] = new Rect();
                    myUiItem2.setRect(myUiItem2.mRect[i2], getIntArray(split[i2 + 1]));
                }
            }
            if (myUiItem2.mLayoutParams == null) {
                myUiItem2.setRect(point, getIntArray(split[0]));
            }
        }
        String GetAttr13 = markup.GetAttr("posIco");
        if (GetAttr13 != null) {
            myUiItem2.setPosIcon(getIntArray(GetAttr13));
        }
        String GetAttr14 = markup.GetAttr("icon");
        if (GetAttr14 != null) {
            myUiItem2.setIcon(GetAttr14);
        }
        String GetAttr15 = markup.GetAttr("padding");
        if (GetAttr15 != null) {
            myUiItem2.setPadding(getIntArray(GetAttr15));
        }
        String GetAttr16 = markup.GetAttr("para");
        if (GetAttr16 != null) {
            myUiItem2.setPara(getIntArray(GetAttr16));
        }
        String GetAttr17 = markup.GetAttr("weight");
        if (GetAttr17 != null) {
            myUiItem2.setWeight(MyApplication.myParseInt(GetAttr17));
        }
        String GetAttr18 = markup.GetAttr("recMark");
        if (GetAttr18 != null) {
            myUiItem2.setRecMark(getIntArray(GetAttr18));
        }
        String GetAttr19 = markup.GetAttr("drawable");
        if (GetAttr19 != null) {
            myUiItem2.setStrDrawable(GetAttr19);
        }
        String GetAttr20 = markup.GetAttr("drawableExtra");
        if (GetAttr20 != null) {
            myUiItem2.setStrDrawableExtra(GetAttr20.split(","));
        }
        String GetAttr21 = markup.GetAttr("longclick");
        if (GetAttr21 != null && GetAttr21.charAt(0) == '1') {
            myUiItem2.setLongClick(true);
        }
        String GetAttr22 = markup.GetAttr("text");
        if (GetAttr22 != null) {
            String string = MyApplication.myApp.getString(GetAttr22);
            if (TextUtils.isEmpty(string)) {
                string = GetAttr22;
            }
            myUiItem2.setText(string);
        }
        String GetAttr23 = markup.GetAttr("color");
        if (GetAttr23 != null) {
            myUiItem2.setColor(GetAttr23.split(","));
        }
        String GetAttr24 = markup.GetAttr("colorBK");
        if (GetAttr24 != null) {
            myUiItem2.setColorBk(GetAttr24.split(","));
        }
        if (markup.IntoItem()) {
            int i3 = 0;
            SparseArray sparseArray = new SparseArray();
            do {
                String FindToken = markup.FindToken(markup.mItemCur.mStrBuff, markup.mItemCur.mPosEnd - markup.mItemCur.mPosStart, 0, (int[]) null);
                MyUiItem item = myUiItem2.getType() == 9 ? getItem(markup, FindToken, myUiItem2.getParentItem()) : getItem(markup, FindToken, myUiItem2);
                if (item != null) {
                    sparseArray.put(i3, item);
                    i3++;
                }
            } while (markup.NextItem());
            if (myUiItem2.getType() == 6) {
                myUiItem2.setType(5);
            }
            myUiItem2.setUiItems(sparseArray);
            markup.ExitItem();
        }
        return myUiItem2;
    }

    public void CreateViewByItem(JPage jPage, MyUiItem myUiItem, ViewGroup viewGroup) {
        int childCount;
        if (myUiItem == null) {
            return;
        }
        if (myUiItem.getType() == 9) {
            JButton jButton = (JButton) createUiItem(jPage, myUiItem, viewGroup);
            if (jButton != null && (childCount = myUiItem.getChildCount()) > 0) {
                jButton.mPartnerViews = new ArrayList();
                if (jButton.mPartnerViews != null) {
                    for (int i = 0; i < childCount; i++) {
                        View createUiItem = createUiItem(jPage, myUiItem.getChildAt(i), viewGroup);
                        if (createUiItem != null) {
                            jButton.mPartnerViews.add(createUiItem);
                        }
                    }
                }
            }
        } else if (myUiItem.getChildCount() > 0) {
            if (myUiItem.getType() == 2) {
                createScrollView(jPage, myUiItem, viewGroup);
            } else if (myUiItem.getType() == 3) {
                createScrollViewHorizontal(jPage, myUiItem, viewGroup);
            } else {
                createLayout(jPage, myUiItem, viewGroup);
            }
        } else if (myUiItem.getType() == 2) {
            createScrollView(jPage, myUiItem, viewGroup);
        } else if (myUiItem.getType() == 3) {
            createScrollViewHorizontal(jPage, myUiItem, viewGroup);
        } else if (myUiItem.getType() == 4) {
            createLayout(jPage, myUiItem, viewGroup);
        } else {
            createUiItem(jPage, myUiItem, viewGroup);
        }
    }

    public void LoadImage(View view, String str, String str2, int i) {
        if (str != null || str2 != null) {
            MyApplication.myApp.postRunnable_DecodeBmp(new DecodeBmp(view, str, str2, i));
        }
    }

    public void ParseXml(String str) {
        int i;
        if (!TextUtils.isEmpty(str)) {
            try {
                if (!TextUtils.isEmpty(this.mStrHeadXml)) {
                    i = MyApplication.mResources.getConfiguration().orientation == 1 ? MyApplication.mResources.getIdentifier("port_" + this.mStrHeadXml + str, "raw", MyApplication.mPkgName) : -1;
                    if (i <= 0) {
                        i = MyApplication.mResources.getIdentifier(String.valueOf(this.mStrHeadXml) + str, "raw", MyApplication.mPkgName);
                    }
                } else {
                    i = -1;
                }
                if (i <= 0 && MyApplication.mResources.getConfiguration().orientation == 1) {
                    i = MyApplication.mResources.getIdentifier("port_" + str, "raw", MyApplication.mPkgName);
                }
                if (i <= 0) {
                    i = MyApplication.mResources.getIdentifier(str, "raw", MyApplication.mPkgName);
                }
                if (i > 0) {
                    String readStrFromStream = FuncUtils.readStrFromStream(MyApplication.mResources.openRawResource(i));
                    if (!TextUtils.isEmpty(readStrFromStream)) {
                        Markup markup = new Markup();
                        markup.ReadXML(readStrFromStream);
                        if (markup.IntoItem()) {
                            do {
                                String FindToken = markup.FindToken(markup.mItemCur.mStrBuff, markup.mItemCur.mPosEnd - markup.mItemCur.mPosStart, 0, (int[]) null);
                                if (FindToken.equals("Page")) {
                                    MyUiItem item = getItem(markup, FindToken, (MyUiItem) null);
                                    if (item.getId() == -1) {
                                        throw new RuntimeException("page id can't be -1!");
                                    } else if (this.mPageUiItems.indexOfKey(item.getId()) < 0) {
                                        this.mPageUiItems.put(item.getId(), item);
                                    }
                                }
                            } while (markup.NextItem());
                            markup.ExitItem();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX WARNING: Failed to insert additional move for type inference */
    @android.annotation.SuppressLint({"NewApi"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.view.View createUiItem(com.syu.ctrl.JPage r10, com.syu.app.MyUiItem r11, android.view.ViewGroup r12) {
        /*
            r9 = this;
            r0 = 0
            com.syu.app.MyActivity r1 = r9.activity
            if (r1 == 0) goto L_0x000b
            com.syu.app.MyActivity r0 = r9.activity
            android.view.View r0 = r0.createUiItemOtherWay(r10, r11)
        L_0x000b:
            if (r0 != 0) goto L_0x0013
            com.syu.app.MyApplication r0 = com.syu.app.MyApplication.myApp
            android.view.View r0 = r0.createUiItemOtherWay(r10, r11)
        L_0x0013:
            if (r0 != 0) goto L_0x0031
            int r0 = r11.getType()
            switch(r0) {
                case 1: goto L_0x00a9;
                case 2: goto L_0x001c;
                case 3: goto L_0x001c;
                case 4: goto L_0x001c;
                case 5: goto L_0x001c;
                case 6: goto L_0x001c;
                case 7: goto L_0x0680;
                case 8: goto L_0x0689;
                case 9: goto L_0x00f0;
                case 10: goto L_0x0273;
                case 11: goto L_0x03cd;
                case 12: goto L_0x040b;
                case 13: goto L_0x0449;
                case 14: goto L_0x04cd;
                case 15: goto L_0x0585;
                case 16: goto L_0x05e5;
                case 17: goto L_0x05ee;
                case 18: goto L_0x063d;
                case 19: goto L_0x06a5;
                case 20: goto L_0x0711;
                case 21: goto L_0x07ce;
                case 22: goto L_0x07de;
                case 23: goto L_0x0812;
                case 24: goto L_0x082b;
                default: goto L_0x001c;
            }
        L_0x001c:
            com.syu.ctrl.JView r0 = new com.syu.ctrl.JView
            android.content.Context r1 = r9.mContext
            r0.<init>(r1, r9)
            java.lang.String r1 = r11.getStrDrawable()
            if (r1 == 0) goto L_0x0031
            java.lang.String r1 = r11.getStrDrawable()
            r2 = 0
            r0.setStrDrawable(r1, r2)
        L_0x0031:
            if (r0 == 0) goto L_0x00a1
            int r1 = r11.getId()
            r2 = -1
            if (r1 == r2) goto L_0x0041
            int r1 = r11.getId()
            r0.setId(r1)
        L_0x0041:
            java.lang.String r1 = r11.getStrDrawable()
            java.lang.String r2 = r11.getIcon()
            int r3 = r11.getType()
            r9.LoadImage(r0, r1, r2, r3)
            r10.addChildView(r0, r11)
            int r1 = r11.getVisible()
            r0.setVisibility(r1)
            android.widget.FrameLayout$LayoutParams r1 = r11.getLayoutParams()
            if (r1 == 0) goto L_0x0089
            boolean r2 = r12 instanceof android.widget.LinearLayout
            if (r2 == 0) goto L_0x0844
            android.widget.LinearLayout$LayoutParams r2 = new android.widget.LinearLayout$LayoutParams
            int r3 = r1.width
            int r4 = r1.height
            r2.<init>(r3, r4)
            int r3 = r1.gravity
            r2.gravity = r3
            int r3 = r1.leftMargin
            r2.leftMargin = r3
            int r1 = r1.topMargin
            r2.topMargin = r1
            int r1 = r11.getWeight()
            if (r1 <= 0) goto L_0x0086
            int r1 = r11.getWeight()
            float r1 = (float) r1
            r2.weight = r1
        L_0x0086:
            r0.setLayoutParams(r2)
        L_0x0089:
            int[] r1 = r11.getPadding()
            if (r1 == 0) goto L_0x009e
            r2 = 0
            r2 = r1[r2]
            r3 = 1
            r3 = r1[r3]
            r4 = 2
            r4 = r1[r4]
            r5 = 3
            r1 = r1[r5]
            r0.setPadding(r2, r3, r4, r1)
        L_0x009e:
            r12.addView(r0)
        L_0x00a1:
            if (r0 == 0) goto L_0x00a8
            com.syu.app.MyApplication r1 = com.syu.app.MyApplication.myApp
            r1.setProperty(r0)
        L_0x00a8:
            return r0
        L_0x00a9:
            android.util.SparseArray<com.syu.ctrl.JPage> r0 = r9.mPages
            int r1 = r11.getId()
            java.lang.Object r0 = r0.get(r1)
            if (r0 == 0) goto L_0x00be
            android.util.SparseArray<com.syu.ctrl.JPage> r0 = r9.mPages
            int r1 = r11.getId()
            r0.remove(r1)
        L_0x00be:
            r0 = 0
            java.lang.String[] r2 = r11.getParaStr()
            if (r2 == 0) goto L_0x00c9
            int r3 = r2.length
            r1 = 0
        L_0x00c7:
            if (r1 < r3) goto L_0x00d7
        L_0x00c9:
            r1 = r0
            r0 = 0
            if (r1 == 0) goto L_0x00e5
            r1 = 1
            int r2 = r11.getId()
            r9.loadPage(r1, r2, r10)
            goto L_0x0031
        L_0x00d7:
            r4 = r2[r1]
            java.lang.String r5 = "AddChild2Parent"
            boolean r4 = r4.contains(r5)
            if (r4 == 0) goto L_0x00e2
            r0 = 1
        L_0x00e2:
            int r1 = r1 + 1
            goto L_0x00c7
        L_0x00e5:
            r0 = 1
            int r1 = r11.getId()
            com.syu.ctrl.JPage r0 = r9.loadPage(r0, r1)
            goto L_0x0031
        L_0x00f0:
            com.syu.ctrl.JButton r2 = new com.syu.ctrl.JButton
            android.content.Context r0 = r9.mContext
            r2.<init>(r0, r10, r9)
            int[] r0 = r11.getPosIcon()
            java.lang.String r1 = r11.getIcon()
            r2.setIcon(r0, r1)
            java.lang.String r0 = r11.getStrDrawable()
            if (r0 == 0) goto L_0x0110
            java.lang.String r0 = r11.getStrDrawable()
            r1 = 0
            r2.setStrDrawable(r0, r1)
        L_0x0110:
            int[] r0 = r11.getColorBk()
            r2.setColorBk(r0)
            java.lang.String r0 = r11.getText()
            r2.setText(r0)
            r0 = 0
            int r1 = r11.getHeight()
            float r1 = (float) r1
            r2.setTextSize(r0, r1)
            int[] r0 = r11.getColor()
            if (r0 == 0) goto L_0x0149
            int[] r0 = r11.getColor()
            int r0 = r0.length
            r1 = 1
            if (r0 < r1) goto L_0x013f
            int[] r1 = r11.getColor()
            r3 = 0
            r1 = r1[r3]
            r2.setTextColor(r1)
        L_0x013f:
            r1 = 1
            if (r0 <= r1) goto L_0x0149
            int[] r0 = r11.getColor()
            r2.setColor(r0)
        L_0x0149:
            r1 = 0
            r0 = 0
            java.lang.String[] r4 = r11.getParaStr()
            if (r4 == 0) goto L_0x0155
            int r5 = r4.length
            r3 = 0
        L_0x0153:
            if (r3 < r5) goto L_0x016a
        L_0x0155:
            switch(r1) {
                case 1: goto L_0x0255;
                case 16: goto L_0x025d;
                case 17: goto L_0x0265;
                default: goto L_0x0158;
            }
        L_0x0158:
            if (r0 != 0) goto L_0x015f
            r0 = 17
            r2.setGravity(r0)
        L_0x015f:
            boolean r0 = r9.bIsInListItem
            if (r0 == 0) goto L_0x026d
            r0 = 0
            r2.setFocusable(r0)
        L_0x0167:
            r0 = r2
            goto L_0x0031
        L_0x016a:
            r6 = r4[r3]
            if (r0 != 0) goto L_0x0184
            java.lang.String r7 = "Left"
            boolean r7 = r6.contains(r7)
            if (r7 == 0) goto L_0x01bf
            r0 = 1
            java.lang.String r7 = "Top"
            boolean r7 = r6.contains(r7)
            if (r7 == 0) goto L_0x01ab
            r7 = 51
            r2.setGravity(r7)
        L_0x0184:
            java.lang.String r7 = "TruncateAtEnd"
            boolean r7 = r6.contains(r7)
            if (r7 == 0) goto L_0x0209
            android.text.TextUtils$TruncateAt r7 = android.text.TextUtils.TruncateAt.END
            r2.setEllipsize(r7)
        L_0x0191:
            java.lang.String r7 = "SingleLine"
            boolean r7 = r6.contains(r7)
            if (r7 == 0) goto L_0x019d
            r7 = 1
            r2.setSingleLine(r7)
        L_0x019d:
            java.lang.String r7 = "PartnerDependent"
            boolean r6 = r6.contains(r7)
            if (r6 == 0) goto L_0x01a8
            r6 = 1
            r2.bPartnerDependent = r6
        L_0x01a8:
            int r3 = r3 + 1
            goto L_0x0153
        L_0x01ab:
            java.lang.String r7 = "Bottom"
            boolean r7 = r6.contains(r7)
            if (r7 == 0) goto L_0x01b9
            r7 = 83
            r2.setGravity(r7)
            goto L_0x0184
        L_0x01b9:
            r7 = 19
            r2.setGravity(r7)
            goto L_0x0184
        L_0x01bf:
            java.lang.String r7 = "Right"
            boolean r7 = r6.contains(r7)
            if (r7 == 0) goto L_0x01ea
            r0 = 1
            java.lang.String r7 = "Top"
            boolean r7 = r6.contains(r7)
            if (r7 == 0) goto L_0x01d6
            r7 = 53
            r2.setGravity(r7)
            goto L_0x0184
        L_0x01d6:
            java.lang.String r7 = "Bottom"
            boolean r7 = r6.contains(r7)
            if (r7 == 0) goto L_0x01e4
            r7 = 85
            r2.setGravity(r7)
            goto L_0x0184
        L_0x01e4:
            r7 = 21
            r2.setGravity(r7)
            goto L_0x0184
        L_0x01ea:
            java.lang.String r7 = "Bottom"
            boolean r7 = r6.contains(r7)
            if (r7 == 0) goto L_0x01f9
            r0 = 1
            r7 = 81
            r2.setGravity(r7)
            goto L_0x0184
        L_0x01f9:
            java.lang.String r7 = "Top"
            boolean r7 = r6.contains(r7)
            if (r7 == 0) goto L_0x0184
            r0 = 1
            r7 = 49
            r2.setGravity(r7)
            goto L_0x0184
        L_0x0209:
            java.lang.String r7 = "TruncateAtMiddle"
            boolean r7 = r6.contains(r7)
            if (r7 == 0) goto L_0x0218
            android.text.TextUtils$TruncateAt r7 = android.text.TextUtils.TruncateAt.MIDDLE
            r2.setEllipsize(r7)
            goto L_0x0191
        L_0x0218:
            java.lang.String r7 = "TruncateAtStart"
            boolean r7 = r6.contains(r7)
            if (r7 == 0) goto L_0x0227
            android.text.TextUtils$TruncateAt r7 = android.text.TextUtils.TruncateAt.START
            r2.setEllipsize(r7)
            goto L_0x0191
        L_0x0227:
            java.lang.String r7 = "TruncateAtMarQuee"
            boolean r7 = r6.contains(r7)
            if (r7 == 0) goto L_0x023e
            r7 = 1
            r2.setFocus(r7)
            android.text.TextUtils$TruncateAt r7 = android.text.TextUtils.TruncateAt.MARQUEE
            r2.setEllipsize(r7)
            r7 = -1
            r2.setMarqueeRepeatLimit(r7)
            goto L_0x0191
        L_0x023e:
            java.lang.String r7 = "Bold"
            boolean r7 = r6.contains(r7)
            if (r7 == 0) goto L_0x0249
            r1 = 1
            goto L_0x0191
        L_0x0249:
            java.lang.String r7 = "Italic"
            boolean r7 = r6.contains(r7)
            if (r7 == 0) goto L_0x0191
            r1 = r1 | 16
            goto L_0x0191
        L_0x0255:
            android.graphics.Typeface r1 = android.graphics.Typeface.DEFAULT_BOLD
            r3 = 1
            r2.setTypeface(r1, r3)
            goto L_0x0158
        L_0x025d:
            android.graphics.Typeface r1 = android.graphics.Typeface.MONOSPACE
            r3 = 2
            r2.setTypeface(r1, r3)
            goto L_0x0158
        L_0x0265:
            android.graphics.Typeface r1 = android.graphics.Typeface.MONOSPACE
            r3 = 3
            r2.setTypeface(r1, r3)
            goto L_0x0158
        L_0x026d:
            r0 = 1
            r2.setFocusable(r0)
            goto L_0x0167
        L_0x0273:
            com.syu.ctrl.JText r3 = new com.syu.ctrl.JText
            android.content.Context r0 = r9.mContext
            r3.<init>(r0, r9)
            java.lang.String r0 = r11.getText()
            r3.setText(r0)
            java.lang.String r0 = r11.getStrDrawable()
            if (r0 == 0) goto L_0x028f
            java.lang.String r0 = r11.getStrDrawable()
            r1 = 0
            r3.setStrDrawable(r0, r1)
        L_0x028f:
            r2 = 1
            r0 = 0
            r1 = 0
            java.lang.String[] r5 = r11.getParaStr()
            if (r5 == 0) goto L_0x029c
            int r6 = r5.length
            r4 = 0
        L_0x029a:
            if (r4 < r6) goto L_0x02e2
        L_0x029c:
            switch(r1) {
                case 1: goto L_0x03b5;
                case 16: goto L_0x03bd;
                case 17: goto L_0x03c5;
                default: goto L_0x029f;
            }
        L_0x029f:
            if (r0 != 0) goto L_0x02a6
            r0 = 17
            r3.setGravity(r0)
        L_0x02a6:
            r0 = 0
            int r1 = r11.getHeight()
            float r1 = (float) r1
            r3.setTextSize(r0, r1)
            int[] r0 = r11.getColor()
            if (r0 == 0) goto L_0x02d1
            int[] r0 = r11.getColor()
            int r0 = r0.length
            r1 = 1
            if (r0 < r1) goto L_0x02c7
            int[] r1 = r11.getColor()
            r4 = 0
            r1 = r1[r4]
            r3.setTextColor(r1)
        L_0x02c7:
            r1 = 1
            if (r0 <= r1) goto L_0x02d1
            int[] r0 = r11.getColor()
            r3.setColor(r0)
        L_0x02d1:
            java.lang.String r0 = r11.getStrDrawable()
            if (r0 != 0) goto L_0x02dc
            r0 = 16777216(0x1000000, float:2.3509887E-38)
            r3.setBackgroundColor(r0)
        L_0x02dc:
            r3.setSingleLine(r2)
            r0 = r3
            goto L_0x0031
        L_0x02e2:
            r7 = r5[r4]
            if (r0 != 0) goto L_0x02fc
            java.lang.String r8 = "Left"
            boolean r8 = r7.contains(r8)
            if (r8 == 0) goto L_0x0320
            r0 = 1
            java.lang.String r8 = "Top"
            boolean r8 = r7.contains(r8)
            if (r8 == 0) goto L_0x030c
            r8 = 51
            r3.setGravity(r8)
        L_0x02fc:
            java.lang.String r8 = "TruncateAtEnd"
            boolean r8 = r7.contains(r8)
            if (r8 == 0) goto L_0x0369
            android.text.TextUtils$TruncateAt r7 = android.text.TextUtils.TruncateAt.END
            r3.setEllipsize(r7)
        L_0x0309:
            int r4 = r4 + 1
            goto L_0x029a
        L_0x030c:
            java.lang.String r8 = "Bottom"
            boolean r8 = r7.contains(r8)
            if (r8 == 0) goto L_0x031a
            r8 = 83
            r3.setGravity(r8)
            goto L_0x02fc
        L_0x031a:
            r8 = 19
            r3.setGravity(r8)
            goto L_0x02fc
        L_0x0320:
            java.lang.String r8 = "Right"
            boolean r8 = r7.contains(r8)
            if (r8 == 0) goto L_0x034b
            r0 = 1
            java.lang.String r8 = "Top"
            boolean r8 = r7.contains(r8)
            if (r8 == 0) goto L_0x0337
            r8 = 53
            r3.setGravity(r8)
            goto L_0x02fc
        L_0x0337:
            java.lang.String r8 = "Bottom"
            boolean r8 = r7.contains(r8)
            if (r8 == 0) goto L_0x0345
            r8 = 85
            r3.setGravity(r8)
            goto L_0x02fc
        L_0x0345:
            r8 = 21
            r3.setGravity(r8)
            goto L_0x02fc
        L_0x034b:
            java.lang.String r8 = "Bottom"
            boolean r8 = r7.contains(r8)
            if (r8 == 0) goto L_0x035a
            r0 = 1
            r8 = 81
            r3.setGravity(r8)
            goto L_0x02fc
        L_0x035a:
            java.lang.String r8 = "Top"
            boolean r8 = r7.contains(r8)
            if (r8 == 0) goto L_0x02fc
            r0 = 1
            r8 = 49
            r3.setGravity(r8)
            goto L_0x02fc
        L_0x0369:
            java.lang.String r8 = "TruncateAtMiddle"
            boolean r8 = r7.contains(r8)
            if (r8 == 0) goto L_0x0377
            android.text.TextUtils$TruncateAt r7 = android.text.TextUtils.TruncateAt.MIDDLE
            r3.setEllipsize(r7)
            goto L_0x0309
        L_0x0377:
            java.lang.String r8 = "TruncateAtStart"
            boolean r8 = r7.contains(r8)
            if (r8 == 0) goto L_0x0385
            android.text.TextUtils$TruncateAt r7 = android.text.TextUtils.TruncateAt.START
            r3.setEllipsize(r7)
            goto L_0x0309
        L_0x0385:
            java.lang.String r8 = "TruncateAtMarQuee"
            boolean r8 = r7.contains(r8)
            if (r8 == 0) goto L_0x0393
            r7 = 1
            r3.setMarQuee(r7)
            goto L_0x0309
        L_0x0393:
            java.lang.String r8 = "Bold"
            boolean r8 = r7.contains(r8)
            if (r8 == 0) goto L_0x039e
            r1 = 1
            goto L_0x0309
        L_0x039e:
            java.lang.String r8 = "Italic"
            boolean r8 = r7.contains(r8)
            if (r8 == 0) goto L_0x03aa
            r1 = r1 | 16
            goto L_0x0309
        L_0x03aa:
            java.lang.String r8 = "Multiline"
            boolean r7 = r7.contains(r8)
            if (r7 == 0) goto L_0x0309
            r2 = 0
            goto L_0x0309
        L_0x03b5:
            android.graphics.Typeface r1 = android.graphics.Typeface.DEFAULT_BOLD
            r4 = 1
            r3.setTypeface(r1, r4)
            goto L_0x029f
        L_0x03bd:
            android.graphics.Typeface r1 = android.graphics.Typeface.MONOSPACE
            r4 = 2
            r3.setTypeface(r1, r4)
            goto L_0x029f
        L_0x03c5:
            android.graphics.Typeface r1 = android.graphics.Typeface.MONOSPACE
            r4 = 3
            r3.setTypeface(r1, r4)
            goto L_0x029f
        L_0x03cd:
            com.syu.ctrl.JViewPager r1 = new com.syu.ctrl.JViewPager
            android.content.Context r0 = r9.mContext
            r1.<init>((android.content.Context) r0, (com.syu.ctrl.JPage) r10)
            java.lang.String[] r3 = r11.getParaStr()
            if (r3 == 0) goto L_0x0855
            int r4 = r3.length
            if (r4 <= 0) goto L_0x0855
            int[] r5 = new int[r4]
            java.util.HashMap<java.lang.String, java.lang.Integer> r0 = r9.mCtrlId
            if (r0 == 0) goto L_0x03e7
            r0 = 0
            r2 = r0
        L_0x03e5:
            if (r2 < r4) goto L_0x03ed
        L_0x03e7:
            r1.setUiLoader(r9, r5)
            r0 = r1
            goto L_0x0031
        L_0x03ed:
            java.util.HashMap<java.lang.String, java.lang.Integer> r0 = r9.mCtrlId
            r6 = r3[r2]
            boolean r0 = r0.containsKey(r6)
            if (r0 == 0) goto L_0x0407
            java.util.HashMap<java.lang.String, java.lang.Integer> r0 = r9.mCtrlId
            r6 = r3[r2]
            java.lang.Object r0 = r0.get(r6)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            r5[r2] = r0
        L_0x0407:
            int r0 = r2 + 1
            r2 = r0
            goto L_0x03e5
        L_0x040b:
            com.syu.ctrl.JViewPagerVertical r1 = new com.syu.ctrl.JViewPagerVertical
            android.content.Context r0 = r9.mContext
            r1.<init>((android.content.Context) r0, (com.syu.ctrl.JPage) r10)
            java.lang.String[] r3 = r11.getParaStr()
            if (r3 == 0) goto L_0x0852
            int r4 = r3.length
            if (r4 <= 0) goto L_0x0852
            int[] r5 = new int[r4]
            java.util.HashMap<java.lang.String, java.lang.Integer> r0 = r9.mCtrlId
            if (r0 == 0) goto L_0x0425
            r0 = 0
            r2 = r0
        L_0x0423:
            if (r2 < r4) goto L_0x042b
        L_0x0425:
            r1.setUiLoader(r9, r5)
            r0 = r1
            goto L_0x0031
        L_0x042b:
            java.util.HashMap<java.lang.String, java.lang.Integer> r0 = r9.mCtrlId
            r6 = r3[r2]
            boolean r0 = r0.containsKey(r6)
            if (r0 == 0) goto L_0x0445
            java.util.HashMap<java.lang.String, java.lang.Integer> r0 = r9.mCtrlId
            r6 = r3[r2]
            java.lang.Object r0 = r0.get(r6)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            r5[r2] = r0
        L_0x0445:
            int r0 = r2 + 1
            r2 = r0
            goto L_0x0423
        L_0x0449:
            com.syu.ctrl.JListViewEx r1 = new com.syu.ctrl.JListViewEx
            android.content.Context r0 = r9.mContext
            r1.<init>(r0, r10, r9)
            r0 = 0
            r1.setDividerHeight(r0)
            r0 = 0
            r1.setDivider(r0)
            r0 = 0
            r1.setGroupIndicator(r0)
            r0 = 0
            r1.setCacheColorHint(r0)
            r0 = 0
            r1.setBackgroundColor(r0)
            android.graphics.drawable.ColorDrawable r0 = new android.graphics.drawable.ColorDrawable
            r2 = 0
            r0.<init>(r2)
            r1.setSelector(r0)
            com.syu.app.MyUi$MyOnChildClickListener r0 = new com.syu.app.MyUi$MyOnChildClickListener
            r0.<init>(r1)
            r1.setOnChildClickListener(r0)
            int[] r0 = r11.getPara()
            if (r0 == 0) goto L_0x0486
            r2 = 0
            r0 = r0[r2]
            r2 = 1
            int r0 = getFixValue(r0, r2)
            r1.initSwipePara(r0)
        L_0x0486:
            java.lang.String[] r2 = r11.getParaStr()
            if (r2 == 0) goto L_0x04ca
            int r0 = r2.length
            if (r0 <= 0) goto L_0x04ca
            java.util.HashMap<java.lang.String, java.lang.Integer> r0 = r9.mCtrlId
            if (r0 == 0) goto L_0x04ca
            java.util.HashMap<java.lang.String, java.lang.Integer> r0 = r9.mCtrlId
            r3 = 0
            r3 = r2[r3]
            boolean r0 = r0.containsKey(r3)
            if (r0 == 0) goto L_0x04ca
            java.util.HashMap<java.lang.String, java.lang.Integer> r0 = r9.mCtrlId
            r3 = 1
            r3 = r2[r3]
            boolean r0 = r0.containsKey(r3)
            if (r0 == 0) goto L_0x04ca
            java.util.HashMap<java.lang.String, java.lang.Integer> r0 = r9.mCtrlId
            r3 = 0
            r3 = r2[r3]
            java.lang.Object r0 = r0.get(r3)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r3 = r0.intValue()
            java.util.HashMap<java.lang.String, java.lang.Integer> r0 = r9.mCtrlId
            r4 = 1
            r2 = r2[r4]
            java.lang.Object r0 = r0.get(r2)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            r1.setPageRow(r3, r0)
        L_0x04ca:
            r0 = r1
            goto L_0x0031
        L_0x04cd:
            com.syu.ctrl.JGridView r1 = new com.syu.ctrl.JGridView
            android.content.Context r0 = r9.mContext
            r1.<init>(r0, r10, r9)
            int r0 = r11.getHeight()
            r1.setMaxRowDisp(r0)
            r0 = 0
            r1.setCacheColorHint(r0)
            android.graphics.drawable.ColorDrawable r0 = new android.graphics.drawable.ColorDrawable
            r2 = 0
            r0.<init>(r2)
            r1.setSelector(r0)
            int[] r0 = r11.getPara()
            if (r0 == 0) goto L_0x056f
            r2 = 0
            r2 = r0[r2]
            r1.setNumColumns(r2)
            int r2 = r0.length
            r3 = 1
            if (r2 <= r3) goto L_0x0503
            r2 = 1
            r2 = r0[r2]
            r3 = 1
            int r2 = getFixValue(r2, r3)
            r1.setHorizontalSpacing(r2)
        L_0x0503:
            int r2 = r0.length
            r3 = 2
            if (r2 <= r3) goto L_0x0512
            r2 = 2
            r2 = r0[r2]
            r3 = 1
            int r2 = getFixValue(r2, r3)
            r1.setVerticalSpacing(r2)
        L_0x0512:
            int r2 = r0.length
            r3 = 3
            if (r2 <= r3) goto L_0x0526
            r2 = 3
            r2 = r0[r2]
            if (r2 <= 0) goto L_0x0526
            r2 = 3
            r0 = r0[r2]
            r2 = 1
            int r0 = getFixValue(r0, r2)
            r1.initSwipePara(r0)
        L_0x0526:
            com.syu.app.MyUi$MyOnItemClickListener r0 = new com.syu.app.MyUi$MyOnItemClickListener
            r0.<init>(r1)
            r1.setOnItemClickListener(r0)
            boolean r0 = r11.isLongClick()
            if (r0 == 0) goto L_0x053c
            com.syu.app.MyUi$MyOnItemLongClickListener r0 = new com.syu.app.MyUi$MyOnItemLongClickListener
            r0.<init>(r1)
            r1.setOnItemLongClickListener(r0)
        L_0x053c:
            java.lang.String[] r2 = r11.getParaStr()
            if (r2 == 0) goto L_0x056c
            int r3 = r2.length
            if (r3 <= 0) goto L_0x0566
            java.util.HashMap<java.lang.String, java.lang.Integer> r0 = r9.mCtrlId
            if (r0 == 0) goto L_0x0566
            java.util.HashMap<java.lang.String, java.lang.Integer> r0 = r9.mCtrlId
            r4 = 0
            r4 = r2[r4]
            boolean r0 = r0.containsKey(r4)
            if (r0 == 0) goto L_0x0566
            java.util.HashMap<java.lang.String, java.lang.Integer> r0 = r9.mCtrlId
            r4 = 0
            r4 = r2[r4]
            java.lang.Object r0 = r0.get(r4)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            r1.setPageRow(r0)
        L_0x0566:
            r0 = 1
            if (r3 <= r0) goto L_0x056c
            r0 = 1
        L_0x056a:
            if (r0 < r3) goto L_0x0574
        L_0x056c:
            r0 = r1
            goto L_0x0031
        L_0x056f:
            r0 = 1
            r1.setNumColumns(r0)
            goto L_0x0526
        L_0x0574:
            r4 = r2[r0]
            java.lang.String r5 = "InScrollView"
            boolean r4 = r4.contains(r5)
            if (r4 == 0) goto L_0x0582
            r0 = 1
            r1.mInScrollView = r0
            goto L_0x056c
        L_0x0582:
            int r0 = r0 + 1
            goto L_0x056a
        L_0x0585:
            android.widget.EditText r0 = new android.widget.EditText
            android.content.Context r1 = r9.mContext
            r0.<init>(r1)
            r1 = 0
            r0.setBackground(r1)
            r1 = 1
            r0.setFocusable(r1)
            r1 = 1
            r0.setFocusableInTouchMode(r1)
            r0.setSingleLine()
            r1 = 0
            int r2 = r11.getHeight()
            float r2 = (float) r2
            r0.setTextSize(r1, r2)
            int[] r1 = r11.getColor()
            if (r1 == 0) goto L_0x05b4
            int[] r1 = r11.getColor()
            r2 = 0
            r1 = r1[r2]
            r0.setTextColor(r1)
        L_0x05b4:
            java.lang.String[] r2 = r11.getParaStr()
            if (r2 == 0) goto L_0x05de
            int r3 = r2.length
            r1 = 0
        L_0x05bc:
            if (r1 >= r3) goto L_0x0031
            r4 = r2[r1]
            if (r4 != 0) goto L_0x05ca
            r4 = 17
            r0.setGravity(r4)
        L_0x05c7:
            int r1 = r1 + 1
            goto L_0x05bc
        L_0x05ca:
            java.lang.String r5 = "Left"
            boolean r4 = r4.equals(r5)
            if (r4 == 0) goto L_0x05d8
            r4 = 19
            r0.setGravity(r4)
            goto L_0x05c7
        L_0x05d8:
            r4 = 21
            r0.setGravity(r4)
            goto L_0x05c7
        L_0x05de:
            r1 = 17
            r0.setGravity(r1)
            goto L_0x0031
        L_0x05e5:
            android.widget.ImageView r0 = new android.widget.ImageView
            android.content.Context r1 = r9.mContext
            r0.<init>(r1)
            goto L_0x0031
        L_0x05ee:
            java.lang.String[] r2 = r11.getStrDrawableExtra()
            r0 = 0
            if (r2 == 0) goto L_0x05fa
            int r1 = r2.length
            r3 = 2
            if (r1 != r3) goto L_0x05fa
            r0 = 1
        L_0x05fa:
            com.syu.ctrl.JSwitchButton r1 = new com.syu.ctrl.JSwitchButton
            android.content.Context r3 = r9.mContext
            r1.<init>(r3, r10, r9, r0)
            java.lang.String r0 = r11.getStrDrawable()
            if (r0 == 0) goto L_0x060f
            java.lang.String r0 = r11.getStrDrawable()
            r3 = 0
            r1.setStrDrawable(r0, r3)
        L_0x060f:
            r1.setIconName(r2)
            java.lang.String[] r2 = r11.getParaStr()
            if (r2 == 0) goto L_0x061c
            int r3 = r2.length
            r0 = 0
        L_0x061a:
            if (r0 < r3) goto L_0x0627
        L_0x061c:
            boolean r0 = r9.bIsInListItem
            if (r0 == 0) goto L_0x0638
            r0 = 0
            r1.setFocusable(r0)
        L_0x0624:
            r0 = r1
            goto L_0x0031
        L_0x0627:
            r4 = r2[r0]
            java.lang.String r5 = "condition"
            boolean r4 = r4.contains(r5)
            if (r4 == 0) goto L_0x0635
            r4 = 1
            r1.setCondition(r4)
        L_0x0635:
            int r0 = r0 + 1
            goto L_0x061a
        L_0x0638:
            r0 = 1
            r1.setFocusable(r0)
            goto L_0x0624
        L_0x063d:
            com.syu.ctrl.JCheckBox r0 = new com.syu.ctrl.JCheckBox
            android.content.Context r1 = r9.mContext
            r0.<init>(r1, r10, r9)
            java.lang.String r1 = r11.getText()
            r0.setText(r1)
            r1 = 0
            int r2 = r11.getHeight()
            float r2 = (float) r2
            r0.setTextSize(r1, r2)
            int[] r1 = r11.getColor()
            if (r1 == 0) goto L_0x0664
            int[] r1 = r11.getColor()
            r2 = 0
            r1 = r1[r2]
            r0.setTextColor(r1)
        L_0x0664:
            java.lang.String r1 = r11.getStrDrawable()
            if (r1 == 0) goto L_0x0672
            java.lang.String r1 = r11.getStrDrawable()
            r2 = 0
            r0.setStrDrawable(r1, r2)
        L_0x0672:
            java.lang.String[] r1 = r11.getStrDrawableExtra()
            r0.setIconName(r1)
            r1 = 19
            r0.setGravity(r1)
            goto L_0x0031
        L_0x0680:
            android.view.SurfaceView r0 = new android.view.SurfaceView
            android.content.Context r1 = r9.mContext
            r0.<init>(r1)
            goto L_0x0031
        L_0x0689:
            r0 = 0
            int r1 = com.syu.ipcself.module.main.Main.mConf_PlatForm
            switch(r1) {
                case 6: goto L_0x069a;
                case 7: goto L_0x069a;
                default: goto L_0x068f;
            }
        L_0x068f:
            if (r0 == 0) goto L_0x069c
            com.syu.ctrl.JCameraTextureView r0 = new com.syu.ctrl.JCameraTextureView
            android.content.Context r1 = r9.mContext
            r0.<init>(r1)
            goto L_0x0031
        L_0x069a:
            r0 = 1
            goto L_0x068f
        L_0x069c:
            com.syu.ctrl.JCameraTexture r0 = new com.syu.ctrl.JCameraTexture
            android.content.Context r1 = r9.mContext
            r0.<init>(r1)
            goto L_0x0031
        L_0x06a5:
            r1 = 16842871(0x1010077, float:2.3693892E-38)
            r0 = 0
            int[] r2 = r11.getPara()
            if (r2 == 0) goto L_0x084f
            r0 = 0
            r0 = r2[r0]
            r2 = 1
            if (r0 != r2) goto L_0x0700
            r0 = 1
        L_0x06b6:
            r2 = r0
        L_0x06b7:
            if (r2 == 0) goto L_0x084c
            r0 = 16842872(0x1010078, float:2.3693894E-38)
        L_0x06bc:
            android.widget.ProgressBar r1 = new android.widget.ProgressBar
            android.content.Context r3 = r9.mContext
            r4 = 0
            r1.<init>(r3, r4, r0)
            if (r2 == 0) goto L_0x0702
            java.lang.String[] r0 = r11.getStrDrawableExtra()
            if (r0 == 0) goto L_0x06fd
            android.graphics.drawable.Drawable r0 = r1.getProgressDrawable()
            android.graphics.drawable.LayerDrawable r0 = (android.graphics.drawable.LayerDrawable) r0
            r2 = 16908288(0x1020000, float:2.387723E-38)
            java.lang.String[] r3 = r11.getStrDrawableExtra()
            r4 = 0
            r3 = r3[r4]
            android.graphics.drawable.Drawable r3 = r9.getDrawableFromPath(r3)
            r0.setDrawableByLayerId(r2, r3)
            android.graphics.drawable.ClipDrawable r2 = new android.graphics.drawable.ClipDrawable
            java.lang.String[] r3 = r11.getStrDrawableExtra()
            r4 = 1
            r3 = r3[r4]
            android.graphics.drawable.Drawable r3 = r9.getDrawableFromPath(r3)
            r4 = 3
            r5 = 1
            r2.<init>(r3, r4, r5)
            r3 = 16908301(0x102000d, float:2.3877265E-38)
            r0.setDrawableByLayerId(r3, r2)
            r1.setIndeterminateDrawable(r0)
        L_0x06fd:
            r0 = r1
            goto L_0x0031
        L_0x0700:
            r0 = 0
            goto L_0x06b6
        L_0x0702:
            java.lang.String[] r0 = r11.getStrDrawableExtra()
            r2 = 0
            r0 = r0[r2]
            android.graphics.drawable.Drawable r0 = r9.getDrawableFromPath(r0)
            r1.setIndeterminateDrawable(r0)
            goto L_0x06fd
        L_0x0711:
            com.syu.ctrl.JSeekBar r1 = new com.syu.ctrl.JSeekBar
            android.content.Context r0 = r9.mContext
            r1.<init>(r0, r10, r9)
            java.lang.String[] r0 = r11.getStrDrawableExtra()
            r1.setIconName(r0)
            int r0 = r11.getHeight()
            float r0 = (float) r0
            r1.setTextSize(r0)
            int[] r0 = r11.getColor()
            if (r0 == 0) goto L_0x0737
            int[] r0 = r11.getColor()
            r2 = 0
            r0 = r0[r2]
            r1.setTextColor(r0)
        L_0x0737:
            int[] r2 = r11.getPara()
            java.lang.String[] r3 = r11.getParaStr()
            if (r3 == 0) goto L_0x0745
            int r4 = r3.length
            r0 = 0
        L_0x0743:
            if (r0 < r4) goto L_0x07b0
        L_0x0745:
            android.graphics.Rect[] r3 = r11.getRect()
            r0 = 0
            r0 = r3[r0]
            r1.rec_seekBar = r0
            int r0 = r3.length
            r4 = 1
            if (r0 <= r4) goto L_0x0757
            r0 = 1
            r0 = r3[r0]
            r1.rec_thumb = r0
        L_0x0757:
            r0 = 0
            if (r2 == 0) goto L_0x0799
            int r4 = r2.length
            r5 = 1
            if (r4 < r5) goto L_0x0764
            r4 = 0
            r4 = r2[r4]
            r1.setProgressMax(r4)
        L_0x0764:
            int r4 = r2.length
            r5 = 2
            if (r4 < r5) goto L_0x076f
            r0 = 1
            r0 = r2[r0]
            r4 = 1
            if (r0 != r4) goto L_0x07cc
            r0 = 1
        L_0x076f:
            int r4 = r2.length
            r5 = 3
            if (r4 < r5) goto L_0x077d
            r4 = 2
            r4 = r2[r4]
            r5 = 1
            int r4 = getFixValue(r4, r5)
            r1.hExtraTxt = r4
        L_0x077d:
            int r4 = r2.length
            r5 = 4
            if (r4 < r5) goto L_0x078b
            r4 = 3
            r4 = r2[r4]
            r5 = 1
            int r4 = getFixValue(r4, r5)
            r1.hExtraTxtBk = r4
        L_0x078b:
            int r4 = r2.length
            r5 = 5
            if (r4 < r5) goto L_0x0799
            r4 = 4
            r2 = r2[r4]
            r4 = 1
            int r2 = getFixValue(r2, r4)
            r1.hLine = r2
        L_0x0799:
            if (r0 == 0) goto L_0x0849
            int r0 = r3.length
            r2 = 2
            if (r0 <= r2) goto L_0x07a4
            r0 = 2
            r0 = r3[r0]
            r1.rec_txtMin = r0
        L_0x07a4:
            int r0 = r3.length
            r2 = 3
            if (r0 <= r2) goto L_0x0849
            r0 = 3
            r0 = r3[r0]
            r1.rec_txtMax = r0
            r0 = r1
            goto L_0x0031
        L_0x07b0:
            r5 = r3[r0]
            java.lang.String r6 = "unSeekAble"
            boolean r6 = r5.contains(r6)
            if (r6 == 0) goto L_0x07c0
            r5 = 0
            r1.bCanSeekAble = r5
        L_0x07bd:
            int r0 = r0 + 1
            goto L_0x0743
        L_0x07c0:
            java.lang.String r6 = "DrawMidTxt"
            boolean r5 = r5.contains(r6)
            if (r5 == 0) goto L_0x07bd
            r5 = 1
            r1.bDrawMidTxt = r5
            goto L_0x07bd
        L_0x07cc:
            r0 = 0
            goto L_0x076f
        L_0x07ce:
            com.syu.ctrl.JIndicator r0 = new com.syu.ctrl.JIndicator
            android.content.Context r1 = r9.mContext
            r0.<init>(r1, r9)
            java.lang.String[] r1 = r11.getStrDrawableExtra()
            r0.setIconName(r1)
            goto L_0x0031
        L_0x07de:
            com.syu.ctrl.JListApp r1 = new com.syu.ctrl.JListApp
            android.content.Context r0 = r9.mContext
            r1.<init>(r0, r10, r9)
            java.lang.String[] r0 = r11.getParaStr()
            if (r0 == 0) goto L_0x080f
            int r2 = r0.length
            if (r2 <= 0) goto L_0x080f
            java.util.HashMap<java.lang.String, java.lang.Integer> r2 = r9.mCtrlId
            if (r2 == 0) goto L_0x080f
            java.util.HashMap<java.lang.String, java.lang.Integer> r2 = r9.mCtrlId
            r3 = 0
            r3 = r0[r3]
            boolean r2 = r2.containsKey(r3)
            if (r2 == 0) goto L_0x080f
            java.util.HashMap<java.lang.String, java.lang.Integer> r2 = r9.mCtrlId
            r3 = 0
            r0 = r0[r3]
            java.lang.Object r0 = r2.get(r0)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            r1.setPageRow(r0)
        L_0x080f:
            r0 = r1
            goto L_0x0031
        L_0x0812:
            com.syu.ctrl.JDashLine r0 = new com.syu.ctrl.JDashLine
            android.content.Context r1 = r9.mContext
            r0.<init>(r1)
            int[] r1 = r11.getColor()
            if (r1 == 0) goto L_0x0031
            int[] r1 = r11.getColor()
            r2 = 0
            r1 = r1[r2]
            r0.setColor(r1)
            goto L_0x0031
        L_0x082b:
            com.syu.ctrl.JLine r0 = new com.syu.ctrl.JLine
            android.content.Context r1 = r9.mContext
            r0.<init>(r1)
            int[] r1 = r11.getColor()
            if (r1 == 0) goto L_0x0031
            int[] r1 = r11.getColor()
            r2 = 0
            r1 = r1[r2]
            r0.setColor(r1)
            goto L_0x0031
        L_0x0844:
            r0.setLayoutParams(r1)
            goto L_0x0089
        L_0x0849:
            r0 = r1
            goto L_0x0031
        L_0x084c:
            r0 = r1
            goto L_0x06bc
        L_0x084f:
            r2 = r0
            goto L_0x06b7
        L_0x0852:
            r0 = r1
            goto L_0x0031
        L_0x0855:
            r0 = r1
            goto L_0x0031
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.app.MyUi.createUiItem(com.syu.ctrl.JPage, com.syu.app.MyUiItem, android.view.ViewGroup):android.view.View");
    }

    public Drawable getDrawableFromPath(String str) {
        Integer num;
        int intValue;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (!TextUtils.isEmpty(mStrHeadDrawable)) {
            str = String.valueOf(mStrHeadDrawable) + str;
        }
        Drawable drawable = this.mDrawables.get(str);
        if (drawable == null && !this.mDrawables.containsKey(str)) {
            Drawable drawableFromPath = mInterface_getDrawableFromPath != null ? mInterface_getDrawableFromPath.getDrawableFromPath(str) : drawable;
            drawable = (drawableFromPath != null || (num = MyApplication.mResMap.get(str)) == null || (intValue = num.intValue()) <= 0) ? drawableFromPath : MyApplication.mResources.getDrawable(intValue);
            this.mDrawables.put(str, drawable);
        }
        return drawable != null ? drawable.getConstantState().newDrawable() : drawable;
    }

    public int[] getIntArray(String str) {
        int length;
        int[] iArr = null;
        String[] split = str.split(",");
        if (split != null && (length = split.length) > 0) {
            iArr = new int[length];
            for (int i = 0; i < length; i++) {
                iArr[i] = MyApplication.myParseInt(split[i]);
            }
        }
        return iArr;
    }

    public JPage getPage(int i) {
        return this.mPages.get(i);
    }

    public Dialog getPopDlg(int i, int i2, boolean z) {
        return getPopDlg(i, i2, z, 0);
    }

    public Dialog getPopDlg(int i, int i2, boolean z, int i3) {
        if (this.activity == null) {
            return null;
        }
        Dialog dialog = this.mDlgs.get(i);
        if (dialog != null) {
            return dialog;
        }
        JPage loadPage = loadPage(true, i);
        if (loadPage != null) {
            Dialog dialog2 = i3 == 0 ? new Dialog(this.activity) : new Dialog(this.activity, i3);
            dialog2.requestWindowFeature(1);
            dialog2.setContentView(loadPage);
            dialog2.setCanceledOnTouchOutside(z);
            loadPage.setDialog(dialog2);
            dialog2.setOnDismissListener(new MyOnDismissListener(loadPage));
            MyUiItem myUiItem = (MyUiItem) loadPage.getTag();
            Window window = dialog2.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.height = myUiItem.getHeight();
            attributes.width = myUiItem.getWidth();
            attributes.dimAmount = 0.0f;
            window.setBackgroundDrawable(new ColorDrawable(0));
            int[] pos = myUiItem.getPos();
            if (IpcUtil.intsOk(pos, 2)) {
                attributes.x = pos[0];
                attributes.y = pos[1];
                window.setGravity(51);
            } else {
                window.setGravity(17);
            }
            window.setAttributes(attributes);
            if (i2 > 0) {
                window.setWindowAnimations(i2);
            }
            dialog = dialog2;
        }
        if (dialog == null) {
            return dialog;
        }
        this.mDlgs.put(i, dialog);
        return dialog;
    }

    public MyUiItem getUiItemAt(int i) {
        if (this.mPageUiItems == null || this.mPageUiItems.size() <= 0) {
            return null;
        }
        return this.mPageUiItems.get(i);
    }

    public JPage loadPage(boolean z, int i) {
        return loadPage(z, i, (JPage) null);
    }

    public JPage loadPage(boolean z, int i, JPage jPage) {
        this.bIsInListItem = !z;
        JPage jPage2 = this.mPages.get(i);
        if (jPage2 == null) {
            MyUiItem uiItemAt = getUiItemAt(i);
            if (uiItemAt == null && this.mMapPage.indexOfKey(i) >= 0) {
                ParseXml(this.mMapPage.get(i));
                uiItemAt = getUiItemAt(i);
            }
            if (uiItemAt != null) {
                jPage2 = createPage(uiItemAt, jPage);
                if (jPage == null) {
                    jPage2.setPadding(0, uiItemAt.mPlusPadding, 0, 0);
                    FrameLayout.LayoutParams layoutParams = uiItemAt.getLayoutParams();
                    if (layoutParams != null) {
                        jPage2.setLayoutParams(layoutParams);
                    }
                    if (z) {
                        jPage2.setStrDrawable(uiItemAt.getStrDrawable(), false);
                        LoadImage(jPage2, uiItemAt.getStrDrawable(), uiItemAt.getIcon(), uiItemAt.getType());
                        this.mPages.put(i, jPage2);
                    }
                    if (this.activity != null) {
                        this.activity.InitPage(jPage2);
                    } else {
                        MyApplication.myApp.InitPage(jPage2);
                    }
                    IPageNotify notify = jPage2.getNotify();
                    if (notify != null) {
                        notify.init();
                    }
                }
            }
        }
        return jPage2;
    }

    public void removePage(int i) {
        this.mPageUiItems.remove(i);
        this.mPages.remove(i);
    }
}
