package defpackage;

/* renamed from: j  reason: default package */
class j {
    private final int[] a;
    private int b;
    private int c;

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer((this.a.length * 11) / 10);
        for (int length = this.c < this.a.length ? this.a.length - this.c : 0; length < this.a.length; length++) {
            int i = this.a[(this.b + length) % this.a.length];
            if (i < 65536) {
                stringBuffer.append((char) i);
            } else {
                stringBuffer.append(Integer.toString(i - 65536));
            }
        }
        return stringBuffer.toString();
    }
}
