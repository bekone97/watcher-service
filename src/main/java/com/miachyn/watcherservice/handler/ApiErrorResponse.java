package com.miachyn.watcherservice.handler;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ApiErrorResponse {

    private LocalDateTime timestamp = LocalDateTime.now();

    private String message;

    public ApiErrorResponse(String message) {
        this.message = message;
    }

}
