package cn.wolfcode.shop.seckill.controller;

import cn.wolfcode.shop.seckill.alipay.AlipayProperties;
import cn.wolfcode.shop.seckill.domain.OrderInfo;
import cn.wolfcode.shop.seckill.service.IOrderInfoService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by leslie
 */
@Controller
@RequestMapping("/pay")
public class PayController {
    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private AlipayProperties alipayProperties;
    @Autowired
    private IOrderInfoService orderInfoService;

    @RequestMapping("/payOrder")
    public void payOrder(String orderNo, HttpServletResponse resp) throws IOException, AlipayApiException {
        OrderInfo orderInfo = orderInfoService.findByOrderNo(orderNo);
        if (orderInfo == null) {
            resp.sendRedirect("http://localhost/5xx.html");//如果报错,就重定向到固定的错误页面
            return;
        }
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(alipayProperties.getReturnUrl());//同步回调地址
        alipayRequest.setNotifyUrl(alipayProperties.getNotifyUrl());//异步回调地址

        String out_trade_no = orderInfo.getOrderNo();
        //付款金额，必填
        String total_amount = orderInfo.getSeckillPrice() + "";
        //订单名称，必填
        String subject = "秒杀商品" + orderInfo.getGoodName();
        //商品描述，可空
        String body = orderInfo.getGoodName();

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        //输出
        out.println(result);
    }

    @RequestMapping("/notify_url")
    @ResponseBody
    public String notifyUrl(@RequestParam Map<String, String> params) throws AlipayApiException {
        //验签操作
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayProperties.getAlipayPublicKey(), alipayProperties.getCharset(), alipayProperties.getSignType());
        if (signVerified) {//验证成功
            //商户订单号
            String out_trade_no = params.get("out_trade_no");
            //处理幂等性问题
            //建立一张支付流水表,主键设置支付宝的流水号
            //修改订单状态
            orderInfoService.changePayStatus(out_trade_no, OrderInfo.STATUS_ACCOUNT_PAID);
            return "success";
        } else {
            return "fail";
        }
    }

    @RequestMapping("/return_url")
    @ResponseBody
    public String returnUrl(@RequestParam Map<String, String> params) throws AlipayApiException {
        //验签操作
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayProperties.getAlipayPublicKey(), alipayProperties.getCharset(), alipayProperties.getSignType());
        if (signVerified) {//验证成功
            //商户订单号
            String out_trade_no = params.get("out_trade_no");
            return "redirect:http://localhost/order_detail.html?orderNo=" + out_trade_no;
        } else {
            return "redirect:http://localhost/5xx.html";
        }
    }
}


/** 不同的地方有不同的回调，这里只是暂时这样写而已**/