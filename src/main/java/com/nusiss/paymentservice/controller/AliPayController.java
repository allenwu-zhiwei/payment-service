package com.nusiss.paymentservice.controller;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.nusiss.paymentservice.config.AliPayConfig;
import com.nusiss.paymentservice.entity.Order;
import com.nusiss.paymentservice.entity.Payment;
import com.nusiss.paymentservice.service.OrderServiceFeignClient;
import com.nusiss.paymentservice.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

// https://natapp.cn 本地测试时可使用该内网穿透工具，将本地服务映射到公网，然后支付宝才能访问到我们的接口
@RestController
@RequestMapping("/api/payment")
public class AliPayController {

    // 支付宝沙箱网关地址
    private static final String GATEWAY_URL = "https://openapi.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    // 签名方式
    private static final String SIGN_TYPE = "RSA2";

    @Autowired
    private AliPayConfig aliPayConfig;

    @Autowired
    private PaymentService paymentService;

    @Resource
    private OrderServiceFeignClient orderServiceFeignClient;

    @GetMapping("/pay")
    @Operation(summary = "pay")
    public String pay(String orderId, HttpServletResponse httpResponse) throws Exception {
        // 调用 order-service 获取订单信息
        Order order = orderServiceFeignClient.getOrderById(orderId);

        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 构造支付请求
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());

        JSONObject bizContent = new JSONObject();
        bizContent.set("orderId", order.getId());  // 使用获取到的订单信息
        bizContent.set("totalPrice", order.getTotalPrice().toString());
        bizContent.set("subject", order.getId());   // 支付的名称(由于本项目中没有商品名称，所以使用订单号代替)
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");  // 固定配置
//        bizContent.set("orderId", orderId);  // 使用获取到的订单信息
//        bizContent.set("totalPrice", "0.01");

        request.setBizContent(bizContent.toString());
        request.setReturnUrl(aliPayConfig.getReturnUrl());

        // 生成并返回表单
        String form = alipayClient.pageExecute(request).getBody();
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
        return "123";
    }

    @PostMapping("/notify")
    @Operation(summary = "payNotify")
    public String payNotify(HttpServletRequest request) throws Exception {
        if ("TRADE_SUCCESS".equals(request.getParameter("trade_status"))) {
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }

            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, aliPayConfig.getAlipayPublicKey(), CHARSET);

            if (checkSignature) {
                // 验签通过，保存支付信息
                Payment payment = new Payment();
                payment.setOrderId(Long.valueOf(params.get("out_trade_no")));
                payment.setAmount(new BigDecimal(params.get("total_amount")));
                payment.setPaymentStatus(params.get("trade_status"));
                payment.setCreateUser("Alipay");
                payment.setPaymentDate(Instant.now());

                // 保存 Payment 实体
                paymentService.save(payment);

                // 通知 OrderService 支付完成
                orderServiceFeignClient.paySuccess(payment.getOrderId());

                return "success";
            }
        }
        return "fail";
    }
}