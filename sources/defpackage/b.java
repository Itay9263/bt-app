package defpackage;

import java.util.List;
import org.ahocorasick.trie.Trie;

/* renamed from: b  reason: default package */
public final class b {
    static Trie a = null;
    static h b = null;
    static List<g> c = null;

    public static String a(char c2) {
        return b(c2) ? c2 == 12295 ? "LING" : f.b[c(c2)] : String.valueOf(c2);
    }

    public static String a(String str, String str2) {
        return a.a(str, a, c, str2, b);
    }

    private static short a(byte[] bArr, byte[] bArr2, int i) {
        short s = (short) (bArr2[i] & 255);
        return (bArr[i / 8] & f.a[i % 8]) != 0 ? (short) (s | 256) : s;
    }

    public static boolean b(char c2) {
        return (19968 <= c2 && c2 <= 40869 && c(c2) > 0) || 12295 == c2;
    }

    private static int c(char c2) {
        int i = c2 - 19968;
        return (i < 0 || i >= 7000) ? (7000 > i || i >= 14000) ? a(e.a, e.b, i - 14000) : a(d.a, d.b, i - 7000) : a(c.a, c.b, i);
    }
}
