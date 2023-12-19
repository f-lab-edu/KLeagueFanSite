package com.kleague.kleaguefinder.response.exception;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ErrorResponse {

    private final int code;
    private final List<String> messages;

    @Builder
    public ErrorResponse(int code, List<String> messages) {
        this.code = code;
        this.messages = (messages != null) ? messages : new ArrayList<>() ;
    }

    public void addMessages(String... msgs) {
        messages.addAll(Arrays.asList(msgs));
    }
}
