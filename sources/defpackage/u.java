package defpackage;

import com.syu.data.FinalChip;

/* renamed from: u  reason: default package */
public class u extends Exception {
    private int a;
    private Throwable b;

    public u(String str, int i, int i2, String str2, String str3) {
        super(a(str, i, i2, str2, str3));
        this.a = -1;
        this.b = null;
        this.a = i;
    }

    public u(String str, Throwable th) {
        super(new StringBuffer().append(str).append(" ").append(th).toString());
        this.a = -1;
        this.b = null;
        this.b = th;
    }

    public u(t tVar, char c, char c2) {
        this(tVar, new StringBuffer().append("got '").append(c).append("' instead of expected '").append(c2).append("'").toString());
    }

    public u(t tVar, char c, String str) {
        this(tVar, new StringBuffer().append("got '").append(c).append("' instead of ").append(str).append(" as expected").toString());
    }

    public u(t tVar, char c, char[] cArr) {
        this(tVar, new StringBuffer().append("got '").append(c).append("' instead of ").append(a(cArr)).toString());
    }

    public u(t tVar, String str) {
        this(tVar.e(), tVar.a(), tVar.b(), tVar.c(), tVar.d(), str);
    }

    public u(t tVar, String str, String str2) {
        this(tVar, new StringBuffer().append("got \"").append(str).append("\" instead of \"").append(str2).append("\" as expected").toString());
    }

    public u(t tVar, String str, char[] cArr) {
        this(tVar, str, new String(cArr));
    }

    public u(w wVar, String str, int i, int i2, String str2, String str3) {
        this(str, i, i2, str2, str3);
        wVar.a(str3, str, i);
    }

    static String a(int i) {
        return i == -1 ? "EOF" : new StringBuffer().append(FinalChip.BSP_PLATFORM_Null).append((char) i).toString();
    }

    private static String a(String str, int i, int i2, String str2, String str3) {
        return new StringBuffer().append(str).append("(").append(i).append("): \n").append(str2).append("\nLast character read was '").append(a(i2)).append("'\n").append(str3).toString();
    }

    private static String a(char[] cArr) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(cArr[0]);
        for (int i = 1; i < cArr.length; i++) {
            stringBuffer.append(new StringBuffer().append("or ").append(cArr[i]).toString());
        }
        return stringBuffer.toString();
    }

    public Throwable getCause() {
        return this.b;
    }
}
