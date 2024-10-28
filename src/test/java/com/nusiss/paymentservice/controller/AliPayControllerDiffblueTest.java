package com.nusiss.paymentservice.controller;

import static org.mockito.Mockito.when;

import com.nusiss.paymentservice.config.AliPayConfig;
import com.nusiss.paymentservice.entity.Order;
import com.nusiss.paymentservice.service.OrderServiceFeignClient;
import com.nusiss.paymentservice.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AliPayController.class, AliPayConfig.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AliPayControllerDiffblueTest {
    @Autowired
    private AliPayConfig aliPayConfig;

    @Autowired
    private AliPayController aliPayController;

    @MockBean
    private OrderServiceFeignClient orderServiceFeignClient;

    @MockBean
    private PaymentService paymentService;

    /**
     * Test {@link AliPayController#pay(String, HttpServletResponse)}.
     * <p>
     * Method under test: {@link AliPayController#pay(String, HttpServletResponse)}
     */
    @Test
    @DisplayName("Test pay(String, HttpServletResponse)")
    void testPay() throws Exception {
        // Arrange
        Order order = new Order();
        order.setCreateDatetime(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setCreateUser("Create User");
        order.setId(1L);
        order.setOrderDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setOrderId(1L);
        order.setStatus("Status");
        order.setTotalPrice(new BigDecimal("2.3"));
        order.setUpdateDatetime(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setUpdateUser("2020-03-01");
        when(orderServiceFeignClient.getOrderById(Mockito.<String>any())).thenReturn(order);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/payment/pay")
                .param("orderId", null);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(aliPayController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Test {@link AliPayController#payNotify(HttpServletRequest)}.
     * <p>
     * Method under test: {@link AliPayController#payNotify(HttpServletRequest)}
     */
    @Test
    @DisplayName("Test payNotify(HttpServletRequest)")
    void testPayNotify() throws Exception {
        // Arrange
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/payment/notify");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(aliPayController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("fail"));
    }
}
