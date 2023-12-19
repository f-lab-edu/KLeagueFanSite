package com.kleague.kleaguefinder.request.post;

import com.kleague.kleaguefinder.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.Math.*;

@Getter
@NoArgsConstructor
public class PostSearchRequest {

    private static final int MAX_SIZE = 30;

    private String title = "";

    private String content = "";

    private int page;

    private int size = 10;

    @Builder
    public PostSearchRequest(String title, String content, int page, int size) {
        this.title = (title == null) ? this.title : title;
        this.content = (content == null) ? this.content : content;
        this.page = page;
        this.size = (size <= 0) ? this.size : size;
    }

    public Post toEntity() {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }

    public int getOffSet() {
        return (max(page, 0) * min(size, MAX_SIZE));
    }

}

