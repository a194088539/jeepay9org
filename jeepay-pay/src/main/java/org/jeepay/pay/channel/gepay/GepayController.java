package org.jeepay.pay.channel.gepay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.core.entity.PayPassageAccount;
import org.jeepay.pay.ctrl.common.BaseController;
import org.jeepay.pay.service.RpcCommonService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @Description: 个付通免签通道界面跳转
 * @date 2018-03-02
 * @version V1.0
 */
@Controller
public class GepayController extends BaseController {

	private static final MyLog _log = MyLog.getLog(GepayController.class);

	@Autowired
	private RpcCommonService rpcCommonService;

	/**
	 * 转到前端收银台界面
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/api/gepay/pay_{type}.htm")
	public String toPay(HttpServletRequest request, ModelMap model, @PathVariable("type") String type) throws ServletException, IOException {
		JSONObject po = getJsonParam(request);
		// https://qr.alipay.com/fkx04112akzpulwo9pvye74?t=1541681073413
		String payId = getString(po, "payId");
		String sk = getString(po, "sk");
		String amount = getString(po, "amount");
		String payOrderId = getString(po, "payOrderId");
		String mchOrderNo = getString(po, "mchOrderNo");
		String collectionName = getString(po, "collectionName");
		String collectionType =getString(po, "collectionType");
		// https://mobilecodec.alipay.com/client_download.htm?qrcode=fkx04319ftj9wcl46eh8m5a&t=1540812174754
		//String toAlipayUrl = "https://mobilecodec.alipay.com/client_download.htm?qrcode=" + codeUrl.substring(codeUrl.lastIndexOf("/") + 1);
		// alipays://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=https%3a%2f%2fmobilecodec.alipay.com%2fclient_download.htm%3fqrcode%3dfkx04112akzpulwo9pvye74%3ft%3d1541681073413
		//String startAppUrl = "alipays://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + URLEncoder.encode(toAlipayUrl);
		model.put("amount", AmountUtil.convertCent2Dollar(amount));
		model.put("amountStr", "￥"+ AmountUtil.convertCent2Dollar(amount));
		model.put("mchOrderNo", mchOrderNo);
		model.put("payOrderId", payOrderId);
		model.put("payId", payId);
		model.put("sk", sk);
		model.put("collectionName", collectionName);
		model.put("collectionType", collectionType);
		//model.put("toAlipayUrl", toAlipayUrl);
		//model.put("startAppUrl", startAppUrl);

		return "payment/gepay/pay_" + type;
	}

	/**
	 * 查询订单
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/api/gepay/query")
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

	private String buildRetObj(String code, String msg) {
		JSONObject retObj = new JSONObject();
		retObj.put("code", code);
		retObj.put("msg", msg);
		_log.info("[响应数据]retObj={}", retObj);
		return retObj.toJSONString();
	}

	private String buildRetObj(String code, String msg, JSONObject data) {
		JSONObject retObj = new JSONObject();
		retObj.put("code", code);
		retObj.put("msg", msg);
		retObj.put("data", data);
		_log.info("[响应数据]retObj={}", retObj);
		return retObj.toJSONString();
	}

	/**
	 * 查询二维码
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws IOException
     */
	@RequestMapping("/api/gepay/query_qr")
	@ResponseBody
	public String qrQuery(HttpServletRequest request) throws ServletException, IOException {
		String logP = "[查询QR]";
		JSONObject po = getJsonParam(request);
		String payOrderId = getString(po, "payOrderId");
		String payId = getString(po, "payId");
		String sk = getString(po, "sk");
		_log.info("{}参数payOrderId={}, payId={}, sk={}, ", logP, payOrderId, payId, sk);

		PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);

		String code = "-1";
		String msg;
		if(payOrder == null) {
			msg = "订单不存在";
			return buildRetObj(code, msg);
		}

		if(payOrder.getChannelOrderNo() != null && !payOrder.getChannelOrderNo().equals(payId)) {
			msg = "订单不配配";
			return buildRetObj(code, msg);
		}

		PayPassageAccount payPassageAccount = rpcCommonService.rpcPayPassageAccountService.findById(payOrder.getPassageAccountId());
		if(payPassageAccount == null) {
			msg = "支付通道异常";
			return buildRetObj(code, msg);
		}

		try {
			String payParam = payPassageAccount.getParam();
			GepayConfig gepayConfig = new GepayConfig(payParam);
			String url = String.format("%s/api/query_qr/%s/%s", gepayConfig.getReqUrl(), payId, sk);
			_log.info("{}请求数据:{}", logP, url);
			String result = JEEPayUtil.call4Post(url);
			_log.info("{}返回数据:{}", logP, result);
			if(StringUtils.isBlank(result)) {
				msg = "获取二维码超时,请重试";
				return buildRetObj(code, msg);
			}

			JSONObject resObj = JSONObject.parseObject(result);
			Integer resCode = resObj.getInteger("code");
			if(resCode == 1) {
				JSONObject resData = resObj.getJSONObject("data");
				String codeUrl = resData.getString("payUrl");     		// 二维码URL
				String expireTime = resData.getString("expireTime");	// 二维码剩余时间
				String collectionType = resData.getString("collectionType");	// 收款类型
				//String codeUrl = "https://qr.alipay.com/fkx07965dicrcqxqd9tw4e6?t=1541806379080";
				JSONObject retData = new JSONObject();
				retData.put("codeUrl", codeUrl);
				retData.put("codeImgUrl", "/api/qrcode_img_get?url="+URLEncoder.encode(codeUrl)+"&width=300&height=300");
				switch (collectionType) {
					case "1" :
						String toAlipayUrl = "https://mobilecodec.alipay.com/client_download.htm?qrcode=" + codeUrl.substring(codeUrl.lastIndexOf("/") + 1);
						String startAppUrl = "alipays://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + URLEncoder.encode(toAlipayUrl);
						retData.put("startAppUrl", startAppUrl);
						break;
					case "2" :
						retData.put("startAppUrl", codeUrl);
						break;
					case "3" :
						retData.put("startAppUrl", codeUrl);
						break;
				}
				retData.put("expireTime", expireTime);
				code = "1";
				msg = "success";
				return buildRetObj(code, msg, retData);
			}else {
				msg = "获取二维码失败,请重试";
				return buildRetObj(code, msg);
			}
		} catch (Exception e) {
			_log.error(e, "");
			msg = "获取二维码异常,请重试";
			return buildRetObj(code, msg);
		}
	}

	public static void main(String[] args) {

		String codeUrl = "https://qr.alipay.com/fkx04112akzpulwo9pvye74?t=1541681073413";
		String toAlipayUrl = codeUrl.substring(codeUrl.lastIndexOf("/") + 1);
		System.out.println(toAlipayUrl);
	}

}
