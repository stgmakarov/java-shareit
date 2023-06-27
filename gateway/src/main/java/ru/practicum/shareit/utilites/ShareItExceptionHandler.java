package ru.practicum.shareit.utilites;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ShareItExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleHappinessOverflow(final ParamNotFoundException e) {
        return new ResponseEntity<>(
                Map.of("error", e.getLocalizedMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}
