package com.miachyn.watcherservice.scheduler;

import com.miachyn.watcherservice.client.CryptoApiClient;
import com.miachyn.watcherservice.dto.CurrencyDto;
import com.miachyn.watcherservice.dto.CurrencyDtoClientResponse;
import com.miachyn.watcherservice.service.CurrencyFollowerService;
import com.miachyn.watcherservice.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final List<CurrencyDto> currencies;
    private final CurrencyService currencyService;
    private final CryptoApiClient cryptoApiClient;
    private final CurrencyFollowerService currencyFollowerService;


    @Scheduled(fixedDelayString = "${schedule.work}")
    public void updatePriceOfCurrency() {
        currencies.stream()
                .map(currency -> {
                    CurrencyDtoClientResponse currencyDtoClientResponse = cryptoApiClient.getCurrencyDtoById(currency.getId());
                    return currencyService.save(currency, currencyDtoClientResponse);
                })
                .forEach(this::checkCurrencyFollowers);

    }

    private void checkCurrencyFollowers(CurrencyDto currencyDto) {
                currencyFollowerService.findAllByCurrencyId(currencyDto.getId())
                        .forEach(currencyFollower -> {
                            BigDecimal percentage =
                                    calculatePriceChangePercentage(currencyFollower.getRegistrationPrice(), currencyDto.getPrice());
                            if (percentage.abs().compareTo(BigDecimal.ONE) > 0)
                                log.warn("The currency with symbol : {} changed by : {} for user : {}",
                                        currencyDto.getSymbol(), percentage, currencyFollower.getUsername());
                        });
    }


    private BigDecimal calculatePriceChangePercentage(BigDecimal registeredPrice, BigDecimal actualPrice) {
        return actualPrice.subtract(registeredPrice)
                .divide(registeredPrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

    }
}
