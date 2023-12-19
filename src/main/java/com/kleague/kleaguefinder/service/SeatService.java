package com.kleague.kleaguefinder.service;

import com.kleague.kleaguefinder.domain.Seat;
import com.kleague.kleaguefinder.exception.DuplicatedValueException;
import com.kleague.kleaguefinder.exception.NoValueException;
import com.kleague.kleaguefinder.repository.seat.SeatRepository;
import com.kleague.kleaguefinder.request.seat.SeatCreateRequest;
import com.kleague.kleaguefinder.request.seat.SeatModifyRequest;
import com.kleague.kleaguefinder.request.seat.SeatSearchRequest;
import com.kleague.kleaguefinder.response.SeatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    @Transactional
    public Long save(SeatCreateRequest request) {
        validationCheck(request);

        Seat seat = seatRepository.save(request.toEntity());

       return seat.getId();
    }

    private void validationCheck(SeatCreateRequest request) {
        List<Seat> seats = seatRepository.findByRequest(request.getSeatNumber()
                        , request.getCategory(), 30, 0)
                .stream().filter(seat -> seat.getSeatNumber().equals(request.getSeatNumber())
                && seat.getCategory().equals(request.getCategory())).collect(Collectors.toList());

        if(!seats.isEmpty()){
            throw new DuplicatedValueException("Seat", "seatNumber & category");
        }
    }

    @Transactional(readOnly = true)
    public SeatResponse findById(Long seatId) {
        Seat seat = getSeat(seatId);
        return SeatResponse.createSeatResponse(seat);
    }

    @Transactional(readOnly = true)
    public List<SeatResponse> findByRequest(SeatSearchRequest request) {
       return seatRepository.findByRequest(request.getSeatNumber(),request.getCategory()
                       ,request.getSize() , request.getOffset() )
                .stream().map(SeatResponse::createSeatResponse).collect(Collectors.toList());
    }

    @Transactional
    public void modify(Long seatId, SeatModifyRequest request) {
        Seat seat = getSeat(seatId);
        seat.modify(request.getSeatNumber(), request.getCategory());
    }

    @Transactional
    public void delete(Long seatId) {
        Seat seat = getSeat(seatId);
        seatRepository.delete(seat);
    }

    private Seat getSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new NoValueException("Seat", "id"));
        return seat;
    }

}
