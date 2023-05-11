package com.miachyn.watcherservice.service;

import com.miachyn.watcherservice.dto.CurrencyDto;
import com.miachyn.watcherservice.dto.CurrencyDtoClientResponse;
import com.miachyn.watcherservice.dto.CurrencyDtoResponse;
import com.miachyn.watcherservice.entity.Currency;
import com.miachyn.watcherservice.exception.InconsistentDataException;
import com.miachyn.watcherservice.exception.ResourceNotFoundException;
import com.miachyn.watcherservice.mapper.CurrencyMapper;
import com.miachyn.watcherservice.repository.CurrencyRepository;
import com.miachyn.watcherservice.service.impl.CurrencyServiceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceUnitTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyServiceImpl currencyService;


    Currency currency ;
    CurrencyDto currencyDto;
    CurrencyDtoResponse currencyDtoResponse;
    CurrencyDtoClientResponse currencyDtoClientResponse;

    @BeforeEach
    void setUp() {
        currency= Currency.builder()
                .id(1L)
                .price(BigDecimal.valueOf(1912.23))
                .symbol("DEK")
                .build();

        currencyDto = CurrencyMapper.INSTANCE.convert(currency);
        currencyDtoResponse = CurrencyMapper.INSTANCE.convert(currencyDto);
        currencyDtoClientResponse = CurrencyDtoClientResponse.builder()
                .id(currency.getId())
                .price(currency.getPrice())
                .symbol(currency.getSymbol())
                .build();
    }

    @AfterEach
    void tearDown() {
        currency=null;
        currencyDto=null;
        currencyDtoResponse=null;
        currencyDtoClientResponse=null;
    }

    @Test
    void save() {
        var expected =  currencyDto;
        when(currencyRepository.save(currency)).thenReturn(currency);

        var actual = currencyService.save(currencyDto, currencyDtoClientResponse);

        assertEquals(currencyDto,actual);
        verify(currencyRepository).save(currency);
    }

    @Test
    void saveFail() {
        currencyDtoClientResponse.setSymbol("asd");

        InconsistentDataException exception = assertThrows(InconsistentDataException.class,
                () -> currencyService.save(currencyDto, currencyDtoClientResponse));

        assertTrue(exception.getMessage().contains("is inconsistent for"));
        verify(currencyRepository,never()).save(currency);
    }

    @Test
    void getCurrencyPriceBySymbol() {
        var expected = currency.getPrice();
        when(currencyRepository.findBySymbol(currency.getSymbol())).thenReturn(Optional.of(currency));

        var actual = currencyService.getCurrencyPriceBySymbol(currency.getSymbol());

        assertEquals(expected,actual);
        verify(currencyRepository).findBySymbol(currency.getSymbol());
    }
    @Test
    void getCurrencyPriceBySymbolFail() {
        when(currencyRepository.findBySymbol(currency.getSymbol())).thenReturn(Optional.empty());

        Exception exception  =  assertThrows(ResourceNotFoundException.class,
                ()->currencyService.getCurrencyPriceBySymbol(currency.getSymbol()));

        assertTrue(exception.getMessage().contains("Currency wasn't found by symbol="+currency.getSymbol()));
        verify(currencyRepository).findBySymbol(currency.getSymbol());
    }

    @Test
    void getCurrencyBySymbol() {
        var expected = currencyDto;
        when(currencyRepository.findBySymbol(currency.getSymbol())).thenReturn(Optional.of(currency));

        var actual = currencyService.getCurrencyBySymbol(currency.getSymbol());

        assertEquals(expected,actual);
        verify(currencyRepository).findBySymbol(currency.getSymbol());
    }

    @Test
    void getCurrencyBySymbolFail() {
        when(currencyRepository.findBySymbol(currency.getSymbol())).thenReturn(Optional.empty());

        Exception exception  =  assertThrows(ResourceNotFoundException.class,
                ()->currencyService.getCurrencyBySymbol(currency.getSymbol()));

        assertTrue(exception.getMessage().contains("Currency wasn't found by symbol="+currency.getSymbol()));
        verify(currencyRepository).findBySymbol(currency.getSymbol());
    }
}