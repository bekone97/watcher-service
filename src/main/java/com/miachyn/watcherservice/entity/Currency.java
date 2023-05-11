package com.miachyn.watcherservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Currency {
    @Id
    private Long id;

    @Column
    private String symbol;

    @Column
    private BigDecimal price;

    @Column(name = "registration_time")
    private Timestamp registrationTime;
}
