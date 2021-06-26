package defpackage;

import com.syu.data.FinalChip;
import java.io.FileNotFoundException;
import java.io.IOException;

/* renamed from: cj  reason: default package */
class cj {
    private l a;

    /* renamed from: cj$1  reason: invalid class name */
    static class AnonymousClass1 {
    }

    /* renamed from: cj$a */
    private static class a {
        static final cj a = new cj((AnonymousClass1) null);
    }

    private cj() {
        c();
    }

    cj(AnonymousClass1 r1) {
        this();
    }

    private void a(l lVar) {
        this.a = lVar;
    }

    static cj b() {
        return a.a;
    }

    private void c() {
        try {
            a(y.a(FinalChip.BSP_PLATFORM_Null, cm.a("/pinyindb/pinyin_mapping.xml")));
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
