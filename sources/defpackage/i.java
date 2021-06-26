package defpackage;

/* renamed from: i  reason: default package */
class i implements m, v {
    private final w c;
    private n d;
    private final l e;
    private x f;

    public i() {
        this((w) null);
    }

    public i(w wVar) {
        this.d = null;
        this.e = new l();
        this.f = null;
        this.c = wVar == null ? x.a : wVar;
    }

    public l a() {
        return this.e;
    }

    public void a(n nVar) {
        if (this.d == null) {
            this.e.a(nVar);
        } else {
            this.d.b((q) nVar);
        }
        this.d = nVar;
    }

    public void a(x xVar) {
        this.f = xVar;
        this.e.a(xVar.toString());
    }

    public void a(char[] cArr, int i, int i2) {
        n nVar = this.d;
        if (nVar.e() instanceof ac) {
            ((ac) nVar.e()).a(cArr, i, i2);
        } else {
            nVar.a((q) new ac(new String(cArr, i, i2)));
        }
    }

    public void b() {
    }

    public void b(n nVar) {
        this.d = this.d.g();
    }

    public void c() {
    }

    public String toString() {
        if (this.f != null) {
            return new StringBuffer().append("BuildDoc: ").append(this.f.toString()).toString();
        }
        return null;
    }
}
