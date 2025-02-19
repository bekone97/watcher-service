package com.miachyn.watcherservice.service;

import com.miachyn.watcherservice.dto.UserDto;
import com.miachyn.watcherservice.entity.User;
import com.miachyn.watcherservice.exception.ResourceNotFoundException;
import com.miachyn.watcherservice.mapper.UserMapper;
import com.miachyn.watcherservice.repository.UserRepository;
import com.miachyn.watcherservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    User user;
    UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("miachyn")
                .build();
        userDto = UserMapper.INSTANCE.convert(user);
    }

    @AfterEach
    void tearDown() {
        user = null;
        userDto = null;
    }

    @Test
    void getOrCreateFirstCase() {
        var expected = userDto;
        var newUser = User.builder()
                .username(user.getUsername()).build();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        var actual = userService.getOrCreate(user.getUsername());

        assertEquals(expected, actual);
        verify(userRepository).findByUsername(user.getUsername());
        verify(userRepository,never()).save(newUser);
    }

    @Test
    void getOrCreateSecondCase() {
        var expected = userDto;
        var newUser = User.builder()
                .username(user.getUsername()).build();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(newUser)).thenReturn(user);

        var actual = userService.getOrCreate(user.getUsername());

        assertEquals(expected, actual);
        verify(userRepository).findByUsername(user.getUsername());
        verify(userRepository).save(newUser);
    }

    @Test
    void getByUsername() {
        var expected = userDto;
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        var actual = userService.
                getByUsername(user.getUsername());

        assertEquals(expected,actual);
        verify(userRepository).findByUsername(user.getUsername());
    }
    @Test
    void getByUsernameFail() {
        var expected = userDto;
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        Exception actual = assertThrows(ResourceNotFoundException.class, () -> userService.
                getByUsername(user.getUsername()));

        assertTrue(actual.getMessage().contains("User wasn't found by username="+user.getUsername()));
        verify(userRepository).findByUsername(user.getUsername());
    }
}