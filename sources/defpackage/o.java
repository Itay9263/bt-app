package defpackage;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/* renamed from: o  reason: default package */
class o implements Enumeration {
    o() {
    }

    public boolean hasMoreElements() {
        return false;
    }

    public Object nextElement() {
        throw new NoSuchElementException();
    }
}
