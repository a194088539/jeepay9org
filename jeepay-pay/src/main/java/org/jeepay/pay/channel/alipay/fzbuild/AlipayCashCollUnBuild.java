package org.jeepay.pay.channel.alipay.fzbuild;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRoyaltyRelationUnbindRequest;
import com.alipay.api.response.AlipayTradeRoyaltyRelationUnbindResponse;

public interface AlipayCashCollUnBuild {
    //此处为解除绑定分账
	public static void main(String[] args) {
        //AlipayConfig alipayConfig = new AlipayConfig();
		//AlipayClient client = new DefaultAlipayClient(alipayConfig.getReqUrl(), alipayConfig.getAppId(), alipayConfig.getPrivateKey(), "json", AlipayConfig.CHARSET, alipayConfig.getAlipayPublicKey(), AlipayConfig.SIGNTYPE);
		AlipayClient client = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", "2017052207311806", "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCRbiIuljSc9XdRxxra3T0JTLh2kvy5dfK070ORQNgl+ebLeLdh90ihFwc8xYwJ+CwcUW3wevPgOdxmfnAvxf9uiTY77W7eGEwWNfwDM+wU4N7xnJfaj1h/sQ3JQjs65KyqG9B3T2ZTVanSLc6tKHEC9zGytO4KXp6XKQEjF9iQzwGfXAaSL4HcRZx5jrJE/AD6L80n9i1u75z4Q3VUp5lqBNa77mDncMxn9n1sc/grUrkMTlhFBfh3xLvDic756I5MHa5jKSWEkTp1Aebxbg84IIP1K9/jadJTE8kg0onbjLUIaxZ165s79fHB1Jm+JFh1mk0snHrh+Cuka1G3TtttAgMBAAECggEAGqPHBSukpZ/dZJCJXdArDxoLrETOoJZ3iCdQBDqRnZtCaMFLLlni5wdV3w780RKx2docvnF3XPqcYfYFTOsDGFdwJNggd/SRc3weFuQa+dQbYrzhoDqj5ngjY1uLiAU9j8eaj1BvGRLXEdYsRtHiqb1oUCkOdb3RZTUwrSIAHL5WJoGoxxh55mcknmeQqa0TOcxT4Kq1IdQorfpREkGQXVCbELM2SDCGIybiD0fjWLORV5eXhSfhwK+2n7ziuBWzXQ2SiVlYD8F+u4nNyJfeuRCK4asecl+RxnSV5d50f9uZHv/j5wee2+j3FvUjU4MI7xDj2ejYN97YUh4BYhQejQKBgQDy5IPzLlvf0xvl6xu98vDPP78k43lqGR42d1oYlgb9kskqVtrbj8cOQiqKdqSM5uvv/oRl7U9l4fQPuPhhjdBmCgvXFlP0yuWfIplN4cfjzOR2/k6N2+RES6uFdckco8v9uDXlcrbj3oR18Ea02XHFXL+HnBwb37yypsJCbNGZzwKBgQCZRzSjnMdkj1tVqvS2Uz5VFkBXz30sJ599h/LfN3sV9+ySq8g+9pO1J9foVk3x5a4dXgi+qFUURLVT1XAhVlWgRv87yFi19VMDuccvsEWw6UyOkd83Qbc80y0PZGcY2gIpR50g/+O1Agzq5jZ6qZQXq0WhXB0f2g5CvgVyqT6SAwKBgAipT28/ivUrUQZc4OtgG2g4jLdjCTRYWvR0Qxk3WF54eoXw/PLxaJbAk0XGv01q+qTfPZdo4/jtGsfov6qy5OiOmSd9W1cSWSfYkwDs+TTNXKRFo0V5vuUUkbQ7pAVKbf7JL9rTwWPUzoJqJAtU6bKxAP9z+KihzNfODIVT3hGPAoGACU0p45fa/b45U6yJJxtMGAu+odWEig5pfkumsGcEgCPIZmooP7Hk6sRdNMsv9bLzavLO4wsCBrXYrxqvsEY18gD88hrJT1lwzTQT40/2GrM9oxU1D3xca0OBY4K7QuXP5cNjxKHippRWRlbsDWuHAhyxoAYf+lPYM7KmT/v3QRMCgYEAlk2Y8lTt6lC46zR3WQU19DvqDHr9NgrkdU1+SdcWI5iTeZBp2wb6HSzYf57X+EP4OzDBgExwIj64IiI1zgKhHWfL+9ETlqvEJlSRNg2SKYwrMebuzJjjYwSTEK/L0v+YOG+GWInvlW33R1bQKRk9pnn+1Ed8rDNIaO4O6cvofOA=", "json", "UTF-8", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAi2hTClrlwAkSVPWtcTXgAy3fXa0EToZ0lFTDDq0W36fJuKdxgboperFGBgY2tEZZOxaXOeuwjRe7ETEWcwfQgBdbSgVJ8iBPabqEvhfQF1tLBhZijyI4mTzvRqaxZdW22Ox+ZKpMTPV9ZWx0ofvSDk16W/Am1FxC8k6Jaj1E3d/T3O6jTeHOz2a+V3Jrk5sENkP+HX4JFdnV4RjIbDqvSQ9KCx/OcQ0X0MBcmMTJRVCb0NqVqSYtv6r4a6S2PUaW2CgWzTzzmoW9lj5IzZHeJMiKtTeiUpRZDObkRD7N0/y50pZ/Lty+b4qRLexdNBu8UHYinFnz8jC/3PydwKn+hwIDAQAB", "RSA2");
        AlipayTradeRoyaltyRelationUnbindRequest request = new AlipayTradeRoyaltyRelationUnbindRequest();
        request.setBizContent("{" +
                "\"receiver_list\":[{" +
                "\"type\":\"loginName\"," +
                "\"account\":\"13599169162\"," +
                "\"name\":\"林滨\"," +
                "\"memo\":\"解绑供货商\"" +
                "}]," +
                "\"out_request_no\":\"2021113000000003\"" +
                "  }");
        AlipayTradeRoyaltyRelationUnbindResponse response = null;
        try {
        	response = client.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }

    }

}

//13808555588 杨键坤 2021113000000001
//892834637@qq.com 陈欣瑜 2021113000000002
//13599169162 林滨 2021113000000003