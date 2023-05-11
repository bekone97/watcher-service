package com.miachyn.watcherservice.client;

import com.miachyn.watcherservice.dto.CurrencyDtoClientResponse;

public interface CryptoApiClient {

    CurrencyDtoClientResponse getCurrencyDtoById(Long id);
}
