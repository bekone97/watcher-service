package com.miachyn.watcherservice.service.impl;

import com.miachyn.watcherservice.dto.*;
import com.miachyn.watcherservice.entity.CurrencyFollower;
import com.miachyn.watcherservice.mapper.CurrencyFollowerMapper;
import com.miachyn.watcherservice.repository.CurrencyFollowerRepository;
import com.miachyn.watcherservice.service.CurrencyFollowerService;
import com.miachyn.watcherservice.service.CurrencyService;
import com.miachyn.watcherservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CurrencyFollowerServiceImpl implements CurrencyFollowerService {

    private final UserService userService;
    private final CurrencyService currencyService;
    private final CurrencyFollowerRepository currencyFollowerRepository;

    @Override
    @Transactional
    public CurrencyFollowerDtoResponse register(CurrencyFollowerDtoRequest currencyFollowerDtoRequest) {
        log.info("Register new currencyFollower by : {}",currencyFollowerDtoRequest);
        CurrencyDto currencyDto = currencyService.getCurrencyBySymbol(currencyFollowerDtoRequest.getSymbol());
        UserDto userDto = userService.getOrCreate(currencyFollowerDtoRequest.getUsername());
        CurrencyFollower currencyFollower = currencyFollowerRepository.save(CurrencyFollowerMapper.INSTANCE.convert(currencyDto, userDto));
        return CurrencyFollowerMapper.INSTANCE.convertToResponse(currencyFollower);
    }

    @Override
    public List<CurrencyFollowerDto> findAllByCurrencyId(Long currencyId) {
        log.info("Find all currency followers by currency id : {}",currencyId);
        return currencyFollowerRepository.findAllByCurrency_Id(currencyId).stream()
                .map(CurrencyFollowerMapper.INSTANCE::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCurrencyFollower(CurrencyFollowerDtoRequest currencyFollowerDtoRequest) {
        log.info("Delete currency follower by : {}",currencyFollowerDtoRequest);
        UserDto user = userService.getByUsername(currencyFollowerDtoRequest.getUsername());
        CurrencyDto currency = currencyService.getCurrencyBySymbol(currencyFollowerDtoRequest.getSymbol());
        Optional<CurrencyFollower> currencyFollower = currencyFollowerRepository.findByCurrency_IdAndUser_Id(currency.getId(),
                user.getId());
        currencyFollower.ifPresent(currencyFollowerRepository::delete);

    }
}
