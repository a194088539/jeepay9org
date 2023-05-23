package org.jeepay.mch.user.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.*;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.mch.common.ctrl.BaseController;
import org.jeepay.mch.common.util.AliSmsUtil;
import org.jeepay.mch.secruity.JwtAuthenticationRequest;
import org.jeepay.mch.secruity.JwtTokenUtil;
import org.jeepay.mch.user.service.UserService;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH)
@RestController
public class AuthController extends BaseController {

    @Value("${jwt.cookie}")
    private String tokenCookie;

    @Value("${jwt.expiration}")
    private Integer expiration;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final String SMS_VERIFY_CODE = "SMS_VERIFY_CODE";

    private static Map<String, Integer> mobileSendMap = new HashMap<>();

    private static final MyLog _log = MyLog.getLog(AuthController.class);

    /**
     * 登录鉴权
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/auth")
    @MethodLog( remark = "登录" )
    public ResponseEntity<?> authToken(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException{
        JSONObject param = getJsonParam(request);
        String username = getStringRequired(param, "username");
        String password = getStringRequired(param, "password");
        JwtAuthenticationRequest authenticationRequest = new JwtAuthenticationRequest(username, password);
        String token;
        try {
           token = userService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        }catch (ServiceException e) {
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        }

        MchInfo mchInfo = userService.findByLoginName(username);

        // 判断IP是否允许登录
        String clintIp = IPUtility.getClientIp(request);
        boolean isAllow = JEEPayUtil.ipAllow(clintIp, mchInfo.getLoginWhiteIp(), mchInfo.getLoginBlackIp());
        if(!isAllow) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_IP_NOT_LOGIN));
        }

        JSONObject data = new JSONObject();
        data.put("access_token", token);
        data.put("mchId", mchInfo.getMchId());
        data.put("loginSecurityType", mchInfo.getLoginSecurityType());
        return ResponseEntity.ok(JeePayResponse.buildSuccess(data));
    }

    /**
     * 登录鉴权
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/google_auth")
    public ResponseEntity<?> authGoogle(HttpServletRequest request,
                                       HttpServletResponse response) throws AuthenticationException{
        JSONObject param = getJsonParam(request);
        Long mchId = getLongRequired(param, "mchId");
        Long googleCode = getLongRequired(param, "googleCode");
        // 判断商户
        MchInfo mchInfo = userService.findByMchId(mchId);
        if(mchInfo == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
        }
        if(MchConstant.PUB_YES != mchInfo.getStatus()) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_STATUS_STOP));
        }
        // 验证谷歌验证码
        boolean checkResult = checkGoogleCode(mchInfo.getGoogleAuthSecretKey(), googleCode);
        if(!checkResult) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_GOOGLECODE_NOT_MATCH));
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    /**
     * 登录鉴权(运营平台登录商户系统鉴权)
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/mgr_auth")
    public ResponseEntity<?> mgrAuthToken(HttpServletRequest request,
                                       HttpServletResponse response) throws AuthenticationException{
        JSONObject param = getJsonParam(request);
        Long mchId = getLongRequired(param, "mchId");
        String token = getStringRequired(param, "token");

        MchInfo mchInfo = userService.findByMchId(mchId);
        if(mchInfo == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
        }
        if(MchConstant.PUB_YES != mchInfo.getStatus()) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_STATUS_STOP));
        }

        // 先校验运营平台传过来的token,是否合法
        // 将商户ID+商户密码+密钥 做32位MD5加密转大写
        String password = mchInfo.getPassword();
        String secret = "Abc%$G&!!!128G";
        String rawToken = mchId + password + secret;
        String myToken = MD5Util.string2MD5(rawToken).toUpperCase();
        if(!myToken.equals(token)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_ILLEGAL_LOGIN));
        }

        // 生成jwtToken返回
        String jwtToken = jwtTokenUtil.generateToken(mchId, String.valueOf(mchId));
        JSONObject data = new JSONObject();
        data.put("access_token", jwtToken);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(data));
    }

    /**
     * 刷新token
     * @param request
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/refresh")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        String token = CookieUtil.getCookieByName(request, tokenCookie);
        String refreshedToken;
        try {
            refreshedToken = userService.refreshToken(token);
        }catch (ServiceException e) {
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        }
        if(refreshedToken == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        } else {
            JSONObject data = new JSONObject();
            data.put("access_token", token);
            // 添加cookie
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            cookie.setDomain("jeepay.org");
            cookie.setMaxAge(expiration);// 秒
            response.addCookie(cookie);
            return ResponseEntity.ok(JeePayResponse.buildSuccess(data));
        }
    }

    /**
     * 申请注册
     * @return
     * @throws AuthenticationException
     */
    @MethodLog( remark = "申请注册" )
    @RequestMapping(value = "/auth/register")
    public ResponseEntity<?> register(HttpServletRequest request) throws AuthenticationException{
        JSONObject param = getJsonParam(request);
        MchInfo mchInfo = getObject(param, MchInfo.class);
        if(mchInfo == null) mchInfo = new MchInfo();
        // 验证参数
        if (ObjectValidUtil.isInvalid(mchInfo.getName(), mchInfo.getEmail(), mchInfo.getMobile(), mchInfo.getPassword(), mchInfo.getType())) {
           return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_PARAM_NOT_FOUND));
        }
        // 判断密码
        if(!StrUtil.checkPassword(mchInfo.getPassword())) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
        }

        String smsCode = getStringRequired(param, "vercode");
        Object obj = request.getSession().getAttribute(SMS_VERIFY_CODE);
        if(obj == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SMS_VERIFY_FAIL));
        }

        JSONObject codeObj = (JSONObject) obj;
        String mobile = codeObj.getString("mobile");
        String code = codeObj.getString("code");
        Long time = codeObj.getLong("time");
        // 判断与发送验证码时手机是否一致
        if(!mchInfo.getMobile().toString().equals(mobile)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SMS_VERIFY_FAIL));
        }
        // 超过10分钟
        if(System.currentTimeMillis() > time + 1000 * 60 * 10) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SMS_VERIFY_OVER_TIME));
        }
        // 判断验证码
        if(!smsCode.equals(code)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SMS_VERIFY_FAIL));
        }

        int count;
        try {
            count = userService.register(mchInfo);
        }catch (ServiceException e) {
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        }
        if(count != 1) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MCH_REGISTER_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 发送验证短信
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/auth/sms_send")
    public ResponseEntity<?> sendSms(HttpServletRequest request) throws AuthenticationException{
        JSONObject param = getJsonParam(request);
        // 验证参数
        String mobile = param.getString("phone");
        if(!StrUtil.checkMobileNumber(mobile)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_FORMAT_ERROR));
        }
        // 验证手机号是否发送超限,一天5次
        // 暂时用内存(重启或分布式部署会防不住),真正使用时应改为redis
        String key = mobile + DateUtil.date2Str(new Date(), DateUtil.FORMAT_YYYY_MM_DD2);
        Integer times = mobileSendMap.get(mobile);
        if(times == null ) {
            mobileSendMap.put(key, 1);
        }else if(times <= 5) {
            mobileSendMap.put(key, times+1);
        }else {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_SEND_TOO_MUCH));
        }

        // 验证手机号是否被使用
        MchInfo mchInfo = userService.findByMobile(Long.parseLong(mobile));
        if(mchInfo != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_USED));
        }

        // 发送短信服务
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
        Map smsMap = new HashMap<>();
        smsMap.put("phoneNumbers", mobile);
        smsMap.put("signName", "JeePay聚合支付");
        smsMap.put("templateCode", "SMS_127164863");
        smsMap.put("templateParam", "{\"code\":\""+verifyCode+"\"}");
        try {
            SendSmsResponse response = AliSmsUtil.sendSms(smsMap);
            _log.info("调用阿里短信服务完成.mobile={},code={},message={},requestId={},bizId={}", mobile,
                    response.getCode(), response.getMessage(), response.getRequestId(), response.getBizId());
        } catch (ClientException e) {
            _log.error(e, "");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_SEND_ERROR));
        }
        // 将短信验证码存在session中,分布式部署考虑放到redis
        JSONObject codeObj = new JSONObject();
        codeObj.put("mobile", mobile);
        codeObj.put("code", verifyCode);
        codeObj.put("time", System.currentTimeMillis());
        request.getSession().setAttribute(SMS_VERIFY_CODE, codeObj);
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 获取验证码
     */
    @RequestMapping(value = "/auth/auth_code_get")
    public void getAuthCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map randomMap = RandomValidateCodeUtil.getRandcode(120, 40, 6, 20);
        // TODO 将验证码存储,用于验证
        String randomString = randomMap.get("randomString").toString();
        BufferedImage randomImage = (BufferedImage)randomMap.get("randomImage");
        ImageIO.write(randomImage, "JPEG", response.getOutputStream());
    }

}
