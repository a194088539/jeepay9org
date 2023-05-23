package org.jeepay.pay.channel.ylpay.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


public class FlaterUtil {
    /**
     * 解压base64压缩包，转成原文
     * @Title: inFlaterFromBase64
     * @Description: TODO
     * @param base64Content
     * @param charset
     * @throws Exception
     * @return String
     */
    public static String inFlaterFromBase64(String base64Content,String charset) throws Exception{
        byte[] fileArray = inflater(Base64.decode(base64Content));
        return new String(fileArray,charset);
    }


    /**
     * 解压缩.
     *
     * @param inputByte
     *            byte[]数组类型的数据
     * @return 解压缩后的数据
     * @throws IOException
     */
    public static byte[] inflater(final byte[] inputByte) throws IOException {
        int compressedDataLength = 0;
        Inflater compresser = new Inflater(false);
        compresser.setInput(inputByte, 0, inputByte.length);
        ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
        byte[] result = new byte[1024];
        try {
            while (!compresser.finished()) {
                compressedDataLength = compresser.inflate(result);
                if (compressedDataLength == 0) {
                    break;
                }
                o.write(result, 0, compressedDataLength);
            }
        } catch (Exception ex) {
            System.err.println("Data format error!\n");
            ex.printStackTrace();
        } finally {
            o.close();
        }
        compresser.end();
        return o.toByteArray();
    }


    /**
     * 把内容压缩成base64字符串
     * @Title: deflaterFromString
     * @Description: TODO
     * @param orgStr
     * @param charset
     * @return
     * @return String
     * @throws IOException
     */
    public static String deflaterFromString(String orgStr,String charset) throws IOException{
        return Base64.encode(deflater(orgStr.getBytes(charset)));
    }

    /**
     * 压缩.
     *
     * @param inputByte
     *            需要解压缩的byte[]数组
     * @return 压缩后的数据
     * @throws IOException
     */
    public static byte[] deflater(final byte[] inputByte) throws IOException {
        int compressedDataLength = 0;
        Deflater compresser = new Deflater();
        compresser.setInput(inputByte);
        compresser.finish();
        ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
        byte[] result = new byte[1024];
        try {
            while (!compresser.finished()) {
                compressedDataLength = compresser.deflate(result);
                o.write(result, 0, compressedDataLength);
            }
        } finally {
            o.close();
        }
        compresser.end();
        return o.toByteArray();
    }


    public static void main(String[] args) throws Exception {
        StringBuffer buf = new StringBuffer();
        buf.append("1手动阀发送到发送到防辐射|3423|沙发方法|asfs").append("\r\n");
        buf.append("2手摔倒粉碎发送|3423|沙发方法|asfs").append("\r\n");
        buf.append("3手动阀发送到发送到防辐射|3423|沙发方法|asfs").append("\r\n");
        buf.append("4手动阀发送到发送到防辐射|3423|沙发方法|asfs").append("\r\n");

        System.out.println("原文："+buf.toString());

        String base64Str = deflaterFromString(buf.toString(), "UTF-8");
        System.out.println("压缩后base64字符串："+base64Str);

        String orgContent = inFlaterFromBase64(base64Str, "UTF-8");
        System.out.println("解压后原文："+orgContent);

    }
}
