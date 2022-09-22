package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.model.ErrorResponse;
import ru.practicum.shareit.exception.model.ErrorResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ErrorHandlerTest {
    private final ErrorHandler handler = new ErrorHandler();

    @Test
    void handleNotFoundException() {
        NotFoundException e = new NotFoundException("Not found");
        ErrorResponse errorResponse = handler.handleNotFoundException(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getError(), e.getMessage());
    }

    @Test
    void handleEmailExistsException() {
        EmailExistsException e = new EmailExistsException("Email exists");
        ErrorResponse errorResponse = handler.handleEmailExistsException(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getError(), e.getMessage());
    }

    @Test
    void handleItemUnavailableException() {
        ItemUnavailableException e = new ItemUnavailableException("Item Unavailable");
        ErrorResponse errorResponse = handler.handleItemUnavailableException(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getError(), e.getMessage());
    }

    @Test
    void handleUnsupportedStatus() {
        UnsupportedStatusException e = new UnsupportedStatusException("Unsupported");
        ErrorResponseStatus errorResponse = handler.handle(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getDescription(), e.getMessage());
    }
}