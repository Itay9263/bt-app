package defpackage;

import com.syu.data.FinalChip;

/* renamed from: ch  reason: default package */
class ch {
    private static String a(String str) {
        char c = 'a';
        String lowerCase = str.toLowerCase();
        if (!lowerCase.matches("[a-z]*[1-5]?")) {
            return lowerCase;
        }
        if (!lowerCase.matches("[a-z]*[1-5]")) {
            return lowerCase.replaceAll("v", "ü");
        }
        int numericValue = Character.getNumericValue(lowerCase.charAt(lowerCase.length() - 1));
        int indexOf = lowerCase.indexOf(97);
        int indexOf2 = lowerCase.indexOf(101);
        int indexOf3 = lowerCase.indexOf("ou");
        if (-1 == indexOf) {
            if (-1 == indexOf2) {
                if (-1 == indexOf3) {
                    indexOf = lowerCase.length() - 1;
                    while (true) {
                        if (indexOf < 0) {
                            indexOf = -1;
                            c = '$';
                            break;
                        } else if (String.valueOf(lowerCase.charAt(indexOf)).matches("[aeiouv]")) {
                            c = lowerCase.charAt(indexOf);
                            break;
                        } else {
                            indexOf--;
                        }
                    }
                } else {
                    indexOf = indexOf3;
                    c = "ou".charAt(0);
                }
            } else {
                indexOf = indexOf2;
                c = 'e';
            }
        }
        if ('$' == c || -1 == indexOf) {
            return lowerCase;
        }
        char charAt = "āáăàaēéĕèeīíĭìiōóŏòoūúŭùuǖǘǚǜü".charAt(("aeiouv".indexOf(c) * 5) + (numericValue - 1));
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(lowerCase.substring(0, indexOf).replaceAll("v", "ü"));
        stringBuffer.append(charAt);
        stringBuffer.append(lowerCase.substring(indexOf + 1, lowerCase.length() - 1).replaceAll("v", "ü"));
        return stringBuffer.toString();
    }

    static String a(String str, cp cpVar) throws cs {
        if (cq.c == cpVar.c() && (cr.b == cpVar.d() || cr.a == cpVar.d())) {
            throw new cs("tone marks cannot be added to v or u:");
        }
        if (cq.b == cpVar.c()) {
            str = str.replaceAll("[1-5]", FinalChip.BSP_PLATFORM_Null);
        } else if (cq.c == cpVar.c()) {
            str = a(str.replaceAll("u:", "v"));
        }
        if (cr.b == cpVar.d()) {
            str = str.replaceAll("u:", "v");
        } else if (cr.c == cpVar.d()) {
            str = str.replaceAll("u:", "ü");
        }
        return co.a == cpVar.b() ? str.toUpperCase() : str;
    }
}
