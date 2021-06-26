package defpackage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;

/* renamed from: a  reason: default package */
final class a {
    static final C0000a a = new C0000a();

    /* renamed from: a$a  reason: collision with other inner class name */
    static final class C0000a implements Comparator<Emit> {
        C0000a() {
        }

        /* renamed from: a */
        public int compare(Emit emit, Emit emit2) {
            int i = -1;
            if (emit.getStart() != emit2.getStart()) {
                if (emit.getStart() >= emit2.getStart()) {
                    i = emit.getStart() == emit2.getStart() ? 0 : 1;
                }
                return i;
            } else if (emit.size() < emit2.size()) {
                return 1;
            } else {
                return emit.size() == emit2.size() ? 0 : -1;
            }
        }
    }

    static String a(String str, Trie trie, List<g> list, String str2, h hVar) {
        int i;
        if (str == null || str.length() == 0) {
            return str;
        }
        if (trie == null || hVar == null) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i2 = 0; i2 < str.length(); i2++) {
                stringBuffer.append(b.a(str.charAt(i2)));
                if (i2 != str.length() - 1) {
                    stringBuffer.append(str2);
                }
            }
            return stringBuffer.toString();
        }
        List<Emit> a2 = hVar.a(trie.parseText(str));
        Collections.sort(a2, a);
        StringBuffer stringBuffer2 = new StringBuffer();
        int i3 = 0;
        int i4 = 0;
        while (i3 < str.length()) {
            if (i4 >= a2.size() || i3 != a2.get(i4).getStart()) {
                stringBuffer2.append(b.a(str.charAt(i3)));
                i = i3 + 1;
            } else {
                String[] a3 = a(a2.get(i4).getKeyword(), list);
                for (int i5 = 0; i5 < a3.length; i5++) {
                    stringBuffer2.append(a3[i5].toUpperCase());
                    if (i5 != a3.length - 1) {
                        stringBuffer2.append(str2);
                    }
                }
                i = a2.get(i4).size() + i3;
                i4++;
            }
            if (i != str.length()) {
                stringBuffer2.append(str2);
                i3 = i;
            } else {
                i3 = i;
            }
        }
        return stringBuffer2.toString();
    }

    static String[] a(String str, List<g> list) {
        if (list != null) {
            for (g next : list) {
                if (next != null && next.a() != null && next.a().contains(str)) {
                    return next.a(str);
                }
            }
        }
        throw new IllegalArgumentException("No pinyin dict contains word: " + str);
    }
}
