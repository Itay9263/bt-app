package com.syu.bt.page.pop;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.ctrl.JGridView;
import com.syu.ctrl.JPage;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Main;
import com.syu.page.Page;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class Page_PopBt_Book extends Page {
    static Comparator<SparseArray<String>> comparator_Contact = new Comparator<SparseArray<String>>() {
        public int compare(SparseArray<String> sparseArray, SparseArray<String> sparseArray2) {
            String str = sparseArray.get(179);
            String str2 = sparseArray2.get(179);
            if (str == null) {
                return str2 != null ? -1 : 0;
            }
            if (str2 == null) {
                return 1;
            }
            int length = str.length();
            int length2 = str2.length();
            if (length < length2) {
                return -1;
            }
            if (length > length2) {
                return 1;
            }
            return str.compareTo(str2);
        }
    };
    boolean bShowAllListBook = false;
    JGridView mGridBook;
    JGridView mGridHistory;
    List<SparseArray<String>> mList = new ArrayList();
    List<SparseArray<String>> mListHistory = null;
    public Runnable mRunnableQueryHistory = new Runnable() {
        public void run() {
            Page_PopBt_Book.this.mListHistory = App.mBtInfo.queryCallLogAll();
            Main.postRunnable_Ui(true, Page_PopBt_Book.this.mRunnableResetListHistory);
        }
    };
    public Runnable mRunnableResetListHistory = new Runnable() {
        public void run() {
            Page_PopBt_Book.this.resetList_History();
        }
    };

    public Page_PopBt_Book(JPage jPage) {
        super(jPage);
    }

    public StringBuffer ConvPinyin2Num(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        String upperCase = str.toUpperCase();
        int length = upperCase.length();
        for (int i = 0; i < length; i++) {
            switch (upperCase.charAt(i)) {
                case 'A':
                case 'B':
                case 'C':
                    stringBuffer.append('2');
                    break;
                case 'D':
                case 'E':
                case 'F':
                    stringBuffer.append('3');
                    break;
                case 'G':
                case 'H':
                case 'I':
                    stringBuffer.append('4');
                    break;
                case 'J':
                case 'K':
                case 'L':
                    stringBuffer.append('5');
                    break;
                case 'M':
                case 'N':
                case 'O':
                    stringBuffer.append('6');
                    break;
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                    stringBuffer.append('7');
                    break;
                case 'T':
                case 'U':
                case 'V':
                    stringBuffer.append('8');
                    break;
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                    stringBuffer.append('9');
                    break;
            }
        }
        if (stringBuffer.length() == 0) {
            stringBuffer.append(str);
        }
        return stringBuffer;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x008b  */
    /* JADX WARNING: Removed duplicated region for block: B:69:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void InitGridItem(com.syu.ctrl.JGridView r15, com.syu.ctrl.JPage r16, android.util.SparseArray<java.lang.String> r17, int r18) {
        /*
            r14 = this;
            int r1 = r15.getId()
            switch(r1) {
                case 64: goto L_0x0008;
                case 65: goto L_0x00d8;
                default: goto L_0x0007;
            }
        L_0x0007:
            return
        L_0x0008:
            r1 = 66
            r0 = r16
            android.view.View r1 = r0.getChildViewByid(r1)
            if (r1 == 0) goto L_0x001d
            int r2 = r17.size()
            if (r2 != 0) goto L_0x0090
            r2 = 8
            r1.setVisibility(r2)
        L_0x001d:
            int r1 = r17.size()
            if (r1 <= 0) goto L_0x0007
            r1 = 68
            r0 = r16
            android.view.View r1 = r0.getChildViewByid(r1)
            com.syu.ctrl.JText r1 = (com.syu.ctrl.JText) r1
            r2 = 69
            r0 = r16
            android.view.View r2 = r0.getChildViewByid(r2)
            com.syu.ctrl.JText r2 = (com.syu.ctrl.JText) r2
            r3 = 180(0xb4, float:2.52E-43)
            r0 = r17
            java.lang.Object r3 = r0.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            r4 = 178(0xb2, float:2.5E-43)
            r0 = r17
            java.lang.Object r4 = r0.get(r4)
            java.lang.String r4 = (java.lang.String) r4
            r8 = 0
            r7 = 0
            boolean r5 = r14.bShowAllListBook
            if (r5 != 0) goto L_0x01d2
            java.lang.String r5 = com.syu.ipcself.module.main.Bt.sPhoneNumber
            boolean r5 = android.text.TextUtils.isEmpty(r5)
            if (r5 != 0) goto L_0x01d2
            java.lang.String r5 = com.syu.ipcself.module.main.Bt.sPhoneNumber
            int r5 = r3.indexOf(r5)
            if (r5 < 0) goto L_0x0095
            android.text.SpannableStringBuilder r6 = new android.text.SpannableStringBuilder
            r6.<init>(r3)
            android.text.style.ForegroundColorSpan r8 = new android.text.style.ForegroundColorSpan
            r9 = 0
            r10 = 230(0xe6, float:3.22E-43)
            r11 = 0
            int r9 = android.graphics.Color.rgb(r9, r10, r11)
            r8.<init>(r9)
            java.lang.String r9 = com.syu.ipcself.module.main.Bt.sPhoneNumber
            int r9 = r9.length()
            int r9 = r9 + r5
            r10 = 33
            r6.setSpan(r8, r5, r9, r10)
            r1.setText(r6)
            r6 = 1
            r5 = r7
        L_0x0084:
            if (r6 != 0) goto L_0x0089
            r1.setText(r3)
        L_0x0089:
            if (r5 != 0) goto L_0x0007
            r2.setText(r4)
            goto L_0x0007
        L_0x0090:
            r2 = 0
            r1.setVisibility(r2)
            goto L_0x001d
        L_0x0095:
            r5 = 176(0xb0, float:2.47E-43)
            r0 = r17
            java.lang.Object r5 = r0.get(r5)
            java.lang.String r5 = (java.lang.String) r5
            r6 = 177(0xb1, float:2.48E-43)
            r0 = r17
            java.lang.Object r6 = r0.get(r6)
            java.lang.String r6 = (java.lang.String) r6
            if (r5 == 0) goto L_0x01d2
            if (r6 == 0) goto L_0x01d2
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ Exception -> 0x00d4 }
            int r6 = java.lang.Integer.parseInt(r6)     // Catch:{ Exception -> 0x00d4 }
            if (r5 < 0) goto L_0x01d2
            android.text.SpannableStringBuilder r9 = new android.text.SpannableStringBuilder     // Catch:{ Exception -> 0x00d4 }
            r9.<init>(r4)     // Catch:{ Exception -> 0x00d4 }
            android.text.style.ForegroundColorSpan r10 = new android.text.style.ForegroundColorSpan     // Catch:{ Exception -> 0x00d4 }
            r11 = 0
            r12 = 230(0xe6, float:3.22E-43)
            r13 = 0
            int r11 = android.graphics.Color.rgb(r11, r12, r13)     // Catch:{ Exception -> 0x00d4 }
            r10.<init>(r11)     // Catch:{ Exception -> 0x00d4 }
            r11 = 33
            r9.setSpan(r10, r5, r6, r11)     // Catch:{ Exception -> 0x00d4 }
            r2.setText(r9)     // Catch:{ Exception -> 0x00d4 }
            r5 = 1
            r6 = r8
            goto L_0x0084
        L_0x00d4:
            r5 = move-exception
            r5 = r7
            r6 = r8
            goto L_0x0084
        L_0x00d8:
            r1 = 203(0xcb, float:2.84E-43)
            r0 = r17
            java.lang.Object r4 = r0.get(r1)
            java.lang.String r4 = (java.lang.String) r4
            r1 = 204(0xcc, float:2.86E-43)
            r0 = r17
            java.lang.Object r1 = r0.get(r1)
            r7 = r1
            java.lang.String r7 = (java.lang.String) r7
            r1 = 66
            r0 = r16
            android.view.View r1 = r0.getChildViewByid(r1)
            if (r1 == 0) goto L_0x0108
            int r2 = r17.size()
            if (r2 != 0) goto L_0x0104
            r2 = 8
            r1.setVisibility(r2)
            goto L_0x0007
        L_0x0104:
            r2 = 0
            r1.setVisibility(r2)
        L_0x0108:
            r1 = 69
            r0 = r16
            android.view.View r3 = r0.getChildViewByid(r1)
            com.syu.ctrl.JText r3 = (com.syu.ctrl.JText) r3
            r1 = 68
            r0 = r16
            android.view.View r1 = r0.getChildViewByid(r1)
            com.syu.ctrl.JText r1 = (com.syu.ctrl.JText) r1
            r2 = 70
            r0 = r16
            android.view.View r2 = r0.getChildViewByid(r2)
            r8 = r2
            com.syu.ctrl.JText r8 = (com.syu.ctrl.JText) r8
            if (r1 == 0) goto L_0x012c
            r1.setText(r4)
        L_0x012c:
            if (r3 == 0) goto L_0x0147
            java.lang.String r9 = "Thread-GetNameByNumberPopBtBook"
            bs r1 = new bs
            java.util.List<android.util.SparseArray<java.lang.String>> r2 = r14.mList
            com.syu.app.App r5 = com.syu.app.App.getApp()
            java.lang.String r6 = "unknown"
            java.lang.String r5 = r5.getString(r6)
            r6 = 1
            r1.<init>(r2, r3, r4, r5, r6)
            r2 = 0
            r3 = 5
            com.syu.app.App.startThread(r9, r1, r2, r3)
        L_0x0147:
            if (r8 == 0) goto L_0x014c
            r8.setText(r7)
        L_0x014c:
            r1 = 199(0xc7, float:2.79E-43)
            r0 = r17
            java.lang.Object r1 = r0.get(r1)
            java.lang.String r1 = (java.lang.String) r1
            int r4 = java.lang.Integer.parseInt(r1)
            r1 = 71
            r0 = r16
            android.view.View r2 = r0.getChildViewByid(r1)
            if (r2 == 0) goto L_0x0007
            java.lang.Object r1 = r2.getTag()
            com.syu.app.MyUiItem r1 = (com.syu.app.MyUiItem) r1
            java.lang.String[] r3 = r1.getParaStr()
            if (r3 == 0) goto L_0x019e
            boolean r3 = r2 instanceof com.syu.ctrl.JView
            if (r3 == 0) goto L_0x0007
            com.syu.ctrl.JView r2 = (com.syu.ctrl.JView) r2
            r3 = 0
            java.lang.String[] r5 = r1.getParaStr()
            switch(r4) {
                case 1: goto L_0x0192;
                case 2: goto L_0x0196;
                case 3: goto L_0x019a;
                default: goto L_0x017e;
            }
        L_0x017e:
            if (r3 != 0) goto L_0x01d0
            java.lang.String r1 = r1.getStrDrawable()
        L_0x0184:
            java.lang.String r3 = r2.getStrDrawable()
            if (r3 == r1) goto L_0x0007
            if (r1 == 0) goto L_0x0007
            r3 = 1
            r2.setStrDrawable(r1, r3)
            goto L_0x0007
        L_0x0192:
            r3 = 0
            r3 = r5[r3]
            goto L_0x017e
        L_0x0196:
            r3 = 1
            r3 = r5[r3]
            goto L_0x017e
        L_0x019a:
            r3 = 2
            r3 = r5[r3]
            goto L_0x017e
        L_0x019e:
            switch(r4) {
                case 1: goto L_0x01a3;
                case 2: goto L_0x01b2;
                case 3: goto L_0x01c1;
                default: goto L_0x01a1;
            }
        L_0x01a1:
            goto L_0x0007
        L_0x01a3:
            com.syu.app.App r1 = com.syu.app.App.getApp()
            java.lang.String r3 = "bt_icoincall"
            int r1 = r1.getIdDrawable(r3)
            r2.setBackgroundResource(r1)
            goto L_0x0007
        L_0x01b2:
            com.syu.app.App r1 = com.syu.app.App.getApp()
            java.lang.String r3 = "bt_icooutcall"
            int r1 = r1.getIdDrawable(r3)
            r2.setBackgroundResource(r1)
            goto L_0x0007
        L_0x01c1:
            com.syu.app.App r1 = com.syu.app.App.getApp()
            java.lang.String r3 = "bt_icomisscall"
            int r1 = r1.getIdDrawable(r3)
            r2.setBackgroundResource(r1)
            goto L_0x0007
        L_0x01d0:
            r1 = r3
            goto L_0x0184
        L_0x01d2:
            r5 = r7
            r6 = r8
            goto L_0x0084
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.page.pop.Page_PopBt_Book.InitGridItem(com.syu.ctrl.JGridView, com.syu.ctrl.JPage, android.util.SparseArray, int):void");
    }

    public void ResponseClick(int i) {
        switch (i) {
            case 36:
                IpcObj.downloadBook();
                return;
            default:
                return;
        }
    }

    public void ResponseClick(View view) {
        ResponseClick(view.getId());
    }

    public void init() {
        this.mGridBook = (JGridView) getPage().getChildViewByid(64);
        this.mGridHistory = (JGridView) getPage().getChildViewByid(65);
        showGridBook();
    }

    public boolean isNumber(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public boolean query(String[] strArr, String str, SparseArray<String> sparseArray) {
        boolean z;
        if (strArr == null) {
            return false;
        }
        int length = strArr.length;
        int length2 = str.length();
        for (int i = 0; i < length; i++) {
            int i2 = 0;
            int i3 = 0;
            int i4 = i;
            while (true) {
                if (i2 >= length2) {
                    z = true;
                    break;
                }
                char charAt = str.charAt(i2);
                if (i4 >= length) {
                    z = false;
                    break;
                } else if (strArr[i4].length() == 0) {
                    z = false;
                    break;
                } else {
                    if (strArr[i4].charAt(i3) == charAt) {
                        i3++;
                        if (i3 >= strArr[i4].length()) {
                            i4++;
                            i3 = 0;
                        }
                    } else if (i3 == 0) {
                        z = false;
                        break;
                    } else {
                        i4++;
                        i2--;
                        i3 = 0;
                    }
                    i2++;
                }
            }
            if (z) {
                int i5 = (i4 - i) + 1;
                if (i5 > length) {
                    i5 = length;
                }
                sparseArray.put(176, Integer.toString(i));
                sparseArray.put(177, Integer.toString(i5));
                return true;
            }
        }
        return false;
    }

    public void queryCalls() {
        App.startThread(App.StrThreadDBHistory, this.mRunnableQueryHistory, true, 10);
    }

    public void queryContacts(String str) {
        if (str.equals("*")) {
            showGridHistory();
            queryCalls();
            return;
        }
        showGridBook();
        if (TextUtils.isEmpty(str)) {
            resetList_Book(this.mList);
            return;
        }
        ArrayList arrayList = new ArrayList();
        if (isNumber(str)) {
            if (!str.contains("1")) {
                ArrayList arrayList2 = new ArrayList();
                for (SparseArray next : this.mList) {
                    String stringBuffer = ConvPinyin2Num(bx.b((String) next.get(178))).toString();
                    if (stringBuffer.contains(str)) {
                        int indexOf = stringBuffer.indexOf(str);
                        int length = str.length() + indexOf;
                        next.put(176, Integer.toString(indexOf));
                        next.put(177, Integer.toString(length));
                        arrayList2.add(next);
                    }
                }
                Collections.sort(arrayList2, comparator_Contact);
                arrayList.addAll(arrayList2);
                ArrayList arrayList3 = new ArrayList();
                for (SparseArray next2 : this.mList) {
                    if (!arrayList.contains(next2)) {
                        String str2 = (String) next2.get(178);
                        String[] split = (bv.h() ? b.a(str2, FinalChip.BSP_PLATFORM_Null) : bx.a(str2, false, false)).split(" ");
                        int length2 = split.length;
                        String[] strArr = new String[length2];
                        for (int i = 0; i < length2; i++) {
                            strArr[i] = ConvPinyin2Num(split[i]).toString();
                        }
                        if (query(strArr, str, next2)) {
                            arrayList3.add(next2);
                        }
                    }
                }
                Collections.sort(arrayList3, comparator_Contact);
                arrayList.addAll(arrayList3);
            }
            for (SparseArray next3 : this.mList) {
                if (!arrayList.contains(next3) && ((String) next3.get(180)).contains(str)) {
                    arrayList.add(next3);
                }
            }
            resetList_Book(arrayList);
            return;
        }
        for (SparseArray next4 : this.mList) {
            String str3 = (String) next4.get(178);
            if (str3.contains(str) || bx.b(str3).startsWith(str.toUpperCase())) {
                arrayList.add(next4);
            }
        }
        resetList_Book(arrayList);
    }

    public void resetList_Book(List<SparseArray<String>> list) {
        this.bShowAllListBook = list == this.mList;
        if (!IpcObj.isConnect()) {
            App.resetList(this.mGridBook, (List<SparseArray<String>>) null);
        } else {
            App.resetList(this.mGridBook, list);
        }
    }

    public void resetList_History() {
        if (!IpcObj.isConnect()) {
            App.resetList(this.mGridHistory, (List<SparseArray<String>>) null);
        } else {
            App.resetList(this.mGridHistory, this.mListHistory);
        }
    }

    public void resume() {
        super.resume();
        App.mBtInfo.sortContact();
        this.mList.clear();
        this.mList.addAll(App.mBtInfo.mListContact);
        showGridBook();
        updateListBook();
    }

    public void showGridBook() {
        if (this.mGridBook != null) {
            this.mGridBook.setVisibility(0);
        }
        if (this.mGridHistory != null) {
            this.mGridHistory.setVisibility(8);
        }
    }

    public void showGridHistory() {
        if (this.mGridBook != null) {
            this.mGridBook.setVisibility(8);
        }
        if (this.mGridHistory != null) {
            this.mGridHistory.setVisibility(0);
        }
    }

    public void updateListBook() {
        resetList_Book(this.mList);
    }
}
