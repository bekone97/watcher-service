package com.miachyn.watcherservice.service.impl;

import com.miachyn.watcherservice.dto.UserDto;
import com.miachyn.watcherservice.entity.User;
import com.miachyn.watcherservice.exception.ResourceNotFoundException;
import com.miachyn.watcherservice.mapper.UserMapper;
import com.miachyn.watcherservice.repository.UserRepository;
import com.miachyn.watcherservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository ;

    @Override
    @Transactional
    public UserDto getOrCreate(String username) {
        log.debug("Get or Create user with username : {}",username);
        return userRepository.findByUsername(username)
                .map(UserMapper.INSTANCE::convert)
                .orElseGet(()->UserMapper.INSTANCE.convert(userRepository.save(User.builder()
                        .username(username)
                        .build())));
    }

    @Override
    public UserDto getByUsername(String username) {
        log.debug("Find user by username : {}",username);
        return userRepository.findByUsername(username)
                .map(UserMapper.INSTANCE::convert)
                .orElseThrow(()->new ResourceNotFoundException(User.class,"username",username));
    }

}
