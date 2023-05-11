package com.miachyn.watcherservice.service;

import com.miachyn.watcherservice.dto.CurrencyDto;
import com.miachyn.watcherservice.dto.CurrencyDtoClientResponse;
import com.miachyn.watcherservice.dto.CurrencyDtoResponse;

import java.math.BigDecimal;
import java.util.List;

public interface CurrencyService {
    List<CurrencyDtoResponse> getCurrencies();

    CurrencyDto save(CurrencyDto currency, CurrencyDtoClientResponse currencyDtoClientResponse);

    BigDecimal getCurrencyPriceBySymbol(String symbol);

    CurrencyDto getCurrencyBySymbol(String symbol);
}
