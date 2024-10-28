package com.nusiss.paymentservice.service;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nusiss.paymentservice.dao.PaymentRepository;
import com.nusiss.paymentservice.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PaymentServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class PaymentServiceImplDiffblueTest {
    @MockBean
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentServiceImpl paymentServiceImpl;

    /**
     * Test {@link PaymentServiceImpl#save(Payment)}.
     * <p>
     * Method under test: {@link PaymentServiceImpl#save(Payment)}
     */
    @Test
    @DisplayName("Test save(Payment)")
    void testSave() {
        // Arrange
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal("2.3"));
        payment.setCreateDatetime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        payment.setCreateUser("Create User");
        payment.setId(1L);
        payment.setOrderId(1L);
        payment.setPaymentDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        payment.setPaymentStatus("Payment Status");
        payment.setUpdateDatetime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        payment.setUpdateUser("2020-03-01");
        when(paymentRepository.save(Mockito.<Payment>any())).thenReturn(payment);

        Payment payment2 = new Payment();
        payment2.setAmount(new BigDecimal("2.3"));
        payment2.setCreateDatetime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        payment2.setCreateUser("Create User");
        payment2.setId(1L);
        payment2.setOrderId(1L);
        payment2.setPaymentDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        payment2.setPaymentStatus("Payment Status");
        payment2.setUpdateDatetime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        payment2.setUpdateUser("2020-03-01");

        // Act
        paymentServiceImpl.save(payment2);

        // Assert that nothing has changed
        verify(paymentRepository).save(isA(Payment.class));
    }
}
