package com.miachyn.watcherservice.service;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.miachyn.watcherservice.dto.CurrencyDto;
import com.miachyn.watcherservice.dto.CurrencyDtoClientResponse;
import com.miachyn.watcherservice.dto.CurrencyDtoResponse;
import com.miachyn.watcherservice.entity.Currency;
import com.miachyn.watcherservice.exception.InconsistentDataException;
import com.miachyn.watcherservice.exception.ResourceNotFoundException;
import com.miachyn.watcherservice.mapper.CurrencyMapper;
import com.miachyn.watcherservice.scheduler.ScheduleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@ActiveProfiles("test")
class CurrencyServiceIntegrationTest {

    @MockBean
    private ScheduleService scheduleService;
    @Autowired
    private CurrencyService currencyService;


    Currency currency ;
    CurrencyDto currencyDto;
    CurrencyDtoResponse currencyDtoResponse;
    CurrencyDtoClientResponse currencyDtoClientResponse;

    @BeforeEach
    void setUp() {
        currency= Currency.builder()
                .id(90L)
                .price(BigDecimal.valueOf(27171.26))
                .symbol("BTC")
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
    @DataSet(value = {"dataset/init/currency/init.yaml"},
            useSequenceFiltering = false,
            executeScriptsAfter = {"scripts/cleanCurrency.sql"})
    @ExpectedDataSet(value =  {"dataset/expected/currency/save.yaml"})
    void save() {
        currency.setId(100L);
        currency.setSymbol("ETH");
        currency.setPrice(new BigDecimal("1806.77"));

        var actual = currencyService.save(CurrencyMapper.INSTANCE.convert(currency),
                new CurrencyDtoClientResponse(currency.getId(),currency.getSymbol(),currency.getPrice()));

    }

    @Test
    void saveFail() {
        currencyDtoClientResponse.setSymbol("asd");

        InconsistentDataException exception = assertThrows(InconsistentDataException.class,
                () -> currencyService.save(currencyDto, currencyDtoClientResponse));

        assertTrue(exception.getMessage().contains("is inconsistent for"));
    }

    @Test
    @DataSet(value = {"dataset/init/currency/init.yaml"},
            useSequenceFiltering = false,
            executeScriptsAfter = {"scripts/cleanCurrency.sql"})
    void getCurrencyPriceBySymbol() {
        var expected = currency.getPrice();

        var actual = currencyService.getCurrencyPriceBySymbol(currency.getSymbol());

        assertEquals(expected,actual);
    }
    @Test
    void getCurrencyPriceBySymbolFail() {
        Exception exception  =  assertThrows(ResourceNotFoundException.class,
                ()->currencyService.getCurrencyPriceBySymbol(currency.getSymbol()));

        assertTrue(exception.getMessage().contains("Currency wasn't found by symbol="+currency.getSymbol()));
    }

    @Test
    @DataSet(value = {"dataset/init/currency/init.yaml"},
            useSequenceFiltering = false,
            executeScriptsAfter = {"scripts/cleanCurrency.sql"})
    void getCurrencyBySymbol() {
        var expected = currencyDto;
        var actual = currencyService.getCurrencyBySymbol(currency.getSymbol());

        assertEquals(expected,actual);
    }

    @Test
    void getCurrencyBySymbolFail() {
        Exception exception  =  assertThrows(ResourceNotFoundException.class,
                ()->currencyService.getCurrencyBySymbol(currency.getSymbol()));

        assertTrue(exception.getMessage().contains("Currency wasn't found by symbol="+currency.getSymbol()));
    }
}