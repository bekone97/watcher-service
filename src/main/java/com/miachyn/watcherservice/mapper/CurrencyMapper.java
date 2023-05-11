package com.miachyn.watcherservice.mapper;

import com.miachyn.watcherservice.dto.CurrencyDto;
import com.miachyn.watcherservice.dto.CurrencyDtoClientResponse;
import com.miachyn.watcherservice.dto.CurrencyDtoResponse;
import com.miachyn.watcherservice.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    @Mappings({
            @Mapping(source = "currency.id",target = "id"),
            @Mapping(source = "currency.symbol",target = "symbol"),
            @Mapping(source = "currency.price",target = "price"),
            @Mapping(source = "registrationTime",target = "registrationTime")
    })
    Currency convert(CurrencyDtoClientResponse currency, LocalDateTime registrationTime);


    CurrencyDto convert(Currency currency);

    CurrencyDtoResponse convert(CurrencyDto currencyDto);

    default LocalDateTime map(Timestamp timestamp){
        return timestamp == null ? null :
                timestamp.toLocalDateTime();
    }

    default Timestamp map(LocalDateTime localDateTime){
        return localDateTime == null ? null :
                Timestamp.valueOf(localDateTime);
    }
}
