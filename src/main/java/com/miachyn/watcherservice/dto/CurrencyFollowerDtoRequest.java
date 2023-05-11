package com.miachyn.watcherservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyFollowerDtoRequest {

    @Size(min = 4, message = "${username.validation.size.min}")
    @Size(max = 100, message = "${username.validation.size.max}")
    @NotBlank(message = "${username.validation.notBlank}")
    private String username;

    @Size(min = 3,  message = "${symbol.validation.size}")
    @NotBlank(message = "${symbol.validation.notBlank}")
    private String symbol;
}
