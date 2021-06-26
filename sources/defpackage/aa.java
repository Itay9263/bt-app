package defpackage;

import defpackage.z;
import java.util.Hashtable;

/* renamed from: aa  reason: default package */
class aa implements z.d {
    private final Hashtable a = new Hashtable();

    aa() {
    }

    public String a(String str) {
        String str2 = (String) this.a.get(str);
        if (str2 != null) {
            return str2;
        }
        this.a.put(str, str);
        return str;
    }
}
