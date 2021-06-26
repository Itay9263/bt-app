package defpackage;

/* renamed from: ci  reason: default package */
public class ci {
    public static String[] a(char c) {
        return g(c);
    }

    private static String[] a(char c, cl clVar) {
        String[] g = g(c);
        if (g == null) {
            return null;
        }
        String[] strArr = new String[g.length];
        for (int i = 0; i < g.length; i++) {
            strArr[i] = ck.a(g[i], cl.a, clVar);
        }
        return strArr;
    }

    public static String[] a(char c, cp cpVar) throws cs {
        return b(c, cpVar);
    }

    public static String[] b(char c) {
        return a(c, cl.e);
    }

    private static String[] b(char c, cp cpVar) throws cs {
        String[] g = g(c);
        if (g == null) {
            return null;
        }
        for (int i = 0; i < g.length; i++) {
            g[i] = ch.a(g[i], cpVar);
        }
        return g;
    }

    public static String[] c(char c) {
        return a(c, cl.b);
    }

    public static String[] d(char c) {
        return a(c, cl.c);
    }

    public static String[] e(char c) {
        return a(c, cl.d);
    }

    public static String[] f(char c) {
        return h(c);
    }

    private static String[] g(char c) {
        return ce.a().a(c);
    }

    private static String[] h(char c) {
        String[] g = g(c);
        if (g == null) {
            return null;
        }
        String[] strArr = new String[g.length];
        for (int i = 0; i < g.length; i++) {
            strArr[i] = cg.a(g[i]);
        }
        return strArr;
    }
}
