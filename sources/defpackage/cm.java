package defpackage;

import java.io.BufferedInputStream;

/* renamed from: cm  reason: default package */
class cm {
    static Class a;

    cm() {
    }

    static BufferedInputStream a(String str) {
        Class cls;
        if (a == null) {
            cls = b("cm");
            a = cls;
        } else {
            cls = a;
        }
        return new BufferedInputStream(cls.getResourceAsStream(str));
    }

    static Class b(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }
}
