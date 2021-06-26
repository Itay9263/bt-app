package defpackage;

/* renamed from: ay  reason: default package */
public abstract class ay extends ao {
    private final String a;

    ay(String str) {
        this.a = str;
    }

    public String a() {
        return this.a;
    }

    /* access modifiers changed from: protected */
    public String a(String str) {
        return new StringBuffer().append("[text()").append(str).append("'").append(this.a).append("']").toString();
    }
}
