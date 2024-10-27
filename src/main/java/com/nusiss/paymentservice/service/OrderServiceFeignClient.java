package com.nusiss.paymentservice.service;

import com.nusiss.paymentservice.config.ApiResponse;
import com.nusiss.paymentservice.entity.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderServiceFeignClient {

    @GetMapping("/api/orders/{orderId}")
    Order getOrderById(@PathVariable("orderId") String orderId);

    @GetMapping("/order/inner/paySuccess")
    ApiResponse paySuccess(Long orderId);
}