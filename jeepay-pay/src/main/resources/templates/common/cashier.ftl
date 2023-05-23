<html><head>
    <title>安全支付</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">
    <link href="/common/css/pay_v4.css" rel="stylesheet" media="screen">
    <script type="text/javascript" src="/common/js/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/mobile-detect/1.4.3/mobile-detect.min.js"></script>
    <script src="http://pv.sohu.com/cityjson?ie=utf-8"></script>
    <script type="text/javascript" src="/common/js/common.js"></script>
    <script src="/common/js/qrcode.js"></script>
    <script src="https://gw.alipayobjects.com/as/g/h5-lib/alipayjsapi/3.1.1/alipayjsapi.min.js"></script>
    <!--<script src="https://gw.alipayobjects.com/as/g/h5-lib/alipayjsapi/3.1.1/alipayjsapi.inc.min.js"></script>-->
    <style type="text/css">
        .hide{display:none;}
        body {
            padding: 0;
            background-color: #eeeeee;
            font-family: "microsoft yahei";
        }

        .pay-main {
            background-color: #1E9FFF;
            padding-top: 20px;
            padding-left: 20px;
            padding-bottom: 20px;
        }

        .pay-main img {
            margin: 0 auto;
            display: block;
        }

        .pay-main .lines {
            margin: 0 auto;
            text-align: center;
            color: #54ff00;
            font-size: 12pt;
            margin-top: 10px;
        }

        .tips .img {
            margin: 20px;
        }

        .tips .img img {
            width: 20px;
        }

        .tips span {
            vertical-align: top;
            color: #1e9fff;
            padding-left: 10px;
            padding-top: 0px;
        }

        .action {
            background: #1D81D1;
            padding: 10px 0;
            color: #ffffff;
            text-align: center;
            font-size: 14pt;
            border-radius: 10px 10px;
            margin: 15px;
        }

        .action:focus {
            background: #4cb131;
        }

        .action.disabled {
            background-color: #aeaeae;
        }

        .footer {
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            text-align: center;
            padding-bottom: 20px;
            font-size: 10pt;
            color: #aeaeae;
        }

        .footer .ct-if {
            margin-top: 6px;
            font-size: 8pt;
        }

        .jieguo {
            top: 20px;
            line-height: 26px;
            max-width: 260px;
            padding: 8px 20px;
            margin: 0 auto;
            position: relative;
            border: 1px #ddd dashed;
            box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);
        }

        .text {
            font-size: 16px;
            font-weight: bold;
            color: #179cff;
        }
        .red{
            font-size: 16px;
        }

        .iospayweixinbtn{width:80%;margin:0 auto;padding-top:20px;font-size: 15px;line-height:30px;}
        .time-item strong{font-size:18px;line-height:30px;}
        .btn-alipay {color: #fff;background-color: #428bca;border-color: #357ebd;}
    </style>
</head>
<body>
<div class="conainer">
    <div id="not_alipay">
        <h1 class="mod-title">
            <span class="ico_log ico_ALIPAY"></span>
        </h1>

        <div class="mod-ct">
            <div class="amount">订单金额￥<span>${money!}</span></div>
            <!--<div class="discount color-red">本次获得优惠￥<span>11</span></div>-->
            <div class="time-item btn_g">
                <!--<p><a onclick="mShare()" id="btn_share" class="hide">步骤1：点我选择在<b>浏览器</b>中打开<b>授权</b></a></p>-->
                <p><a onclick="qry.jumpApp()" id="alipay_active_btn" class="hide btn btn-alipay">启动支付宝付款</a></p>
                <!--<span class='red'>1、请先截屏保存二维码到手机</span><br><span class='red'>2、打开支付宝App 扫一扫本地图片。</span>-->
            </div>

            <div class="qrcode-img-wrapper" style="padding-top:20px;">
                <div class="time-item" id="show_img_wrap" style="text-align:center;">
                    <img id="show_qrcode" src="/common/img/loading.gif">
                </div>
                <div class="time-item" style="padding:10px 0;text-align:center;">
                    <strong id="hour_show"><s id="h"></s>0时</strong>
                    <strong id="minute_show"><s></s>00分</strong>
                    <strong id="second_show"><s></s>00秒</strong>
                </div>
                <div class="iospayweixinbtn"></div>
            </div>
            <div class="time-item" style="padding:20px 0;text-align:center;">
                <div id="msg1"><h1></h1></div>
                <div><h1 style="color:#cc5005">付款即时到账 未到账可联系我们</h1></div>
                <div><h1>订单:${orderNo!}</h1></div>
            </div>
        </div>
    </div>

<script>
    var qry = {__id:''};
    
    var is_paying= "0";
    var istype="2";
    var expireIn = "300";
    
    var return_url = "";
    var order_no="${orderNo!}";
    var toUrl = decodeURIComponent('${codeUrl!}');
    var isJump = "${RequestParameters.isJump}";//1跳转;2不跳转

    var myTimer,is_mobile=isMobile(),is_weixin=isWeixin(),isAlipay=isAli(),is_time_out=0;
    var isiOS = !!navigator.userAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
    var pay_way = 1; //拉起支付的客户端：1=PC 2=Android 3=Ios
    var qrcodeTxt = toUrl;

    qry.listener = function(){
        $.getJSON('/api/order_query/'+order_no,{order_no:order_no},function(data){
            qry.showResult(data);
        });
    };
    
    qry.showResult = function( data ){
        if (data.state == "SUCCESS" || data.state == 'ALREADY_PAID') {
            qry.pay_success(data);
        }else if(data.state == "DEFAULT" || data.state == 'PAYING'){
            if(!is_time_out){
                setTimeout(function () { qry.listener() }, 2000);
            }
        }
    }

    qry.pay_success= function(){
        if(isAlipay){
            $("#payzt font").html("支付成功")
            $("#okpay").addClass("hide")
        }else{
            qry.resetTime();
            $("#show_img_wrap canvas,#show_img_wrap canvas + img").hide();
            $('#show_qrcode').removeClass("hide").attr('src','common/img/success.jpg');
            $('#msg1').html('<h1 style="color:#ff0000;font-size:35px;font-weight:normal">订单支付成功！</h1>');

            if (is_mobile == 1) {
                $('#alipay_active_btn').remove();
            } else {
                if(return_url){
                    $('#msg1').html('<h1>支付成功，即将跳转到<a style="font-weight:bold;" href="'+return_url+'">商家</a>页面</h1>');
                    setTimeout(function () { window.location = return_url }, 3000);
                }
            }
        } 
    }
    
    qry.disabled = function() {
        if(isAlipay){
            $("#payzt font").html("过期未支付！")
            $("#okpay").addClass("hide")            
        } else {
            $("#btn_share").addClass("hide");
            $('#show_qrcode').removeClass("hide").attr('src','/common/img/qrcode_timeout.png');
            $("#show_img_wrap canvas,#show_img_wrap canvas + img").hide();
            $('#msg1').html('<h1>过期未支付！</h1>');
            clearInterval(myTimer);

            if(is_mobile == 1){
                //$('#alipay_active_btn').attr("href","javascript:history.go(-1)").html('返回上一页');
                $('#alipay_active_btn').remove();
            }
        }
    }
    
    qry.resetTime = function() {
        clearInterval(myTimer);
        var hour   = 0;
        var minute = 0;
        var second = 0;
        $('#hour_show').html('<s id="h"></s>' + hour + '时');
        $('#minute_show').html('<s></s>' + minute + '分');
        $('#second_show').html('<s></s>' + second + '秒');
    }; 
    
    qry.showTime = function() {
        myTimer = window.setInterval(function () {
            var day = 0,
                hour = 0,
                minute = 0,
                second = 0;//时间默认值
            if (expireIn > 0) {
                day = Math.floor(expireIn / (60 * 60 * 24));
                hour = Math.floor(expireIn / (60 * 60)) - (day * 24);
                minute = Math.floor(expireIn / 60) - (day * 24 * 60) - (hour * 60);
                second = Math.floor(expireIn) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
            }
            if (minute <= 9) minute = '0' + minute;
            if (second <= 9) second = '0' + second;
            $('#hour_show').html('<s id="h"></s>' + hour + '时');
            $('#minute_show').html('<s></s>' + minute + '分');
            $('#second_show').html('<s></s>' + second + '秒');
            if (hour <= 0 && minute <= 0 && second <= 0) {
                clearInterval(myTimer);
                qry.disabled();
            }
            expireIn = expireIn - 1;
        }, 1000);
    }

    qry.jumpApp = function() {
        window.location.href = toUrl;
    }


$(function(){
	$("#show_qrcode").addClass("hide");
	jQuery('#show_img_wrap').qrcode({
        render: "canvas",
        text: qrcodeTxt,
        width: "200",               //二维码的宽度
        height: "200",              //二维码的高度
        background: "#ffffff",      //二维码的后景色
        foreground: "#000000",      //二维码的前景色
        //src: './js/logo.png'             //二维码中间的图片
   });


    //设置显示样式
    if(is_mobile=='1') {
       if(is_weixin==1) {
           
       }else if(is_weixin==0){
            if(istype==1){ //其他手机浏览器+微信支付
                
            }else if(istype==2){ //其他手机浏览器+支付宝支付
                if(!isAlipay){
                    var scan_tip="温馨提示 请按下面步骤：<br><span class='red'>1、请先截屏保存二维码到手机</span><br><span class='red'>2、打开支付宝App 扫一扫本地图片。</span>";
                    $(".iospayweixinbtn").html(scan_tip);
                }

                if(!isAlipay){
                    $('#alipay_active_btn').removeClass("hide");
                }else{
                   $("#not_alipay").addClass("hide")
                   $("#in_alipay").removeClass("hide")
                }
            }
       }
    }
    
    if(is_paying == 0){
        qry.showTime(); //开始付款倒计时;
        
        if (is_mobile == 1) {
            pay_way=2;
            if(isiOS)pay_way=3;
        }
        
        if(is_mobile && !isAlipay && isJump!=1){//移动端先跳到支付宝内部
            qry.jumpApp();
        }
        //qry.listener();
    }else if(is_paying == 1){
         qry.pay_success();
    }else if(is_paying == 2){
        qry.disabled();
    }
});
</script>
</div>

</body></html>