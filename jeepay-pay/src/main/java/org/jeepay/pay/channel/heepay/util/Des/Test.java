package org.jeepay.pay.channel.heepay.util.Des;

import org.jeepay.pay.channel.heepay.util.HttpUtil;
import org.jeepay.pay.channel.heepay.util.SmallTools;

/**
 * Created by Administrator on 2018/1/10.
 */
public class Test {
    public static void main(String[] args){
        String url = "https://open.heepay.com/HY_B2CEBANKPC/transQuery.do";

        //请求参数
        String merchantId = "100213";//商户号
        String merchantOrderNo = "20180111182319407755";//商户批次号
        String version = "2.0";//版本号
        String sign = "";//签名结果
        String key = "77f214b4b5e6e18b15dd97796815d915";//商户密钥
        String requestTime = SmallTools.getDate("yyyyMMddHHmmss");//请求时间

        //拼接签名串
        String sign1 = "merchantId="+merchantId+
                "&merchantOrderNo="+merchantOrderNo+
                "&requestTime="+requestTime+
                "&version="+version+
                "&key="+key;

        System.out.println("签名参数："+sign1);
        //对签名参数进行MD5加密得到sign
        sign = SmallTools.MD5en(sign1);
        //拼接请求参数
        String parameter = "merchantId="+merchantId+
                "&merchantOrderNo="+merchantOrderNo+
                "&requestTime="+requestTime+
                "&version="+version+
                "&sign="+sign;
        System.out.println("请求参数："+parameter);
        String ret = HttpUtil.sendPost(url,parameter);
        System.out.println("返回的数据："+ret);
        SmallTools.checkSign(ret,key);
    }
}
