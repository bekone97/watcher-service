package com.miachyn.watcherservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miachyn.watcherservice.dto.CurrencyFollowerDtoRequest;
import com.miachyn.watcherservice.dto.CurrencyFollowerDtoResponse;
import com.miachyn.watcherservice.service.CurrencyFollowerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CurrencyFollowerController.class)
class CurrencyFollowerControllerTest {

    public static final String APPLICATION_JSON = "application/json";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CurrencyFollowerService currencyFollowerService;

    CurrencyFollowerDtoResponse currencyFollowerDtoResponse;
    CurrencyFollowerDtoRequest currencyFollowerDtoRequest;

    @BeforeEach
    void setUp() {
        currencyFollowerDtoRequest = new CurrencyFollowerDtoRequest("artem","ABC");
        currencyFollowerDtoResponse = new CurrencyFollowerDtoResponse("ABC","artem",BigDecimal.valueOf(123.22));
    }

    @AfterEach
    void tearDown() {
        currencyFollowerDtoRequest= null;
        currencyFollowerDtoResponse=null;
    }

    @Test
    void testNotify() throws Exception {
        var expected = currencyFollowerDtoResponse;
        when(currencyFollowerService.saveCurrencyFollower(currencyFollowerDtoRequest)).thenReturn(expected);

        var actual = mockMvc.perform(post("/notify")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currencyFollowerDtoRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected),actual);
        verify(currencyFollowerService).saveCurrencyFollower(currencyFollowerDtoRequest);
    }

    @Test
    void testNotifyFail() throws Exception {
        var expectedMessage = "{username.validation.size.min}";
        currencyFollowerDtoRequest.setUsername("ar");

      mockMvc.perform(post("/notify")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currencyFollowerDtoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(expectedMessage)));

        verify(currencyFollowerService,never()).saveCurrencyFollower(currencyFollowerDtoRequest);
    }

    @Test
    void cancelNotification() throws Exception{
        var actual = mockMvc.perform(delete("/notify")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currencyFollowerDtoRequest)))
                .andExpect(status().isNoContent());

        verify(currencyFollowerService).deleteCurrencyFollower(currencyFollowerDtoRequest);
    }

    @Test
    void cancelNotificationFail() throws Exception{
        var expectedMessage = "{symbol.validation.notBlank}";
        currencyFollowerDtoRequest.setSymbol("");

        mockMvc.perform(delete("/notify")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currencyFollowerDtoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(expectedMessage)));


        verify(currencyFollowerService,never()).deleteCurrencyFollower(currencyFollowerDtoRequest);
    }
}