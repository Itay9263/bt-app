package defpackage;

import com.syu.data.FinalChip;
import java.io.FileNotFoundException;
import java.io.IOException;

/* renamed from: cf  reason: default package */
class cf {
    private l a;

    /* renamed from: cf$1  reason: invalid class name */
    static class AnonymousClass1 {
    }

    /* renamed from: cf$a */
    private static class a {
        static final cf a = new cf((AnonymousClass1) null);
    }

    private cf() {
        c();
    }

    cf(AnonymousClass1 r1) {
        this();
    }

    private void a(l lVar) {
        this.a = lVar;
    }

    static cf b() {
        return a.a;
    }

    private void c() {
        try {
            a(y.a(FinalChip.BSP_PLATFORM_Null, cm.a("/pinyindb/pinyin_gwoyeu_mapping.xml")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (u e3) {
            e3.printStackTrace();
        }
    }

    /* access modifiers changed from: package-private */
    public l a() {
        return this.a;
    }
}
