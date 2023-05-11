package com.miachyn.watcherservice.service;

import com.miachyn.watcherservice.dto.*;
import com.miachyn.watcherservice.entity.Currency;
import com.miachyn.watcherservice.entity.CurrencyFollower;
import com.miachyn.watcherservice.entity.User;
import com.miachyn.watcherservice.mapper.CurrencyFollowerMapper;
import com.miachyn.watcherservice.mapper.CurrencyMapper;
import com.miachyn.watcherservice.mapper.UserMapper;
import com.miachyn.watcherservice.repository.CurrencyFollowerRepository;
import com.miachyn.watcherservice.service.impl.CurrencyFollowerServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyFollowerServiceUnitTest {

    @Mock
    private CurrencyFollowerRepository currencyFollowerRepository;

    @Mock
    private UserService userService;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyFollowerServiceImpl currencyFollowerService;


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
                .username("miachyn")
                .build();
        userDto = UserMapper.INSTANCE.convert(user);

        currency= com.miachyn.watcherservice.entity.Currency.builder()
                .id(1L)
                .price(BigDecimal.valueOf(1912.23))
                .symbol("DEK")
                .build();

        currencyDto = CurrencyMapper.INSTANCE.convert(currency);

        currencyFollower = CurrencyFollower.builder()
                .currency(Currency.builder()
                        .id(1L)
                        .build())
                .user(User.builder()
                        .id(1L)
                        .build())
                .registrationPrice(currency.getPrice())
                .build();
        currencyFollowerDtoRequest = new CurrencyFollowerDtoRequest(user.getUsername(),currency.getSymbol());
        currencyFollowerDto = CurrencyFollowerMapper.INSTANCE.convert(currencyFollower);
        currencyFollowerDtoResponse = CurrencyFollowerMapper.INSTANCE.convertToResponse(currencyFollower);
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
    void register() {
        var expected = currencyFollowerDtoResponse;
        when(currencyService.getCurrencyBySymbol(currency.getSymbol())).thenReturn(currencyDto);
        when(userService.getOrCreate(user.getUsername())).thenReturn(userDto);
        when(currencyFollowerRepository.save(currencyFollower)).thenReturn(currencyFollower);

        var actual  = currencyFollowerService.register(currencyFollowerDtoRequest);

        assertEquals(expected,actual);
        verify(currencyService).getCurrencyBySymbol(currency.getSymbol());
        verify(userService).getOrCreate(user.getUsername());
        verify(currencyFollowerRepository).save(currencyFollower);
    }

    @Test
    void findAllByCurrencyId() {
        var expected  = List.of(currencyFollowerDto);
        when(currencyFollowerRepository.findAllByCurrency_Id(currency.getId())).thenReturn(List.of(currencyFollower));

        var actual  = currencyFollowerService.findAllByCurrencyId(currency.getId());

        assertEquals(expected,actual);
        verify(currencyFollowerRepository).findAllByCurrency_Id(currency.getId());
    }

    @Test
    void deleteCurrencyFollowerFirstCase() {
        when(userService.getByUsername(user.getUsername())).thenReturn(userDto);
        when(currencyService.getCurrencyBySymbol(currency.getSymbol())).thenReturn(currencyDto);
        when(currencyFollowerRepository.findByCurrency_IdAndUser_Id(currency.getId(),user.getId())).thenReturn(Optional.of(currencyFollower));

        currencyFollowerService.deleteCurrencyFollower(currencyFollowerDtoRequest);

        verify(userService).getByUsername(user.getUsername());
        verify(currencyService).getCurrencyBySymbol(currency.getSymbol());
        verify(currencyFollowerRepository).findByCurrency_IdAndUser_Id(currency.getId(),user.getId());
        verify(currencyFollowerRepository).delete(currencyFollower);
    }

    @Test
    void deleteCurrencyFollowerSecondCase() {
        when(userService.getByUsername(user.getUsername())).thenReturn(userDto);
        when(currencyService.getCurrencyBySymbol(currency.getSymbol())).thenReturn(currencyDto);
        when(currencyFollowerRepository.findByCurrency_IdAndUser_Id(currency.getId(),user.getId())).thenReturn(Optional.empty());

        currencyFollowerService.deleteCurrencyFollower(currencyFollowerDtoRequest);

        verify(userService).getByUsername(user.getUsername());
        verify(currencyService).getCurrencyBySymbol(currency.getSymbol());
        verify(currencyFollowerRepository).findByCurrency_IdAndUser_Id(currency.getId(),user.getId());
        verify(currencyFollowerRepository,never()).delete(currencyFollower);
    }
}