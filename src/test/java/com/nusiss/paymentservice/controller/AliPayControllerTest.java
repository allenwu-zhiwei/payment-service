package com.nusiss.paymentservice.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.nusiss.paymentservice.config.AliPayConfig;
import com.nusiss.paymentservice.entity.Order;
import com.nusiss.paymentservice.entity.Payment;
import com.nusiss.paymentservice.service.OrderServiceFeignClient;
import com.nusiss.paymentservice.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AliPayControllerTest {

    @Mock
    private AliPayConfig aliPayConfig;

    @Mock
    private PaymentService paymentService;

    @Mock
    private OrderServiceFeignClient orderServiceFeignClient;

    @Mock
    private AlipayClient alipayClient;

    @InjectMocks
    private AliPayController aliPayController;

    private MockHttpServletResponse response;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();

        // Configure AliPayConfig mock
        when(aliPayConfig.getAppId()).thenReturn("test-app-id");
        when(aliPayConfig.getAppPrivateKey()).thenReturn("test-private-key");
        when(aliPayConfig.getAlipayPublicKey()).thenReturn("test-public-key");
        when(aliPayConfig.getNotifyUrl()).thenReturn("http://test.com/notify");
        when(aliPayConfig.getReturnUrl()).thenReturn("http://test.com/return");
    }



    @Test
    void pay_ShouldThrowException_WhenOrderNotFound() {
        // Arrange
        String orderId = "123";
        when(orderServiceFeignClient.getOrderById(orderId)).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            aliPayController.pay(orderId, response);
        });
    }



    @Test
    void payNotify_ShouldReturnFail_WhenTradeStatusNotSuccess() throws Exception {
        // Arrange
        request.setParameter("trade_status", "TRADE_FAILED");

        // Act
        String result = aliPayController.payNotify(request);

        // Assert
        assertEquals("fail", result);
        verify(paymentService, never()).save(any(Payment.class));
        verify(orderServiceFeignClient, never()).paySuccess(any());
    }
}