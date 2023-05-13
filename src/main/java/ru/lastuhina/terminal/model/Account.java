package ru.lastuhina.terminal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String cardNumber;

    private BigDecimal balance;

    private String pin;

    private Boolean isLocked;

    private Integer failedAttempt;

    private LocalDateTime lockTime;
}