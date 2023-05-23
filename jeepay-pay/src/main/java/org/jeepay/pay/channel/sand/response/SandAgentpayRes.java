package org.jeepay.pay.channel.sand.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Package org.jeepay.pay.channel.sand.response
 * @Class: SandAgentpayRes.java
 * @Description: sand代付公共响应
 * @Author leo
 * @Date 2019/4/12 14:37
 * @Version
 **/
@Data
@NoArgsConstructor
@ToString
public class SandAgentpayRes {
    /**
     * 响应码
     */
    private String respCode;
    /**
     * 响应描述
     */
    private String respDesc;
    /**
     * 交易时间
     */
    private String tranTime;
    /**
     * 扩展域
     */
    private String extend;

}
