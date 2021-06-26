package defpackage;

import com.syu.data.FinalChip;
import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;

/* renamed from: t  reason: default package */
class t implements x {
    private static final char[] c = {'.', '-', '_', ':'};
    private static final boolean[] d = new boolean[128];
    private static final char[] e = "<!--".toCharArray();
    private static final char[] f = "-->".toCharArray();
    private static final char[] g = "<?".toCharArray();
    private static final char[] h = "?>".toCharArray();
    private static final char[] i = "<!DOCTYPE".toCharArray();
    private static final char[] j = "<?xml".toCharArray();
    private static final char[] k = "encoding".toCharArray();
    private static final char[] l = "version".toCharArray();
    private static final char[] m = {'_', '.', ':', '-'};
    private static final char[] n = "<!".toCharArray();
    private static final char[] o = "&#".toCharArray();
    private static final char[] p = "<!ENTITY".toCharArray();
    private static final char[] q = "NDATA".toCharArray();
    private static final char[] r = "SYSTEM".toCharArray();
    private static final char[] s = "PUBLIC".toCharArray();
    private static final char[] t = "<![CDATA[".toCharArray();
    private static final char[] u = "]]>".toCharArray();
    private static final char[] v = "/>".toCharArray();
    private static final char[] w = "</".toCharArray();
    private final Hashtable A;
    private final Hashtable B;
    private final w C;
    private final String D;
    private int E;
    private boolean F;
    private final int G;
    private final char[] H;
    private int I;
    private int J;
    private boolean K;
    private final char[] L;
    private int M;
    private final j N;
    private final v O;
    private String x;
    private String y;
    private final Reader z;

    static {
        for (char c2 = 0; c2 < 128; c2 = (char) (c2 + 1)) {
            d[c2] = d(c2);
        }
    }

    public t(String str, Reader reader, w wVar, String str2, v vVar) throws u, p, IOException {
        this(str, reader, (char[]) null, wVar, str2, vVar);
    }

    public t(String str, Reader reader, char[] cArr, w wVar, String str2, v vVar) throws u, p, IOException {
        this.y = null;
        this.A = new Hashtable();
        this.B = new Hashtable();
        this.E = -2;
        this.F = false;
        this.G = 1024;
        this.I = 0;
        this.J = 0;
        this.K = false;
        this.L = new char[255];
        this.M = -1;
        this.M = 1;
        this.N = null;
        this.C = wVar == null ? x.a : wVar;
        this.D = str2 == null ? null : str2.toLowerCase();
        this.A.put("lt", "<");
        this.A.put("gt", ">");
        this.A.put("amp", "&");
        this.A.put("apos", "'");
        this.A.put("quot", "\"");
        if (cArr != null) {
            this.H = cArr;
            this.I = 0;
            this.J = this.H.length;
            this.K = true;
            this.z = null;
        } else {
            this.z = reader;
            this.H = new char[1024];
            f();
        }
        this.x = str;
        this.O = vVar;
        this.O.a((x) this);
        x();
        this.O.b();
        n Z = Z();
        if (this.y != null && !this.y.equals(Z.a())) {
            this.C.b(new StringBuffer().append("DOCTYPE name \"").append(this.y).append("\" not same as tag name, \"").append(Z.a()).append("\" of root element").toString(), this.x, b());
        }
        while (r()) {
            s();
        }
        if (this.z != null) {
            this.z.close();
        }
        this.O.c();
    }

    private boolean A() throws u, IOException {
        return b(j);
    }

    private boolean B() throws u, IOException {
        return b(k);
    }

    private String C() throws u, IOException {
        a(k);
        E();
        char a = a('\'', '\"');
        StringBuffer stringBuffer = new StringBuffer();
        while (!b(a)) {
            stringBuffer.append(g());
        }
        a(a);
        return stringBuffer.toString();
    }

    private void D() throws u, IOException {
        i();
        a(l);
        E();
        char a = a('\'', '\"');
        G();
        a(a);
    }

    private final void E() throws u, IOException {
        if (j()) {
            i();
        }
        a('=');
        if (j()) {
            i();
        }
    }

    private boolean F() throws u, IOException {
        char h2 = h();
        return Character.isDigit(h2) || ('a' <= h2 && h2 <= 'z') || (('Z' <= h2 && h2 <= 'Z') || a(h2, m));
    }

    private void G() throws u, IOException {
        g();
        while (F()) {
            g();
        }
    }

    private void H() throws u, IOException {
        a(i);
        i();
        this.y = l();
        if (j()) {
            i();
            if (!b('>') && !b('[')) {
                this.F = true;
                T();
                if (j()) {
                    i();
                }
            }
        }
        if (b('[')) {
            g();
            while (!b(']')) {
                if (J()) {
                    I();
                } else {
                    K();
                }
            }
            a(']');
            if (j()) {
                i();
            }
        }
        a('>');
    }

    private void I() throws u, IOException {
        if (Q()) {
            P();
        } else {
            i();
        }
    }

    private boolean J() throws u, IOException {
        return Q() || j();
    }

    private void K() throws u, IOException {
        if (w()) {
            v();
        } else if (u()) {
            t();
        } else if (S()) {
            R();
        } else if (b(n)) {
            while (!b('>')) {
                if (b('\'', '\"')) {
                    char g2 = g();
                    while (!b(g2)) {
                        g();
                    }
                    a(g2);
                } else {
                    g();
                }
            }
            a('>');
        } else {
            throw new u(this, "expecting processing instruction, comment, or \"<!\"");
        }
    }

    private char L() throws u, IOException {
        a(o);
        int i2 = 10;
        if (b('x')) {
            g();
            i2 = 16;
        }
        int i3 = 0;
        while (!b(';')) {
            int i4 = i3 + 1;
            this.L[i3] = g();
            if (i4 >= 255) {
                this.C.b("Tmp buffer overflow on readCharRef", this.x, b());
                return ' ';
            }
            i3 = i4;
        }
        a(';');
        String str = new String(this.L, 0, i3);
        try {
            return (char) Integer.parseInt(str, i2);
        } catch (NumberFormatException e2) {
            this.C.b(new StringBuffer().append("\"").append(str).append("\" is not a valid ").append(i2 == 16 ? "hexadecimal" : "decimal").append(" number").toString(), this.x, b());
            return ' ';
        }
    }

    private final char[] M() throws u, IOException {
        if (!b(o)) {
            return O().toCharArray();
        }
        return new char[]{L()};
    }

    private final boolean N() throws u, IOException {
        return b('&');
    }

    private String O() throws u, IOException {
        a('&');
        String l2 = l();
        String str = (String) this.A.get(l2);
        if (str == null) {
            str = FinalChip.BSP_PLATFORM_Null;
            if (this.F) {
                this.C.b(new StringBuffer().append("&").append(l2).append("; not found -- possibly defined in external DTD)").toString(), this.x, b());
            } else {
                this.C.b(new StringBuffer().append("No declaration of &").append(l2).append(";").toString(), this.x, b());
            }
        }
        a(';');
        return str;
    }

    private String P() throws u, IOException {
        a('%');
        String l2 = l();
        String str = (String) this.B.get(l2);
        if (str == null) {
            str = FinalChip.BSP_PLATFORM_Null;
            this.C.b(new StringBuffer().append("No declaration of %").append(l2).append(";").toString(), this.x, b());
        }
        a(';');
        return str;
    }

    private boolean Q() throws u, IOException {
        return b('%');
    }

    private void R() throws u, IOException {
        String T;
        a(p);
        i();
        if (b('%')) {
            a('%');
            i();
            String l2 = l();
            i();
            this.B.put(l2, o() ? n() : T());
        } else {
            String l3 = l();
            i();
            if (o()) {
                T = n();
            } else if (U()) {
                T = T();
                if (j()) {
                    i();
                }
                if (b(q)) {
                    a(q);
                    i();
                    l();
                }
            } else {
                throw new u(this, "expecting double-quote, \"PUBLIC\" or \"SYSTEM\" while reading entity declaration");
            }
            this.A.put(l3, T);
        }
        if (j()) {
            i();
        }
        a('>');
    }

    private boolean S() throws u, IOException {
        return b(p);
    }

    private String T() throws u, IOException {
        if (b(r)) {
            a(r);
        } else if (b(s)) {
            a(s);
            i();
            q();
        } else {
            throw new u(this, "expecting \"SYSTEM\" or \"PUBLIC\" while reading external ID");
        }
        i();
        p();
        return "(WARNING: external ID not read)";
    }

    private boolean U() throws u, IOException {
        return b(r) || b(s);
    }

    private String V() throws u, IOException {
        char a = a('\'', '\"');
        StringBuffer stringBuffer = new StringBuffer();
        while (!b(a)) {
            if (N()) {
                stringBuffer.append(M());
            } else {
                stringBuffer.append(g());
            }
        }
        a(a);
        return stringBuffer.toString();
    }

    private void W() throws u, IOException {
        int i2 = 0;
        while (!b('<') && !b('&') && !b(u)) {
            this.L[i2] = g();
            if (this.L[i2] == 13 && h() == 10) {
                this.L[i2] = g();
            }
            i2++;
            if (i2 == 255) {
                this.O.a(this.L, 0, 255);
                i2 = 0;
            }
        }
        if (i2 > 0) {
            this.O.a(this.L, 0, i2);
        }
    }

    private void X() throws u, IOException {
        StringBuffer stringBuffer = null;
        a(t);
        int i2 = 0;
        while (!b(u)) {
            if (i2 >= 255) {
                if (stringBuffer == null) {
                    stringBuffer = new StringBuffer(i2);
                    stringBuffer.append(this.L, 0, i2);
                } else {
                    stringBuffer.append(this.L, 0, i2);
                }
                i2 = 0;
            }
            this.L[i2] = g();
            i2++;
        }
        a(u);
        if (stringBuffer != null) {
            stringBuffer.append(this.L, 0, i2);
            char[] charArray = stringBuffer.toString().toCharArray();
            this.O.a(charArray, 0, charArray.length);
            return;
        }
        this.O.a(this.L, 0, i2);
    }

    private boolean Y() throws u, IOException {
        return b(t);
    }

    private final n Z() throws u, IOException {
        n nVar = new n();
        boolean a = a(nVar);
        this.O.a(nVar);
        if (a) {
            ab();
            c(nVar);
        }
        this.O.b(nVar);
        return nVar;
    }

    private final char a(char c2, char c3) throws u, IOException {
        char g2 = g();
        if (g2 == c2 || g2 == c3) {
            return g2;
        }
        throw new u(this, g2, new char[]{c2, c3});
    }

    private final char a(char c2, char c3, char c4, char c5) throws u, IOException {
        char g2 = g();
        if (g2 == c2 || g2 == c3 || g2 == c4 || g2 == c5) {
            return g2;
        }
        throw new u(this, g2, new char[]{c2, c3, c4, c5});
    }

    private int a(int i2) throws IOException {
        int i3 = 0;
        if (this.K) {
            return -1;
        }
        if (this.H.length - this.I < i2) {
            for (int i4 = 0; this.I + i4 < this.J; i4++) {
                this.H[i4] = this.H[this.I + i4];
            }
            int i5 = this.J - this.I;
            this.J = i5;
            this.I = 0;
            i3 = i5;
        }
        int f2 = f();
        if (f2 != -1) {
            return i3 + f2;
        }
        if (i3 == 0) {
            return -1;
        }
        return i3;
    }

    private final void a(char c2) throws u, IOException {
        char g2 = g();
        if (g2 != c2) {
            throw new u(this, g2, c2);
        }
    }

    private final void a(char[] cArr) throws u, IOException {
        int length = cArr.length;
        if (this.J - this.I >= length || a(length) > 0) {
            this.E = this.H[this.J - 1];
            if (this.J - this.I < length) {
                throw new u(this, "end of XML file", cArr);
            }
            for (int i2 = 0; i2 < length; i2++) {
                if (this.H[this.I + i2] != cArr[i2]) {
                    throw new u(this, new String(this.H, this.I, length), cArr);
                }
            }
            this.I += length;
            return;
        }
        this.E = -1;
        throw new u(this, "end of XML file", cArr);
    }

    private static final boolean a(char c2, char[] cArr) {
        for (char c3 : cArr) {
            if (c2 == c3) {
                return true;
            }
        }
        return false;
    }

    private boolean a(n nVar) throws u, IOException {
        a('<');
        nVar.a(l());
        while (j()) {
            i();
            if (!b('/', '>')) {
                b(nVar);
            }
        }
        if (j()) {
            i();
        }
        boolean b = b('>');
        if (b) {
            a('>');
        } else {
            a(v);
        }
        return b;
    }

    private boolean aa() throws u, IOException {
        return b(w);
    }

    private void ab() throws u, IOException {
        W();
        boolean z2 = true;
        while (z2) {
            if (aa()) {
                z2 = false;
            } else if (N()) {
                char[] M2 = M();
                this.O.a(M2, 0, M2.length);
            } else if (Y()) {
                X();
            } else if (w()) {
                v();
            } else if (u()) {
                t();
            } else if (b('<')) {
                Z();
            } else {
                z2 = false;
            }
            W();
        }
    }

    private void b(n nVar) throws u, IOException {
        String l2 = l();
        E();
        String V = V();
        if (nVar.b(l2) != null) {
            this.C.b(new StringBuffer().append("Element ").append(this).append(" contains attribute ").append(l2).append("more than once").toString(), this.x, b());
        }
        nVar.a(l2, V);
    }

    private final boolean b(char c2) throws u, IOException {
        if (this.I < this.J || f() != -1) {
            return this.H[this.I] == c2;
        }
        throw new u(this, "unexpected end of expression.");
    }

    private final boolean b(char c2, char c3) throws u, IOException {
        if (this.I >= this.J && f() == -1) {
            return false;
        }
        char c4 = this.H[this.I];
        return c4 == c2 || c4 == c3;
    }

    private final boolean b(char c2, char c3, char c4, char c5) throws u, IOException {
        if (this.I >= this.J && f() == -1) {
            return false;
        }
        char c6 = this.H[this.I];
        return c6 == c2 || c6 == c3 || c6 == c4 || c6 == c5;
    }

    private final boolean b(char[] cArr) throws u, IOException {
        int length = cArr.length;
        if (this.J - this.I >= length || a(length) > 0) {
            this.E = this.H[this.J - 1];
            if (this.J - this.I < length) {
                return false;
            }
            for (int i2 = 0; i2 < length; i2++) {
                if (this.H[this.I + i2] != cArr[i2]) {
                    return false;
                }
            }
            return true;
        }
        this.E = -1;
        return false;
    }

    private void c(n nVar) throws u, IOException {
        a(w);
        String l2 = l();
        if (!l2.equals(nVar.a())) {
            this.C.b(new StringBuffer().append("end tag (").append(l2).append(") does not match begin tag (").append(nVar.a()).append(")").toString(), this.x, b());
        }
        if (j()) {
            i();
        }
        a('>');
    }

    private static boolean c(char c2) {
        return "abcdefghijklmnopqrstuvwxyz".indexOf(Character.toLowerCase(c2)) != -1;
    }

    private static boolean d(char c2) {
        return Character.isDigit(c2) || c(c2) || a(c2, c) || e(c2);
    }

    private static boolean e(char c2) {
        switch (c2) {
            case 183:
            case 720:
            case 721:
            case 903:
            case 1600:
            case 3654:
            case 3782:
            case 12293:
            case 12337:
            case 12338:
            case 12339:
            case 12340:
            case 12341:
            case 12445:
            case 12446:
            case 12540:
            case 12541:
            case 12542:
                return true;
            default:
                return false;
        }
    }

    private int f() throws IOException {
        if (this.K) {
            return -1;
        }
        if (this.J == this.H.length) {
            this.J = 0;
            this.I = 0;
        }
        int read = this.z.read(this.H, this.J, this.H.length - this.J);
        if (read <= 0) {
            this.K = true;
            return -1;
        }
        this.J += read;
        return read;
    }

    private final char g() throws u, IOException {
        if (this.I < this.J || f() != -1) {
            if (this.H[this.I] == 10) {
                this.M++;
            }
            char[] cArr = this.H;
            int i2 = this.I;
            this.I = i2 + 1;
            return cArr[i2];
        }
        throw new u(this, "unexpected end of expression.");
    }

    private final char h() throws u, IOException {
        if (this.I < this.J || f() != -1) {
            return this.H[this.I];
        }
        throw new u(this, "unexpected end of expression.");
    }

    private final void i() throws u, IOException {
        a(' ', 9, 13, 10);
        while (b(' ', 9, 13, 10)) {
            g();
        }
    }

    private final boolean j() throws u, IOException {
        return b(' ', 9, 13, 10);
    }

    private boolean k() throws u, IOException {
        char h2 = h();
        return h2 < 128 ? d[h2] : d(h2);
    }

    private final String l() throws u, IOException {
        StringBuffer stringBuffer = null;
        int i2 = 1;
        this.L[0] = m();
        while (k()) {
            if (i2 >= 255) {
                if (stringBuffer == null) {
                    stringBuffer = new StringBuffer(i2);
                    stringBuffer.append(this.L, 0, i2);
                } else {
                    stringBuffer.append(this.L, 0, i2);
                }
                i2 = 0;
            }
            this.L[i2] = g();
            i2++;
        }
        if (stringBuffer == null) {
            return z.a(new String(this.L, 0, i2));
        }
        stringBuffer.append(this.L, 0, i2);
        return stringBuffer.toString();
    }

    private char m() throws u, IOException {
        char g2 = g();
        if (c(g2) || g2 == '_' || g2 == ':') {
            return g2;
        }
        throw new u(this, g2, "letter, underscore, colon");
    }

    private final String n() throws u, IOException {
        char a = a('\'', '\"');
        StringBuffer stringBuffer = new StringBuffer();
        while (!b(a)) {
            if (Q()) {
                stringBuffer.append(P());
            } else if (N()) {
                stringBuffer.append(M());
            } else {
                stringBuffer.append(g());
            }
        }
        a(a);
        return stringBuffer.toString();
    }

    private final boolean o() throws u, IOException {
        return b('\'', '\"');
    }

    private final void p() throws u, IOException {
        char g2 = g();
        while (h() != g2) {
            g();
        }
        a(g2);
    }

    private final void q() throws u, IOException {
        p();
    }

    private boolean r() throws u, IOException {
        return u() || w() || j();
    }

    private void s() throws u, IOException {
        if (u()) {
            t();
        } else if (w()) {
            v();
        } else if (j()) {
            i();
        } else {
            throw new u(this, "expecting comment or processing instruction or space");
        }
    }

    private final void t() throws u, IOException {
        a(e);
        while (!b(f)) {
            g();
        }
        a(f);
    }

    private final boolean u() throws u, IOException {
        return b(e);
    }

    private final void v() throws u, IOException {
        a(g);
        while (!b(h)) {
            g();
        }
        a(h);
    }

    private final boolean w() throws u, IOException {
        return b(g);
    }

    private void x() throws u, p, IOException {
        if (A()) {
            z();
        }
        while (r()) {
            s();
        }
        if (y()) {
            H();
            while (r()) {
                s();
            }
        }
    }

    private boolean y() throws u, IOException {
        return b(i);
    }

    private void z() throws u, p, IOException {
        a(j);
        D();
        if (j()) {
            i();
        }
        if (B()) {
            String C2 = C();
            if (this.D != null && !C2.toLowerCase().equals(this.D)) {
                throw new p(this.x, C2, this.D);
            }
        }
        while (!b(h)) {
            g();
        }
        a(h);
    }

    public String a() {
        return this.x;
    }

    public int b() {
        return this.M;
    }

    /* access modifiers changed from: package-private */
    public int c() {
        return this.E;
    }

    /* access modifiers changed from: package-private */
    public final String d() {
        return FinalChip.BSP_PLATFORM_Null;
    }

    /* access modifiers changed from: package-private */
    public w e() {
        return this.C;
    }

    public String toString() {
        return this.x;
    }
}
