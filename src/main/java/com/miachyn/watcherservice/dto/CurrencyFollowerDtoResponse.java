package com.miachyn.watcherservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyFollowerDtoResponse {

    private String symbol;

    private String username;

    private BigDecimal registrationPrice;
}
