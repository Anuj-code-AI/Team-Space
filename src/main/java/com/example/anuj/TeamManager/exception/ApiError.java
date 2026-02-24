package com.example.anuj.TeamManager.exception;

import java.time.LocalDateTime;

public class ApiError {

    private final String error;
    private final String message;
    private final LocalDateTime timestamp;

    public ApiError(String error, String message) {
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getError() { return error; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
}