package com.kleague.kleaguefinder.repository.seat;

import com.kleague.kleaguefinder.domain.Category;
import com.kleague.kleaguefinder.domain.Seat;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;


import static com.kleague.kleaguefinder.domain.QSeat.*;

@RequiredArgsConstructor
public class SeatCustomRepositoryImpl implements SeatCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Seat> findByRequest(String seatNumber, Category category, int size, int offset) {
        return jpaQueryFactory.selectFrom(seat)
                .where(seat.seatNumber.contains(seatNumber)
                        .and(seat.category.eq(category)))
                .limit(size)
                .offset(offset)
                .fetch();
    }

}
