package com.miachyn.watcherservice.service;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.miachyn.watcherservice.dto.UserDto;
import com.miachyn.watcherservice.entity.User;
import com.miachyn.watcherservice.exception.ResourceNotFoundException;
import com.miachyn.watcherservice.initializer.DatabaseContainerInitializer;
import com.miachyn.watcherservice.mapper.UserMapper;
import com.miachyn.watcherservice.scheduler.ScheduleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@ActiveProfiles("test")
class UserServiceIntegrationTest extends DatabaseContainerInitializer {

    @Autowired
    private UserService userService;

    @MockBean
    private ScheduleService scheduleService;

    User user;
    UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("Artem")
                .build();
        userDto = UserMapper.INSTANCE.convert(user);
    }

    @AfterEach
    void tearDown() {
        user = null;
        userDto = null;
    }

    @Test
    @DataSet(value = {"dataset/init/currencyUser/init.yaml"},
            useSequenceFiltering = false,
            executeScriptsAfter = {"scripts/cleanCurrencyUser.sql"})
    @ExpectedDataSet(value =  {"dataset/expected/currencyUser/nothingHappened.yaml"})
    void getOrCreateFirstCase() {
        var expected = userDto;

        var actual = userService.getOrCreate(user.getUsername());

        assertEquals(expected, actual);
    }

    @Test
    @DataSet(value = {"dataset/init/currencyUser/init.yaml"},
            useSequenceFiltering = false,
            executeScriptsAfter = {"scripts/cleanCurrencyUser.sql"},
            executeScriptsBefore = {"scripts/currencyUserSequence.sql"})
    @ExpectedDataSet(value =  {"dataset/expected/currencyUser/save.yaml"})
    void getOrCreateSecondCase() {
        var expected = new UserDto(2L,"Another_artem");

        var actual = userService.getOrCreate(expected.getUsername());

        assertEquals(expected, actual);
    }

    @Test
    @DataSet(value = {"dataset/init/currencyUser/init.yaml"},
            useSequenceFiltering = false,
            executeScriptsAfter = {"scripts/cleanCurrencyUser.sql"})
    void getByUsername() {
        var expected = userDto;

        var actual = userService.
                getByUsername(user.getUsername());

        assertEquals(expected,actual);
    }

    @Test
    @DataSet(value = {"dataset/init/currencyUser/init.yaml"},
            useSequenceFiltering = false,
            executeScriptsAfter = {"scripts/cleanCurrencyUser.sql"})
    void getByUsernameFail() {
        var username = "someName";

        Exception actual = assertThrows(ResourceNotFoundException.class, () -> userService.
                getByUsername(username));

        assertTrue(actual.getMessage().contains("User wasn't found by username="+username));
    }
}