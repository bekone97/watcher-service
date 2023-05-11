package com.miachyn.watcherservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyFollowerDto {
    private BigDecimal registrationPrice;
    private String username;
    private Long currencyId;
}
