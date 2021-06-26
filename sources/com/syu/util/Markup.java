package com.syu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Markup {
    public XmlItem mItemCur;
    public List<XmlItem> mItems;
    public int mLenData;
    public boolean mNoError;
    public String mStrData;

    public class XmlItem {
        public HashMap<String, String> mAttrs;
        public XmlItem mChild;
        public XmlItem mNext;
        public XmlItem mParent;
        public int mPosEnd;
        public int mPosStart;
        public String mStrBuff;

        public XmlItem() {
        }
    }

    public void ExitItem() {
        if (this.mItemCur != null) {
            this.mItemCur = this.mItemCur.mParent;
        }
    }

    public boolean FindItem(String str) {
        boolean z = false;
        if (str != null) {
            XmlItem xmlItem = this.mItemCur;
            while (true) {
                if (this.mItemCur == null) {
                    break;
                } else if (str.equals(FindToken(this.mStrData, this.mLenData, this.mItemCur.mPosStart, (int[]) null))) {
                    z = true;
                    break;
                } else {
                    this.mItemCur = this.mItemCur.mNext;
                }
            }
            this.mItemCur = xmlItem;
        }
        return z;
    }

    public boolean FindNextItem(String str) {
        if (NextItem()) {
            return FindItem(str);
        }
        return false;
    }

    public String FindToken(String str, int i, int i2, int[] iArr) {
        int i3;
        String str2 = null;
        if (iArr != null) {
            iArr[0] = -1;
        }
        if (i2 >= 0 && i2 < i && str != null) {
            char charAt = str.charAt(i2);
            int i4 = i2;
            while (true) {
                if ((charAt == ' ' || charAt == 9 || charAt == 13 || charAt == 10 || charAt == '=' || charAt == '<') && i4 < i - 1) {
                    i4++;
                    charAt = str.charAt(i4);
                }
            }
            if (charAt == '\'' || charAt == '\"') {
                int i5 = i4 + 1;
                i3 = i4 + 1;
                if (i3 < i) {
                    char charAt2 = str.charAt(i3);
                    while (true) {
                        if (charAt2 == charAt) {
                            i4 = i5;
                            break;
                        } else if (i3 >= i - 1) {
                            i4 = i5;
                            break;
                        } else {
                            i3++;
                            charAt2 = str.charAt(i3);
                        }
                    }
                } else {
                    i4 = i5;
                }
            } else {
                char charAt3 = str.charAt(i4);
                i3 = i4;
                while (charAt3 != ' ' && charAt3 != 9 && charAt3 != 13 && charAt3 != 10 && charAt3 != '=' && charAt3 != '<' && i3 < i - 1) {
                    i3++;
                    charAt3 = str.charAt(i3);
                }
            }
            if (i3 > i4) {
                str2 = str.substring(i4, i3);
            }
            if (iArr != null) {
                iArr[0] = i3 + 1;
            }
        }
        return str2;
    }

    public String GetAttr(String str) {
        if (str == null || this.mItemCur == null || !this.mItemCur.mAttrs.containsKey(str)) {
            return null;
        }
        return this.mItemCur.mAttrs.get(str);
    }

    public String GetData() {
        if (this.mItemCur == null || this.mItemCur.mChild != null) {
            return null;
        }
        return FindToken(this.mStrData, this.mLenData, this.mItemCur.mPosEnd + 1, (int[]) null);
    }

    public boolean HeadItem() {
        if (this.mItemCur == null) {
            ToRoot();
            return true;
        } else if (this.mItemCur.mParent == null) {
            return true;
        } else {
            this.mItemCur = this.mItemCur.mParent.mChild;
            return true;
        }
    }

    public boolean IntoItem() {
        if (this.mItemCur == null || this.mItemCur.mChild == null) {
            return false;
        }
        this.mItemCur = this.mItemCur.mChild;
        return true;
    }

    public boolean NextItem() {
        if (this.mItemCur == null || this.mItemCur.mNext == null) {
            return false;
        }
        this.mItemCur = this.mItemCur.mNext;
        return true;
    }

    public int ParseItem(int i, XmlItem xmlItem) {
        String FindToken;
        String FindToken2;
        if (this.mLenData <= i || i < 0) {
            return -1;
        }
        XmlItem xmlItem2 = null;
        while (this.mLenData > i && i >= 0) {
            int indexOf = this.mStrData.indexOf(60, i);
            if (indexOf < 0) {
                return -1;
            }
            int i2 = indexOf + 1;
            int indexOf2 = this.mStrData.indexOf(62, indexOf);
            if (indexOf2 >= 1) {
                boolean z = this.mStrData.charAt(indexOf2 + -1) == '/';
                int i3 = z ? indexOf2 - 1 : indexOf2;
                i = i3 + 1;
                char charAt = this.mStrData.charAt(indexOf + 1);
                if (!(charAt == '!' || charAt == '?')) {
                    if (charAt == '/') {
                        return i;
                    }
                    XmlItem xmlItem3 = new XmlItem();
                    xmlItem3.mNext = null;
                    xmlItem3.mChild = null;
                    xmlItem3.mParent = xmlItem;
                    xmlItem3.mPosStart = i2;
                    xmlItem3.mPosEnd = i3;
                    xmlItem3.mStrBuff = new String(this.mStrData.substring(i2, i3));
                    xmlItem3.mAttrs = new HashMap<>();
                    int[] iArr = {0};
                    int i4 = xmlItem3.mPosEnd - xmlItem3.mPosStart;
                    while (iArr[0] >= 0 && iArr[0] < i4 && (FindToken = FindToken(xmlItem3.mStrBuff, i4, iArr[0], iArr)) != null && iArr[0] < i4) {
                        if (xmlItem3.mStrBuff.charAt(iArr[0] - 1) == '=' && (FindToken2 = FindToken(xmlItem3.mStrBuff, i4, iArr[0], iArr)) != null) {
                            xmlItem3.mAttrs.put(FindToken, FindToken2);
                        }
                    }
                    if (xmlItem2 != null) {
                        xmlItem2.mNext = xmlItem3;
                    } else if (xmlItem != null) {
                        xmlItem.mChild = xmlItem3;
                    }
                    if (this.mItems == null) {
                        this.mItems = new ArrayList();
                    }
                    if (this.mItems != null) {
                        this.mItems.add(xmlItem3);
                    }
                    if (!z) {
                        i = ParseItem(i, xmlItem3);
                        xmlItem2 = xmlItem3;
                    } else {
                        xmlItem2 = xmlItem3;
                    }
                }
            } else {
                this.mNoError = false;
                return -1;
            }
        }
        return i;
    }

    public void ReadXML(String str) {
        if (str != null && str.length() > 0) {
            this.mStrData = new String(str);
            if (this.mStrData != null) {
                this.mLenData = this.mStrData.length();
                this.mNoError = true;
                ParseItem(0, (XmlItem) null);
                if (this.mNoError && this.mItems != null && this.mItems.size() > 0) {
                    this.mItemCur = this.mItems.get(0);
                }
            }
        }
    }

    public void ToRoot() {
        if (this.mNoError && this.mItems != null && this.mItems.size() > 0) {
            this.mItemCur = this.mItems.get(0);
        }
    }
}
