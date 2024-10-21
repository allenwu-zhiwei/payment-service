package com.nusiss.paymentservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "payment", schema = "nusmall_payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "payment_date")
    private Instant paymentDate;

    @NotNull
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @NotNull
    @Lob
    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @Size(max = 100)
    @NotNull
    @Column(name = "create_user", nullable = false, length = 100)
    private String createUser;

    @Size(max = 100)
    @Column(name = "update_user", length = 100)
    private String updateUser;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "create_datetime")
    private Instant createDatetime;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "update_datetime")
    private Instant updateDatetime;

}