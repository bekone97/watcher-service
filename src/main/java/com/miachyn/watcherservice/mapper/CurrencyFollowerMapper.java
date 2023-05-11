package com.miachyn.watcherservice.mapper;

import com.miachyn.watcherservice.dto.CurrencyDto;
import com.miachyn.watcherservice.dto.CurrencyFollowerDto;
import com.miachyn.watcherservice.dto.UserDto;
import com.miachyn.watcherservice.entity.Currency;
import com.miachyn.watcherservice.entity.CurrencyFollower;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyFollowerMapper {
    CurrencyFollowerMapper INSTANCE = Mappers.getMapper(CurrencyFollowerMapper.class);

    @Mappings(
            {
                    @Mapping(source = "currencyDto.id",target = "currency.id"),
                    @Mapping(source = "userDto.id",target = "user.id"),
                    @Mapping(source = "currencyDto.price",target = "registrationPrice")
            }
    )
    CurrencyFollower convert(CurrencyDto currencyDto, UserDto userDto);

    @Mappings({
            @Mapping(source = "currencyFollower.user.username",target = "username"),
            @Mapping(source = "currencyFollower.registrationPrice",target = "registrationPrice"),
            @Mapping(source = "currencyFollower.currency.id",target = "currencyId")
    })
    CurrencyFollowerDto convert(CurrencyFollower currencyFollower);
}
