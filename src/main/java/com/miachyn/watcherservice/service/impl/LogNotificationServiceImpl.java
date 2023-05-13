package com.miachyn.watcherservice.service.impl;

import com.miachyn.watcherservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class LogNotificationServiceImpl implements NotificationService {
    @Override
    public void makeNotify(String symbol, BigDecimal percentage, String username) {
        log.warn("The currency with symbol : {} changed by : {} for user : {}",
                symbol, percentage, username);
    }
}
