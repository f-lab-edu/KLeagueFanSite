package com.kleague.kleaguefinder.request.gameinfo;

import com.kleague.kleaguefinder.domain.GameInfo;
import com.kleague.kleaguefinder.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

import static com.kleague.kleaguefinder.exception.ErrorCode.AnnotationMsg.*;

@Getter
public class GameInfoCreateRequest {

    @NotBlank(message = NOT_BLANK)
    private String name;

    @NotBlank(message = NOT_BLANK)
    private String date;

    @NotBlank(message = NOT_BLANK)
    private String location;

    @Builder
    private GameInfoCreateRequest(String name, String date, String location) {
        this.name = name;
        this.date = date;
        this.location = location;
    }

    public GameInfo toEntity() {
        return GameInfo.builder()
                .name(this.getName())
                .date(this.getDate())
                .location(this.getLocation())
                .build();
    }
}
