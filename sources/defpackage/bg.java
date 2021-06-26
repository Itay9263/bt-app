package defpackage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;

/* renamed from: bg  reason: default package */
public class bg {
    private static Hashtable d = new Hashtable();
    private Stack a;
    private boolean b;
    private String c;

    private bg(String str) throws bh {
        this(str, (Reader) new InputStreamReader(new ByteArrayInputStream(str.getBytes())));
    }

    private bg(String str, Reader reader) throws bh {
        boolean z;
        boolean z2;
        this.a = new Stack();
        try {
            this.c = str;
            aw awVar = new aw(reader);
            awVar.a('/');
            awVar.a('.');
            awVar.a(':', ':');
            awVar.a('_', '_');
            if (awVar.a() == 47) {
                this.b = true;
                if (awVar.a() == 47) {
                    awVar.a();
                    z = true;
                } else {
                    z = false;
                }
            } else {
                this.b = false;
                z = false;
            }
            this.a.push(new ax(this, z, awVar));
            while (awVar.a == 47) {
                if (awVar.a() == 47) {
                    awVar.a();
                    z2 = true;
                } else {
                    z2 = false;
                }
                this.a.push(new ax(this, z2, awVar));
            }
            if (awVar.a != -1) {
                throw new bh(this, "at end of XPATH expression", awVar, "end of expression");
            }
        } catch (IOException e) {
            throw new bh(this, (Exception) e);
        }
    }

    private bg(boolean z, ax[] axVarArr) {
        this.a = new Stack();
        for (ax addElement : axVarArr) {
            this.a.addElement(addElement);
        }
        this.b = z;
        this.c = null;
    }

    public static bg a(String str) throws bh {
        bg bgVar;
        synchronized (d) {
            bgVar = (bg) d.get(str);
            if (bgVar == null) {
                bgVar = new bg(str);
                d.put(str, bgVar);
            }
        }
        return bgVar;
    }

    private String d() {
        StringBuffer stringBuffer = new StringBuffer();
        boolean z = true;
        Enumeration elements = this.a.elements();
        while (true) {
            boolean z2 = z;
            if (!elements.hasMoreElements()) {
                return stringBuffer.toString();
            }
            ax axVar = (ax) elements.nextElement();
            if (!z2 || this.b) {
                stringBuffer.append('/');
                if (axVar.a()) {
                    stringBuffer.append('/');
                }
            }
            stringBuffer.append(axVar.toString());
            z = false;
        }
    }

    public boolean a() {
        return this.b;
    }

    public boolean b() {
        return ((ax) this.a.peek()).b();
    }

    public Enumeration c() {
        return this.a.elements();
    }

    public Object clone() {
        ax[] axVarArr = new ax[this.a.size()];
        Enumeration elements = this.a.elements();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= axVarArr.length) {
                return new bg(this.b, axVarArr);
            }
            axVarArr[i2] = (ax) elements.nextElement();
            i = i2 + 1;
        }
    }

    public String toString() {
        if (this.c == null) {
            this.c = d();
        }
        return this.c;
    }
}
