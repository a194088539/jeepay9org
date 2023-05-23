package org.jeepay.common.util.sign.encrypt;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Package org.jeepay.common.util.sign.encrypt
 * @Class: RSA_S.java
 * @Description:
 * @Author leo
 * @Date 2019/5/24 21:05
 * @Version
 **/
public class RSA_S {

    private static final String SIGN_MD5RSA_ALGORITHMS = "MD5withRSA";


    public static String sign(String content, String privateKey, String characterEncoding) {

        return RSA.sign(content, privateKey, SIGN_MD5RSA_ALGORITHMS, characterEncoding);
    }

    /**
     * RSA签名
     * @param content 待签名数据
     * @param privateKey 私钥
     * @param characterEncoding 编码格式
     * @return 签名值
     */
    public static String sign(String content, PrivateKey privateKey , String characterEncoding){
        return RSA.sign(content, privateKey, SIGN_MD5RSA_ALGORITHMS, characterEncoding);
    }

    /**
     * RSA验签名检查
     * @param content 待签名数据
     * @param sign 签名值
     * @param  publicKey 公钥
     * @param characterEncoding 编码格式
     * @return 布尔值
     */
    public static boolean verify(String content, String sign, String publicKey, String characterEncoding){

        return RSA.verify(content, sign, publicKey, SIGN_MD5RSA_ALGORITHMS, characterEncoding );
    }

    /**
     * RSA验签名检查
     * @param content 待签名数据
     * @param sign 签名值
     * @param  publicKey 公钥
     * @param characterEncoding 编码格式
     * @return 布尔值
     */
    public static boolean verify(String content, String sign, PublicKey publicKey, String characterEncoding){
        return RSA.verify(content, sign, publicKey, SIGN_MD5RSA_ALGORITHMS, characterEncoding);
    }

}
