package org.jeepay.pay.channel.sukebaopay.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * MD5工具类
 * </p>
 */
public class MD5Utils {
    
    private static final String ALGORIGTHM_MD5 = "MD5";
    private static final int CACHE_SIZE = 2048;
    
    /**
     * <p>
     * 字符串生成MD5
     * </p>
     * 
     * @param input
     * @return
     * @throws Exception
     */
    public static String createMD5(String input) throws Exception {
        return createMD5(input, null);
    }
    
    /**
     * <p>
     * 字符串生成MD5
     * </p>
     * 
     * @param input
     * @param charset 编码(可选)
     * @return
     * @throws Exception
     */
    public static String createMD5(String input, String charset) throws Exception {
        byte[] data;
        if (charset != null && !"".equals(charset)) {
            data = input.getBytes(charset);
        } else {
            data = input.getBytes();
        }
        MessageDigest messageDigest = getMD5();
        messageDigest.update(data);
        return byteArrayToHexString(messageDigest.digest());
    }
    
    /**
     * <p>
     * 生成文件MD5
     * </p>
     * <p>
     * 该方法中使用的FileChannel存在一个巨大Bug，不释放文件句柄，即生成MD5的文件无法操作(移动或删除等)<br>
     * 该方法已被generateFileMD5取代
     * </p>
     * 
     * @param filePath
     * @return
     * @throws Exception
     */
    @Deprecated
    public static String createFileMD5(String filePath) throws Exception {
        String md5 = "";
        File file = new File(filePath);
        if (file.exists()) {
            MessageDigest messageDigest = getMD5();
            FileInputStream in = new FileInputStream(file);
            FileChannel fileChannel = in.getChannel();
            MappedByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            messageDigest.update(byteBuffer);
            fileChannel.close();
            in.close();
            byte data[] = messageDigest.digest();
            md5 = byteArrayToHexString(data);
         }
        return md5;
    }
    
    /**
     * <p>
     * 生成文件MD5值
     * <p>
     * <p>
     * 在进行文件校验时，文件读取的缓冲大小[CACHE_SIZE]需与该方法的一致，否则校验失败
     * </p>
     * 
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String generateFileMD5(String filePath) throws Exception {
        String md5 = "";
        File file = new File(filePath);
        if (file.exists()) {
            MessageDigest messageDigest = getMD5();
            InputStream in = new FileInputStream(file);
            byte[] cache = new byte[CACHE_SIZE];
            int nRead = 0;
            while ((nRead = in.read(cache)) != -1) {
                messageDigest.update(cache, 0, nRead);
            }
            in.close();
            byte data[] = messageDigest.digest();
            md5 = byteArrayToHexString(data);
         }
        return md5;
    }
    
    /**
     * <p>
     * MD5摘要字节数组转换为16进制字符串
     * </p>
     * 
     * @param data MD5摘要
     * @return
     */
    private static String byteArrayToHexString(byte[] data) {
        // 用来将字节转换成 16 进制表示的字符
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' 
        };
        // 每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
        char arr[] = new char[16 * 2];
        int k = 0; // 表示转换结果中对应的字符位置
        // 从第一个字节开始，对 MD5 的每一个字节转换成 16 进制字符的转换
        for (int i = 0; i < 16; i++) {
            byte b = data[i]; // 取第 i 个字节
            // 取字节中高 4 位的数字转换, >>>为逻辑右移，将符号位一起右移
            arr[k++] = hexDigits[b >>> 4 & 0xf];
            // 取字节中低 4 位的数字转换
            arr[k++] = hexDigits[b & 0xf];
        }
        // 换后的结果转换为字符串
        return new String(arr);
    }
    
    /**
     * <p>
     * 获取MD5实例
     * </p>
     * 
     * @return
     * @throws NoSuchAlgorithmException 
     */
    private static MessageDigest getMD5() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(ALGORIGTHM_MD5);
    }
    
	 //标准MD5方法工具方法
   public static String getMd5(String plainText) {  
       try {  
           MessageDigest md = MessageDigest.getInstance("MD5");  
           md.update(plainText.getBytes());  
           byte b[] = md.digest();  
           int i;  
           StringBuffer buf = new StringBuffer("");  
           for (int offset = 0; offset < b.length; offset++) {  
               i = b[offset];  
               if (i < 0)  
                   i += 256;  
               if (i < 16)  
                   buf.append("0");  
               buf.append(Integer.toHexString(i));  
           }  
          // System.out.println(buf.toString());
           //32位加密  
           return buf.toString();  
           // 16位的加密  
           //return buf.toString().substring(8, 24);  
       } catch (NoSuchAlgorithmException e) {  
           e.printStackTrace();  
           return null;  
       }  
   }  
}