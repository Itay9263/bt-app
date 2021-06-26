package defpackage;

import com.syu.data.FinalChip;
import java.io.IOException;

/* renamed from: bh  reason: default package */
public class bh extends Exception {
    private Throwable a;

    bh(bg bgVar, Exception exc) {
        super(new StringBuffer().append(bgVar).append(" ").append(exc).toString());
        this.a = null;
        this.a = exc;
    }

    public bh(bg bgVar, String str) {
        super(new StringBuffer().append(bgVar).append(" ").append(str).toString());
        this.a = null;
    }

    bh(bg bgVar, String str, aw awVar, String str2) {
        this(bgVar, new StringBuffer().append(str).append(" got \"").append(a(awVar)).append("\" instead of expected ").append(str2).toString());
    }

    private static String a(aw awVar) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(b(awVar));
            if (awVar.a != -1) {
                awVar.a();
                stringBuffer.append(b(awVar));
                awVar.b();
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            return new StringBuffer().append("(cannot get  info: ").append(e).append(")").toString();
        }
    }

    private static String b(aw awVar) {
        switch (awVar.a) {
            case -3:
                return awVar.c;
            case -2:
                return new StringBuffer().append(awVar.b).append(FinalChip.BSP_PLATFORM_Null).toString();
            case -1:
                return "<end of expression>";
            default:
                return new StringBuffer().append((char) awVar.a).append(FinalChip.BSP_PLATFORM_Null).toString();
        }
    }

    public Throwable getCause() {
        return this.a;
    }
}
