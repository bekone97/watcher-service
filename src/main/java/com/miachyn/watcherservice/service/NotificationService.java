package com.miachyn.watcherservice.service;

import java.math.BigDecimal;

public interface NotificationService {
    void makeNotify(String symbol, BigDecimal percentage, String username);
}
