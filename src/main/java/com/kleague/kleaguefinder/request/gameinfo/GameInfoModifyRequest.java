package com.kleague.kleaguefinder.request.gameinfo;

import com.kleague.kleaguefinder.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

import static com.kleague.kleaguefinder.exception.ErrorCode.*;
import static com.kleague.kleaguefinder.exception.ErrorCode.AnnotationMsg.*;


@Getter
public class GameInfoModifyRequest {

    @NotBlank(message = NOT_BLANK)
    private String name;

    @NotBlank(message = NOT_BLANK)
    private String date;

    @NotBlank(message = NOT_BLANK)
    private String location;

    @Builder
    private GameInfoModifyRequest(String name, String date, String location) {
        this.name = name;
        this.date = date;
        this.location = location;
    }
}
