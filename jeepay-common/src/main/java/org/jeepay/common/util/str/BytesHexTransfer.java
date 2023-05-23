package org.jeepay.common.util.str;

/**
 * @Package com.egzosn.pay.common.util.str
 * @Class: BytesHexTransfer.java
 * @Description: byte数组与16进制互转工具类
 * @Author leo
 * @Date 2018/12/27 14:11
 * @Version
 **/
public class BytesHexTransfer {

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 方法一：
     * byte[] to hex string
     *
     * @param bytes
     * @return
     */
//    public static String bytesToHex1(byte[] bytes) {
//        // 一个byte为8位，可用两个十六进制位标识
//        char[] buf = new char[bytes.length * 2];
//        int a = 0;
//        int index = 0;
//        for(byte b : bytes) { // 使用除与取余进行转换
//            if(b < 0) {
//                a = 256 + b;
//            } else {
//                a = b;
//            }
//
//            buf[index++] = HEX_CHAR[a / 16];
//            buf[index++] = HEX_CHAR[a % 16];
//        }
//
//        return new String(buf);
//    }

    /**
     * 方法二：
     * byte[] to hex string
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for(byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }

        return new String(buf);
    }

    /**
     * 方法三：
     * byte[] to hex string
     *
     * @param bytes
     * @return
     */
//    public static String bytesToHex3(byte[] bytes) {
//        StringBuilder buf = new StringBuilder(bytes.length * 2);
//        for(byte b : bytes) { // 使用String的format方法进行转换
//            buf.append(String.format("%02x", new Integer(b & 0xff)));
//        }
//
//        return buf.toString();
//    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] toBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

}
