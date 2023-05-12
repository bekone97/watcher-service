package com.miachyn.watcherservice.service;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.miachyn.watcherservice.dto.*;
import com.miachyn.watcherservice.entity.Currency;
import com.miachyn.watcherservice.entity.CurrencyFollower;
import com.miachyn.watcherservice.entity.User;
import com.miachyn.watcherservice.mapper.CurrencyFollowerMapper;
import com.miachyn.watcherservice.mapper.CurrencyMapper;
import com.miachyn.watcherservice.mapper.UserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@ActiveProfiles("test")
class CurrencyFollowerServiceIntegrationTest {

    @Autowired
    private CurrencyFollowerService currencyFollowerService;


        CurrencyFollower currencyFollower ;
        CurrencyDto currencyDto;
        Currency currency;
        User user;
        UserDto userDto;
        CurrencyFollowerDtoRequest currencyFollowerDtoRequest;
        CurrencyFollowerDto currencyFollowerDto;
        CurrencyFollowerDtoResponse currencyFollowerDtoResponse;
    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("Artem")
                .build();
        userDto = UserMapper.INSTANCE.convert(user);

        currency= Currency.builder()
                .id(90L)
                .price(BigDecimal.valueOf(27171.26))
                .symbol("BTC")
                .build();

        currencyDto = CurrencyMapper.INSTANCE.convert(currency);

        currencyFollower = CurrencyFollower.builder()
                .currency(Currency.builder()
                        .id(90L)
                        .build())
                .user(User.builder()
                        .id(1L)
                        .username("Artem")
                        .build())
                .registrationPrice(currency.getPrice())
                .build();
        currencyFollowerDtoRequest = new CurrencyFollowerDtoRequest(user.getUsername(),currency.getSymbol());
        currencyFollowerDto = CurrencyFollowerMapper.INSTANCE.convert(currencyFollower);
        currencyFollowerDtoResponse = new CurrencyFollowerDtoResponse("BTC","Artem",BigDecimal.valueOf(27171.26));
    }

    @AfterEach
    void tearDown() {
        currencyFollower=null ;
        currencyDto= null;
        currency=null;
        user=null;
        userDto=null;
        currencyFollowerDtoRequest=null;
    }

    @Test
    @DataSet(value = {"dataset/init/currencyUser/init.yaml","dataset/init/currency/init.yaml"},
            useSequenceFiltering = false,
            executeScriptsAfter = {"scripts/cleanCurrencyFollower.sql"})
    @ExpectedDataSet(value = {"dataset/expected/currencyFollower/register.yaml"})
    void register() {
        currencyFollowerService.register(currencyFollowerDtoRequest);
    }

    @Test
    @DataSet(value = {"dataset/init/currencyUser/init.yaml","dataset/init/currency/init.yaml","dataset/init/currencyFollower/init.yaml"},
            useSequenceFiltering = false,
            executeScriptsAfter = {"scripts/cleanCurrencyFollower.sql"})
    void findAllByCurrencyId() {
        var expected  = List.of(currencyFollowerDto);
        var actual  = currencyFollowerService.findAllByCurrencyId(currency.getId());

        assertEquals(expected,actual);
    }
//
    @Test
    @DataSet(value = {"dataset/init/currencyUser/init.yaml","dataset/init/currency/init.yaml","dataset/init/currencyFollower/init.yaml"},
            useSequenceFiltering = false,
            executeScriptsAfter = {"scripts/cleanCurrencyFollower.sql"})
    @ExpectedDataSet(value = {"dataset/expected/currencyFollower/cancel.yaml"})
    void deleteCurrencyFollowerFirstCase() {

        currencyFollowerService.deleteCurrencyFollower(currencyFollowerDtoRequest);

    }
}