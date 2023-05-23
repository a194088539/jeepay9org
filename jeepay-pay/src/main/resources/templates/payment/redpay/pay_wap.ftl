<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="Content-Language" content="zh-cn">
    <meta name="apple-mobile-web-app-capable" content="no"/>
    <meta name="apple-touch-fullscreen" content="yes"/>
    <meta name="format-detection" content="telephone=no,email=no"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="white">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-control" content="no-cache">
    <meta http-equiv="Cache" content="no-cache">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>支付宝</title>
    <link href="/css/gepay.css?t=20181110" rel="stylesheet" media="screen">
    <script type="text/javascript" src="https://cdn.staticfile.org/jquery/1.11.1/jquery.min.js"></script>
    <script type="text/javascript">
        var _hmt = _hmt || [];
        _hmt.push(['_trackEvent', 'pay', 'show1']);
    </script>
</head>

<body>
<div class="body">
    <h1 class="mod-title" style="line-height: normal;">
        <span class="ico_log ico-1"></span>
    </h1>

    <div class="mod-ct">
        <div class="order">
        </div>
        <div class="amount" id="money">${amountStr!}</div>
        <div class="qrcode-img-wrapper" data-role="qrPayImgWrapper">
            <div data-role="qrPayImg" class="qrcode-img-area">
                <div class="ui-loading qrcode-loading" data-role="qrPayImgLoading" ></div>
                <div style="position: relative;display: inline-block;">
                    <img  id="show_qrcode" width="250" height="250" src="/images/qrcode_loading.png"   style="display: block;" title="本二维码仅可支付一次,请勿重复使用,本二维码仅可支付一次,请勿重复使用,本二维码仅可支付一次,请勿重复使用,本二维码仅可支付一次,请勿重复使用">
                    <img onclick="$('#use').hide()" id="use" src="/images/logo_alipay.png" style="display:none; position: absolute;top: 50%;left: 50%;width:32px;height:32px;margin-left: -16px;margin-top: -30px">
                    <img id="loading" src="/images/loading.gif" style="position: absolute;top: 50%;left: 50%;width:32px;height:32px;margin-left: -16px;margin-top: -15px">
                </div>
            </div>
        </div>
        <div class="time-item time"  style = "padding-top: 0px; padding-bottom: 10px;display: none;">
            <strong id="hour_show">0时</strong>
            <strong id="minute_show">0分</strong>
            <strong id="second_show">0秒</strong>
        </div>
        <div id="doDiv" style="display:none;">
            <#if autoJump?? && autoJump == true >
            <div class ="paybtn" style="padding-top: 10px;"><a href="" id="alipaybtn" class="btn btn-primary" target="_blank" style="font-size: 26px;">点击启动支付宝支付</a></div>
            <#else>
            手机用户请按下面步骤完成支付操作: </br>
            <div style="color: red">
                1. 请截图或长按二维码保存 </br>
                2. 打开支付宝，点击扫一扫 </br>
                3. 点击右上角相册选中保存的二维码即可 </br>
                4. 请勿二次支付
            </div>
            </#if>
        </div>
        <div class="time-item"  style = "padding-top: 10px;">
            <div class="time-item"><h1>订单:${mchOrderNo!}</h1> </div>
            <!--其他手机浏览器+支付宝支付-->
            <div class="time-item" id="msg"><h1><h1>支付完成后，请返回此页</h1></div>
        </div>


        <div class="tip" style="display:none;">
            <div class="tip-text">
                <p id="showtext">打开支付宝 [扫一扫]</p>
            </div>
        </div>

        <div class="time-item">
            <p>
                <br/>
                <#--<a href="https://www.wenjuan.com/s/bmuyUb/?user=2a43bb261e64d2c996f3cfea&repeat=1" id="alipayerror" target="_blank">支付宝支付遇到问题,点此反馈</a>-->
            </p>
        </div>


        <div class="tip-text">
        </div>


    </div>

    <div class="foot">
        <div class="inner" style="display:none;">
            <p>手机用户可保存上方二维码到手机中</p>
            <p>在微信扫一扫中选择“相册”即可</p>
            <p></p>
        </div>
    </div>

</div>
<script type="text/javascript">
    var strcode = '';
    var myTimer;
    var myGetCodeTimer;
    var outTime=0;
    var intDiff=0;
    var goTimerBegin =  0;
    var open_alipay_url;
    
    $(document).on('visibilitychange', function (e) {
        if (e.target.visibilityState === "visible") {
            var s = Math.floor((parseInt(new Date().getTime())-parseInt(goTimerBegin))/1000);
            intDiff = outTime-s;
            $("#show_qrcode").attr("src",$("#show_qrcode").attr("src"));
        }
    });

    //构建倒计时
    function buildGoTime(expire_time){
        outTime=expire_time;
        intDiff=expire_time;
        goTimerBegin =  new Date().getTime();
        goTimer();
    }

    function goTimer() {
        myTimer = window.setInterval(function () {
            //支付宝或QQ二维码过期
            var day = 0,
                    hour = 0,
                    minute = 0,
                    second = 0;//时间默认值
            if (intDiff > 0) {
                day = Math.floor(intDiff / (60 * 60 * 24));
                hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
                minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
                second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
            }
            if (minute <= 9) minute = '0' + minute;
            if (second <= 9) second = '0' + second;
            $('#hour_show').html('<s id="h"></s>' + hour + '时');
            $('#minute_show').html('<s></s>' + minute + '分');
            $('#second_show').html('<s></s>' + second + '秒');
            if (hour <= 0 && minute <= 0 && second <= 0) {
                qrcode_timeout();
                clearInterval(myTimer);
            }
            intDiff = intDiff-2;
            checkdata();

        }, 2000);
    }

    function checkdata(){
        var x = 100000;
        var y = 0;
        var rand = parseInt(Math.random() * (x - y + 1) + y);
        $.get(
                "/api/tcpay/query",
                {
                    payOrderId : "${payOrderId!}",
                    r : rand
                },
                function(data){
                    data = JSON.parse(data);
                    if (data.status == '3' || data.status == '2'){
                        window.clearInterval(myTimer);
                        $("#show_qrcode").attr("src","/images/pay_ok.png");
                        $("#use").remove();
                        $("#money").text("支付成功");
                        $("#msg").html("<h1>即将返回商家页</h1>");
                        if(data.url != null && data.url != '') {
                            $(".paybtn").html('<a href="' + data.url + '" class="btn btn-primary">返回商家页</a>');
                        }else {
                            $(".paybtn").remove();
                        }
                        setTimeout(function(){
                            // window.location = data.url;
                            if(data.url != null && data.url != '') {
                                location.replace(data.url)
                            }
                        }, 3000);

                    }
                }
        );
    }
    function qrcode_hideloaing(){
        $('#use').show();
        $("#loading").hide();
        //$(".tip").show();
        $(".time").show();
        $('#doDiv').show();
    }
    function qrcode_timeout(){
        $('#show_qrcode').attr("src","/images/qrcode_timeout.png");
        $("#use").hide();
        $('#doDiv').hide();
        $('#msg').html("<h1>请刷新本页</h1>");
    }
    //不用点击跳到支付宝去
    function noClickGoAliPay(){
        /*var isWV = isWebview();
         if(isWV){
         return;
         }*/
        if(open_alipay_url!=""){
            window.location = open_alipay_url;
        }else{
            var goPay = '<span id="goPay"> <span>';
            //给A标签中的文字添加一个能被jQuery捕获的元素
            $('#alipaybtn').append(goPay);
            //模拟点击A标签中的文字
            $('#goPay').click();
        }
    }

    $().ready(function(){
        var show_image_place = "0";
        var aliplay_click = "0";
        $('#show_qrcode').load(function(){
            //图片加载完成
            _hmt.push(['_trackEvent', 'pay', 'image_show_success_1']);
        });
        $('#show_qrcode').error(function(){
            //图片加载失败
            _hmt.push(['_trackEvent', 'pay', 'image_show_error_1']);
            if(strcode!=""){
                $("#show_qrcode").attr("src","https://www.kuaizhan.com/common/encode-png?large=true&data="+strcode);
            }
        });


        $('#alipaybtn').click(function(){
            _hmt.push(['_trackEvent', 'pay', 'a_click1']);
            aliplay_click = "1";
        });

        var expireTime = "${expireTime}";
        if(expireTime <= 0) {
            qrcode_timeout();
            $("#loading").hide();
        }else {
            $("#show_qrcode").attr("src", "${codeImgUrl}");
            buildGoTime("${expireTime}");
            qrcode_hideloaing();
            strcode ="${codeUrl}";
            <#if autoJump?? && autoJump == true >
            open_alipay_url = "alipays://platformapi/startapp?appId=20000067&url=" + encodeURIComponent("${codeUrl!}");
            $("#alipaybtn").attr("href", open_alipay_url);
            noClickGoAliPay();
            </#if>
        }

    });

    //
</script>
</body>
</html>