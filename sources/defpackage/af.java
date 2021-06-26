package defpackage;

/* renamed from: af  reason: default package */
public abstract class af extends ai {
    private final String a;

    af(String str, String str2) {
        super(str);
        this.a = z.a(str2);
    }

    public String a() {
        return this.a;
    }

    /* access modifiers changed from: protected */
    public String a(String str) {
        return new StringBuffer().append("[").append(super.toString()).append(str).append("'").append(this.a).append("']").toString();
    }
}
