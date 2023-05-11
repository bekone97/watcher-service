package com.miachyn.watcherservice.service.impl;

import com.miachyn.watcherservice.dto.CurrencyDto;
import com.miachyn.watcherservice.dto.CurrencyDtoClientResponse;
import com.miachyn.watcherservice.dto.CurrencyDtoResponse;
import com.miachyn.watcherservice.entity.Currency;
import com.miachyn.watcherservice.exception.InconsistentDataException;
import com.miachyn.watcherservice.exception.ResourceNotFoundException;
import com.miachyn.watcherservice.mapper.CurrencyMapper;
import com.miachyn.watcherservice.repository.CurrencyRepository;
import com.miachyn.watcherservice.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final List<CurrencyDto> currencies;
    @Override
    public List<CurrencyDtoResponse> getCurrencies() {
        return currencies.stream()
                .map(CurrencyMapper.INSTANCE::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CurrencyDto save(CurrencyDto currencyDto, CurrencyDtoClientResponse currencyDtoClientResponse) {
        log.debug("Save currency by : {} and {}",currencyDto, currencyDtoClientResponse);
        if (currencyDto.getId().equals(currencyDtoClientResponse.getId())
                && currencyDto.getSymbol().equals(currencyDtoClientResponse.getSymbol())){
            Currency currency = currencyRepository.save(CurrencyMapper.INSTANCE.convert(currencyDtoClientResponse, LocalDateTime.now()));
            return CurrencyMapper.INSTANCE.convert(currency);
        }
        throw new InconsistentDataException(currencyDto, currencyDtoClientResponse);
    }

    @Override
    public BigDecimal getCurrencyPriceBySymbol(String symbol) {
        log.info("Get price of currency by symbol : {}",symbol);
        return currencyRepository.findBySymbol(symbol)
                .map(Currency::getPrice)
                .orElseThrow(() -> new ResourceNotFoundException(Currency.class, "symbol", symbol));
    }

    @Override
    public CurrencyDto getCurrencyBySymbol(String symbol) {
        log.info("Get currency by symbol : {}",symbol);
        return currencyRepository.findBySymbol(symbol)
                .map(CurrencyMapper.INSTANCE::convert)
                .orElseThrow(()->new ResourceNotFoundException(Currency.class, "symbol", symbol));
    }

}
