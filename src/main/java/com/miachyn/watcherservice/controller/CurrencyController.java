package com.miachyn.watcherservice.controller;

import com.miachyn.watcherservice.dto.CurrencyDtoResponse;
import com.miachyn.watcherservice.service.CurrencyService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CurrencyDtoResponse> getCurrencies(){
        log.info("Get all currencies");
        return currencyService.getCurrencies();
    }

    @GetMapping("/{symbol}/price")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getCurrencyPrice(@PathVariable @NotBlank(message = "${symbol.validation.notBlank}")
                                                   @Size(min = 3, message = "${symbol.validation.size}") String symbol){
        log.info("Get price of currency by symbol :{}",symbol);
        return currencyService.getCurrencyPriceBySymbol(symbol);
    }

}
