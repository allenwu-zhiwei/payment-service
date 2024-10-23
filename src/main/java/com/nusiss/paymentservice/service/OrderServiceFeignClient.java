package com.nusiss.paymentservice.service;

import com.nusiss.paymentservice.entity.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Feign 客户端，指向 order-service 微服务
@FeignClient(name = "order-service")
public interface OrderServiceFeignClient {

    // 调用 order-service 的获取订单 API
    @GetMapping("/api/orders/{orderId}")
    Order getOrderById(@PathVariable("orderId") String orderId);
}