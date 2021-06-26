package defpackage;

import java.io.IOException;
import java.io.Writer;

/* renamed from: ac  reason: default package */
public class ac extends q {
    private StringBuffer a;

    public ac(String str) {
        this.a = new StringBuffer(str);
    }

    public String a() {
        return this.a.toString();
    }

    /* access modifiers changed from: package-private */
    public void a(Writer writer) throws IOException {
        writer.write(this.a.toString());
    }

    public void a(char[] cArr, int i, int i2) {
        this.a.append(cArr, i, i2);
        b();
    }

    /* access modifiers changed from: package-private */
    public void b(Writer writer) throws IOException {
        String stringBuffer = this.a.toString();
        if (stringBuffer.length() < 50) {
            q.a(writer, stringBuffer);
            return;
        }
        writer.write("<![CDATA[");
        writer.write(stringBuffer);
        writer.write("]]>");
    }

    /* access modifiers changed from: protected */
    public int c() {
        return this.a.toString().hashCode();
    }

    public Object clone() {
        return new ac(this.a.toString());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ac)) {
            return false;
        }
        return this.a.toString().equals(((ac) obj).a.toString());
    }
}
