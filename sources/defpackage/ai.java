package defpackage;

/* renamed from: ai  reason: default package */
public abstract class ai extends ao {
    private final String a;

    ai(String str) {
        this.a = str;
    }

    public String b() {
        return this.a;
    }

    public String toString() {
        return new StringBuffer().append("@").append(this.a).toString();
    }
}
