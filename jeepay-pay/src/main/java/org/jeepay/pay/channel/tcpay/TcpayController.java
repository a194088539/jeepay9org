package org.jeepay.pay.channel.tcpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.ctrl.common.BaseController;
import org.jeepay.pay.service.RpcCommonService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Description: 转卡通道跳转
 * @author aragom
 * @date 2019-01-20
 */
@Controller
public class TcpayController extends BaseController {

	private static final MyLog _log = MyLog.getLog(TcpayController.class);

	@Autowired
	private RpcCommonService rpcCommonService;

	/**
	 * 转到前端收银台界面
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/api/"+TcpayConfig.CHANNEL_NAME+"/pay_{type}.htm")
	public String toPay(HttpServletRequest request, ModelMap model, @PathVariable("type") String type) {
		JSONObject po = getJsonParam(request);
		String amount = getString(po, "amount");
		String payOrderId = getString(po, "payOrderId");
		String mchOrderNo = getString(po, "mchOrderNo");
		// 订单创建时间与当前时间差值(此处使用db计算，为了避免服务器和db时间有差异)
		Long expireTime = rpcCommonService.rpcPayOrderService.getOrderTimeLeft(payOrderId, TcpayConfig.PAY_ORDER_TIME_OUT);
		if(expireTime == null || expireTime < 0) expireTime = 0l;
		String codeUrl = getString(po, "codeUrl");
		String codeImgUrl = getString(po, "codeImgUrl");
		model.put("amount", AmountUtil.convertCent2Dollar(amount));
		model.put("amountStr", "￥"+ AmountUtil.convertCent2Dollar(amount));
		model.put("mchOrderNo", mchOrderNo);
		model.put("payOrderId", payOrderId);
		model.put("expireTime", expireTime);
		model.put("codeUrl", codeUrl);
		model.put("codeImgUrl", codeImgUrl);
		return "payment/"+TcpayConfig.CHANNEL_NAME+"/pay_" + type;
	}

	/**
	 * 查询订单
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/api/"+TcpayConfig.CHANNEL_NAME+"/query")
	@ResponseBody
	public String toQuery(HttpServletRequest request) throws ServletException, IOException {
		JSONObject po = getJsonParam(request);
		String payOrderId = getString(po, "payOrderId");
		_log.info("[查询订单]参数payOrderId={}", payOrderId);

		PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
		JSONObject retObj = new JSONObject();
		String status = "-1";
		String url = "";
		if(payOrder != null) {
			status = payOrder.getStatus() + "";
			url = payOrder.getReturnUrl();
		}
		retObj.put("status", status);
		retObj.put("url", url);
		_log.info("[查询订单]结果payOrderId={},retObj={}", payOrderId, retObj);
		return retObj.toJSONString();
	}

}
