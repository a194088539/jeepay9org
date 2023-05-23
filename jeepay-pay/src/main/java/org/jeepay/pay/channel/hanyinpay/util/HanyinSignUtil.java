package org.jeepay.pay.channel.hanyinpay.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jeepay.common.util.sign.CertDescriptor;
import org.jeepay.common.util.sign.SignUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

public class HanyinSignUtil {
    protected static final Log LOG = LogFactory.getLog(CertDescriptor.class);
    public static String getSign(Map<String,Object> map, String path){
        try {
        File privateFile = new File(path);
        FileInputStream fileInputStream = new FileInputStream(privateFile);
        byte[]privateKeyContent = new byte[fileInputStream.available()];
        fileInputStream.read(privateKeyContent);
        fileInputStream.close();

        String privateKeyStr = new String(privateKeyContent)
                .replaceAll("-.*", "");

        privateKeyContent = Base64.getDecoder().decode(privateKeyStr.replace("\n", ""));

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyContent);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        String context = SignUtils.parameterText(map);

        java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");

        signature.initSign(privateKey);
        signature.update(context.getBytes(Charset.forName("UTF8")));

        String sign = Base64.getEncoder().encodeToString(signature.sign());
        return sign;
        } catch (IOException e) {
            LOG.error( "hanyin签名失败",e);
            return null;
        } catch (NoSuchAlgorithmException e) {
            LOG.error( "hanyin签名失败",e);
            return null;
        } catch (InvalidKeySpecException e) {
            LOG.error( "hanyin签名失败",e);
            return null;
        } catch (InvalidKeyException e) {
            LOG.error( "hanyin签名失败",e);
            return null;
        } catch (SignatureException e) {
            LOG.error("hanyin签名失败", e);
            return null;
        }
    }
}
