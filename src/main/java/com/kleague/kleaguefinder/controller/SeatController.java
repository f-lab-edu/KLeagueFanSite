package com.kleague.kleaguefinder.controller;

import com.kleague.kleaguefinder.request.gameinfo.GameInfoCreateRequest;
import com.kleague.kleaguefinder.request.seat.SeatCreateRequest;
import com.kleague.kleaguefinder.request.seat.SeatModifyRequest;
import com.kleague.kleaguefinder.request.seat.SeatSearchRequest;
import com.kleague.kleaguefinder.response.GameInfoResponse;
import com.kleague.kleaguefinder.response.SeatResponse;
import com.kleague.kleaguefinder.service.SeatService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @PostMapping("api/v1/seats/save")
    public Long save(@Valid @RequestBody SeatCreateRequest request) {
        return seatService.save(request);
    }

    @GetMapping("api/v1/seats/{id}")
    public SeatResponse findOne(@PathVariable("id") Long id) {
        return seatService.findById(id);
    }

    @PostMapping("api/v1/seats/search")
    public List<SeatResponse> search(@Valid @RequestBody SeatSearchRequest request) {
        return seatService.findByRequest(request);
    }

    @PutMapping("api/v1/seats/{id}")
    public void modify(@PathVariable("id") Long id, @Valid @RequestBody SeatModifyRequest request) {
        seatService.modify(id, request);
    }

    @DeleteMapping("api/v1/seats/{id}")
    public void delete(@PathVariable("id") Long id) {
        seatService.delete(id);
    }
}
