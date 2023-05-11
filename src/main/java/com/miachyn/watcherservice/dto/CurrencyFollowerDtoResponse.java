package com.miachyn.watcherservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder

public class CurrencyFollowerDtoResponse {

    private String symbol;

    private String username;

    private BigDecimal registrationPrice;
}
