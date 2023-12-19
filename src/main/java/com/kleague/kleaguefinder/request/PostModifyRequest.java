package com.kleague.kleaguefinder.request;

import com.kleague.kleaguefinder.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import static com.kleague.kleaguefinder.exception.ErrorCode.AnnotationMsg.NOT_BLANK;

@Getter
public class PostModifyRequest {

    @NotBlank(message = NOT_BLANK)
    private String title;
    @NotBlank(message = NOT_BLANK)
    private String content;

    @Builder
    public PostModifyRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
