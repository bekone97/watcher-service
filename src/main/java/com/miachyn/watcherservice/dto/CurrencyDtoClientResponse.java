package com.miachyn.watcherservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDtoClientResponse {

    private Long id;

    private String symbol;

    @JsonProperty("price_usd")
    private BigDecimal price;
}
