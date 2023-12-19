package com.kleague.kleaguefinder.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.Math.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class PostSearchRequest {

    @Autowired
    private static final int MAX_SIZE = 30;

    @Builder.Default
    private String title = "";
    @Builder.Default
    private String content = "";

    @Builder.Default
    private int page = 0;
    @Builder.Default
    private int size = 10;

    public PostSearchRequest(String title, String content, int page, int size) {
        this.title = title;
        this.content = content;
        this.page = page;
        this.size = size;
    }

    public int getOffSet() {
        return (max(page, 0) * min(size, MAX_SIZE));
    }

}
