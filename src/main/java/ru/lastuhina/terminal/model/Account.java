package ru.lastuhina.terminal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Account {

    @Id
    private Long id;

    @Column(unique = true)
    private String cardNumber;

    private BigDecimal balance;

    private String pin;

    private Boolean isLocked;

    private Integer failedAttempt;

    private Date lockTime;
}