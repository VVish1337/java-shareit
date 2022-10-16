package ru.practicum.shareit.exception.model;

public class ErrorResponseStatus {
    private final String error;
    private final String description;

    public ErrorResponseStatus(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }
}