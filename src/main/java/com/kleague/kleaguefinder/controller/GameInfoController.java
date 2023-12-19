package com.kleague.kleaguefinder.controller;

import com.kleague.kleaguefinder.request.gameinfo.GameInfoCreateRequest;
import com.kleague.kleaguefinder.request.gameinfo.GameInfoModifyRequest;
import com.kleague.kleaguefinder.request.gameinfo.GameInfoSearchRequest;
import com.kleague.kleaguefinder.response.GameInfoResponse;
import com.kleague.kleaguefinder.service.GameInfoService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GameInfoController {

    private final GameInfoService gameInfoService;

    @PostMapping("api/v1/gameInfos/save")
    public Long save(@Valid @RequestBody GameInfoCreateRequest request) {
        return gameInfoService.save(request);
    }

    @GetMapping("api/v1/gameInfos/{id}")
    public GameInfoResponse findOne(@PathVariable("id") Long id) {
        return gameInfoService.findById(id);
    }

    @PostMapping("api/v1/gameInfos/search")
    public List<GameInfoResponse> search(@RequestBody GameInfoSearchRequest request) {
        return gameInfoService.findByRequest(request);
    }

    @PutMapping("api/v1/gameInfos/{id}")
    public void modify(@PathVariable("id") Long id, @Valid @RequestBody GameInfoModifyRequest request) {
        gameInfoService.modify(id,request);
    }

    @DeleteMapping("api/v1/gameInfos/{id}")
    public void delete(@PathVariable("id") Long id) {
        gameInfoService.delete(id);
    }

}
