package com.syu.bt;

import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.SparseArray;
import com.syu.app.App;
import com.syu.bt.act.InterfaceBt;
import com.syu.bt.page.pop.Page_PopBt_Book;
import com.syu.ctrl.JPage;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Bt;
import com.syu.util.MySharePreference;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Bt_Info {
    public static String[] b = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public static boolean bIsTR = false;
    static Collator collator_TR = Collator.getInstance(new Locale("tr", "TR"));
    static Comparator<SparseArray<String>> comparator2_Contact = new Comparator<SparseArray<String>>() {
        public int compare(SparseArray<String> sparseArray, SparseArray<String> sparseArray2) {
            String str = sparseArray.get(178);
            String str2 = sparseArray2.get(178);
            if (str == null) {
                return str2 != null ? -1 : 0;
            }
            if (str2 == null) {
                return 1;
            }
            if (!Bt_Info.bIsTR) {
                return str.compareTo(str2);
            }
            if (str.charAt(0) < 'A' && str2.charAt(0) >= 'A') {
                return 1;
            }
            if (str.charAt(0) < 'A' || str2.charAt(0) >= 'A') {
                return Bt_Info.collator_TR.compare(str, str2);
            }
            return -1;
        }
    };
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
            if (!Bt_Info.bIsTR) {
                return str.compareTo(str2);
            }
            if (str.charAt(0) < 'A' && str2.charAt(0) >= 'A') {
                return 1;
            }
            if (str.charAt(0) < 'A' || str2.charAt(0) >= 'A') {
                return Bt_Info.collator_TR.compare(str, str2);
            }
            return -1;
        }
    };
    public int PAIR_INDEX = 100;
    public boolean bClearing_Contact = false;
    public boolean bClearing_Log = false;
    public boolean bSaving = false;
    GetListContact mGetListContact = null;
    GetListHistory mGetListHistory = null;
    GetListHistoryAll mGetListHistoryAll = null;
    public List<SparseArray<String>> mListContact = new ArrayList();
    public List<SparseArray<String>> mListContactDownload = new ArrayList();
    public List<SparseArray<String>> mListFavContact = new ArrayList();
    public List<SparseArray<String>> mListHasPaired = new ArrayList();
    public List<SparseArray<String>> mListHistoryAll = new ArrayList();
    public List<SparseArray<String>> mListHistoryIn = new ArrayList();
    public List<SparseArray<String>> mListHistoryMiss = new ArrayList();
    public List<SparseArray<String>> mListHistoryOut = new ArrayList();
    public List<SparseArray<String>> mListPair = new ArrayList();
    public List<SparseArray<String>> mListSetPair = new ArrayList();
    public char[] mPinYin = new char[27];
    public Runnable mRunnableClearAllContact = new Runnable() {
        public void run() {
            Cursor cursor = null;
            try {
                Bt_Info.this.bClearing_Contact = true;
                Cursor access$0 = Bt_Info.this.clearContacts();
                if (access$0 != null) {
                    try {
                        access$0.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                }
                throw th;
            }
            Bt_Info.this.bClearing_Contact = false;
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().disPostLoading(0);
            }
        }
    };
    public Runnable mRunnable_ClearAllLog = new Runnable() {
        public void run() {
            try {
                Bt_Info.this.bClearing_Log = true;
                App.mContentResolver.delete(CallLog.Calls.CONTENT_URI, (String) null, (String[]) null);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Bt_Info.this.bClearing_Log = false;
            }
            App.queryCallLog();
        }
    };
    public RefreshColllogListener refresh;

    public class GetListContact implements Runnable {
        public boolean bRun = true;
        List<SparseArray<String>> listContact = new ArrayList();

        public GetListContact(List<SparseArray<String>> list) {
            this.listContact.addAll(list);
        }

        public void run() {
            JPage jPage;
            Page_PopBt_Book page_PopBt_Book;
            if (this.bRun) {
                Bt_Info.this.mListContact.clear();
                Bt_Info.this.mListContact.addAll(this.listContact);
                Bt_Info.this.sortContact();
                if (Bt_Info.this.mListContact.size() > 0) {
                    App.sContactsSaveFlag = true;
                }
                if (!(!App.getApp().bPopBtBook || (jPage = App.uiApp.mPages.get(4)) == null || (page_PopBt_Book = (Page_PopBt_Book) jPage.getNotify()) == null)) {
                    page_PopBt_Book.queryContacts(Bt.sPhoneNumber);
                }
                App.getApp().updatePhoneName();
                Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                while (it.hasNext()) {
                    it.next().resetList();
                }
            }
        }

        public void stopRun() {
            this.bRun = false;
        }
    }

    public class GetListHistory implements Runnable {
        public boolean bRun = true;
        List<SparseArray<String>> listHistoryAll = new ArrayList();
        List<SparseArray<String>> listHistoryIn = new ArrayList();
        List<SparseArray<String>> listHistoryMiss = new ArrayList();
        List<SparseArray<String>> listHistoryOut = new ArrayList();

        public GetListHistory(List<SparseArray<String>> list, List<SparseArray<String>> list2, List<SparseArray<String>> list3, List<SparseArray<String>> list4, List<SparseArray<String>> list5) {
            for (SparseArray next : list2) {
                if (bv.h()) {
                    String nameByNumber = Bt_Info.this.getNameByNumber((String) next.get(203), list);
                    if (TextUtils.isEmpty(nameByNumber)) {
                        next.put(201, (String) next.get(203));
                    } else {
                        next.put(201, nameByNumber);
                    }
                } else {
                    next.put(201, Bt_Info.this.getNameByNumber((String) next.get(203), list));
                }
            }
            for (SparseArray next2 : list3) {
                if (bv.h()) {
                    String nameByNumber2 = Bt_Info.this.getNameByNumber((String) next2.get(203), list);
                    if (TextUtils.isEmpty(nameByNumber2)) {
                        next2.put(201, (String) next2.get(203));
                    } else {
                        next2.put(201, nameByNumber2);
                    }
                } else {
                    next2.put(201, Bt_Info.this.getNameByNumber((String) next2.get(203), list));
                }
            }
            for (SparseArray next3 : list4) {
                if (bv.h()) {
                    String nameByNumber3 = Bt_Info.this.getNameByNumber((String) next3.get(203), list);
                    if (TextUtils.isEmpty(nameByNumber3)) {
                        next3.put(201, (String) next3.get(203));
                    } else {
                        next3.put(201, nameByNumber3);
                    }
                } else {
                    next3.put(201, Bt_Info.this.getNameByNumber((String) next3.get(203), list));
                }
            }
            this.listHistoryIn.addAll(list2);
            this.listHistoryOut.addAll(list3);
            this.listHistoryMiss.addAll(list4);
            if (bv.h()) {
                for (SparseArray next4 : list5) {
                    if (bv.h()) {
                        String nameByNumber4 = Bt_Info.this.getNameByNumber((String) next4.get(203), list);
                        if (TextUtils.isEmpty(nameByNumber4)) {
                            next4.put(201, (String) next4.get(203));
                        } else {
                            next4.put(201, nameByNumber4);
                        }
                    } else {
                        next4.put(201, Bt_Info.this.getNameByNumber((String) next4.get(203), list));
                    }
                }
                this.listHistoryAll.addAll(list5);
            }
        }

        public void run() {
            if (this.bRun) {
                Bt_Info.this.mListHistoryIn.clear();
                Bt_Info.this.mListHistoryOut.clear();
                Bt_Info.this.mListHistoryMiss.clear();
                Bt_Info.this.mListHistoryIn.addAll(this.listHistoryIn);
                Bt_Info.this.mListHistoryOut.addAll(this.listHistoryOut);
                Bt_Info.this.mListHistoryMiss.addAll(this.listHistoryMiss);
                if (bv.h()) {
                    Bt_Info.this.mListHistoryAll.clear();
                    Bt_Info.this.mListHistoryAll.addAll(this.listHistoryAll);
                }
                Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                while (it.hasNext()) {
                    it.next().resetList();
                }
            }
        }

        public void stopRun() {
            this.bRun = false;
        }
    }

    public class GetListHistoryAll implements Runnable {
        public boolean bRun = true;
        List<SparseArray<String>> listHistoryAll = new ArrayList();

        public GetListHistoryAll(List<SparseArray<String>> list, List<SparseArray<String>> list2) {
            for (SparseArray next : list2) {
                if (bv.h()) {
                    String nameByNumber = Bt_Info.this.getNameByNumber((String) next.get(203), list);
                    if (TextUtils.isEmpty(nameByNumber)) {
                        next.put(201, (String) next.get(203));
                    } else {
                        next.put(201, nameByNumber);
                    }
                } else {
                    next.put(201, Bt_Info.this.getNameByNumber((String) next.get(203), list));
                }
            }
            this.listHistoryAll.addAll(list2);
        }

        public void run() {
            if (this.bRun) {
                Bt_Info.this.mListHistoryAll.clear();
                Bt_Info.this.mListHistoryAll.addAll(this.listHistoryAll);
                Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                while (it.hasNext()) {
                    it.next().resetList();
                }
            }
        }

        public void stopRun() {
            this.bRun = false;
        }
    }

    public interface RefreshColllogListener {
        void refreshCollLogList();
    }

    public class RunnableClearCallLog implements Runnable {
        private int type;

        public RunnableClearCallLog(int i) {
            this.type = i;
        }

        public void run() {
            try {
                App.mContentResolver.delete(CallLog.Calls.CONTENT_URI, "type = ? ", new String[]{String.valueOf(this.type)});
                if (!App.bHistoryLogAllFlag) {
                    Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                    while (it.hasNext()) {
                        it.next().disPostLoading(0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (!App.bHistoryLogAllFlag) {
                    Iterator<InterfaceBt> it2 = App.mInterfaceBt.iterator();
                    while (it2.hasNext()) {
                        it2.next().disPostLoading(0);
                    }
                }
            } catch (Throwable th) {
                Throwable th2 = th;
                if (!App.bHistoryLogAllFlag) {
                    Iterator<InterfaceBt> it3 = App.mInterfaceBt.iterator();
                    while (it3.hasNext()) {
                        it3.next().disPostLoading(0);
                    }
                }
                throw th2;
            }
            App.queryCallLog();
        }
    }

    public class RunnableDelCallLog implements Runnable {
        private String number;
        private long time;

        public RunnableDelCallLog(String str, long j) {
            this.number = str;
            this.time = j;
        }

        public synchronized void run() {
            try {
                App.mContentResolver.delete(CallLog.Calls.CONTENT_URI, "number = ? and date = ? ", new String[]{this.number, String.valueOf(this.time)});
            } catch (Exception e) {
                e.printStackTrace();
            }
            App.queryCallLog();
            return;
        }
    }

    public class RunnableDelContact implements Runnable {
        private String name;
        private String number;

        public RunnableDelContact(String str, String str2) {
            this.name = str;
            this.number = str2;
        }

        /* JADX WARNING: Removed duplicated region for block: B:28:0x0070 A[SYNTHETIC, Splitter:B:28:0x0070] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r8 = this;
                r6 = 0
                android.content.ContentResolver r0 = com.syu.app.App.mContentResolver     // Catch:{ Exception -> 0x0080, all -> 0x006c }
                android.net.Uri r1 = android.provider.ContactsContract.Data.CONTENT_URI     // Catch:{ Exception -> 0x0080, all -> 0x006c }
                r2 = 1
                java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ Exception -> 0x0080, all -> 0x006c }
                r3 = 0
                java.lang.String r4 = "raw_contact_id"
                r2[r3] = r4     // Catch:{ Exception -> 0x0080, all -> 0x006c }
                java.lang.String r3 = "display_name = ? and data1 = ?"
                r4 = 2
                java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ Exception -> 0x0080, all -> 0x006c }
                r5 = 0
                java.lang.String r7 = r8.name     // Catch:{ Exception -> 0x0080, all -> 0x006c }
                r4[r5] = r7     // Catch:{ Exception -> 0x0080, all -> 0x006c }
                r5 = 1
                java.lang.String r7 = r8.number     // Catch:{ Exception -> 0x0080, all -> 0x006c }
                r4[r5] = r7     // Catch:{ Exception -> 0x0080, all -> 0x006c }
                r5 = 0
                android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x0080, all -> 0x006c }
                java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ Exception -> 0x005d }
                r0.<init>()     // Catch:{ Exception -> 0x005d }
                if (r1 == 0) goto L_0x003b
                int r2 = r1.getCount()     // Catch:{ Exception -> 0x005d }
                if (r2 <= 0) goto L_0x003b
            L_0x002e:
                boolean r2 = r1.moveToNext()     // Catch:{ Exception -> 0x005d }
                if (r2 != 0) goto L_0x0041
                android.content.ContentResolver r2 = com.syu.app.App.mContentResolver     // Catch:{ Exception -> 0x005d }
                java.lang.String r3 = "com.android.contacts"
                r2.applyBatch(r3, r0)     // Catch:{ Exception -> 0x005d }
            L_0x003b:
                if (r1 == 0) goto L_0x0040
                r1.close()     // Catch:{ Exception -> 0x0079 }
            L_0x0040:
                return
            L_0x0041:
                java.lang.String r2 = "raw_contact_id"
                int r2 = r1.getColumnIndex(r2)     // Catch:{ Exception -> 0x005d }
                long r2 = r1.getLong(r2)     // Catch:{ Exception -> 0x005d }
                android.net.Uri r4 = android.provider.ContactsContract.RawContacts.CONTENT_URI     // Catch:{ Exception -> 0x005d }
                android.net.Uri r2 = android.content.ContentUris.withAppendedId(r4, r2)     // Catch:{ Exception -> 0x005d }
                android.content.ContentProviderOperation$Builder r2 = android.content.ContentProviderOperation.newDelete(r2)     // Catch:{ Exception -> 0x005d }
                android.content.ContentProviderOperation r2 = r2.build()     // Catch:{ Exception -> 0x005d }
                r0.add(r2)     // Catch:{ Exception -> 0x005d }
                goto L_0x002e
            L_0x005d:
                r0 = move-exception
            L_0x005e:
                r0.printStackTrace()     // Catch:{ all -> 0x007e }
                if (r1 == 0) goto L_0x0040
                r1.close()     // Catch:{ Exception -> 0x0067 }
                goto L_0x0040
            L_0x0067:
                r0 = move-exception
                r0.printStackTrace()
                goto L_0x0040
            L_0x006c:
                r0 = move-exception
                r1 = r6
            L_0x006e:
                if (r1 == 0) goto L_0x0073
                r1.close()     // Catch:{ Exception -> 0x0074 }
            L_0x0073:
                throw r0
            L_0x0074:
                r1 = move-exception
                r1.printStackTrace()
                goto L_0x0073
            L_0x0079:
                r0 = move-exception
                r0.printStackTrace()
                goto L_0x0040
            L_0x007e:
                r0 = move-exception
                goto L_0x006e
            L_0x0080:
                r0 = move-exception
                r1 = r6
                goto L_0x005e
            */
            throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.Bt_Info.RunnableDelContact.run():void");
        }
    }

    public class RunnableInsertCallLog implements Runnable {
        private boolean bQueryCallLog;
        private ContentValues values;

        public RunnableInsertCallLog(ContentValues contentValues, boolean z) {
            this.values = contentValues;
            this.bQueryCallLog = z;
        }

        public synchronized void run() {
            if (this.values.getAsInteger("type") != null) {
                try {
                    if (this.values.get("number") != null) {
                        App.mContentResolver.insert(CallLog.Calls.CONTENT_URI, this.values);
                    }
                    this.values.clear();
                    if (Bt.values3rd.getAsInteger("type") != null) {
                        Bt.values.put("type", (Integer) Bt.values3rd.get("type"));
                        Bt.values.put("date", (Long) Bt.values3rd.get("date"));
                        Bt.values.put("number", (String) Bt.values3rd.get("number"));
                        Bt.values3rd.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (this.bQueryCallLog) {
                    App.queryCallLog();
                }
            }
            return;
        }
    }

    public class RunnableSaveDownloadContacts implements Runnable {
        private boolean isException = false;
        private List<SparseArray<String>> listSave;

        public RunnableSaveDownloadContacts(List<SparseArray<String>> list) {
            this.listSave = list;
        }

        public void run() {
            boolean z = true;
            Bt_Info.this.bSaving = true;
            MySharePreference.saveBooleanValue("save_content", false);
            try {
                if (!App.bAutoDownPhoneBook && !App.bAutoSavePhoneBook) {
                    Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                    while (it.hasNext()) {
                        it.next().showPostLoading(App.getApp().getString("bt_saving"));
                    }
                }
                MySharePreference.saveBooleanValue("save_content", false);
                Cursor query = App.mContentResolver.query(ContactsContract.Data.CONTENT_URI, new String[]{"raw_contact_id"}, (String) null, (String[]) null, (String) null);
                ArrayList arrayList = new ArrayList();
                try {
                    if (query.moveToFirst()) {
                        int count = query.getCount();
                        int i = 0;
                        do {
                            arrayList.add(ContentProviderOperation.newDelete(ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, query.getLong(query.getColumnIndex("raw_contact_id")))).build());
                            if (i == count - 1 || i % 80 == 0) {
                                App.mContentResolver.applyBatch("com.android.contacts", arrayList);
                                arrayList.clear();
                            }
                            i++;
                        } while (query.moveToNext());
                    }
                } catch (Exception e) {
                }
                arrayList.clear();
                int size = this.listSave.size();
                for (int i2 = 0; i2 < size; i2++) {
                    Bt_Info.this.opsAdd(this.listSave, arrayList, i2);
                    if (i2 % 40 == 0 || i2 == size - 1) {
                        App.mContentResolver.applyBatch("com.android.contacts", arrayList);
                        arrayList.clear();
                    }
                }
                Iterator<InterfaceBt> it2 = App.mInterfaceBt.iterator();
                while (it2.hasNext()) {
                    it2.next().disPostLoading(0);
                }
                if (!App.bAutoDownPhoneBook && !App.bAutoSavePhoneBook && this.isException) {
                    cb.a().a(App.getApp().getString("bt_save_failed"));
                }
                if (this.isException) {
                    z = false;
                }
                MySharePreference.saveBooleanValue("save_content", z);
                Bt_Info.this.bSaving = false;
            } catch (Exception e2) {
                e2.printStackTrace();
                this.isException = true;
                Iterator<InterfaceBt> it3 = App.mInterfaceBt.iterator();
                while (it3.hasNext()) {
                    it3.next().disPostLoading(0);
                }
                if (!App.bAutoDownPhoneBook && !App.bAutoSavePhoneBook && this.isException) {
                    cb.a().a(App.getApp().getString("bt_save_failed"));
                }
                MySharePreference.saveBooleanValue("save_content", !this.isException);
                Bt_Info.this.bSaving = false;
            } catch (Throwable th) {
                Throwable th2 = th;
                Iterator<InterfaceBt> it4 = App.mInterfaceBt.iterator();
                while (it4.hasNext()) {
                    it4.next().disPostLoading(0);
                }
                if (!App.bAutoDownPhoneBook && !App.bAutoSavePhoneBook && this.isException) {
                    cb.a().a(App.getApp().getString("bt_save_failed"));
                }
                if (this.isException) {
                    z = false;
                }
                MySharePreference.saveBooleanValue("save_content", z);
                Bt_Info.this.bSaving = false;
                throw th2;
            }
        }
    }

    public class RunnableSaveDownloadContactsIntoFile implements Runnable {
        private List<SparseArray<String>> listSave;

        public RunnableSaveDownloadContactsIntoFile(List<SparseArray<String>> list) {
            this.listSave = list;
        }

        public void run() {
            bt.a().a(this.listSave, Bt.sPhoneAddr.replace(":", FinalChip.BSP_PLATFORM_Null));
        }
    }

    /* access modifiers changed from: private */
    public Cursor clearContacts() {
        MySharePreference.saveBooleanValue("save_content", false);
        Cursor query = App.mContentResolver.query(ContactsContract.Data.CONTENT_URI, new String[]{"raw_contact_id"}, (String) null, (String[]) null, (String) null);
        ArrayList arrayList = new ArrayList();
        try {
            if (query.moveToFirst()) {
                int count = query.getCount();
                int i = 0;
                do {
                    arrayList.add(ContentProviderOperation.newDelete(ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, query.getLong(query.getColumnIndex("raw_contact_id")))).build());
                    if (i == count - 1 || i % 80 == 0) {
                        App.mContentResolver.applyBatch("com.android.contacts", arrayList);
                        arrayList.clear();
                    }
                    i++;
                } while (query.moveToNext());
            }
        } catch (Exception e) {
        }
        return query;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0010, code lost:
        r0 = r7.lastIndexOf(com.syu.app.App.sDialInput);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean match(java.lang.String r6, java.lang.String r7) {
        /*
            r5 = this;
            r2 = 0
            r1 = 1
            boolean r0 = defpackage.bv.h()
            if (r0 == 0) goto L_0x006d
            java.lang.String r0 = com.syu.app.App.sDialInput
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x006d
            java.lang.String r0 = com.syu.app.App.sDialInput
            int r0 = r7.lastIndexOf(r0)
            if (r0 <= 0) goto L_0x006d
            java.lang.String r0 = r7.substring(r2, r0)
        L_0x001c:
            boolean r3 = android.text.TextUtils.isEmpty(r6)
            if (r3 != 0) goto L_0x0028
            boolean r3 = android.text.TextUtils.isEmpty(r7)
            if (r3 == 0) goto L_0x002a
        L_0x0028:
            r0 = r2
        L_0x0029:
            return r0
        L_0x002a:
            java.lang.String r3 = r5.simplifyNumber(r6)
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x0036
            r0 = r1
            goto L_0x0029
        L_0x0036:
            java.lang.String r3 = "+972"
            boolean r3 = r6.startsWith(r3)
            if (r3 == 0) goto L_0x0042
            java.lang.String r0 = r0.substring(r1)
        L_0x0042:
            boolean r3 = r6.endsWith(r0)
            if (r3 == 0) goto L_0x005d
            int r3 = r6.length()
            int r4 = r0.length()
            int r3 = r3 - r4
            r4 = 6
            if (r3 > r4) goto L_0x005d
            int r3 = r0.length()
            r4 = 7
            if (r3 < r4) goto L_0x005d
            r0 = r1
            goto L_0x0029
        L_0x005d:
            boolean r3 = defpackage.bv.i()
            if (r3 == 0) goto L_0x006b
            boolean r0 = r6.contains(r0)
            if (r0 == 0) goto L_0x006b
            r0 = r1
            goto L_0x0029
        L_0x006b:
            r0 = r2
            goto L_0x0029
        L_0x006d:
            r0 = r7
            goto L_0x001c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.Bt_Info.match(java.lang.String, java.lang.String):boolean");
    }

    /* access modifiers changed from: private */
    public void opsAdd(List<SparseArray<String>> list, ArrayList<ContentProviderOperation> arrayList, int i) {
        SparseArray sparseArray = list.get(i);
        int size = arrayList.size();
        arrayList.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue("account_type", (Object) null).withValue("account_name", (Object) null).build());
        arrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", size).withValue("mimetype", "vnd.android.cursor.item/name").withValue("data1", sparseArray.get(178)).build());
        arrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", size).withValue("mimetype", "vnd.android.cursor.item/phone_v2").withValue("data1", sparseArray.get(180)).withValue("data2", 2).build());
    }

    public void clearAllCallLog() {
        App.startThread(App.StrThreadDBHistory, this.mRunnable_ClearAllLog, true, 1);
    }

    public void clearAllContacts() {
        App.startThread(App.StrThreadDBContact, this.mRunnableClearAllContact, true, 1);
    }

    public void clearCallLog(int i) {
        App.startThread(App.StrThreadDBHistory, new RunnableClearCallLog(i), false, 1);
    }

    public void delCallLog(String str, long j) {
        App.startThread(App.StrThreadDBHistory, new RunnableDelCallLog(str, j), false, 1);
    }

    public void delContact(String str, String str2) {
        App.startThread(App.StrThreadDBContact, new RunnableDelContact(str, str2), false, 1);
    }

    public boolean getContentState() {
        return this.bClearing_Contact;
    }

    public SparseArray<String> getMapContact(String str, String str2) {
        return getMapContact(str, str2, this.mListContact);
    }

    public SparseArray<String> getMapContact(String str, String str2, List<SparseArray<String>> list) {
        if (!TextUtils.isEmpty(str2) && list != null) {
            for (SparseArray<String> next : list) {
                if (next.get(180).equals(str2) && next.get(178).equals(str)) {
                    return next;
                }
            }
        }
        return null;
    }

    public SparseArray<String> getMapContactDownload(String str, String str2) {
        return getMapContact(str, str2, this.mListContactDownload);
    }

    public String getNameByNumber(String str) {
        return getNameByNumber(str, this.mListContact);
    }

    public String getNameByNumber(String str, List<SparseArray<String>> list) {
        if (!TextUtils.isEmpty(str) && list != null && list.size() > 0) {
            String simplifyNumber = simplifyNumber(str);
            for (SparseArray next : list) {
                if (match((String) next.get(180), simplifyNumber)) {
                    return (String) next.get(178);
                }
            }
        }
        return FinalChip.BSP_PLATFORM_Null;
    }

    public void handlerPairedBt(String str, String str2) {
        if (bv.c() && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            SparseArray sparseArray = new SparseArray();
            sparseArray.put(283, str);
            sparseArray.put(284, str2);
            sparseArray.put(282, Integer.toString(2));
            if (App.mBtInfo.mListPair != null && App.mBtInfo.mListPair.size() < 1) {
                App.mBtInfo.mListPair.add(sparseArray);
            }
        }
    }

    public void insertCallLog(ContentValues contentValues, boolean z) {
        App.startThread(App.StrThreadDBHistory, new RunnableInsertCallLog(contentValues, z), false, 1);
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x00b9 A[SYNTHETIC, Splitter:B:35:0x00b9] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<android.util.SparseArray<java.lang.String>> queryCallLogAll() {
        /*
            r11 = this;
            r6 = 0
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>()
            android.content.ContentResolver r0 = com.syu.app.App.mContentResolver     // Catch:{ Exception -> 0x00ca, all -> 0x00c7 }
            android.net.Uri r1 = android.provider.CallLog.Calls.CONTENT_URI     // Catch:{ Exception -> 0x00ca, all -> 0x00c7 }
            r2 = 3
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ Exception -> 0x00ca, all -> 0x00c7 }
            r3 = 0
            java.lang.String r4 = "type"
            r2[r3] = r4     // Catch:{ Exception -> 0x00ca, all -> 0x00c7 }
            r3 = 1
            java.lang.String r4 = "number"
            r2[r3] = r4     // Catch:{ Exception -> 0x00ca, all -> 0x00c7 }
            r3 = 2
            java.lang.String r4 = "date"
            r2[r3] = r4     // Catch:{ Exception -> 0x00ca, all -> 0x00c7 }
            r3 = 0
            r4 = 0
            java.lang.String r5 = "date DESC"
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x00ca, all -> 0x00c7 }
            if (r1 == 0) goto L_0x0044
            int r0 = r1.getCount()     // Catch:{ Exception -> 0x0096 }
            if (r0 <= 0) goto L_0x0044
            java.lang.String r0 = "type"
            int r2 = r1.getColumnIndex(r0)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r0 = "date"
            int r3 = r1.getColumnIndex(r0)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r0 = "number"
            int r4 = r1.getColumnIndex(r0)     // Catch:{ Exception -> 0x0096 }
        L_0x003e:
            boolean r0 = r1.moveToNext()     // Catch:{ Exception -> 0x0096 }
            if (r0 != 0) goto L_0x004a
        L_0x0044:
            if (r1 == 0) goto L_0x0049
            r1.close()     // Catch:{ Exception -> 0x00c2 }
        L_0x0049:
            return r7
        L_0x004a:
            int r5 = r1.getInt(r2)     // Catch:{ Exception -> 0x0096 }
            long r8 = r1.getLong(r3)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r0 = r1.getString(r4)     // Catch:{ Exception -> 0x0096 }
            android.util.SparseArray r6 = new android.util.SparseArray     // Catch:{ Exception -> 0x0096 }
            r6.<init>()     // Catch:{ Exception -> 0x0096 }
            r10 = 203(0xcb, float:2.84E-43)
            if (r0 == 0) goto L_0x00a5
        L_0x005f:
            r6.put(r10, r0)     // Catch:{ Exception -> 0x0096 }
            r0 = 199(0xc7, float:2.79E-43)
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ Exception -> 0x0096 }
            r6.put(r0, r5)     // Catch:{ Exception -> 0x0096 }
            r0 = 200(0xc8, float:2.8E-43)
            java.lang.String r5 = java.lang.Long.toString(r8)     // Catch:{ Exception -> 0x0096 }
            r6.put(r0, r5)     // Catch:{ Exception -> 0x0096 }
            boolean r0 = com.syu.app.App.hideBtnWhenDisconnect     // Catch:{ Exception -> 0x0096 }
            if (r0 == 0) goto L_0x00a8
            r0 = 204(0xcc, float:2.86E-43)
            java.lang.Long r5 = java.lang.Long.valueOf(r8)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r5 = defpackage.bt.a((java.lang.Long) r5)     // Catch:{ Exception -> 0x0096 }
            r6.put(r0, r5)     // Catch:{ Exception -> 0x0096 }
            r0 = 205(0xcd, float:2.87E-43)
            java.lang.Long r5 = java.lang.Long.valueOf(r8)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r5 = defpackage.bt.b((java.lang.Long) r5)     // Catch:{ Exception -> 0x0096 }
            r6.put(r0, r5)     // Catch:{ Exception -> 0x0096 }
        L_0x0092:
            r7.add(r6)     // Catch:{ Exception -> 0x0096 }
            goto L_0x003e
        L_0x0096:
            r0 = move-exception
        L_0x0097:
            r0.printStackTrace()     // Catch:{ all -> 0x00b6 }
            if (r1 == 0) goto L_0x0049
            r1.close()     // Catch:{ Exception -> 0x00a0 }
            goto L_0x0049
        L_0x00a0:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0049
        L_0x00a5:
            java.lang.String r0 = ""
            goto L_0x005f
        L_0x00a8:
            r0 = 204(0xcc, float:2.86E-43)
            com.syu.app.App r5 = com.syu.app.App.getApp()     // Catch:{ Exception -> 0x0096 }
            java.lang.String r5 = com.syu.util.FuncUtils.formatHistoryTimeString(r5, r8)     // Catch:{ Exception -> 0x0096 }
            r6.put(r0, r5)     // Catch:{ Exception -> 0x0096 }
            goto L_0x0092
        L_0x00b6:
            r0 = move-exception
        L_0x00b7:
            if (r1 == 0) goto L_0x00bc
            r1.close()     // Catch:{ Exception -> 0x00bd }
        L_0x00bc:
            throw r0
        L_0x00bd:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00bc
        L_0x00c2:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0049
        L_0x00c7:
            r0 = move-exception
            r1 = r6
            goto L_0x00b7
        L_0x00ca:
            r0 = move-exception
            r1 = r6
            goto L_0x0097
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.Bt_Info.queryCallLogAll():java.util.List");
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x007c  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0084  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x009e  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00ed A[SYNTHETIC, Splitter:B:60:0x00ed] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0101  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void queryContacts() {
        /*
            r10 = this;
            r8 = 0
            r6 = 1
            r7 = 0
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            java.lang.String r0 = "save_content"
            boolean r0 = com.syu.util.MySharePreference.getBooleanValue(r0, r7)
            if (r0 == 0) goto L_0x0050
            boolean r0 = com.syu.app.App.bDoClearWork
            if (r0 != 0) goto L_0x0050
            r0 = 2
            java.lang.String[] r2 = new java.lang.String[r0]     // Catch:{ Exception -> 0x00fe, all -> 0x00e9 }
            r0 = 0
            java.lang.String r1 = "data1"
            r2[r0] = r1     // Catch:{ Exception -> 0x00fe, all -> 0x00e9 }
            r0 = 1
            java.lang.String r1 = "display_name"
            r2[r0] = r1     // Catch:{ Exception -> 0x00fe, all -> 0x00e9 }
            android.content.ContentResolver r0 = com.syu.app.App.mContentResolver     // Catch:{ Exception -> 0x00fe, all -> 0x00e9 }
            android.net.Uri r1 = android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI     // Catch:{ Exception -> 0x00fe, all -> 0x00e9 }
            r3 = 0
            r4 = 0
            java.lang.String r5 = "display_name COLLATE LOCALIZED ASC"
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x00fe, all -> 0x00e9 }
            if (r1 == 0) goto L_0x004b
            int r0 = r1.getCount()     // Catch:{ Exception -> 0x00d8 }
            if (r0 <= 0) goto L_0x004b
            java.lang.String r0 = "display_name"
            int r0 = r1.getColumnIndex(r0)     // Catch:{ Exception -> 0x00d8 }
            java.lang.String r2 = "data1"
            int r2 = r1.getColumnIndex(r2)     // Catch:{ Exception -> 0x00d8 }
        L_0x0041:
            boolean r3 = r1.moveToNext()     // Catch:{ Exception -> 0x00d8 }
            if (r3 == 0) goto L_0x004b
            boolean r3 = com.syu.app.App.bDoClearWork     // Catch:{ Exception -> 0x00d8 }
            if (r3 == 0) goto L_0x00ad
        L_0x004b:
            if (r1 == 0) goto L_0x0050
            r1.close()     // Catch:{ Exception -> 0x00f6 }
        L_0x0050:
            java.lang.String r0 = com.syu.app.ipc.IpcObj.sPhoneAddrScan
            java.lang.String r1 = com.syu.ipcself.module.main.Bt.sPhoneAddr
            boolean r0 = r0.equalsIgnoreCase(r1)
            if (r0 == 0) goto L_0x00aa
            boolean r0 = com.syu.app.App.bDoClearWork
            if (r0 != 0) goto L_0x00aa
            int r0 = r9.size()
            if (r0 > 0) goto L_0x0101
            boolean r0 = com.syu.app.App.bAutoSavePhoneBook
            if (r0 == 0) goto L_0x0101
            bt r0 = defpackage.bt.a()
            java.lang.String r1 = com.syu.ipcself.module.main.Bt.sPhoneAddr
            java.lang.String r2 = ":"
            java.lang.String r3 = ""
            java.lang.String r1 = r1.replace(r2, r3)
            java.util.List r0 = r0.d(r1)
            if (r0 == 0) goto L_0x0101
            r9.addAll(r0)
            r0 = r6
        L_0x0080:
            com.syu.bt.Bt_Info$GetListContact r1 = r10.mGetListContact
            if (r1 == 0) goto L_0x0090
            com.syu.bt.Bt_Info$GetListContact r1 = r10.mGetListContact
            com.syu.ipcself.module.main.Main.removeRunnable_Ui(r1)
            com.syu.bt.Bt_Info$GetListContact r1 = r10.mGetListContact
            r1.stopRun()
            r10.mGetListContact = r8
        L_0x0090:
            com.syu.bt.Bt_Info$GetListContact r1 = new com.syu.bt.Bt_Info$GetListContact
            r1.<init>(r9)
            r10.mGetListContact = r1
            com.syu.bt.Bt_Info$GetListContact r1 = r10.mGetListContact
            com.syu.ipcself.module.main.Main.postRunnable_Ui(r7, r1)
            if (r0 == 0) goto L_0x00aa
            com.syu.app.App.sContactsSaveFlag = r6
            java.lang.String r0 = "db-operation-contact"
            com.syu.bt.Bt_Info$RunnableSaveDownloadContacts r1 = new com.syu.bt.Bt_Info$RunnableSaveDownloadContacts
            r1.<init>(r9)
            com.syu.app.App.startThread(r0, r1, r7, r6)
        L_0x00aa:
            com.syu.app.App.bDoClearWork = r7
            return
        L_0x00ad:
            java.lang.String r3 = com.syu.app.ipc.IpcObj.sPhoneAddrScan     // Catch:{ Exception -> 0x00d8 }
            java.lang.String r4 = com.syu.ipcself.module.main.Bt.sPhoneAddr     // Catch:{ Exception -> 0x00d8 }
            boolean r3 = r3.equalsIgnoreCase(r4)     // Catch:{ Exception -> 0x00d8 }
            if (r3 == 0) goto L_0x004b
            java.lang.String r3 = r1.getString(r0)     // Catch:{ Exception -> 0x00d8 }
            java.lang.String r4 = r1.getString(r2)     // Catch:{ Exception -> 0x00d8 }
            if (r3 == 0) goto L_0x0041
            if (r4 == 0) goto L_0x0041
            int r5 = r3.length()     // Catch:{ Exception -> 0x00d8 }
            if (r5 <= 0) goto L_0x0041
            int r5 = r4.length()     // Catch:{ Exception -> 0x00d8 }
            if (r5 <= 0) goto L_0x0041
            android.util.SparseArray r3 = com.syu.app.App.getNewMapContact(r3, r4)     // Catch:{ Exception -> 0x00d8 }
            r9.add(r3)     // Catch:{ Exception -> 0x00d8 }
            goto L_0x0041
        L_0x00d8:
            r0 = move-exception
        L_0x00d9:
            r0.printStackTrace()     // Catch:{ all -> 0x00fc }
            if (r1 == 0) goto L_0x0050
            r1.close()     // Catch:{ Exception -> 0x00e3 }
            goto L_0x0050
        L_0x00e3:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0050
        L_0x00e9:
            r0 = move-exception
            r1 = r8
        L_0x00eb:
            if (r1 == 0) goto L_0x00f0
            r1.close()     // Catch:{ Exception -> 0x00f1 }
        L_0x00f0:
            throw r0
        L_0x00f1:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00f0
        L_0x00f6:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0050
        L_0x00fc:
            r0 = move-exception
            goto L_0x00eb
        L_0x00fe:
            r0 = move-exception
            r1 = r8
            goto L_0x00d9
        L_0x0101:
            r0 = r7
            goto L_0x0080
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.Bt_Info.queryContacts():void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0062  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0126 A[SYNTHETIC, Splitter:B:55:0x0126] */
    /* JADX WARNING: Removed duplicated region for block: B:94:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void queryHistory(java.util.List<android.util.SparseArray<java.lang.String>> r15) {
        /*
            r14 = this;
            java.util.ArrayList r8 = new java.util.ArrayList
            r8.<init>()
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            r7 = 0
            android.content.ContentResolver r0 = com.syu.app.App.mContentResolver     // Catch:{ Exception -> 0x0191, all -> 0x018e }
            android.net.Uri r1 = android.provider.CallLog.Calls.CONTENT_URI     // Catch:{ Exception -> 0x0191, all -> 0x018e }
            r2 = 3
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ Exception -> 0x0191, all -> 0x018e }
            r3 = 0
            java.lang.String r4 = "type"
            r2[r3] = r4     // Catch:{ Exception -> 0x0191, all -> 0x018e }
            r3 = 1
            java.lang.String r4 = "number"
            r2[r3] = r4     // Catch:{ Exception -> 0x0191, all -> 0x018e }
            r3 = 2
            java.lang.String r4 = "date"
            r2[r3] = r4     // Catch:{ Exception -> 0x0191, all -> 0x018e }
            r3 = 0
            r4 = 0
            java.lang.String r5 = "date DESC"
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x0191, all -> 0x018e }
            if (r1 == 0) goto L_0x0053
            int r0 = r1.getCount()     // Catch:{ Exception -> 0x00fd }
            if (r0 <= 0) goto L_0x0053
            java.lang.String r0 = "type"
            int r2 = r1.getColumnIndex(r0)     // Catch:{ Exception -> 0x00fd }
            java.lang.String r0 = "date"
            int r3 = r1.getColumnIndex(r0)     // Catch:{ Exception -> 0x00fd }
            java.lang.String r0 = "number"
            int r4 = r1.getColumnIndex(r0)     // Catch:{ Exception -> 0x00fd }
        L_0x004d:
            boolean r0 = r1.moveToNext()     // Catch:{ Exception -> 0x00fd }
            if (r0 != 0) goto L_0x0094
        L_0x0053:
            if (r1 == 0) goto L_0x0058
            r1.close()     // Catch:{ Exception -> 0x015b }
        L_0x0058:
            java.lang.String r0 = com.syu.app.ipc.IpcObj.sPhoneAddrScan
            java.lang.String r1 = com.syu.ipcself.module.main.Bt.sPhoneAddr
            boolean r0 = r0.equalsIgnoreCase(r1)
            if (r0 == 0) goto L_0x0093
            boolean r0 = com.syu.app.App.bHistoryLogAllFlag
            if (r0 == 0) goto L_0x0169
            boolean r0 = defpackage.bv.h()
            if (r0 != 0) goto L_0x0169
            com.syu.bt.Bt_Info$GetListHistoryAll r0 = r14.mGetListHistoryAll
            if (r0 == 0) goto L_0x007d
            com.syu.bt.Bt_Info$GetListHistoryAll r0 = r14.mGetListHistoryAll
            com.syu.ipcself.module.main.Main.removeRunnable_Ui(r0)
            com.syu.bt.Bt_Info$GetListHistoryAll r0 = r14.mGetListHistoryAll
            r0.stopRun()
            r0 = 0
            r14.mGetListHistoryAll = r0
        L_0x007d:
            com.syu.bt.Bt_Info$GetListHistoryAll r0 = new com.syu.bt.Bt_Info$GetListHistoryAll
            r0.<init>(r15, r6)
            r14.mGetListHistoryAll = r0
            boolean r0 = com.syu.app.App.bBtAcc_On
            if (r0 == 0) goto L_0x0161
            r0 = 0
            com.syu.app.App.bBtAcc_On = r0
            r0 = 0
            com.syu.bt.Bt_Info$GetListHistoryAll r1 = r14.mGetListHistoryAll
            r2 = 3000(0xbb8, double:1.482E-320)
            com.syu.ipcself.module.main.Main.postRunnable_Ui(r0, r1, r2)
        L_0x0093:
            return
        L_0x0094:
            java.lang.String r0 = com.syu.app.ipc.IpcObj.sPhoneAddrScan     // Catch:{ Exception -> 0x00fd }
            java.lang.String r5 = com.syu.ipcself.module.main.Bt.sPhoneAddr     // Catch:{ Exception -> 0x00fd }
            boolean r0 = r0.equalsIgnoreCase(r5)     // Catch:{ Exception -> 0x00fd }
            if (r0 == 0) goto L_0x0053
            int r5 = r1.getInt(r2)     // Catch:{ Exception -> 0x00fd }
            long r12 = r1.getLong(r3)     // Catch:{ Exception -> 0x00fd }
            java.lang.String r0 = r1.getString(r4)     // Catch:{ Exception -> 0x00fd }
            android.util.SparseArray r7 = new android.util.SparseArray     // Catch:{ Exception -> 0x00fd }
            r7.<init>()     // Catch:{ Exception -> 0x00fd }
            r11 = 203(0xcb, float:2.84E-43)
            if (r0 == 0) goto L_0x010e
        L_0x00b3:
            r7.put(r11, r0)     // Catch:{ Exception -> 0x00fd }
            r0 = 199(0xc7, float:2.79E-43)
            java.lang.String r11 = java.lang.Integer.toString(r5)     // Catch:{ Exception -> 0x00fd }
            r7.put(r0, r11)     // Catch:{ Exception -> 0x00fd }
            r0 = 200(0xc8, float:2.8E-43)
            java.lang.String r11 = java.lang.Long.toString(r12)     // Catch:{ Exception -> 0x00fd }
            r7.put(r0, r11)     // Catch:{ Exception -> 0x00fd }
            boolean r0 = com.syu.app.App.hideBtnWhenDisconnect     // Catch:{ Exception -> 0x00fd }
            if (r0 == 0) goto L_0x0111
            r0 = 204(0xcc, float:2.86E-43)
            java.lang.Long r11 = java.lang.Long.valueOf(r12)     // Catch:{ Exception -> 0x00fd }
            java.lang.String r11 = defpackage.bt.a((java.lang.Long) r11)     // Catch:{ Exception -> 0x00fd }
            r7.put(r0, r11)     // Catch:{ Exception -> 0x00fd }
            r0 = 205(0xcd, float:2.87E-43)
            java.lang.Long r11 = java.lang.Long.valueOf(r12)     // Catch:{ Exception -> 0x00fd }
            java.lang.String r11 = defpackage.bt.b((java.lang.Long) r11)     // Catch:{ Exception -> 0x00fd }
            r7.put(r0, r11)     // Catch:{ Exception -> 0x00fd }
        L_0x00e6:
            boolean r0 = com.syu.app.App.bHistoryLogAllFlag     // Catch:{ Exception -> 0x00fd }
            if (r0 == 0) goto L_0x0142
            r6.add(r7)     // Catch:{ Exception -> 0x00fd }
            boolean r0 = defpackage.bv.h()     // Catch:{ Exception -> 0x00fd }
            if (r0 == 0) goto L_0x004d
            switch(r5) {
                case 1: goto L_0x00f8;
                case 2: goto L_0x0138;
                case 3: goto L_0x013d;
                default: goto L_0x00f6;
            }     // Catch:{ Exception -> 0x00fd }
        L_0x00f6:
            goto L_0x004d
        L_0x00f8:
            r8.add(r7)     // Catch:{ Exception -> 0x00fd }
            goto L_0x004d
        L_0x00fd:
            r0 = move-exception
        L_0x00fe:
            r0.printStackTrace()     // Catch:{ all -> 0x0123 }
            if (r1 == 0) goto L_0x0058
            r1.close()     // Catch:{ Exception -> 0x0108 }
            goto L_0x0058
        L_0x0108:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0058
        L_0x010e:
            java.lang.String r0 = ""
            goto L_0x00b3
        L_0x0111:
            boolean r0 = com.syu.app.App.bSyncHistory     // Catch:{ Exception -> 0x00fd }
            if (r0 == 0) goto L_0x012a
            r0 = 204(0xcc, float:2.86E-43)
            bt r11 = defpackage.bt.a()     // Catch:{ Exception -> 0x00fd }
            java.lang.String r11 = r11.a((long) r12)     // Catch:{ Exception -> 0x00fd }
            r7.put(r0, r11)     // Catch:{ Exception -> 0x00fd }
            goto L_0x00e6
        L_0x0123:
            r0 = move-exception
        L_0x0124:
            if (r1 == 0) goto L_0x0129
            r1.close()     // Catch:{ Exception -> 0x0156 }
        L_0x0129:
            throw r0
        L_0x012a:
            r0 = 204(0xcc, float:2.86E-43)
            com.syu.app.App r11 = com.syu.app.App.getApp()     // Catch:{ Exception -> 0x00fd }
            java.lang.String r11 = com.syu.util.FuncUtils.formatHistoryTimeString(r11, r12)     // Catch:{ Exception -> 0x00fd }
            r7.put(r0, r11)     // Catch:{ Exception -> 0x00fd }
            goto L_0x00e6
        L_0x0138:
            r9.add(r7)     // Catch:{ Exception -> 0x00fd }
            goto L_0x004d
        L_0x013d:
            r10.add(r7)     // Catch:{ Exception -> 0x00fd }
            goto L_0x004d
        L_0x0142:
            switch(r5) {
                case 1: goto L_0x0147;
                case 2: goto L_0x014c;
                case 3: goto L_0x0151;
                default: goto L_0x0145;
            }     // Catch:{ Exception -> 0x00fd }
        L_0x0145:
            goto L_0x004d
        L_0x0147:
            r8.add(r7)     // Catch:{ Exception -> 0x00fd }
            goto L_0x004d
        L_0x014c:
            r9.add(r7)     // Catch:{ Exception -> 0x00fd }
            goto L_0x004d
        L_0x0151:
            r10.add(r7)     // Catch:{ Exception -> 0x00fd }
            goto L_0x004d
        L_0x0156:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0129
        L_0x015b:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0058
        L_0x0161:
            r0 = 0
            com.syu.bt.Bt_Info$GetListHistoryAll r1 = r14.mGetListHistoryAll
            com.syu.ipcself.module.main.Main.postRunnable_Ui(r0, r1)
            goto L_0x0093
        L_0x0169:
            com.syu.bt.Bt_Info$GetListHistory r0 = new com.syu.bt.Bt_Info$GetListHistory
            r1 = r14
            r2 = r15
            r3 = r8
            r4 = r9
            r5 = r10
            r0.<init>(r2, r3, r4, r5, r6)
            r14.mGetListHistory = r0
            boolean r0 = com.syu.app.App.bBtAcc_On
            if (r0 == 0) goto L_0x0186
            r0 = 0
            com.syu.app.App.bBtAcc_On = r0
            r0 = 0
            com.syu.bt.Bt_Info$GetListHistory r1 = r14.mGetListHistory
            r2 = 3000(0xbb8, double:1.482E-320)
            com.syu.ipcself.module.main.Main.postRunnable_Ui(r0, r1, r2)
            goto L_0x0093
        L_0x0186:
            r0 = 0
            com.syu.bt.Bt_Info$GetListHistory r1 = r14.mGetListHistory
            com.syu.ipcself.module.main.Main.postRunnable_Ui(r0, r1)
            goto L_0x0093
        L_0x018e:
            r0 = move-exception
            r1 = r7
            goto L_0x0124
        L_0x0191:
            r0 = move-exception
            r1 = r7
            goto L_0x00fe
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.Bt_Info.queryHistory(java.util.List):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0027 A[SYNTHETIC, Splitter:B:12:0x0027] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002c A[SYNTHETIC, Splitter:B:15:0x002c] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0097 A[SYNTHETIC, Splitter:B:43:0x0097] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x009c A[SYNTHETIC, Splitter:B:46:0x009c] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00b1 A[SYNTHETIC, Splitter:B:56:0x00b1] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x00b6 A[SYNTHETIC, Splitter:B:59:0x00b6] */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0105  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x0023 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:95:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String queryNumberByName(java.lang.String r11) {
        /*
            r10 = this;
            r7 = 0
            java.lang.String r6 = ""
            boolean r0 = android.text.TextUtils.isEmpty(r11)
            if (r0 != 0) goto L_0x010f
            android.content.ContentResolver r0 = com.syu.app.App.mContentResolver     // Catch:{ Exception -> 0x008f, all -> 0x00ac }
            android.net.Uri r1 = android.provider.ContactsContract.Contacts.CONTENT_URI     // Catch:{ Exception -> 0x008f, all -> 0x00ac }
            r2 = 0
            r3 = 0
            r4 = 0
            r5 = 0
            android.database.Cursor r8 = r0.query(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x008f, all -> 0x00ac }
            if (r8 == 0) goto L_0x010b
            int r0 = r8.getCount()     // Catch:{ Exception -> 0x00fe, all -> 0x00f6 }
            if (r0 <= 0) goto L_0x010b
        L_0x001d:
            boolean r0 = r8.moveToNext()     // Catch:{ Exception -> 0x00fe, all -> 0x00f6 }
            if (r0 != 0) goto L_0x0045
            r1 = r7
            r0 = r6
        L_0x0025:
            if (r1 == 0) goto L_0x002a
            r1.close()     // Catch:{ Exception -> 0x00c4 }
        L_0x002a:
            if (r8 == 0) goto L_0x00ce
            r8.close()     // Catch:{ Exception -> 0x00ca }
            r2 = r0
        L_0x0030:
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            if (r0 == 0) goto L_0x0044
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListContact
            java.util.Iterator r3 = r0.iterator()
        L_0x003e:
            boolean r0 = r3.hasNext()
            if (r0 != 0) goto L_0x00d1
        L_0x0044:
            return r2
        L_0x0045:
            java.lang.String r0 = "display_name"
            int r0 = r8.getColumnIndex(r0)     // Catch:{ Exception -> 0x00fe, all -> 0x00f6 }
            java.lang.String r5 = r8.getString(r0)     // Catch:{ Exception -> 0x00fe, all -> 0x00f6 }
            boolean r0 = r5.contains(r11)     // Catch:{ Exception -> 0x00fe, all -> 0x00f6 }
            if (r0 != 0) goto L_0x005b
            boolean r0 = r11.contains(r5)     // Catch:{ Exception -> 0x00fe, all -> 0x00f6 }
            if (r0 == 0) goto L_0x001d
        L_0x005b:
            r0 = 1
            java.lang.String[] r2 = new java.lang.String[r0]     // Catch:{ Exception -> 0x00fe, all -> 0x00f6 }
            r0 = 0
            java.lang.String r1 = "data1"
            r2[r0] = r1     // Catch:{ Exception -> 0x00fe, all -> 0x00f6 }
            android.content.ContentResolver r0 = com.syu.app.App.mContentResolver     // Catch:{ Exception -> 0x00fe, all -> 0x00f6 }
            android.net.Uri r1 = android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI     // Catch:{ Exception -> 0x00fe, all -> 0x00f6 }
            java.lang.String r3 = "display_name = ? "
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ Exception -> 0x00fe, all -> 0x00f6 }
            r9 = 0
            r4[r9] = r5     // Catch:{ Exception -> 0x00fe, all -> 0x00f6 }
            java.lang.String r5 = "display_name COLLATE LOCALIZED ASC"
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x00fe, all -> 0x00f6 }
            if (r1 == 0) goto L_0x0108
            int r0 = r1.getCount()     // Catch:{ Exception -> 0x0102, all -> 0x00f9 }
            if (r0 <= 0) goto L_0x0108
            java.lang.String r0 = "data1"
            int r0 = r1.getColumnIndex(r0)     // Catch:{ Exception -> 0x0102, all -> 0x00f9 }
            boolean r2 = r1.moveToNext()     // Catch:{ Exception -> 0x0102, all -> 0x00f9 }
            if (r2 == 0) goto L_0x0108
            java.lang.String r6 = r1.getString(r0)     // Catch:{ Exception -> 0x0102, all -> 0x00f9 }
            r0 = r6
            goto L_0x0025
        L_0x008f:
            r0 = move-exception
            r1 = r7
            r2 = r7
        L_0x0092:
            r0.printStackTrace()     // Catch:{ all -> 0x00fb }
            if (r1 == 0) goto L_0x009a
            r1.close()     // Catch:{ Exception -> 0x00a1 }
        L_0x009a:
            if (r2 == 0) goto L_0x0105
            r2.close()     // Catch:{ Exception -> 0x00a6 }
            r2 = r6
            goto L_0x0030
        L_0x00a1:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x009a
        L_0x00a6:
            r0 = move-exception
            r0.printStackTrace()
            r2 = r6
            goto L_0x0030
        L_0x00ac:
            r0 = move-exception
            r1 = r7
            r8 = r7
        L_0x00af:
            if (r1 == 0) goto L_0x00b4
            r1.close()     // Catch:{ Exception -> 0x00ba }
        L_0x00b4:
            if (r8 == 0) goto L_0x00b9
            r8.close()     // Catch:{ Exception -> 0x00bf }
        L_0x00b9:
            throw r0
        L_0x00ba:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00b4
        L_0x00bf:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00b9
        L_0x00c4:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x002a
        L_0x00ca:
            r1 = move-exception
            r1.printStackTrace()
        L_0x00ce:
            r2 = r0
            goto L_0x0030
        L_0x00d1:
            java.lang.Object r0 = r3.next()
            android.util.SparseArray r0 = (android.util.SparseArray) r0
            r1 = 178(0xb2, float:2.5E-43)
            java.lang.Object r1 = r0.get(r1)
            java.lang.String r1 = (java.lang.String) r1
            boolean r4 = r1.contains(r11)
            if (r4 != 0) goto L_0x00eb
            boolean r1 = r11.contains(r1)
            if (r1 == 0) goto L_0x003e
        L_0x00eb:
            r1 = 180(0xb4, float:2.52E-43)
            java.lang.Object r0 = r0.get(r1)
            java.lang.String r0 = (java.lang.String) r0
            r2 = r0
            goto L_0x0044
        L_0x00f6:
            r0 = move-exception
            r1 = r7
            goto L_0x00af
        L_0x00f9:
            r0 = move-exception
            goto L_0x00af
        L_0x00fb:
            r0 = move-exception
            r8 = r2
            goto L_0x00af
        L_0x00fe:
            r0 = move-exception
            r1 = r7
            r2 = r8
            goto L_0x0092
        L_0x0102:
            r0 = move-exception
            r2 = r8
            goto L_0x0092
        L_0x0105:
            r2 = r6
            goto L_0x0030
        L_0x0108:
            r0 = r6
            goto L_0x0025
        L_0x010b:
            r1 = r7
            r0 = r6
            goto L_0x0025
        L_0x010f:
            r2 = r6
            goto L_0x0044
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.Bt_Info.queryNumberByName(java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:106:0x016e, code lost:
        r2 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x016f, code lost:
        r3 = r7;
        r4 = r8;
        r1 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x0174, code lost:
        r2 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x0175, code lost:
        r3 = r7;
        r4 = r8;
        r0 = r6;
        r1 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00ca, code lost:
        r2 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00cb, code lost:
        r3 = null;
        r4 = null;
        r0 = -1;
        r1 = com.syu.data.FinalChip.BSP_PLATFORM_Null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x00e9, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:?, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:?, code lost:
        r8.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x00f5, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x00f6, code lost:
        r1.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x00fa, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x00fb, code lost:
        r1.printStackTrace();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x0181  */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x0187  */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x00bb A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002e A[SYNTHETIC, Splitter:B:15:0x002e] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0033 A[SYNTHETIC, Splitter:B:18:0x0033] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x00e9 A[ExcHandler: all (th java.lang.Throwable), PHI: r7 
      PHI: (r7v2 android.database.Cursor) = (r7v0 android.database.Cursor), (r7v0 android.database.Cursor), (r7v0 android.database.Cursor), (r7v0 android.database.Cursor), (r7v5 android.database.Cursor), (r7v5 android.database.Cursor), (r7v5 android.database.Cursor), (r7v5 android.database.Cursor), (r7v5 android.database.Cursor), (r7v5 android.database.Cursor), (r7v5 android.database.Cursor) binds: [B:3:0x000f, B:4:?, B:6:0x001d, B:7:?, B:10:0x0025, B:11:?, B:27:0x0050, B:53:0x00c1, B:54:?, B:40:0x0087, B:41:?] A[DONT_GENERATE, DONT_INLINE], Splitter:B:3:0x000f] */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x00ec A[SYNTHETIC, Splitter:B:71:0x00ec] */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x00f1 A[SYNTHETIC, Splitter:B:74:0x00f1] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String queryNumberByPinYin(java.lang.String r13) {
        /*
            r12 = this;
            java.lang.String r6 = ""
            r9 = -1
            java.lang.String r11 = defpackage.bx.a(r13)
            boolean r0 = android.text.TextUtils.isEmpty(r11)
            if (r0 != 0) goto L_0x0190
            r8 = 0
            r7 = 0
            android.content.ContentResolver r0 = com.syu.app.App.mContentResolver     // Catch:{ Exception -> 0x00ca, all -> 0x00e9 }
            android.net.Uri r1 = android.provider.ContactsContract.Contacts.CONTENT_URI     // Catch:{ Exception -> 0x00ca, all -> 0x00e9 }
            r2 = 0
            r3 = 0
            r4 = 0
            r5 = 0
            android.database.Cursor r8 = r0.query(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x00ca, all -> 0x00e9 }
            if (r8 == 0) goto L_0x018c
            int r0 = r8.getCount()     // Catch:{ Exception -> 0x0167, all -> 0x00e9 }
            if (r0 <= 0) goto L_0x018c
            r0 = r9
            r10 = r6
        L_0x0025:
            boolean r1 = r8.moveToNext()     // Catch:{ Exception -> 0x016e, all -> 0x00e9 }
            if (r1 != 0) goto L_0x004e
            r1 = r10
        L_0x002c:
            if (r7 == 0) goto L_0x0031
            r7.close()     // Catch:{ Exception -> 0x00ff }
        L_0x0031:
            if (r8 == 0) goto L_0x0036
            r8.close()     // Catch:{ Exception -> 0x0105 }
        L_0x0036:
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 == 0) goto L_0x0181
            com.syu.bt.Bt_Info r2 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r2 = r2.mListContact
            java.util.Iterator r4 = r2.iterator()
            r2 = r0
            r3 = r1
        L_0x0046:
            boolean r0 = r4.hasNext()
            if (r0 != 0) goto L_0x010b
            r0 = r3
        L_0x004d:
            return r0
        L_0x004e:
            java.lang.String r1 = "display_name"
            int r1 = r8.getColumnIndex(r1)     // Catch:{ Exception -> 0x016e, all -> 0x00e9 }
            java.lang.String r5 = r8.getString(r1)     // Catch:{ Exception -> 0x016e, all -> 0x00e9 }
            boolean r1 = defpackage.bv.h()     // Catch:{ Exception -> 0x016e, all -> 0x00e9 }
            if (r1 == 0) goto L_0x00bf
            java.lang.String r1 = ""
            java.lang.String r1 = defpackage.b.a(r5, r1)     // Catch:{ Exception -> 0x016e, all -> 0x00e9 }
            java.lang.String r1 = defpackage.bx.a(r1)     // Catch:{ Exception -> 0x016e, all -> 0x00e9 }
        L_0x0068:
            boolean r2 = r1.contains(r11)     // Catch:{ Exception -> 0x016e, all -> 0x00e9 }
            if (r2 != 0) goto L_0x0074
            boolean r2 = r11.contains(r1)     // Catch:{ Exception -> 0x016e, all -> 0x00e9 }
            if (r2 == 0) goto L_0x0025
        L_0x0074:
            int r1 = r1.length()     // Catch:{ Exception -> 0x016e, all -> 0x00e9 }
            int r2 = r11.length()     // Catch:{ Exception -> 0x016e, all -> 0x00e9 }
            int r1 = r1 - r2
            int r6 = java.lang.Math.abs(r1)     // Catch:{ Exception -> 0x016e, all -> 0x00e9 }
            if (r6 < r0) goto L_0x0086
            r1 = -1
            if (r0 != r1) goto L_0x0025
        L_0x0086:
            r0 = 1
            java.lang.String[] r2 = new java.lang.String[r0]     // Catch:{ Exception -> 0x0174, all -> 0x00e9 }
            r0 = 0
            java.lang.String r1 = "data1"
            r2[r0] = r1     // Catch:{ Exception -> 0x0174, all -> 0x00e9 }
            android.content.ContentResolver r0 = com.syu.app.App.mContentResolver     // Catch:{ Exception -> 0x0174, all -> 0x00e9 }
            android.net.Uri r1 = android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI     // Catch:{ Exception -> 0x0174, all -> 0x00e9 }
            java.lang.String r3 = "display_name = ? "
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ Exception -> 0x0174, all -> 0x00e9 }
            r9 = 0
            r4[r9] = r5     // Catch:{ Exception -> 0x0174, all -> 0x00e9 }
            java.lang.String r5 = "display_name COLLATE LOCALIZED ASC"
            android.database.Cursor r3 = r0.query(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x0174, all -> 0x00e9 }
            if (r3 == 0) goto L_0x0184
            int r0 = r3.getCount()     // Catch:{ Exception -> 0x017b, all -> 0x0160 }
            if (r0 <= 0) goto L_0x0184
            java.lang.String r0 = "data1"
            int r0 = r3.getColumnIndex(r0)     // Catch:{ Exception -> 0x017b, all -> 0x0160 }
            boolean r1 = r3.moveToNext()     // Catch:{ Exception -> 0x017b, all -> 0x0160 }
            if (r1 == 0) goto L_0x0184
            java.lang.String r10 = r3.getString(r0)     // Catch:{ Exception -> 0x017b, all -> 0x0160 }
            r1 = r10
        L_0x00b9:
            if (r6 != 0) goto L_0x0187
            r7 = r3
            r0 = r6
            goto L_0x002c
        L_0x00bf:
            r1 = 0
            r2 = 0
            java.lang.String r1 = defpackage.bx.a((java.lang.String) r5, (boolean) r1, (boolean) r2)     // Catch:{ Exception -> 0x016e, all -> 0x00e9 }
            java.lang.String r1 = defpackage.bx.a(r1)     // Catch:{ Exception -> 0x016e, all -> 0x00e9 }
            goto L_0x0068
        L_0x00ca:
            r2 = move-exception
            r3 = r7
            r4 = r8
            r0 = r9
            r1 = r6
        L_0x00cf:
            r2.printStackTrace()     // Catch:{ all -> 0x0163 }
            if (r3 == 0) goto L_0x00d7
            r3.close()     // Catch:{ Exception -> 0x00e4 }
        L_0x00d7:
            if (r4 == 0) goto L_0x0036
            r4.close()     // Catch:{ Exception -> 0x00de }
            goto L_0x0036
        L_0x00de:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0036
        L_0x00e4:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x00d7
        L_0x00e9:
            r0 = move-exception
        L_0x00ea:
            if (r7 == 0) goto L_0x00ef
            r7.close()     // Catch:{ Exception -> 0x00f5 }
        L_0x00ef:
            if (r8 == 0) goto L_0x00f4
            r8.close()     // Catch:{ Exception -> 0x00fa }
        L_0x00f4:
            throw r0
        L_0x00f5:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00ef
        L_0x00fa:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00f4
        L_0x00ff:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0031
        L_0x0105:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0036
        L_0x010b:
            java.lang.Object r0 = r4.next()
            android.util.SparseArray r0 = (android.util.SparseArray) r0
            boolean r1 = defpackage.bv.h()
            if (r1 == 0) goto L_0x0151
            r1 = 178(0xb2, float:2.5E-43)
            java.lang.Object r1 = r0.get(r1)
            java.lang.String r1 = (java.lang.String) r1
            java.lang.String r5 = ""
            java.lang.String r1 = defpackage.b.a(r1, r5)
        L_0x0125:
            boolean r5 = r1.contains(r11)
            if (r5 != 0) goto L_0x0131
            boolean r5 = r11.contains(r1)
            if (r5 == 0) goto L_0x0046
        L_0x0131:
            int r1 = r1.length()
            int r5 = r11.length()
            int r1 = r1 - r5
            int r1 = java.lang.Math.abs(r1)
            if (r1 < r2) goto L_0x0143
            r5 = -1
            if (r2 != r5) goto L_0x0046
        L_0x0143:
            r2 = 180(0xb4, float:2.52E-43)
            java.lang.Object r0 = r0.get(r2)
            java.lang.String r0 = (java.lang.String) r0
            if (r1 == 0) goto L_0x004d
            r2 = r1
            r3 = r0
            goto L_0x0046
        L_0x0151:
            r1 = 178(0xb2, float:2.5E-43)
            java.lang.Object r1 = r0.get(r1)
            java.lang.String r1 = (java.lang.String) r1
            r5 = 0
            r6 = 0
            java.lang.String r1 = defpackage.bx.a((java.lang.String) r1, (boolean) r5, (boolean) r6)
            goto L_0x0125
        L_0x0160:
            r0 = move-exception
            r7 = r3
            goto L_0x00ea
        L_0x0163:
            r0 = move-exception
            r7 = r3
            r8 = r4
            goto L_0x00ea
        L_0x0167:
            r2 = move-exception
            r3 = r7
            r4 = r8
            r0 = r9
            r1 = r6
            goto L_0x00cf
        L_0x016e:
            r2 = move-exception
            r3 = r7
            r4 = r8
            r1 = r10
            goto L_0x00cf
        L_0x0174:
            r2 = move-exception
            r3 = r7
            r4 = r8
            r0 = r6
            r1 = r10
            goto L_0x00cf
        L_0x017b:
            r2 = move-exception
            r4 = r8
            r0 = r6
            r1 = r10
            goto L_0x00cf
        L_0x0181:
            r0 = r1
            goto L_0x004d
        L_0x0184:
            r1 = r10
            goto L_0x00b9
        L_0x0187:
            r7 = r3
            r0 = r6
            r10 = r1
            goto L_0x0025
        L_0x018c:
            r0 = r9
            r1 = r6
            goto L_0x002c
        L_0x0190:
            r0 = r6
            goto L_0x004d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.Bt_Info.queryNumberByPinYin(java.lang.String):java.lang.String");
    }

    public void saveDownloadContacts() {
        App.sContactsSaveFlag = true;
        if (this.mListContact != null && this.mListContact.size() != 0) {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(this.mListContact);
            if (App.bAutoSavePhoneBook) {
                App.startThread(App.StrThreadDBContact, new RunnableSaveDownloadContactsIntoFile(arrayList), false, 1);
            }
            App.startThread(App.StrThreadDBContact, new RunnableSaveDownloadContacts(arrayList), false, 1);
        }
    }

    public void savePhoneBook() {
        if (App.sContactsSaveFlag) {
            cb.a().a(App.getApp().getString("bt_contacts_saved"));
        } else if (this.mListContact == null || this.mListContact.size() <= 0) {
            cb.a().a(App.getApp().getString("bt_no_download_data"));
        } else {
            saveDownloadContacts();
        }
    }

    public void setOnRefreshListener(RefreshColllogListener refreshColllogListener) {
        this.refresh = refreshColllogListener;
    }

    public String simplifyNumber(String str) {
        String str2;
        if (str == null) {
            str2 = str;
        } else if (str.indexOf("+") == 0) {
            str2 = str.startsWith("+86") ? str.substring(3) : str.startsWith("+90") ? str.substring(3) : str.substring(1);
        } else {
            str2 = str.startsWith("0") ? str.substring(1) : str;
            if (str.startsWith("00")) {
                str2 = str.substring(2);
            }
            if (str.startsWith("0086")) {
                str2 = str.substring(4);
            }
            if (str.startsWith("0090")) {
                str2 = str.substring(4);
            }
        }
        return str2.replaceAll("\\D", FinalChip.BSP_PLATFORM_Null);
    }

    public void sortContact() {
        if (this.mListContact.size() > 0) {
            bIsTR = Locale.getDefault().getLanguage().equalsIgnoreCase("tr");
            if (App.mLocale.contains("zh") || App.mLocale.contains("en")) {
                Collections.sort(this.mListContact, comparator_Contact);
            } else {
                Collections.sort(this.mListContact, comparator2_Contact);
            }
        }
    }

    public void sortContact_Fav() {
        if (this.mListFavContact.size() > 0) {
            bIsTR = Locale.getDefault().getLanguage().equalsIgnoreCase("tr");
            if (App.mLocale.contains("zh") || App.mLocale.contains("en")) {
                Collections.sort(this.mListFavContact, comparator_Contact);
            } else {
                Collections.sort(this.mListFavContact, comparator2_Contact);
            }
        }
    }
}
