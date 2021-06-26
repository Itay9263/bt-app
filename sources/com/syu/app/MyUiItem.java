package com.syu.app;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.SparseArray;
import android.widget.FrameLayout;
import com.syu.util.MySize;

public class MyUiItem {
    public static final int FORWORD_Down = -4;
    public static final int FORWORD_Left = -1;
    public static final int FORWORD_Right = -3;
    public static final int FORWORD_Up = -2;
    int[] mColor;
    int[] mColorBk;
    int mId = -1;
    public int[] mIntDirs = new int[4];
    public int[] mIntPrevNext = new int[2];
    MyUiItem mItemParent;
    public FrameLayout.LayoutParams mLayoutParams;
    boolean mLongClick = false;
    int[] mPadding;
    int[] mPara;
    public int mPlusPadding = 0;
    int[] mPos;
    int[] mPosIcon;
    Point mPtConfig;
    int[] mRecMark;
    public Rect[] mRect;
    public MySize[] mSize;
    MySize mSizeCurrent = new MySize(100, 18);
    public String mStrAnim;
    String mStrDrawable;
    String[] mStrDrawableExtra;
    String mStrIcon;
    String[] mStrPara;
    String mStrText;
    int mType = 0;
    SparseArray<MyUiItem> mUiItems;
    int mVisible = 0;
    int mWeight = -1;

    public MyUiItem(int i) {
        this.mType = i;
    }

    public String getAnim() {
        return this.mStrAnim;
    }

    public MyUiItem getChildAt(int i) {
        if (this.mUiItems == null || i < 0 || i >= getChildCount()) {
            return null;
        }
        return this.mUiItems.get(this.mUiItems.keyAt(i));
    }

    public int getChildCount() {
        if (this.mUiItems != null) {
            return this.mUiItems.size();
        }
        return 0;
    }

    public int[] getColor() {
        return this.mColor;
    }

    public int[] getColorBk() {
        return this.mColorBk;
    }

    public int getHeight() {
        return this.mSizeCurrent.height;
    }

    public String getIcon() {
        return this.mStrIcon;
    }

    public int getId() {
        return this.mId;
    }

    public FrameLayout.LayoutParams getLayoutParams() {
        return this.mLayoutParams;
    }

    public int[] getPadding() {
        return this.mPadding;
    }

    public int[] getPara() {
        return this.mPara;
    }

    public String[] getParaStr() {
        return this.mStrPara;
    }

    public MyUiItem getParentItem() {
        return this.mItemParent;
    }

    public int[] getPos() {
        return this.mPos;
    }

    public int[] getPosIcon() {
        return this.mPosIcon;
    }

    public Point getPtConfig() {
        return this.mPtConfig;
    }

    public int[] getRecMark() {
        return this.mRecMark;
    }

    public Rect[] getRect() {
        return this.mRect;
    }

    public MySize[] getSize() {
        return this.mSize;
    }

    public String getStrDrawable() {
        return this.mStrDrawable;
    }

    public String[] getStrDrawableExtra() {
        return this.mStrDrawableExtra;
    }

    public String getText() {
        return this.mStrText;
    }

    public int getType() {
        return this.mType;
    }

    public int getVisible() {
        return this.mVisible;
    }

    public int getWeight() {
        return this.mWeight;
    }

    public int getWidth() {
        return this.mSizeCurrent.width;
    }

    public boolean isLongClick() {
        return this.mLongClick;
    }

    public boolean isPadding() {
        String[] paraStr = getParaStr();
        if (paraStr == null) {
            return false;
        }
        for (String contains : paraStr) {
            if (contains.contains("setPadding")) {
                return true;
            }
        }
        return false;
    }

    public void setAnim(String str) {
        this.mStrAnim = str;
    }

    public void setColor(int[] iArr) {
        if (iArr != null) {
            this.mColor = iArr;
        }
    }

    public void setColor(String... strArr) {
        int i;
        int i2 = 3;
        if (strArr != null && strArr.length != 0) {
            if (strArr.length <= 3) {
                i2 = strArr.length;
            }
            int[] iArr = new int[i2];
            int length = strArr.length;
            int i3 = 0;
            int i4 = 0;
            while (i3 < length) {
                String str = strArr[i3];
                if (i4 < iArr.length) {
                    i = i4 + 1;
                    iArr[i4] = MyApplication.myParseColor(str);
                } else {
                    i = i4;
                }
                i3++;
                i4 = i;
            }
            this.mColor = iArr;
        }
    }

    public void setColorBk(String... strArr) {
        int i;
        int i2 = 3;
        if (strArr != null && strArr.length != 0) {
            if (strArr.length <= 3) {
                i2 = strArr.length;
            }
            int[] iArr = new int[i2];
            int length = strArr.length;
            int i3 = 0;
            int i4 = 0;
            while (i3 < length) {
                String str = strArr[i3];
                if (i4 < iArr.length) {
                    i = i4 + 1;
                    iArr[i4] = MyApplication.myParseColor(str);
                } else {
                    i = i4;
                }
                i3++;
                i4 = i;
            }
            this.mColorBk = iArr;
        }
    }

    public void setDirStr(MyUi myUi, String... strArr) {
        if (myUi.mCtrlId != null && strArr != null && strArr.length == 4) {
            for (int i = 0; i < 4; i++) {
                if (myUi.mCtrlId.containsKey(strArr[i])) {
                    this.mIntDirs[i] = myUi.mCtrlId.get(strArr[i]).intValue();
                }
            }
        }
    }

    public void setHeight(int i) {
        this.mSizeCurrent.height = i;
    }

    public void setIcon(String str) {
        this.mStrIcon = str;
    }

    public void setId(int i) {
        this.mId = i;
    }

    public void setLongClick(boolean z) {
        this.mLongClick = z;
    }

    public void setPadding(int... iArr) {
        int i;
        if (iArr != null && iArr.length == 4) {
            if (this.mPadding == null) {
                this.mPadding = new int[4];
            }
            int length = iArr.length;
            int i2 = 0;
            int i3 = 0;
            while (i2 < length) {
                int i4 = iArr[i2];
                if (i3 < this.mPadding.length) {
                    i = i3 + 1;
                    this.mPadding[i3] = Math.round((float) MyUi.getFixValue(i4, false));
                } else {
                    i = i3;
                }
                i2++;
                i3 = i;
            }
        }
    }

    public void setPara(int... iArr) {
        if (iArr != null && iArr.length > 0) {
            if (this.mPara == null) {
                this.mPara = new int[iArr.length];
            }
            int length = iArr.length;
            int i = 0;
            int i2 = 0;
            while (i < length) {
                this.mPara[i2] = iArr[i];
                i++;
                i2++;
            }
        }
    }

    public void setParaStr(String... strArr) {
        this.mStrPara = strArr;
    }

    public void setParentItem(MyUiItem myUiItem) {
        this.mItemParent = myUiItem;
    }

    public void setPos(int... iArr) {
        int i;
        if (iArr != null && iArr.length == 2) {
            if (this.mPos == null) {
                this.mPos = new int[2];
            }
            int length = iArr.length;
            int i2 = 0;
            int i3 = 0;
            while (i2 < length) {
                int i4 = iArr[i2];
                if (i3 < this.mPos.length) {
                    i = i3 + 1;
                    this.mPos[i3] = Math.round((float) MyUi.getFixValue(i4, false));
                } else {
                    i = i3;
                }
                i2++;
                i3 = i;
            }
        }
    }

    public void setPosIcon(int... iArr) {
        int i;
        if (iArr != null && iArr.length == 4) {
            if (this.mPosIcon == null) {
                this.mPosIcon = new int[4];
            }
            int length = iArr.length;
            int i2 = 0;
            int i3 = 0;
            while (i2 < length) {
                int i4 = iArr[i2];
                if (i3 < this.mPosIcon.length) {
                    i = i3 + 1;
                    this.mPosIcon[i3] = Math.round((float) MyUi.getFixValue(i4, false));
                } else {
                    i = i3;
                }
                i2++;
                i3 = i;
            }
            int[] iArr2 = this.mPosIcon;
            iArr2[2] = iArr2[2] + this.mPosIcon[0];
            int[] iArr3 = this.mPosIcon;
            iArr3[3] = iArr3[3] + this.mPosIcon[1];
        }
    }

    public void setPrevNextStr(MyUi myUi, String... strArr) {
        if (myUi.mCtrlId != null && strArr != null && strArr.length == 2) {
            for (int i = 0; i < 2; i++) {
                if ("Left".equals(strArr[i])) {
                    this.mIntPrevNext[i] = -1;
                } else if ("Up".equals(strArr[i])) {
                    this.mIntPrevNext[i] = -2;
                } else if ("Right".equals(strArr[i])) {
                    this.mIntPrevNext[i] = -3;
                } else if ("Down".equals(strArr[i])) {
                    this.mIntPrevNext[i] = -4;
                } else if (myUi.mCtrlId.containsKey(strArr[i])) {
                    this.mIntPrevNext[i] = myUi.mCtrlId.get(strArr[i]).intValue();
                }
            }
        }
    }

    public void setRecMark(int... iArr) {
        if (iArr != null && iArr.length > 0) {
            if (this.mRecMark == null) {
                this.mRecMark = new int[iArr.length];
            }
            int length = iArr.length;
            int i = 0;
            int i2 = 0;
            while (i < length) {
                this.mRecMark[i2] = Math.round((float) MyUi.getFixValue(iArr[i], false));
                i++;
                i2++;
            }
        }
    }

    public void setRect(Point point, int... iArr) {
        int i;
        if (iArr != null && iArr.length != 0) {
            int[] iArr2 = new int[4];
            int length = iArr.length;
            int i2 = 0;
            int i3 = 0;
            while (i2 < length) {
                int i4 = iArr[i2];
                if (i3 < iArr2.length) {
                    i = i3 + 1;
                    iArr2[i3] = Math.round(((float) i4) * MyApplication.mScale);
                } else {
                    i = i3;
                }
                i2++;
                i3 = i;
            }
            this.mPtConfig = new Point(iArr2[0], iArr2[1]);
            if (point != null) {
                iArr2[0] = iArr2[0] - point.x;
                iArr2[1] = iArr2[1] - point.y;
            }
            iArr2[1] = iArr2[1] + this.mPlusPadding;
            if ((this.mType == 14 || this.mType == 13) && getHeight() > 0) {
                iArr2[3] = (iArr2[3] / getHeight()) * getHeight();
            }
            this.mLayoutParams = new FrameLayout.LayoutParams(iArr2[2], iArr2[3]);
            this.mLayoutParams.leftMargin = iArr2[0];
            this.mLayoutParams.topMargin = iArr2[1];
        }
    }

    public void setRect(Rect rect, int... iArr) {
        int i;
        if (rect != null && iArr != null && iArr.length != 0) {
            int[] iArr2 = new int[4];
            int length = iArr.length;
            int i2 = 0;
            int i3 = 0;
            while (i2 < length) {
                int i4 = iArr[i2];
                if (i3 < iArr2.length) {
                    i = i3 + 1;
                    iArr2[i3] = Math.round((float) MyUi.getFixValue(i4, false));
                } else {
                    i = i3;
                }
                i2++;
                i3 = i;
            }
            rect.left = iArr2[0];
            rect.top = this.mPlusPadding + iArr2[1];
            rect.right = iArr2[2] + rect.left;
            rect.bottom = iArr2[3] + rect.top;
            if (MyApplication.mScale < 1.0f) {
                if (rect.right <= rect.left) {
                    rect.right = rect.left + 1;
                }
                if (rect.bottom <= rect.top) {
                    rect.bottom = rect.top + 1;
                }
            }
        }
    }

    public void setSize(int... iArr) {
        if (iArr != null && iArr.length == 2) {
            this.mSizeCurrent.width = MyUi.getFixValue(iArr[0], true);
            this.mSizeCurrent.height = MyUi.getFixValue(iArr[1], true);
        }
    }

    public void setStrDrawable(String str) {
        this.mStrDrawable = str;
    }

    public void setStrDrawableExtra(String... strArr) {
        this.mStrDrawableExtra = strArr;
    }

    public void setText(String str) {
        this.mStrText = str;
    }

    public void setType(int i) {
        this.mType = i;
    }

    public void setUiItems(SparseArray<MyUiItem> sparseArray) {
        if (sparseArray != null && sparseArray.size() > 0) {
            this.mUiItems = sparseArray;
        }
    }

    public void setVisible(int i) {
        this.mVisible = i;
    }

    public void setWeight(int i) {
        this.mWeight = i;
    }
}
