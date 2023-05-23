package org.jeepay.pay.channel.heepay.util.Des;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


public final class a {
    private static final byte[] a = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] b = new byte[128];

    static {
        int var0;
        for(var0 = 0; var0 < 128; ++var0) {
            b[var0] = -1;
        }

        for(var0 = 65; var0 <= 90; ++var0) {
            b[var0] = (byte)(var0 - 65);
        }

        for(var0 = 97; var0 <= 122; ++var0) {
            b[var0] = (byte)(var0 - 97 + 26);
        }

        for(var0 = 48; var0 <= 57; ++var0) {
            b[var0] = (byte)(var0 - 48 + 52);
        }

        b[43] = 62;
        b[47] = 63;
    }

    public a() {
    }

    public static String a(byte[] var0) {
        byte[] var1;
        int var2;
        if((var2 = var0.length % 3) == 0) {
            var1 = new byte[4 * var0.length / 3];
        } else {
            var1 = new byte[4 * (var0.length / 3 + 1)];
        }

        int var3 = var0.length - var2;
        int var7 = 0;

        int var8;
        for(var8 = 0; var7 < var3; var8 += 4) {
            int var4 = var0[var7] & 255;
            int var5 = var0[var7 + 1] & 255;
            int var6 = var0[var7 + 2] & 255;
            var1[var8] = a[var4 >>> 2 & 63];
            var1[var8 + 1] = a[(var4 << 4 | var5 >>> 4) & 63];
            var1[var8 + 2] = a[(var5 << 2 | var6 >>> 6) & 63];
            var1[var8 + 3] = a[var6 & 63];
            var7 += 3;
        }

        switch(var2) {
            case 0:
            default:
                break;
            case 1:
                var7 = (var2 = var0[var0.length - 1] & 255) >>> 2 & 63;
                var8 = var2 << 4 & 63;
                var1[var1.length - 4] = a[var7];
                var1[var1.length - 3] = a[var8];
                var1[var1.length - 2] = 61;
                var1[var1.length - 1] = 61;
                break;
            case 2:
                var2 = var0[var0.length - 2] & 255;
                int var9 = var0[var0.length - 1] & 255;
                var7 = var2 >>> 2 & 63;
                var8 = (var2 << 4 | var9 >>> 4) & 63;
                var9 = var9 << 2 & 63;
                var1[var1.length - 4] = a[var7];
                var1[var1.length - 3] = a[var8];
                var1[var1.length - 2] = a[var9];
                var1[var1.length - 1] = 61;
        }

        return new String(var1);
    }

    public static byte[] a(String var0) {
        var0 = var0;
        StringBuffer var1 = new StringBuffer();
        int var2 = var0.length();

        byte var4;
        for(int var3 = 0; var3 < var2; ++var3) {
            boolean var10000;
            label54: {
                if((var4 = (byte)var0.charAt(var3)) != 61) {
                    if(var4 < 0 || var4 >= 128) {
                        var10000 = false;
                        break label54;
                    }

                    if(b[var4] == -1) {
                        var10000 = false;
                        break label54;
                    }
                }

                var10000 = true;
            }

            if(var10000) {
                var1.append(var0.charAt(var3));
            }
        }

        byte[] var8;
        if((var0 = var1.toString()).charAt(var0.length() - 2) == 61) {
            var8 = new byte[(var0.length() / 4 - 1) * 3 + 1];
        } else if(var0.charAt(var0.length() - 1) == 61) {
            var8 = new byte[(var0.length() / 4 - 1) * 3 + 2];
        } else {
            var8 = new byte[var0.length() / 4 * 3];
        }

        int var6 = 0;

        byte var5;
        byte var9;
        byte var10;
        for(int var7 = 0; var6 < var0.length() - 4; var7 += 3) {
            var9 = b[var0.charAt(var6)];
            var10 = b[var0.charAt(var6 + 1)];
            var4 = b[var0.charAt(var6 + 2)];
            var5 = b[var0.charAt(var6 + 3)];
            var8[var7] = (byte)(var9 << 2 | var10 >> 4);
            var8[var7 + 1] = (byte)(var10 << 4 | var4 >> 2);
            var8[var7 + 2] = (byte)(var4 << 6 | var5);
            var6 += 4;
        }

        if(var0.charAt(var0.length() - 2) == 61) {
            var9 = b[var0.charAt(var0.length() - 4)];
            var10 = b[var0.charAt(var0.length() - 3)];
            var8[var8.length - 1] = (byte)(var9 << 2 | var10 >> 4);
        } else if(var0.charAt(var0.length() - 1) == 61) {
            var9 = b[var0.charAt(var0.length() - 4)];
            var10 = b[var0.charAt(var0.length() - 3)];
            var4 = b[var0.charAt(var0.length() - 2)];
            var8[var8.length - 2] = (byte)(var9 << 2 | var10 >> 4);
            var8[var8.length - 1] = (byte)(var10 << 4 | var4 >> 2);
        } else {
            var9 = b[var0.charAt(var0.length() - 4)];
            var10 = b[var0.charAt(var0.length() - 3)];
            var4 = b[var0.charAt(var0.length() - 2)];
            var5 = b[var0.charAt(var0.length() - 1)];
            var8[var8.length - 3] = (byte)(var9 << 2 | var10 >> 4);
            var8[var8.length - 2] = (byte)(var10 << 4 | var4 >> 2);
            var8[var8.length - 1] = (byte)(var4 << 6 | var5);
        }

        return var8;
    }

    private static String b(String var0) {
        StringBuffer var1 = new StringBuffer();
        int var2 = var0.length();

        for(int var3 = 0; var3 < var2; ++var3) {
            boolean var10000;
            label29: {
                byte var4;
                if((var4 = (byte)var0.charAt(var3)) != 61) {
                    if(var4 < 0 || var4 >= 128) {
                        var10000 = false;
                        break label29;
                    }

                    if(b[var4] == -1) {
                        var10000 = false;
                        break label29;
                    }
                }

                var10000 = true;
            }

            if(var10000) {
                var1.append(var0.charAt(var3));
            }
        }

        return var1.toString();
    }

    private static boolean a(byte var0) {
        return var0 == 61?true:(var0 >= 0 && var0 < 128?b[var0] != -1:false);
    }
}

