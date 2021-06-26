package defpackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/* renamed from: s  reason: default package */
class s implements x {
    private t c;

    public s(String str, InputStream inputStream, w wVar, String str2, v vVar) throws u, IOException {
        w wVar2 = wVar == null ? x.a : wVar;
        if (!inputStream.markSupported()) {
            throw new Error("Precondition violation: the InputStream passed to ParseByteStream must support mark");
        }
        inputStream.mark(x.b);
        byte[] bArr = new byte[4];
        String a = str2 == null ? a(str, bArr, inputStream.read(bArr), wVar2) : str2;
        try {
            inputStream.reset();
            try {
                this.c = new t(str, new InputStreamReader(inputStream, a(a)), wVar2, a, vVar);
            } catch (IOException e) {
                wVar2.c(new StringBuffer().append("Problem reading with assumed encoding of ").append(a).append(" so restarting with ").append("euc-jp").toString(), str, 1);
                inputStream.reset();
                this.c = new t(str, new InputStreamReader(inputStream, a("euc-jp")), wVar2, (String) null, vVar);
            }
        } catch (UnsupportedEncodingException e2) {
            throw new u(wVar2, str, 1, 0, "euc-jp", new StringBuffer().append("\"").append("euc-jp").append("\" is not a supported encoding").toString());
        } catch (p e3) {
            String a2 = e3.a();
            wVar2.c(new StringBuffer().append("Encoding declaration of ").append(a2).append(" is different that assumed ").append(a).append(" so restarting the parsing with the new encoding").toString(), str, 1);
            inputStream.reset();
            try {
                this.c = new t(str, new InputStreamReader(inputStream, a(a2)), wVar2, (String) null, vVar);
            } catch (UnsupportedEncodingException e4) {
                throw new u(wVar2, str, 1, 0, a2, new StringBuffer().append("\"").append(a2).append("\" is not a supported encoding").toString());
            }
        }
    }

    private static String a(byte b) {
        String hexString = Integer.toHexString(b);
        switch (hexString.length()) {
            case 1:
                return new StringBuffer().append("0").append(hexString).toString();
            case 2:
                return hexString;
            default:
                return hexString.substring(hexString.length() - 2);
        }
    }

    private static String a(String str) {
        return str.toLowerCase().equals("utf8") ? "UTF-8" : str;
    }

    private static String a(String str, byte[] bArr, int i, w wVar) throws IOException {
        String str2;
        if (i != 4) {
            wVar.a(i <= 0 ? "no characters in input" : new StringBuffer().append("less than 4 characters in input: \"").append(new String(bArr, 0, i)).append("\"").toString(), str, 1);
            str2 = "UTF-8";
        } else {
            str2 = (a(bArr, 65279) || a(bArr, -131072) || a(bArr, 65534) || a(bArr, -16842752) || a(bArr, 60) || a(bArr, 1006632960) || a(bArr, 15360) || a(bArr, 3932160)) ? "UCS-4" : a(bArr, 3932223) ? "UTF-16BE" : a(bArr, 1006649088) ? "UTF-16LE" : a(bArr, 1010792557) ? "UTF-8" : a(bArr, 1282385812) ? "EBCDIC" : (a(bArr, -2) || a(bArr, -257)) ? "UTF-16" : "UTF-8";
        }
        if (!str2.equals("UTF-8")) {
            wVar.c(new StringBuffer().append("From start ").append(a(bArr[0])).append(" ").append(a(bArr[1])).append(" ").append(a(bArr[2])).append(" ").append(a(bArr[3])).append(" deduced encoding = ").append(str2).toString(), str, 1);
        }
        return str2;
    }

    private static boolean a(byte[] bArr, int i) {
        return bArr[0] == ((byte) (i >>> 24)) && bArr[1] == ((byte) ((i >>> 16) & 255)) && bArr[2] == ((byte) ((i >>> 8) & 255)) && bArr[3] == ((byte) (i & 255));
    }

    private static boolean a(byte[] bArr, short s) {
        return bArr[0] == ((byte) (s >>> 8)) && bArr[1] == ((byte) (s & 255));
    }

    public String toString() {
        return this.c.toString();
    }
}
