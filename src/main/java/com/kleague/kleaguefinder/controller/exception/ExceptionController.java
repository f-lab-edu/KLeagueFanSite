package com.kleague.kleaguefinder.controller.exception;

import com.kleague.kleaguefinder.exception.MainException;
import com.kleague.kleaguefinder.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MainException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> NoValueException(MainException e) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getStatusCode())
                .build();

        errorResponse.addMessages(e.getMessage());

        return ResponseEntity.status(e.getStatusCode()).body(errorResponse);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> ControllerValidException(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(400)
                .messages(makeMessage(e))
                .build();

        return ResponseEntity.status(400).body(errorResponse);
    }

    public List<String> makeMessage(MethodArgumentNotValidException e) {

        List<String> messages = new ArrayList<>();

        for (FieldError fieldError : e.getFieldErrors()) {
            String msg = "[" + fieldError.getObjectName() + "] "
                    + fieldError.getDefaultMessage() + " { 필드 : " + fieldError.getField() + " }";

            messages.add(msg);
        }

        return messages;
    }
}
