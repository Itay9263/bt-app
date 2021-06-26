package defpackage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/* renamed from: ce  reason: default package */
class ce {
    private Properties a;

    /* renamed from: ce$1  reason: invalid class name */
    static class AnonymousClass1 {
    }

    /* renamed from: ce$a */
    private static class a {
        static final ce a = new ce((AnonymousClass1) null);
    }

    private ce() {
        this.a = null;
        c();
    }

    ce(AnonymousClass1 r1) {
        this();
    }

    static ce a() {
        return a.a;
    }

    private void a(Properties properties) {
        this.a = properties;
    }

    private boolean a(String str) {
        return str != null && !str.equals("(none0)") && str.startsWith("(") && str.endsWith(")");
    }

    private String b(char c) {
        String property = b().getProperty(Integer.toHexString(c).toUpperCase());
        if (a(property)) {
            return property;
        }
        return null;
    }

    private Properties b() {
        return this.a;
    }

    private void c() {
        try {
            a(new Properties());
            b().load(cm.a("/pinyindb/unicode_to_hanyu_pinyin.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    /* access modifiers changed from: package-private */
    public String[] a(char c) {
        String b = b(c);
        if (b == null) {
            return null;
        }
        int indexOf = b.indexOf("(");
        return b.substring(indexOf + "(".length(), b.lastIndexOf(")")).split(",");
    }
}
