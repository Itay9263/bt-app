package defpackage;

import java.io.IOException;

/* renamed from: ax  reason: default package */
public class ax {
    public static ax a = new ax(bd.a, be.a);
    private final as b;
    private final ao c;
    private final boolean d;

    ax(as asVar, ao aoVar) {
        this.b = asVar;
        this.c = aoVar;
        this.d = false;
    }

    ax(bg bgVar, boolean z, aw awVar) throws bh, IOException {
        this.d = z;
        switch (awVar.a) {
            case -3:
                if (awVar.c.equals("text")) {
                    if (awVar.a() == 40 && awVar.a() == 41) {
                        this.b = bc.a;
                        break;
                    } else {
                        throw new bh(bgVar, "after text", awVar, "()");
                    }
                } else {
                    this.b = new aq(awVar.c);
                    break;
                }
                break;
            case 42:
                this.b = ae.a;
                break;
            case 46:
                if (awVar.a() != 46) {
                    awVar.b();
                    this.b = bd.a;
                    break;
                } else {
                    this.b = au.a;
                    break;
                }
            case 64:
                if (awVar.a() == -3) {
                    this.b = new an(awVar.c);
                    break;
                } else {
                    throw new bh(bgVar, "after @ in node test", awVar, "name");
                }
            default:
                throw new bh(bgVar, "at begininning of step", awVar, "'.' or '*' or name");
        }
        if (awVar.a() == 91) {
            awVar.a();
            this.c = ar.a(bgVar, awVar);
            if (awVar.a != 93) {
                throw new bh(bgVar, "after predicate expression", awVar, "]");
            }
            awVar.a();
            return;
        }
        this.c = be.a;
    }

    public boolean a() {
        return this.d;
    }

    public boolean b() {
        return this.b.a();
    }

    public as c() {
        return this.b;
    }

    public ao d() {
        return this.c;
    }

    public String toString() {
        return new StringBuffer().append(this.b.toString()).append(this.c.toString()).toString();
    }
}
