package defpackage;

import defpackage.z;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/* renamed from: l  reason: default package */
public class l extends q {
    static final Enumeration a = new o();
    private static final Integer b = new Integer(1);
    private n c;
    private String d;
    private z.a e;
    private Vector f;
    private final Hashtable g;

    /* renamed from: l$a */
    public interface a {
        void a(l lVar);
    }

    public l() {
        this.c = null;
        this.e = z.a();
        this.f = new Vector();
        this.g = null;
        this.d = "MEMORY";
    }

    l(String str) {
        this.c = null;
        this.e = z.a();
        this.f = new Vector();
        this.g = null;
        this.d = str;
    }

    /* access modifiers changed from: package-private */
    public ad a(bg bgVar, boolean z) throws bh {
        if (bgVar.b() == z) {
            return new ad(this, bgVar);
        }
        throw new bh(bgVar, new StringBuffer().append("\"").append(bgVar).append("\" evaluates to ").append(z ? "evaluates to element not string" : "evaluates to string not element").toString());
    }

    public n a() {
        return this.c;
    }

    /* access modifiers changed from: package-private */
    public void a(bg bgVar) throws bh {
    }

    public void a(Writer writer) throws IOException {
        this.c.a(writer);
    }

    public void a(String str) {
        this.d = str;
        b();
    }

    public void a(n nVar) {
        this.c = nVar;
        this.c.a(this);
        b();
    }

    public n b(String str) throws u {
        try {
            if (str.charAt(0) != '/') {
                str = new StringBuffer().append("/").append(str).toString();
            }
            bg a2 = bg.a(str);
            a(a2);
            return a(a2, false).a();
        } catch (bh e2) {
            throw new u("XPath problem", (Throwable) e2);
        }
    }

    /* access modifiers changed from: package-private */
    public void b() {
        Enumeration elements = this.f.elements();
        while (elements.hasMoreElements()) {
            ((a) elements.nextElement()).a(this);
        }
    }

    public void b(Writer writer) throws IOException {
        writer.write("<?xml version=\"1.0\" ?>\n");
        this.c.b(writer);
    }

    /* access modifiers changed from: protected */
    public int c() {
        return this.c.hashCode();
    }

    public Object clone() {
        l lVar = new l(this.d);
        lVar.c = (n) this.c.clone();
        return lVar;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof l)) {
            return false;
        }
        return this.c.equals(((l) obj).c);
    }

    public String toString() {
        return this.d;
    }
}
