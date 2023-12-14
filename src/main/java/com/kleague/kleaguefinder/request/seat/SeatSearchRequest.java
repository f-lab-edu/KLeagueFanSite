package com.kleague.kleaguefinder.request.seat;

import com.kleague.kleaguefinder.domain.Category;
import com.kleague.kleaguefinder.domain.GameInfo;
import com.kleague.kleaguefinder.domain.Seat;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Getter
@Builder
public class SeatSearchRequest {

    private static final int MAX_SIZE = 10;

    @Builder.Default
    private String seatNumber = "";

    @NotBlank
    private Category category ;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    public Seat toEntity() {
        return Seat.builder()
                .seatNumber(seatNumber)
                .category(category)
                .build();
    }

    public int getOffset() {
        return (max(page, 0) * min(size, MAX_SIZE));
    }
}
