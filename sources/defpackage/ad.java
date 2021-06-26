package defpackage;

import java.util.Enumeration;
import java.util.Vector;

/* renamed from: ad  reason: default package */
class ad implements bf {
    private static final Boolean a = new Boolean(true);
    private static final Boolean b = new Boolean(false);
    private final r c;
    private Vector d;
    private Enumeration e;
    private Object f;
    private final a g;
    private q h;
    private boolean i;
    private bg j;

    /* renamed from: ad$1  reason: invalid class name */
    class AnonymousClass1 {
    }

    /* renamed from: ad$a */
    private static class a {
        private C0001a a;

        /* renamed from: ad$a$a  reason: collision with other inner class name */
        private static class C0001a {
            final Boolean a;
            final C0001a b;

            C0001a(Boolean bool, C0001a aVar) {
                this.a = bool;
                this.b = aVar;
            }
        }

        private a() {
            this.a = null;
        }

        a(AnonymousClass1 r1) {
            this();
        }

        /* access modifiers changed from: package-private */
        public Boolean a() {
            Boolean bool = this.a.a;
            this.a = this.a.b;
            return bool;
        }

        /* access modifiers changed from: package-private */
        public void a(Boolean bool) {
            this.a = new C0001a(bool, this.a);
        }
    }

    private ad(bg bgVar, q qVar) throws bh {
        this.c = new r();
        this.d = new Vector();
        this.e = null;
        this.f = null;
        this.g = new a((AnonymousClass1) null);
        this.j = bgVar;
        this.h = qVar;
        this.d = new Vector(1);
        this.d.addElement(this.h);
        Enumeration c2 = bgVar.c();
        while (c2.hasMoreElements()) {
            ax axVar = (ax) c2.nextElement();
            this.i = axVar.a();
            this.e = null;
            axVar.c().a(this);
            this.e = this.c.a();
            this.d.removeAllElements();
            ao d2 = axVar.d();
            while (this.e.hasMoreElements()) {
                this.f = this.e.nextElement();
                d2.a(this);
                if (this.g.a().booleanValue()) {
                    this.d.addElement(this.f);
                }
            }
        }
    }

    public ad(l lVar, bg bgVar) throws bh {
        this(bgVar, (q) lVar);
    }

    public ad(n nVar, bg bgVar) throws bh {
        this(bgVar, (q) nVar);
        if (bgVar.a()) {
            throw new bh(bgVar, "Cannot use element as context node for absolute xpath");
        }
    }

    private void a(l lVar) {
        n a2 = lVar.a();
        this.c.a(a2, 1);
        if (this.i) {
            a(a2);
        }
    }

    private void a(l lVar, String str) {
        n a2 = lVar.a();
        if (a2 != null) {
            if (a2.a() == str) {
                this.c.a(a2, 1);
            }
            if (this.i) {
                a(a2, str);
            }
        }
    }

    private void a(n nVar) {
        int i2 = 0;
        for (q d2 = nVar.d(); d2 != null; d2 = d2.i()) {
            if (d2 instanceof n) {
                int i3 = i2 + 1;
                this.c.a(d2, i3);
                if (this.i) {
                    a((n) d2);
                }
                i2 = i3;
            }
        }
    }

    private void a(n nVar, String str) {
        int i2 = 0;
        for (q d2 = nVar.d(); d2 != null; d2 = d2.i()) {
            if (d2 instanceof n) {
                n nVar2 = (n) d2;
                if (nVar2.a() == str) {
                    i2++;
                    this.c.a(nVar2, i2);
                }
                if (this.i) {
                    a(nVar2, str);
                }
            }
        }
    }

    public n a() {
        if (this.d.size() == 0) {
            return null;
        }
        return (n) this.d.elementAt(0);
    }

    public void a(ae aeVar) {
        Vector vector = this.d;
        this.c.b();
        Enumeration elements = vector.elements();
        while (elements.hasMoreElements()) {
            Object nextElement = elements.nextElement();
            if (nextElement instanceof n) {
                a((n) nextElement);
            } else if (nextElement instanceof l) {
                a((l) nextElement);
            }
        }
    }

    public void a(ag agVar) throws bh {
        if (!(this.f instanceof n)) {
            throw new bh(this.j, "Cannot test attribute of document");
        }
        this.g.a(agVar.a().equals(((n) this.f).b(agVar.b())) ? a : b);
    }

    public void a(ah ahVar) throws bh {
        if (!(this.f instanceof n)) {
            throw new bh(this.j, "Cannot test attribute of document");
        }
        String b2 = ((n) this.f).b(ahVar.b());
        this.g.a(b2 != null && b2.length() > 0 ? a : b);
    }

    public void a(aj ajVar) throws bh {
        if (!(this.f instanceof n)) {
            throw new bh(this.j, "Cannot test attribute of document");
        }
        this.g.a((((double) Long.parseLong(((n) this.f).b(ajVar.b()))) > ajVar.a() ? 1 : (((double) Long.parseLong(((n) this.f).b(ajVar.b()))) == ajVar.a() ? 0 : -1)) > 0 ? a : b);
    }

    public void a(ak akVar) throws bh {
        if (!(this.f instanceof n)) {
            throw new bh(this.j, "Cannot test attribute of document");
        }
        this.g.a((((double) Long.parseLong(((n) this.f).b(akVar.b()))) > akVar.a() ? 1 : (((double) Long.parseLong(((n) this.f).b(akVar.b()))) == akVar.a() ? 0 : -1)) < 0 ? a : b);
    }

    public void a(al alVar) throws bh {
        if (!(this.f instanceof n)) {
            throw new bh(this.j, "Cannot test attribute of document");
        }
        this.g.a(!alVar.a().equals(((n) this.f).b(alVar.b())) ? a : b);
    }

    public void a(an anVar) {
        String b2;
        Vector vector = this.d;
        this.c.b();
        Enumeration elements = vector.elements();
        while (elements.hasMoreElements()) {
            q qVar = (q) elements.nextElement();
            if ((qVar instanceof n) && (b2 = ((n) qVar).b(anVar.b())) != null) {
                this.c.a(b2);
            }
        }
    }

    public void a(aq aqVar) {
        String b2 = aqVar.b();
        Vector vector = this.d;
        int size = vector.size();
        this.c.b();
        for (int i2 = 0; i2 < size; i2++) {
            Object elementAt = vector.elementAt(i2);
            if (elementAt instanceof n) {
                a((n) elementAt, b2);
            } else if (elementAt instanceof l) {
                a((l) elementAt, b2);
            }
        }
    }

    public void a(au auVar) throws bh {
        this.c.b();
        n g2 = this.h.g();
        if (g2 == null) {
            throw new bh(this.j, "Illegal attempt to apply \"..\" to node with no parent.");
        }
        this.c.a(g2, 1);
    }

    public void a(av avVar) throws bh {
        if (!(this.f instanceof n)) {
            throw new bh(this.j, "Cannot test position of document");
        }
        this.g.a(this.c.a((q) (n) this.f) == avVar.a() ? a : b);
    }

    public void a(az azVar) throws bh {
        if (!(this.f instanceof n)) {
            throw new bh(this.j, "Cannot test attribute of document");
        }
        q d2 = ((n) this.f).d();
        while (d2 != null) {
            if (!(d2 instanceof ac) || !((ac) d2).a().equals(azVar.a())) {
                d2 = d2.i();
            } else {
                this.g.a(a);
                return;
            }
        }
        this.g.a(b);
    }

    public void a(ba baVar) throws bh {
        if (!(this.f instanceof n)) {
            throw new bh(this.j, "Cannot test attribute of document");
        }
        for (q d2 = ((n) this.f).d(); d2 != null; d2 = d2.i()) {
            if (d2 instanceof ac) {
                this.g.a(a);
                return;
            }
        }
        this.g.a(b);
    }

    public void a(bb bbVar) throws bh {
        if (!(this.f instanceof n)) {
            throw new bh(this.j, "Cannot test attribute of document");
        }
        q d2 = ((n) this.f).d();
        while (d2 != null) {
            if (!(d2 instanceof ac) || ((ac) d2).a().equals(bbVar.a())) {
                d2 = d2.i();
            } else {
                this.g.a(a);
                return;
            }
        }
        this.g.a(b);
    }

    public void a(bc bcVar) {
        Vector vector = this.d;
        this.c.b();
        Enumeration elements = vector.elements();
        while (elements.hasMoreElements()) {
            Object nextElement = elements.nextElement();
            if (nextElement instanceof n) {
                for (q d2 = ((n) nextElement).d(); d2 != null; d2 = d2.i()) {
                    if (d2 instanceof ac) {
                        this.c.a(((ac) d2).a());
                    }
                }
            }
        }
    }

    public void a(bd bdVar) {
        this.c.b();
        this.c.a(this.h, 1);
    }

    public void a(be beVar) {
        this.g.a(a);
    }

    public String b() {
        if (this.d.size() == 0) {
            return null;
        }
        return this.d.elementAt(0).toString();
    }
}
