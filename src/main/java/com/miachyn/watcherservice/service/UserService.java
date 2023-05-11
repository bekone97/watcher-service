package com.miachyn.watcherservice.service;

import com.miachyn.watcherservice.dto.UserDto;

public interface UserService {
    UserDto getOrCreate(String username);

    UserDto getByUsername(String username);
}
