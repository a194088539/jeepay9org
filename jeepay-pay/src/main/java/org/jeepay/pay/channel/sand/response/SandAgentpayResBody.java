package org.jeepay.pay.channel.sand.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Package org.jeepay.pay.channel.sand.response
 * @Class: SandAgentpayResBody.java
 * @Description:
 * @Author leo
 * @Date 2019/4/12 14:36
 * @Version
 **/
@Data
@NoArgsConstructor
@ToString
public class SandAgentpayResBody extends SandAgentpayRes {

    /**
     * 订单号，商户订单号
     */
    private String orderCode;
    /**
     * 原交易响应码
     */
    private String origRespCode;

    private String origRespDesc;
    /**
     * 结果状态：0-成功 1-失败 2-处理中
     */
    private String resultFlag;
    /**
     * 杉德系统流水号
     */
    private String sandSerial;

    private String tranDate;

    private String tranFee;

    private String extraFee;

    private String holidayFee;

    private String tranTime;

    // ===============余额查询时的额外参数================
    /**
     * 账户余额
     */
    private String balance;
    /**
     * 可用余额
     */
    private String creditAmt;

}
