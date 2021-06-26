package defpackage;

/* renamed from: k  reason: default package */
class k implements w {
    k() {
    }

    public void a(String str, String str2, int i) {
        System.err.println(new StringBuffer().append(str2).append("(").append(i).append("): ").append(str).append(" (ERROR)").toString());
    }

    public void b(String str, String str2, int i) {
        System.out.println(new StringBuffer().append(str2).append("(").append(i).append("): ").append(str).append(" (WARNING)").toString());
    }

    public void c(String str, String str2, int i) {
        System.out.println(new StringBuffer().append(str2).append("(").append(i).append("): ").append(str).append(" (NOTE)").toString());
    }
}
