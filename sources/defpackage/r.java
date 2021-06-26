package defpackage;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/* renamed from: r  reason: default package */
class r {
    private static final Integer a = new Integer(1);
    private static final Integer b = new Integer(2);
    private static final Integer c = new Integer(3);
    private static final Integer d = new Integer(4);
    private static final Integer e = new Integer(5);
    private static final Integer f = new Integer(6);
    private static final Integer g = new Integer(7);
    private static final Integer h = new Integer(8);
    private static final Integer i = new Integer(9);
    private static final Integer j = new Integer(10);
    private final Vector k = new Vector();
    private Hashtable l = new Hashtable();

    r() {
    }

    private static Integer b(q qVar) {
        return new Integer(System.identityHashCode(qVar));
    }

    /* access modifiers changed from: package-private */
    public int a(q qVar) {
        return ((Integer) this.l.get(b(qVar))).intValue();
    }

    /* access modifiers changed from: package-private */
    public Enumeration a() {
        return this.k.elements();
    }

    /* access modifiers changed from: package-private */
    public void a(String str) {
        this.k.addElement(str);
    }

    /* access modifiers changed from: package-private */
    public void a(q qVar, int i2) {
        Integer num;
        this.k.addElement(qVar);
        switch (i2) {
            case 1:
                num = a;
                break;
            case 2:
                num = b;
                break;
            case 3:
                num = c;
                break;
            case 4:
                num = d;
                break;
            case 5:
                num = e;
                break;
            case 6:
                num = f;
                break;
            case 7:
                num = g;
                break;
            case 8:
                num = h;
                break;
            case 9:
                num = i;
                break;
            case 10:
                num = j;
                break;
            default:
                num = new Integer(i2);
                break;
        }
        this.l.put(b(qVar), num);
    }

    /* access modifiers changed from: package-private */
    public void b() {
        this.k.removeAllElements();
        this.l.clear();
    }

    public String toString() {
        try {
            StringBuffer stringBuffer = new StringBuffer("{ ");
            Enumeration elements = this.k.elements();
            while (elements.hasMoreElements()) {
                Object nextElement = elements.nextElement();
                if (nextElement instanceof String) {
                    stringBuffer.append(new StringBuffer().append("String(").append(nextElement).append(") ").toString());
                } else {
                    q qVar = (q) nextElement;
                    stringBuffer.append(new StringBuffer().append("Node(").append(qVar.k()).append(")[").append(this.l.get(b(qVar))).append("] ").toString());
                }
            }
            stringBuffer.append("}");
            return stringBuffer.toString();
        } catch (IOException e2) {
            return e2.toString();
        }
    }
}
