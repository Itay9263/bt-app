package defpackage;

/* renamed from: am  reason: default package */
public abstract class am extends ai {
    private final int a;

    am(String str, int i) {
        super(str);
        this.a = i;
    }

    public double a() {
        return (double) this.a;
    }

    /* access modifiers changed from: protected */
    public String a(String str) {
        return new StringBuffer().append("[").append(super.toString()).append(str).append("'").append(this.a).append("']").toString();
    }
}
