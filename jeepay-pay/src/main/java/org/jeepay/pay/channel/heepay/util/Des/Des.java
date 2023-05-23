package org.jeepay.pay.channel.heepay.util.Des;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class Des {
    private static final String b = "DES/ECB/NoPadding";
    private static final String c = "DES";
    private static final String CODE = "UTF-8";

    public Des() {
    }

    private static char a(byte var0) {
        return (var0 = (byte)(var0 & 15)) < 10?(char)(var0 + 48):(char)(var0 + 65 - 10);
    }

    private static String a(byte[] var0) {
        if(var0 == null) {
            return null;
        } else {
            StringBuffer var1 = new StringBuffer();

            for(int var2 = 0; var2 < var0.length; ++var2) {
                byte var3 = (byte)((var0[var2] & 240) >> 4);
                byte var4 = (byte)(var0[var2] & 15);
                var1.append(a(var3));
                var1.append(a(var4));
            }

            return var1.toString();
        }
    }

    private static byte a(char var0) {
        return var0 >= 48 && var0 <= 57?(byte)(var0 - 48):(var0 >= 97 && var0 <= 102?(byte)(10 + (var0 - 97)):(var0 >= 65 && var0 <= 70?(byte)(10 + (var0 - 65)):0));
    }

    private static byte[] a(String var0) {
        int var1;
        byte[] var2 = new byte[var1 = var0.length() / 2];

        for(int var3 = 0; var3 < var1; ++var3) {
            char var4 = var0.charAt(var3 << 1);
            char var5 = var0.charAt((var3 << 1) + 1);
            var2[var3] = (byte)((a(var4) << 4) + a(var5));
        }

        return var2;
    }

    public static String Encrypt3Des(String var0, String var1, String var2)  {
        try {
            String var3 = var1.substring(0, 8);
            String var4 = var1.substring(8, 16);
            var1 = var1.substring(16, 24);
            boolean var12 = "ToHex16".equalsIgnoreCase(var2);
            int var5;
            byte[] var7;
            byte[] var10;
            if((var5 = (var10 = var0.getBytes(CODE)).length + 8 & -8) != var10.length) {
                var7 = new byte[var5];

                int var6;
                for(var6 = 0; var6 < var10.length; ++var6) {
                    var7[var6] = var10[var6];
                }

                for(var6 = var10.length; var6 < var7.length; ++var6) {
                    var7[var6] = (byte)(var5 - var10.length);
                }

                var10 = var7;
            }

            byte[] var16 = new byte[var10.length];
            var7 = new byte[8];

            for(var5 = 0; var5 + 8 <= var10.length; var5 += 8) {
                byte[] var8 = new byte[8];

                int var9;
                for(var9 = 0; var9 < 8; ++var9) {
                    var8[var9] = var10[var5 + var9];
                }

                if(var5 == 0) {
                    var7 = var3.getBytes(CODE);
                } else {
                    for(var9 = 0; var9 < 8; ++var9) {
                        var7[var9] = var16[var5 + var9 - 8];
                    }
                }

                var8 = b(a(b(var8, var3, "DES/ECB/NoPadding", var7), var4, "DES/ECB/NoPadding", (byte[])null), var1, "DES/ECB/NoPadding", (byte[])null);

                for(var9 = 0; var9 < 8; ++var9) {
                    var16[var5 + var9] = var8[var9];
                }
            }

            String var17;
            if(var12) {
                var10 = var16;
                String var10000;
                if(var16 == null) {
                    var10000 = null;
                } else {
                    StringBuffer var11 = new StringBuffer();

                    for(int var13 = 0; var13 < var10.length; ++var13) {
                        byte var14 = (byte)((var10[var13] & 240) >> 4);
                        byte var15 = (byte)(var10[var13] & 15);
                        var11.append(a(var14));
                        var11.append(a(var15));
                    }

                    var10000 = var11.toString();
                }

                var17 = var10000;
            } else {
                var17 = a.a(var16);
            }

            return var17;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String Decrypt3Des(String var0, String var1, String var2) {
        try {
            String var3 = var1.substring(0, 8);
            String var4 = var1.substring(8, 16);
            var1 = var1.substring(16, 24);
            byte[] var5 = new byte[8];
            byte[] var6;
            byte[] var12;
            if("ToHex16".equalsIgnoreCase(var2)) {
                int var11;
                var6 = new byte[var11 = (var0 = var0).length() / 2];

                for(int var7 = 0; var7 < var11; ++var7) {
                    char var8 = var0.charAt(var7 << 1);
                    char var9 = var0.charAt((var7 << 1) + 1);
                    var6[var7] = (byte)((a(var8) << 4) + a(var9));
                }

                var12 = var6;
            } else {
                var12 = a.a(var0);
            }

            var6 = new byte[var12.length];

            int var10;
            for(var10 = 0; var10 + 8 <= var12.length; var10 += 8) {
                byte[] var13 = new byte[8];

                int var14;
                for(var14 = 0; var14 < 8; ++var14) {
                    var13[var14] = var12[var10 + var14];
                }

                var13 = b(a(var13, var1, "DES/ECB/NoPadding", (byte[])null), var4, "DES/ECB/NoPadding", (byte[])null);
                if(var10 == 0) {
                    var5 = var3.getBytes(CODE);
                } else {
                    for(var14 = 0; var14 < 8; ++var14) {
                        var5[var14] = var12[var10 + var14 - 8];
                    }
                }

                var13 = a(var13, var3, "DES/ECB/NoPadding", var5);

                for(var14 = 0; var14 < 8; ++var14) {
                    var6[var10 + var14] = var13[var14];
                }
            }

            byte var15;
            if((var15 = var6[var6.length - 1]) > 0 && var15 <= var6.length) {
                if(var15 == var6.length) {
                    return "";
                } else {
                    byte[] var16 = new byte[var6.length - var15];

                    for(var10 = 0; var10 < var16.length; ++var10) {
                        var16[var10] = var6[var10];
                    }

                    return new String(var16, CODE);
                }
            } else {
                return new String(var6, CODE);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] a(byte[] var0, String var1, String var2, byte[] var3) throws Exception {
        Cipher var4 = Cipher.getInstance(var2);
        DESKeySpec var6 = new DESKeySpec(var1.getBytes(CODE));
        SecretKey var7 = SecretKeyFactory.getInstance("DES").generateSecret(var6);
        if(var2.contains("/CBC/") && var3 != null) {
            IvParameterSpec var8 = new IvParameterSpec(var3);
            var4.init(2, var7, var8);
        } else {
            var4.init(2, var7);
        }

        return var4.doFinal(var0);
    }

    private static byte[] b(byte[] var0, String var1, String var2, byte[] var3) throws Exception {
        Cipher var4 = Cipher.getInstance(var2);
        DESKeySpec var6 = new DESKeySpec(var1.getBytes(CODE));
        SecretKey var7 = SecretKeyFactory.getInstance("DES").generateSecret(var6);
        if(var2.contains("/CBC/")) {
            IvParameterSpec var8 = new IvParameterSpec(var3);
            var4.init(1, var7, var8);
        } else {
            var4.init(1, var7);
        }

        return var4.doFinal(var0);
    }
}
