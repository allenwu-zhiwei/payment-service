package com.nusiss.paymentservice.service;

import com.nusiss.paymentservice.dao.PaymentRepository;
import com.nusiss.paymentservice.entity.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ShouldCallRepository() {
        // Arrange
        Payment payment = new Payment();
        payment.setOrderId(123L);
        payment.setAmount(new BigDecimal("100.00"));
        payment.setPaymentStatus("TRADE_SUCCESS");

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // Act
        paymentService.save(payment);

        // Assert
        verify(paymentRepository).save(payment);
    }

    @Test
    void save_ShouldHandleLargeAmount() {
        // Arrange
        Payment payment = new Payment();
        payment.setOrderId(123L);
        payment.setAmount(new BigDecimal("999999999.99"));
        payment.setPaymentStatus("TRADE_SUCCESS");

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // Act
        paymentService.save(payment);

        // Assert
        verify(paymentRepository).save(payment);
    }

    @Test
    void save_ShouldHandleZeroAmount() {
        // Arrange
        Payment payment = new Payment();
        payment.setOrderId(123L);
        payment.setAmount(BigDecimal.ZERO);
        payment.setPaymentStatus("TRADE_SUCCESS");

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // Act
        paymentService.save(payment);

        // Assert
        verify(paymentRepository).save(payment);
    }
}