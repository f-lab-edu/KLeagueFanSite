package com.kleague.kleaguefinder.repository.seat;

import com.kleague.kleaguefinder.domain.Category;
import com.kleague.kleaguefinder.domain.Seat;

import java.util.List;

public interface SeatCustomRepository {

    List<Seat> findByRequest(String seatNumber, Category category, int size, int offset);



}
