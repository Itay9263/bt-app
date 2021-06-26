package defpackage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/* renamed from: q  reason: default package */
public abstract class q {
    private l a = null;
    private n b = null;
    private q c = null;
    private q d = null;
    private Object e = null;
    private int f = 0;

    protected static void a(Writer writer, String str) throws IOException {
        String str2;
        int length = str.length();
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str.charAt(i2);
            if (charAt < 128) {
                switch (charAt) {
                    case '\"':
                        str2 = "&quot;";
                        break;
                    case '&':
                        str2 = "&amp;";
                        break;
                    case '\'':
                        str2 = "&#39;";
                        break;
                    case '<':
                        str2 = "&lt;";
                        break;
                    case '>':
                        str2 = "&gt;";
                        break;
                    default:
                        str2 = null;
                        break;
                }
            } else {
                str2 = new StringBuffer().append("&#").append(charAt).append(";").toString();
            }
            if (str2 != null) {
                writer.write(str, i, i2 - i);
                writer.write(str2);
                i = i2 + 1;
            }
        }
        if (i < length) {
            writer.write(str, i, length - i);
        }
    }

    /* access modifiers changed from: package-private */
    public abstract void a(Writer writer) throws IOException;

    /* access modifiers changed from: package-private */
    public void a(l lVar) {
        this.a = lVar;
    }

    /* access modifiers changed from: package-private */
    public void b() {
        this.f = 0;
        if (this.a != null) {
            this.a.b();
        }
    }

    /* access modifiers changed from: package-private */
    public abstract void b(Writer writer) throws IOException;

    /* access modifiers changed from: package-private */
    public void b(n nVar) {
        this.b = nVar;
    }

    /* access modifiers changed from: protected */
    public abstract int c();

    public abstract Object clone();

    /* access modifiers changed from: package-private */
    public void d(q qVar) {
        this.c = qVar;
        if (qVar != null) {
            qVar.d = this;
        }
    }

    public l f() {
        return this.a;
    }

    public n g() {
        return this.b;
    }

    public q h() {
        return this.c;
    }

    public int hashCode() {
        if (this.f == 0) {
            this.f = c();
        }
        return this.f;
    }

    public q i() {
        return this.d;
    }

    /* access modifiers changed from: package-private */
    public void j() {
        if (this.c != null) {
            this.c.d = this.d;
        }
        if (this.d != null) {
            this.d.c = this.c;
        }
        this.d = null;
        this.c = null;
    }

    public String k() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);
        b((Writer) outputStreamWriter);
        outputStreamWriter.flush();
        return new String(byteArrayOutputStream.toByteArray());
    }

    public String toString() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);
            a((Writer) outputStreamWriter);
            outputStreamWriter.flush();
            return new String(byteArrayOutputStream.toByteArray());
        } catch (IOException e2) {
            return super.toString();
        }
    }
}
