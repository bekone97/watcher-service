package com.miachyn.watcherservice.service;

import com.miachyn.watcherservice.dto.CurrencyFollowerDto;
import com.miachyn.watcherservice.dto.CurrencyFollowerDtoRequest;

import java.util.List;

public interface CurrencyFollowerService {
    void register(CurrencyFollowerDtoRequest currencyFollowerDtoRequest);

    List<CurrencyFollowerDto> findAllByCurrencyId(Long id);

    void deleteCurrencyFollower(CurrencyFollowerDtoRequest currencyFollowerDtoRequest);
}
