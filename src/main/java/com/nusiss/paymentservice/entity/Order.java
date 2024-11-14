package com.nusiss.paymentservice.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order {
    private Long id;

    private BigDecimal totalPrice;

    private Long orderId;

    private Date orderDate;

    private Object status;

    private String createUser;

    private String updateUser;

    private Date createDatetime;

    private Date updateDatetime;
}