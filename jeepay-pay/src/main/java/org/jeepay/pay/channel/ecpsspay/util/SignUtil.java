package org.jeepay.pay.channel.ecpsspay.util;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;

public class SignUtil {

    //#priKeyText
    private final static String priKeyText = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC5/AqsjlrrHvRFS36KZR0ODRWb\n" +
            "lWMMa5ku2KP6vUMaNYL5qDctiDCHFnHpjeCr2MZKkfR/EcXIoMtiWPQPB55dVz17i1FODqarfean\n" +
            "/12saA3ZlD2woUdMkEzcjfWv5cR67xfAuPKnwyOLvOHBevyPh5M1ovLo8bjethvSv4trClOeTBmH\n" +
            "JfFNGUBOd4WzCDGKebs67+jue2mfBX8c3moRJo1doZW5Qa1HUxcwsNmQ3qaKypzB38KULE5DxSZc\n" +
            "CU5Ze9C+aFAvWwOJIBg6GTG3yJUNVlT46ixkEqlA/uketnlrDCU76iR5HLnxHriGOsEqoDnNdbW0\n" +
            "Ugu3XWXXyXdDAgMBAAECggEACUkUH1lcILEC8RK71p0FvlXY5lOnBk+47iXSygn4964ERAKp/pP2\n" +
            "GEh+l3KF7Q59l+uBkXaJsWKVxOs8BCQVPAPHBgwGmycQGy/F/E07mAf56QsFqXPj44aw77IGuil2\n" +
            "jcyyGuUg+E89zxQ3ETnasi/Kobro9chbuoLNENCPXWCozrhSxB1ZFBiIRfwQjzopW+Btjl7nfRNF\n" +
            "afCgxf6PaJBqQq77jZ1rO/10nXv9WxrjOz4sji65RYeRpsOvSdeeE65UqNPQEahyLaV+vm1PrjYL\n" +
            "54UAsagllWuTUdJyWsldzJwX3HyV/gLaaRJG0saKFQrezgBveMCL3eE9kA5GwQKBgQDiKhu+tNJz\n" +
            "EbUJ/Cp+feF3vswKX0K/lpRMZgZTKW9CxIe6AOIA10wpUB/HrbJ6mjZJU141p+5kIec06KVTxOqQ\n" +
            "kKQrXi3FCdaMB2Hpl5K46IuBgte+JFh1ORjr3BQZutz52izRPg2io+B+ZnvMeq/N11+UklinHvLx\n" +
            "4QPdJFeepwKBgQDShQAK+6CzUmyiGGCLpPo9ZEMsQfpRFzeppW7/nlyURwA/Z0qsTsri/qhPSBwx\n" +
            "V6feTTh3qQVw5B77cT1kn1e0w318wdWzZzLMJ6Lsm1KrO7M1h+t7ZRcYYBZ5X3Vv11pkLxTOoEB1\n" +
            "vMIlU9CfSbcCsQDkYVqYMTkvPdo0GctyBQKBgCijaFjcJPSIOf+RUBFqjRj3L7Kta1EBvaP8sSZA\n" +
            "S72zkq/ZeMKt7QF/ZDZ8/5UIILkMQeFiS3Oyo9jd9NcYiw1u8BDa216MZgDDUtSmZ7bSEj8zQ5Dm\n" +
            "9v4ZRGEdKn91FD/Q9Dyk6c5PpAcSYyxa3zwdiE8p/MQKy5yq0gbqgNQBAoGAcWkpLsGEP1bGI0US\n" +
            "rehAz1X2Qh+BJLJnUENJWvgLU+40GfQhbFY+Qq+CKMPmPEbGrqHXK8+omLqsXjURJ+YXr9ApCE68\n" +
            "O5v657TqtqCuloTg9JO2rpE1MfKB6P0eHRRdI9+hS/dVApif1tAicc2ahB6rJl20Go1aCzM9HBhR\n" +
            "W6kCgYBFMu00s8c6rEQtjOZ/eK5L7QA2cPXlg4IKp8y/kLm5Zqr3kYgLn2w+l9O/YwaWjAuYR1IQ\n" +
            "OL+f+2tpU7MIB7ts/SGkjYHwl6If4R4qWETJOkxVRtrVYKRjAtqvZzJSnJSIamlKKlSx5RiylNxy\n" +
            "Vf/On7wixP52bbJ/dpnwgnpL9g==";

    //#pubKeyText
    private final static String pubKeyText = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAufwKrI5a6x70RUt+imUdDg0Vm5VjDGuZ\n" +
            "Ltij+r1DGjWC+ag3LYgwhxZx6Y3gq9jGSpH0fxHFyKDLYlj0DweeXVc9e4tRTg6mq11mp/9drGgN\n" +
            "2ZQ9sKFHTJBM3I21r+XEeu8XwLjyp8Mji7zhwXr8j4eTNaLy6PG43rYb0r+LawpTnkwZhyXxTRlA\n" +
            "TneFswgxinm7Ou/o7ntpnwV/HN5qESaNXaGVuUGtR1MXMLDZkN6misqcwd/ClCxOQ8UmXAlOWXvQ\n" +
            "vmhQL1sDiSAYOhkxt8iVDVZU+OosZBKpQP7pHrZ2awwlO+okeRy58R64hjrBKqA5zXW1tFILt11l\n" +
            "18l3QwIDAQAB";

    public final static String CHARACTER_ENCODING_UTF_8 = "UTF-8";



    /**
     * RSA私钥加签
     * @param priKeyText经过base64处理后的私钥
     * @param plainText明文内容
     * @return 十六进制的签名字符串
     * @throws Exception
     */
    public static String sign(byte[] priKeyText, String plainText) throws Exception {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(priKeyText));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey prikey = keyf.generatePrivate(priPKCS8);

            // 用私钥对信息生成数字签名
            Signature signet = Signature.getInstance("SHA1WithRSA");
            signet.initSign(prikey);
            signet.update(plainText.getBytes("UTF-8"));
            return DigestUtil.byte2hex(signet.sign());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 公钥验签
     * @param pubKeyText经过base64处理后的公钥
     * @param plainText明文内容
     * @param signText十六进制的签名字符串
     * @return 验签结果 true验证一致 false验证不一致
     */
    public static boolean verify(byte[] pubKeyText, String plainText, String signText) {
        try {
            // 解密由base64编码的公钥,并构造X509EncodedKeySpec对象
            X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(pubKeyText));
            // RSA算法
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            // 取公钥匙对象
            PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
            // 十六进制数字签名转为字节
            byte[] signed = DigestUtil.hex2byte(signText.getBytes("UTF-8"));
            Signature signatureChecker = Signature.getInstance("SHA256withRSA");
            signatureChecker.initVerify(pubKey);
            signatureChecker.update(plainText.getBytes("UTF-8"));
            // 验证签名是否正常
            return signatureChecker.verify(signed);
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 公钥加密
     * @param pubKeyText
     * @param plainText
     * @return
     * @throws Exception
     */
    public static String signPubKey(byte[] pubKeyText, String plainText) throws Exception {
        try {
            byte[] data = plainText.getBytes("UTF-8");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(pubKeyText));
            PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            //分段加密
            byte[] enBytes = null;
            for (int i = 0; i < data.length; i += 128) {
                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i,i + 128));
                enBytes = ArrayUtils.addAll(enBytes, doFinal);
            }
            return DigestUtil.byte2hex(enBytes);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 私钥解密
     * @param privateText
     * @param plainText
     * @return
     */
    public static String privateDecrypt(byte[] privateText, String plainText) throws Exception {
        try {
            byte[] data = DigestUtil.hex2byte(plainText.getBytes("UTF-8"));
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateText));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            //分段解密
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i += 256) {
                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + 256));
                sb.append(new String(doFinal));
            }
            return sb.toString();
        } catch (Exception e) {
            throw e;
        }
    }
}