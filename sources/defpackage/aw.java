package defpackage;

import com.syu.data.FinalChip;
import java.io.IOException;
import java.io.Reader;

/* renamed from: aw  reason: default package */
public class aw {
    public int a = Integer.MIN_VALUE;
    public int b = Integer.MIN_VALUE;
    public String c = FinalChip.BSP_PLATFORM_Null;
    private final StringBuffer d = new StringBuffer();
    private int e;
    private final Reader f;
    private final int[] g = new int[256];
    private boolean h = false;
    private char i = 0;

    public aw(Reader reader) throws IOException {
        this.f = reader;
        for (int i2 = 0; i2 < this.g.length; i2 = (char) (i2 + 1)) {
            if ((65 <= i2 && i2 <= 90) || ((97 <= i2 && i2 <= 122) || i2 == 45)) {
                this.g[i2] = -3;
            } else if (48 <= i2 && i2 <= 57) {
                this.g[i2] = -2;
            } else if (i2 < 0 || i2 > 32) {
                this.g[i2] = i2;
            } else {
                this.g[i2] = -5;
            }
        }
        a();
    }

    public int a() throws IOException {
        int read;
        int i2;
        boolean z;
        boolean z2;
        if (this.h) {
            this.h = false;
            return this.a;
        }
        this.a = this.e;
        do {
            boolean z3 = false;
            do {
                read = this.f.read();
                if (read != -1) {
                    i2 = this.g[read];
                } else if (this.i != 0) {
                    throw new IOException("Unterminated quote");
                } else {
                    i2 = -1;
                }
                z = this.i == 0 && i2 == -5;
                if (z3 || z) {
                    z3 = true;
                    continue;
                } else {
                    z3 = false;
                    continue;
                }
            } while (z);
            if (i2 == 39 || i2 == 34) {
                if (this.i == 0) {
                    this.i = (char) i2;
                } else if (this.i == i2) {
                    this.i = 0;
                }
            }
            if (this.i != 0) {
                i2 = this.i;
            }
            z2 = z3 || !((this.a < -1 || this.a == 39 || this.a == 34) && this.a == i2);
            if (z2) {
                switch (this.a) {
                    case -3:
                        this.c = this.d.toString();
                        this.d.setLength(0);
                        break;
                    case -2:
                        this.b = Integer.parseInt(this.d.toString());
                        this.d.setLength(0);
                        break;
                    case 34:
                    case 39:
                        this.c = this.d.toString().substring(1, this.d.length() - 1);
                        this.d.setLength(0);
                        break;
                }
                if (i2 != -5) {
                    this.e = i2 == -6 ? read : i2;
                }
            }
            switch (i2) {
                case -3:
                case -2:
                case 34:
                case 39:
                    this.d.append((char) read);
                    continue;
            }
        } while (!z2);
        return this.a;
    }

    public void a(char c2) {
        this.g[c2] = c2;
    }

    public void a(char c2, char c3) {
        while (c2 <= c3) {
            this.g[c2] = -3;
            c2 = (char) (c2 + 1);
        }
    }

    public void b() {
        this.h = true;
    }

    public String toString() {
        switch (this.a) {
            case -3:
            case 34:
                return new StringBuffer().append("\"").append(this.c).append("\"").toString();
            case -2:
                return Integer.toString(this.b);
            case -1:
                return "(EOF)";
            case 39:
                return new StringBuffer().append("'").append(this.c).append("'").toString();
            default:
                return new StringBuffer().append("'").append((char) this.a).append("'").toString();
        }
    }
}
