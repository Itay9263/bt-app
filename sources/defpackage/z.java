package defpackage;

import java.util.Hashtable;

/* renamed from: z  reason: default package */
public class z {
    private static d a = new aa();
    private static b b = new ab();

    /* renamed from: z$a */
    public interface a {
    }

    /* renamed from: z$b */
    public interface b {
        a a();
    }

    /* renamed from: z$c */
    private static class c extends Hashtable implements a {
        private c() {
        }

        c(aa aaVar) {
            this();
        }
    }

    /* renamed from: z$d */
    public interface d {
        String a(String str);
    }

    public static String a(String str) {
        return a.a(str);
    }

    static a a() {
        return b.a();
    }
}
