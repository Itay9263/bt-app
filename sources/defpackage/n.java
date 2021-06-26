package defpackage;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/* renamed from: n  reason: default package */
public class n extends q {
    private q a = null;
    private q b = null;
    private Hashtable c = null;
    private Vector d = null;
    private String e = null;

    n() {
    }

    public n(String str) {
        this.e = z.a(str);
    }

    private ad a(String str, boolean z) throws bh {
        bg a2 = bg.a(str);
        if (a2.b() == z) {
            return new ad(this, a2);
        }
        throw new bh(a2, new StringBuffer().append("\"").append(a2).append("\" evaluates to ").append(z ? "evaluates to element not string" : "evaluates to string not element").toString());
    }

    private boolean e(q qVar) {
        int i = 0;
        for (q qVar2 = this.a; qVar2 != null; qVar2 = qVar2.i()) {
            if (qVar2.equals(qVar)) {
                if (this.a == qVar2) {
                    this.a = qVar2.i();
                }
                if (this.b == qVar2) {
                    this.b = qVar2.h();
                }
                qVar2.j();
                qVar2.b((n) null);
                qVar2.a((l) null);
                return true;
            }
            i++;
        }
        return false;
    }

    public String a() {
        return this.e;
    }

    public n a(boolean z) {
        n nVar = new n(this.e);
        if (this.d != null) {
            Enumeration elements = this.d.elements();
            while (elements.hasMoreElements()) {
                String str = (String) elements.nextElement();
                nVar.a(str, (String) this.c.get(str));
            }
        }
        if (z) {
            for (q qVar = this.a; qVar != null; qVar = qVar.i()) {
                nVar.b((q) qVar.clone());
            }
        }
        return nVar;
    }

    /* access modifiers changed from: package-private */
    public void a(Writer writer) throws IOException {
        for (q qVar = this.a; qVar != null; qVar = qVar.i()) {
            qVar.a(writer);
        }
    }

    public void a(String str) {
        this.e = z.a(str);
        b();
    }

    public void a(String str, String str2) {
        if (this.c == null) {
            this.c = new Hashtable();
            this.d = new Vector();
        }
        if (this.c.get(str) == null) {
            this.d.addElement(str);
        }
        this.c.put(str, str2);
        b();
    }

    /* access modifiers changed from: package-private */
    public void a(q qVar) {
        n g = qVar.g();
        if (g != null) {
            g.e(qVar);
        }
        qVar.d(this.b);
        if (this.a == null) {
            this.a = qVar;
        }
        qVar.b(this);
        this.b = qVar;
        qVar.a(f());
    }

    public String b(String str) {
        if (this.c == null) {
            return null;
        }
        return (String) this.c.get(str);
    }

    public void b(Writer writer) throws IOException {
        writer.write(new StringBuffer().append("<").append(this.e).toString());
        if (this.d != null) {
            Enumeration elements = this.d.elements();
            while (elements.hasMoreElements()) {
                String str = (String) elements.nextElement();
                writer.write(new StringBuffer().append(" ").append(str).append("=\"").toString());
                q.a(writer, (String) this.c.get(str));
                writer.write("\"");
            }
        }
        if (this.a == null) {
            writer.write("/>");
            return;
        }
        writer.write(">");
        for (q qVar = this.a; qVar != null; qVar = qVar.i()) {
            qVar.b(writer);
        }
        writer.write(new StringBuffer().append("</").append(this.e).append(">").toString());
    }

    public void b(q qVar) {
        a(!c(qVar) ? (n) qVar.clone() : qVar);
        b();
    }

    /* access modifiers changed from: protected */
    public int c() {
        int i;
        int hashCode = this.e.hashCode();
        if (this.c != null) {
            Enumeration keys = this.c.keys();
            while (true) {
                i = hashCode;
                if (!keys.hasMoreElements()) {
                    break;
                }
                String str = (String) keys.nextElement();
                int hashCode2 = (i * 31) + str.hashCode();
                hashCode = ((String) this.c.get(str)).hashCode() + (hashCode2 * 31);
            }
        } else {
            i = hashCode;
        }
        for (q qVar = this.a; qVar != null; qVar = qVar.i()) {
            i = (i * 31) + qVar.hashCode();
        }
        return i;
    }

    public String c(String str) throws u {
        try {
            return a(str, true).b();
        } catch (bh e2) {
            throw new u("XPath problem", (Throwable) e2);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean c(q qVar) {
        if (qVar == this) {
            return false;
        }
        n g = g();
        if (g == null) {
            return true;
        }
        return g.c(qVar);
    }

    public Object clone() {
        return a(true);
    }

    public q d() {
        return this.a;
    }

    public q e() {
        return this.b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof n)) {
            return false;
        }
        n nVar = (n) obj;
        if (!this.e.equals(nVar.e)) {
            return false;
        }
        if ((this.c == null ? 0 : this.c.size()) != (nVar.c == null ? 0 : nVar.c.size())) {
            return false;
        }
        if (this.c != null) {
            Enumeration keys = this.c.keys();
            while (keys.hasMoreElements()) {
                String str = (String) keys.nextElement();
                if (!((String) this.c.get(str)).equals((String) nVar.c.get(str))) {
                    return false;
                }
            }
        }
        q qVar = this.a;
        q qVar2 = nVar.a;
        while (qVar != null) {
            if (!qVar.equals(qVar2)) {
                return false;
            }
            qVar = qVar.i();
            qVar2 = qVar2.i();
        }
        return true;
    }
}
