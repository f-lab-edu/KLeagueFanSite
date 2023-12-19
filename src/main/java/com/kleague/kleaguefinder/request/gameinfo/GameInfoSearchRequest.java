package com.kleague.kleaguefinder.request.gameinfo;

import com.kleague.kleaguefinder.domain.GameInfo;
import lombok.Builder;
import lombok.Getter;

import static java.lang.Math.*;

@Getter
public class GameInfoSearchRequest {

    private static final int MAX_SIZE = 30;

    private String name = "" ;

    private String date = "";

    private String location ="" ;

    private int page = 0;

    private int size = 10;

    @Builder
    private GameInfoSearchRequest(String name, String date, String location, int page, int size) {
        this.name = (name == null) ? this.name : name;
        this.date = (date == null) ? this.date : date;
        this.location = (location == null) ? this.location : location;
        this.page = page;
        this.size = (size <= 0) ? this.size : size;
    }

    public GameInfo toEntity() {
        return GameInfo.builder()
                .name(this.getName())
                .date(this.getDate())
                .location(this.getLocation())
                .build();
    }

    public int getOffset() {
        return (max(page, 0) * min(size, MAX_SIZE));
    }
}
