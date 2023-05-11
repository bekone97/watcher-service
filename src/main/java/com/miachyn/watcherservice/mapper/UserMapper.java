package com.miachyn.watcherservice.mapper;

import com.miachyn.watcherservice.dto.UserDto;
import com.miachyn.watcherservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto convert(User user);
}
