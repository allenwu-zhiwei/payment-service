package com.nusiss.paymentservice.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Order {
    private Long id;
    private String status;
    private BigDecimal totalPrice;
}