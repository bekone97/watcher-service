package com.miachyn.watcherservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miachyn.watcherservice.dto.CurrencyDtoResponse;
import com.miachyn.watcherservice.entity.Currency;
import com.miachyn.watcherservice.exception.ResourceNotFoundException;
import com.miachyn.watcherservice.service.CurrencyService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CurrencyController.class)
class CurrencyControllerTest {
    public static final String APPLICATION_JSON = "application/json";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CurrencyService currencyService;

    CurrencyDtoResponse currencyDtoResponse;
    String symbol;
    BigDecimal price ;
    @BeforeEach
    void setUp() {
        symbol = "ABC";
        price= BigDecimal.valueOf(1231.23);
        currencyDtoResponse=new CurrencyDtoResponse(12L,symbol);
    }

    @AfterEach
    void tearDown() {
        currencyDtoResponse=null;
        symbol=null;
        price=null;
    }

    @Test
    void getCurrencies() throws Exception {
        var expected = Collections.singletonList(currencyDtoResponse);
        when(currencyService.getCurrencies()).thenReturn(expected);

        var actual = mockMvc.perform(get("/currencies")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected),actual);
        verify(currencyService).getCurrencies();
    }

    @Test
    void getCurrencyPrice() throws Exception {
        var expected = price;
        when(currencyService.getCurrencyPriceBySymbol(symbol)).thenReturn(price);

        var actual = mockMvc.perform(
                get("/currencies/{symbol}/price",symbol)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(price),actual);
        verify(currencyService).getCurrencyPriceBySymbol(symbol);
    }

    @Test
    void getCurrencyPriceFailFirstCase() throws Exception {
        when(currencyService.getCurrencyPriceBySymbol(symbol)).thenReturn(price);

        mockMvc.perform(
                        get("/currencies/{symbol}/price","AC")
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains("${symbol.validation.size}")));

        verify(currencyService,never()).getCurrencyPriceBySymbol(symbol);
    }
    @Test
    void getCurrencyPriceFailSecondCase() throws Exception {
        var expectedMessage = "Currency wasn't found by symbol";
        when(currencyService.getCurrencyPriceBySymbol(symbol)).thenThrow(new ResourceNotFoundException(Currency.class,"symbol",symbol));

        var actual = mockMvc.perform(
                        get("/currencies/{symbol}/price",symbol)
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(expectedMessage)));

        verify(currencyService).getCurrencyPriceBySymbol(symbol);
    }

}