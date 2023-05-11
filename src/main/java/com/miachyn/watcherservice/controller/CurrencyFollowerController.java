package com.miachyn.watcherservice.controller;

import com.miachyn.watcherservice.dto.CurrencyFollowerDto;
import com.miachyn.watcherservice.dto.CurrencyFollowerDtoRequest;
import com.miachyn.watcherservice.dto.CurrencyFollowerDtoResponse;
import com.miachyn.watcherservice.service.CurrencyFollowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class CurrencyFollowerController {

    private final CurrencyFollowerService currencyFollowerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CurrencyFollowerDtoResponse notify(@Valid @RequestBody CurrencyFollowerDtoRequest currencyFollowerDtoRequest){
        return currencyFollowerService.register(currencyFollowerDtoRequest);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelNotification(@Valid @RequestBody CurrencyFollowerDtoRequest currencyFollowerDtoRequest){
        currencyFollowerService.deleteCurrencyFollower(currencyFollowerDtoRequest);
    }
}
