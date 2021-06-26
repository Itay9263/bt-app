package defpackage;

import android.util.SparseArray;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* renamed from: bw  reason: default package */
public class bw {
    private static bw t;
    public int a = 1;
    public String b;
    public boolean c = true;
    public boolean d = false;
    public long e = 0;
    public long f = 0;
    public long g = 0;
    public long h = 0;
    public long i = 0;
    public long j = 0;
    public long k = 0;
    public long l = 0;
    public long m = 0;
    public long n = 0;
    public int o = 0;
    public int p = 0;
    public ArrayList<File> q = new ArrayList<>();
    public ArrayList<File> r = new ArrayList<>();
    public HashMap<String, String> s = new HashMap<>();

    public static bw a() {
        if (t == null) {
            t = new bw();
        }
        return t;
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0039 A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00c1 A[Catch:{ all -> 0x0103 }] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00d8  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x00ef A[SYNTHETIC, Splitter:B:74:0x00ef] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x00f4 A[Catch:{ Exception -> 0x00f8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x00fd A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int a(java.lang.String r9, java.lang.String r10, boolean r11, boolean r12) {
        /*
            r8 = this;
            if (r11 == 0) goto L_0x0004
            r8.b = r9
        L_0x0004:
            boolean r0 = r8.c
            if (r0 != 0) goto L_0x000a
            r0 = 0
        L_0x0009:
            return r0
        L_0x000a:
            java.io.File r0 = new java.io.File
            r0.<init>(r9)
            boolean r1 = r0.exists()
            if (r1 != 0) goto L_0x0017
            r0 = 2
            goto L_0x0009
        L_0x0017:
            java.io.File r5 = new java.io.File
            r5.<init>(r10)
            boolean r1 = r0.isDirectory()
            if (r1 == 0) goto L_0x006a
            boolean r1 = r5.exists()
            if (r1 != 0) goto L_0x002b
            r5.mkdirs()
        L_0x002b:
            java.io.File[] r1 = r0.listFiles()
            if (r1 == 0) goto L_0x0035
            r0 = 0
        L_0x0032:
            int r2 = r1.length
            if (r0 < r2) goto L_0x003b
        L_0x0035:
            boolean r0 = r8.c
            if (r0 != 0) goto L_0x00fd
            r0 = 0
            goto L_0x0009
        L_0x003b:
            r2 = r1[r0]
            java.lang.String r2 = r2.getPath()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = java.lang.String.valueOf(r10)
            r3.<init>(r4)
            java.lang.String r4 = "/"
            java.lang.StringBuilder r3 = r3.append(r4)
            r4 = r1[r0]
            java.lang.String r4 = r4.getName()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            r4 = 1
            int r2 = r8.a(r2, r3, r4, r12)
            if (r2 != 0) goto L_0x0067
            r0 = 0
            goto L_0x0009
        L_0x0067:
            int r0 = r0 + 1
            goto L_0x0032
        L_0x006a:
            java.lang.String r1 = "/"
            int r1 = r10.lastIndexOf(r1)
            if (r1 <= 0) goto L_0x0085
            r2 = 0
            java.lang.String r1 = r10.substring(r2, r1)
            java.io.File r2 = new java.io.File
            r2.<init>(r1)
            boolean r1 = r2.exists()
            if (r1 != 0) goto L_0x0085
            r2.mkdirs()
        L_0x0085:
            r4 = 0
            r2 = 0
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0105, all -> 0x00ea }
            r3.<init>(r0)     // Catch:{ Exception -> 0x0105, all -> 0x00ea }
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0109, all -> 0x0100 }
            r1.<init>(r5)     // Catch:{ Exception -> 0x0109, all -> 0x0100 }
            r0 = 32768(0x8000, float:4.5918E-41)
            byte[] r0 = new byte[r0]     // Catch:{ Exception -> 0x00bb }
        L_0x0096:
            int r2 = r3.read(r0)     // Catch:{ Exception -> 0x00bb }
            if (r2 > 0) goto L_0x00ac
        L_0x009c:
            if (r3 == 0) goto L_0x00a1
            r3.close()     // Catch:{ Exception -> 0x00a7 }
        L_0x00a1:
            if (r1 == 0) goto L_0x0035
            r1.close()     // Catch:{ Exception -> 0x00a7 }
            goto L_0x0035
        L_0x00a7:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0035
        L_0x00ac:
            boolean r4 = r8.c     // Catch:{ Exception -> 0x00bb }
            if (r4 == 0) goto L_0x009c
            r4 = 0
            r1.write(r0, r4, r2)     // Catch:{ Exception -> 0x00bb }
            long r4 = r8.e     // Catch:{ Exception -> 0x00bb }
            long r6 = (long) r2     // Catch:{ Exception -> 0x00bb }
            long r4 = r4 + r6
            r8.e = r4     // Catch:{ Exception -> 0x00bb }
            goto L_0x0096
        L_0x00bb:
            r0 = move-exception
        L_0x00bc:
            r0.printStackTrace()     // Catch:{ all -> 0x0103 }
            if (r12 == 0) goto L_0x00d8
            java.util.HashMap<java.lang.String, java.lang.String> r0 = r8.s     // Catch:{ all -> 0x0103 }
            r0.put(r9, r10)     // Catch:{ all -> 0x0103 }
            if (r3 == 0) goto L_0x00cb
            r3.close()     // Catch:{ Exception -> 0x00d3 }
        L_0x00cb:
            if (r1 == 0) goto L_0x00d0
            r1.close()     // Catch:{ Exception -> 0x00d3 }
        L_0x00d0:
            r0 = 2
            goto L_0x0009
        L_0x00d3:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x00d0
        L_0x00d8:
            if (r3 == 0) goto L_0x00dd
            r3.close()     // Catch:{ Exception -> 0x00e4 }
        L_0x00dd:
            if (r1 == 0) goto L_0x0035
            r1.close()     // Catch:{ Exception -> 0x00e4 }
            goto L_0x0035
        L_0x00e4:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0035
        L_0x00ea:
            r0 = move-exception
            r1 = r2
            r3 = r4
        L_0x00ed:
            if (r3 == 0) goto L_0x00f2
            r3.close()     // Catch:{ Exception -> 0x00f8 }
        L_0x00f2:
            if (r1 == 0) goto L_0x00f7
            r1.close()     // Catch:{ Exception -> 0x00f8 }
        L_0x00f7:
            throw r0
        L_0x00f8:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00f7
        L_0x00fd:
            r0 = 1
            goto L_0x0009
        L_0x0100:
            r0 = move-exception
            r1 = r2
            goto L_0x00ed
        L_0x0103:
            r0 = move-exception
            goto L_0x00ed
        L_0x0105:
            r0 = move-exception
            r1 = r2
            r3 = r4
            goto L_0x00bc
        L_0x0109:
            r0 = move-exception
            r1 = r2
            goto L_0x00bc
        */
        throw new UnsupportedOperationException("Method not decompiled: defpackage.bw.a(java.lang.String, java.lang.String, boolean, boolean):int");
    }

    public void a(String str, List<SparseArray<String>> list) {
        File[] listFiles;
        if (this.c) {
            list.clear();
            File file = new File(str);
            if (file.isDirectory() && (listFiles = file.listFiles()) != null && listFiles.length > 0) {
                for (File path : listFiles) {
                    SparseArray sparseArray = new SparseArray();
                    String path2 = path.getPath();
                    int lastIndexOf = path2.lastIndexOf("/");
                    if (lastIndexOf >= 0) {
                        sparseArray.put(326, path2.substring(lastIndexOf + 1));
                    }
                    sparseArray.put(327, path2);
                    list.add(sparseArray);
                }
            }
        }
    }
}
