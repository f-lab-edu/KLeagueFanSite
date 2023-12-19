package com.kleague.kleaguefinder.request;

import com.kleague.kleaguefinder.domain.Post;
import com.kleague.kleaguefinder.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import static com.kleague.kleaguefinder.exception.ErrorCode.AnnotationMsg.*;

@Getter
public class PostCreateRequest {

    @NotBlank(message = NOT_BLANK)
    private String title;
    @NotBlank(message = NOT_BLANK)
    private String content;

    @Builder
    public PostCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post toEntity() {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
