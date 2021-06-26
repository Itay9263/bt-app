package defpackage;

import java.io.IOException;

/* renamed from: ar  reason: default package */
public class ar {
    static ao a(bg bgVar, aw awVar) throws bh, IOException {
        int i;
        int i2;
        switch (awVar.a) {
            case -3:
                if (!awVar.c.equals("text")) {
                    throw new bh(bgVar, "at beginning of expression", awVar, "text()");
                } else if (awVar.a() != 40) {
                    throw new bh(bgVar, "after text", awVar, "(");
                } else if (awVar.a() != 41) {
                    throw new bh(bgVar, "after text(", awVar, ")");
                } else {
                    switch (awVar.a()) {
                        case 33:
                            awVar.a();
                            if (awVar.a != 61) {
                                throw new bh(bgVar, "after !", awVar, "=");
                            }
                            awVar.a();
                            if (awVar.a == 34 || awVar.a == 39) {
                                String str = awVar.c;
                                awVar.a();
                                return new bb(str);
                            }
                            throw new bh(bgVar, "right hand side of !=", awVar, "quoted string");
                        case 61:
                            awVar.a();
                            if (awVar.a == 34 || awVar.a == 39) {
                                String str2 = awVar.c;
                                awVar.a();
                                return new az(str2);
                            }
                            throw new bh(bgVar, "right hand side of equals", awVar, "quoted string");
                        default:
                            return ba.a;
                    }
                }
            case -2:
                int i3 = awVar.b;
                awVar.a();
                return new av(i3);
            case 64:
                if (awVar.a() != -3) {
                    throw new bh(bgVar, "after @", awVar, "name");
                }
                String str3 = awVar.c;
                switch (awVar.a()) {
                    case 33:
                        awVar.a();
                        if (awVar.a != 61) {
                            throw new bh(bgVar, "after !", awVar, "=");
                        }
                        awVar.a();
                        if (awVar.a == 34 || awVar.a == 39) {
                            String str4 = awVar.c;
                            awVar.a();
                            return new al(str3, str4);
                        }
                        throw new bh(bgVar, "right hand side of !=", awVar, "quoted string");
                    case 60:
                        awVar.a();
                        if (awVar.a == 34 || awVar.a == 39) {
                            i2 = Integer.parseInt(awVar.c);
                        } else if (awVar.a == -2) {
                            i2 = awVar.b;
                        } else {
                            throw new bh(bgVar, "right hand side of less-than", awVar, "quoted string or number");
                        }
                        awVar.a();
                        return new ak(str3, i2);
                    case 61:
                        awVar.a();
                        if (awVar.a == 34 || awVar.a == 39) {
                            String str5 = awVar.c;
                            awVar.a();
                            return new ag(str3, str5);
                        }
                        throw new bh(bgVar, "right hand side of equals", awVar, "quoted string");
                    case 62:
                        awVar.a();
                        if (awVar.a == 34 || awVar.a == 39) {
                            i = Integer.parseInt(awVar.c);
                        } else if (awVar.a == -2) {
                            i = awVar.b;
                        } else {
                            throw new bh(bgVar, "right hand side of greater-than", awVar, "quoted string or number");
                        }
                        awVar.a();
                        return new aj(str3, i);
                    default:
                        return new ah(str3);
                }
            default:
                throw new bh(bgVar, "at beginning of expression", awVar, "@, number, or text()");
        }
    }
}
