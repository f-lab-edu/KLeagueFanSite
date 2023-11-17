package com.kleague.kleaguefinder.response;

import lombok.Builder;
import lombok.Data;

@Data
public class PostResponse {

    private String title;
    private String content;

    @Builder
    public PostResponse(String title, String context) {
        this.title = title;
        this.content = context;
    }
}