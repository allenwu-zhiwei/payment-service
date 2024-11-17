package com.nusiss.paymentservice.controller;

import com.alipay.api.AlipayApiException;
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

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AliPayControllerTest {

    @InjectMocks
    private AliPayController aliPayController;

    @Mock
    private AliPayConfig aliPayConfig;

    @Mock
    private PaymentService paymentService;

    @Mock
    private OrderServiceFeignClient orderServiceFeignClient;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter writer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configure AliPayConfig mock
        when(aliPayConfig.getAppId()).thenReturn("9021000141643317");
        when(aliPayConfig.getAppPrivateKey()).thenReturn("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC+7WzAsOmQ30OabBU3zdzdnYCt+I2UZ+Ml11iVSs68I9oLRx2XKl7Fc8MIxSHFnBJEkPiRPR/Mprb0wnGTFTl7CCFjVKc9sMViwTc3aEeJpe/V5RfNtIl1oPJhQ9wIpCkQv4Z/S335l2HZun1iFZka19rSiGT3fEE2gp0+KPqQ5CsfFv1M7Wk6gO/gsbt6rjBLedDDvcbAZwwYimsa83AA8ifQvzQqnR8uhznX66+zDYpecYRrNyZi2gSfiUjKg9/1oFM2cb5w4JOyUKVrTBquBx1xjB7ujYE+XxQnbauM4dJTa7hIj80a469KHYqIygQhP+Ao6yItZ36GM1677UXvAgMBAAECggEBAIyGW4EHtSE38p355zQHhQRoJInnYu1T+U+kzEI1qSu4h4e9c/4K5W4W4fnWfiDf2mI1AX6Eqp8KJabIZqeG+6OePFTLvbweG2mwJF/XlK+vPnMEBx0UQAgfycXlGFIT4VW+YdPXUIUf8pk6NYa3ttSXAyvpz8aH4cepIurZZnK09lTBAYtKw53QbHp2qyd7y+qSw4Pp8I/Fo6WjJfianQiMNBBkmxJXx+Umt8kmDn9kxBUD77zjjuwckAiAK8cSpGfCrkWumi01vqjeOCoq6s5KfrK+JTV02DgdrnGFR4x8xor44UCFB/LN3XNv3VECCzt89uXjWYWS5gpk3KvlrgECgYEA8uqu640hC3zAhyFsywjib4uNV2Y86imiiwndVFay4r5F20CQJ6J+PYIfYusaMrFJ7tKCnULQ9cmrWR3cvB6c9ZxRJRM9RD/uflzKeJDymv4ykccydW5PMXM3iUY6EtPBmiK50paYCvfVMhvgl/eIZSSTm/XNbnAJhmF0YQgCRq8CgYEAyTXq09gPtdEtBoxCuQv3VtNrPgmObKkpUtMSVPgBWDFHq6pwwWMEvJ+9QokNvOtp9eM6Acu3oAfqT9sBH8rMbKkikUDrv2YKKFzSfOJQud52n5fFOsBoW3xSPdgTFuChmApQ5qSfuCkDJsAosOw/PfvYDJCN0LvLPJ8NSdNoxMECgYBV+T+deJ6YbTfK28ageW2C4xZMN5AZpFC1+vDNt72qSIN63PfoPzeE13f6T5E/HcY382Ns+0fvpceyS8JjBkaxGvkRMST+8c2cBYVXdJ2Yw5zsQV8Xdsoqi0e9mxBUt4OO5Pl1kf0P7LUU/g7YWjB486AWk8F4c8i7OeSxrt73WwKBgBP4txNJ6d1JIuf4ehL1/hnvKEDjH/DnJYPkEXi/mRqQ4aVHaKO5WVoDnPyCsAtlboIKiNJuzw4iWv6MJWNrdLkAXL8AK6Y20aFJ/VaJ/CtGyo1yug/n0zPpdhxPWy21JDT8KSWLh5L29yocfq149ZoIalxs6LakPTsKKn5+bnLBAoGBAODUil73AdoZlpEtqIIBgY1wFDDfBqvYfVwbyHJKqbfol1eUrEsrBIouwU31efgXowm3BhH/36OUByJQWO1VXBLCG1AO+qHsCVO0ABQS9ewyeToCPFJzWPdGQAWwv7ClyB0+QiKaV7HZJSFRZC2I009nCL/kXfmrP6VQNX5F2qmB");
        when(aliPayConfig.getAlipayPublicKey()).thenReturn("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvu1swLDpkN9DmmwVN83c3Z2ArfiNlGfjJddYlUrOvCPaC0cdlypexXPDCMUhxZwSRJD4kT0fzKa29MJxkxU5ewghY1SnPbDFYsE3N2hHiaXv1eUXzbSJdaDyYUPcCKQpEL+Gf0t9+Zdh2bp9YhWZGtfa0ohk93xBNoKdPij6kOQrHxb9TO1pOoDv4LG7eq4wS3nQw73GwGcMGIprGvNwAPIn0L80Kp0fLoc51+uvsw2KXnGEazcmYtoEn4lIyoPf9aBTNnG+cOCTslCla0wargcdcYwe7o2BPl8UJ22rjOHSU2u4SI/NGuOvSh2KiMoEIT/gKOsiLWd+hjNeu+1F7wIDAQAB");
        when(aliPayConfig.getReturnUrl()).thenReturn("http://47.128.247.254/api/home");
        when(aliPayConfig.getNotifyUrl()).thenReturn("http://167.71.195.130:8085/api/payment/notify");
    }

    @Test
    void pay_ShouldGeneratePaymentForm() throws Exception {
        // Arrange
        String orderId = "123";
        Order mockOrder = new Order();
        mockOrder.setTotalPrice(new BigDecimal("100.00"));

        when(orderServiceFeignClient.getOrderById(orderId)).thenReturn(mockOrder);
        when(response.getWriter()).thenReturn(writer);

        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        // Act
        aliPayController.pay(orderId, mockResponse);

        // Assert
        verify(orderServiceFeignClient).getOrderById(orderId);
        assertTrue(mockResponse.getContentAsString().contains("form"));
        assertEquals("text/html;charset=UTF-8", mockResponse.getContentType());
    }

    @Test
    void pay_ShouldThrowException_WhenOrderNotFound() {
        // Arrange
        String orderId = "nonexistent";
        when(orderServiceFeignClient.getOrderById(orderId)).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            aliPayController.pay(orderId, response);
        });
    }

    @Test
    void payNotify_ShouldReturnSuccess_WhenValidPayment() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("trade_status", "TRADE_SUCCESS");
        request.setParameter("out_trade_no", "123");
        request.setParameter("total_amount", "100.00");
        request.setParameter("sign", "dummy_sign");
        request.setParameter("sign_type", "RSA2");

        // Act
        String result = aliPayController.payNotify(request);

        // Assert
        verify(paymentService).save(any(Payment.class));
        verify(orderServiceFeignClient).paySuccess(any(Long.class));
        assertEquals("success", result);
    }

    @Test
    void payNotify_ShouldReturnFail_WhenInvalidTradeStatus() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("trade_status", "TRADE_FAILED");

        // Act
        String result = aliPayController.payNotify(request);

        // Assert
        assertEquals("fail", result);
        verify(paymentService, never()).save(any(Payment.class));
        verify(orderServiceFeignClient, never()).paySuccess(any(Long.class));
    }
}

