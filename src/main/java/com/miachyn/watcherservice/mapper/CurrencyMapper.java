package com.miachyn.watcherservice.mapper;

import com.miachyn.watcherservice.dto.CurrencyDto;
import com.miachyn.watcherservice.dto.CurrencyDtoClientResponse;
import com.miachyn.watcherservice.dto.CurrencyDtoResponse;
import com.miachyn.watcherservice.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    @Mappings({
            @Mapping(source = "currency.id",target = "id"),
            @Mapping(source = "currency.symbol",target = "symbol"),
            @Mapping(source = "currency.price",target = "price")
    })
    Currency convert(CurrencyDtoClientResponse currency);


    CurrencyDto convert(Currency currency);

    CurrencyDtoResponse convert(CurrencyDto currencyDto);

}
