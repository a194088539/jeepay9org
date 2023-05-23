package org.jeepay.manage.wx.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.WxUser;
import org.jeepay.manage.common.config.MainConfig;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;
import org.jeepay.manage.wx.service.WxUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/wx_user")
public class WxUserController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private MainConfig mainConfig;

    /**
     * 查询微信用户信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long userId = getLongRequired(param, "userId");
        WxUser wxUser = rpcCommonService.rpcWxUserService.findByUserId(userId);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(wxUser));
    }

    /**
     * 新增微信用户信息
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        WxUser wxUser = getObject(param, WxUser.class);
        // 确认账号不能重复
        if(rpcCommonService.rpcWxUserService.findByAccount(wxUser.getAccount()) != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_WX_ACCOUNT_EXIST));
        }
        wxUser.setRandomId(UUID.randomUUID().toString());    // 设置randomId
        int count = rpcCommonService.rpcWxUserService.add(wxUser);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 修改微信用户信息
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long userId = getLongRequired(param, "userId");
        WxUser wxUser = getObject(param, WxUser.class);
        WxUser dbWxUser = rpcCommonService.rpcWxUserService.findByUserId(userId);
        if(dbWxUser == null) {
            ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }

        if(wxUser.getAccount() != null && !wxUser.getAccount().equals(dbWxUser.getAccount())) {
            // 确认账号不能重复
            if(rpcCommonService.rpcWxUserService.findByAccount(wxUser.getAccount()) != null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_WX_ACCOUNT_EXIST));
            }
        }

        int count = rpcCommonService.rpcWxUserService.update(wxUser);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 查询微信用户列表
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        WxUser wxUser = getObject(param, WxUser.class);
        int count = rpcCommonService.rpcWxUserService.count(wxUser);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<WxUser> wxUserList = rpcCommonService.rpcWxUserService.select((getPageIndex(param) - 1) * getPageSize(param), getPageSize(param), wxUser);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(wxUserList, count));
    }

    /**
     * 获取登录二维码
     * @return
     */
    @RequestMapping("/qr_login")
    @ResponseBody
    public ResponseEntity<?> qrLogin(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long userId = getLongRequired(param, "userId");
        WxUser wxUser = rpcCommonService.rpcWxUserService.findByUserId(userId);
        if(wxUser == null) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_WX_USER_NOT_EXIST));

        // 调用微信API服务端,得到该用户登录二维码
        JSONObject resObj = wxUserService.geLoginQrcode(wxUser);
        if(resObj == null) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_WX_USER_LOGIN_FAIL));
        if("0".equals(resObj.getString("code"))) {
            return ResponseEntity.ok(JeePayResponse.buildSuccess(resObj.getJSONObject("data")));
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    /**
     * 获取登录二维码
     * @return
     */
    @RequestMapping("/again_login")
    @ResponseBody
    public ResponseEntity<?> againLogin(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long userId = getLongRequired(param, "userId");
        WxUser wxUser = rpcCommonService.rpcWxUserService.findByUserId(userId);
        if(wxUser == null) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_WX_USER_NOT_EXIST));

        // 调用微信API服务端,用户二次登录
        JSONObject resObj = wxUserService.againLogin(wxUser);
        if(resObj == null) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_WX_API_RETURN_FAIL));
        if("0".equals(resObj.getString("code"))) {
            return ResponseEntity.ok(JeePayResponse.buildSuccess(resObj.getJSONObject("data")));
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    /**
     * 退出登录
     * @return
     */
    @RequestMapping("/logout")
    @ResponseBody
    public ResponseEntity<?> logout(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long userId = getLongRequired(param, "userId");
        WxUser wxUser = rpcCommonService.rpcWxUserService.findByUserId(userId);
        if(wxUser == null) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_WX_USER_NOT_EXIST));

        // 调用微信API服务端,得到该用户登录二维码
        JSONObject resObj = wxUserService.logout(wxUser);
        if(resObj == null) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_WX_USER_LOGOUT_FAIL));
        if("0".equals(resObj.getString("code"))) {
            return ResponseEntity.ok(JeePayResponse.buildSuccess(resObj.getJSONObject("data")));
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    /**
     * 获取登录状态
     * @return
     */
    @RequestMapping("/login_status")
    @ResponseBody
    public ResponseEntity<?> getLoginStatus(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long userId = getLongRequired(param, "userId");
        WxUser wxUser = rpcCommonService.rpcWxUserService.findByUserId(userId);
        if(wxUser == null) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_WX_USER_NOT_EXIST));

        // 调用微信API服务端,获取状态
        JSONObject resObj = wxUserService.getLoginStatus(wxUser);
        if(resObj == null) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_WX_API_RETURN_FAIL));
        if("0".equals(resObj.getString("code"))) {
            return ResponseEntity.ok(JeePayResponse.buildSuccess(resObj.getJSONObject("data")));
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    /**
     * 获取收款二维码
     * @return
     */
    @RequestMapping("/payment_qrcode")
    @ResponseBody
    public ResponseEntity<?> getPaymentQrcode(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long userId = getLongRequired(param, "userId");
        WxUser wxUser = rpcCommonService.rpcWxUserService.findByUserId(userId);
        if(wxUser == null) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_WX_USER_NOT_EXIST));
        // '登录状态,与微信服务端一致.-1:未登录,0:等待扫码登录,1:已扫码,未确认,2:已扫码,已确认,等待登录,3:已登录',
        if(wxUser.getLoginStatus() != 3) {
            return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_WX_USER_NOT_LOGIN));
        }

        // 调用微信API服务端,得到该用户登录二维码
        JSONObject resObj = wxUserService.getPaymentQrcode(wxUser);
        if(resObj == null) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_WX_USER_LOGIN_FAIL));
        if("0".equals(resObj.getString("code"))) {

            JSONObject data = resObj.getJSONObject("data");
            String payUrl = data.getString("payUrl");

            if(StringUtils.isEmpty(payUrl)) {
                return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_WX_USER_LOGIN_FAIL));
            }

            JSONObject object = new JSONObject();
            String codeImgUrl = mainConfig.getPayUrl() + "/qrcode_img_get?url=" + payUrl + "&widht=200&height=200";
            object.put("codeImgUrl", codeImgUrl);
            object.put("codeUrl", payUrl);
            return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

}