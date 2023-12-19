package com.kleague.kleaguefinder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kleague.kleaguefinder.domain.GameInfo;
import com.kleague.kleaguefinder.exception.ErrorCode;
import com.kleague.kleaguefinder.repository.gameinfo.GameInfoRepository;
import com.kleague.kleaguefinder.request.gameinfo.GameInfoCreateRequest;
import com.kleague.kleaguefinder.request.gameinfo.GameInfoModifyRequest;
import com.kleague.kleaguefinder.request.gameinfo.GameInfoSearchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.kleague.kleaguefinder.exception.ErrorCode.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class GameInfoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GameInfoRepository gameInfoRepository;

    GameInfo gameInfo;


    @BeforeEach
    public void setUp() {
        List<GameInfo> gameInfoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            gameInfoList.add(GameInfo.builder()
                    .name("경기 이름 " + i)
                    .date("12월 12일")
                    .location("경기 위치 " + i)
                    .build());
        }
        gameInfoRepository.saveAll(gameInfoList);

        for (int i = 10; i < 15; i++) {
            gameInfoRepository.save(GameInfo.builder()
                    .name("경기 이름 " + i)
                    .date("12월 11일")
                    .location("경기 위치 " + i)
                    .build());
        }

        gameInfo  = GameInfo.builder()
                .name("서울 vs 부산")
                .date("12월 12일")
                .location("경기장")
                .build();

        gameInfoRepository.save(gameInfo);

    }

    @Test
    @DisplayName("GameInfo 저장 성공")
    public void saveSuccess() throws Exception {
        GameInfoCreateRequest request = GameInfoCreateRequest.builder()
                .name("경기 내용")
                .date("12월 12일")
                .location("경기장")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/gameInfos/save")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("저장 - Request 필드 누락")
    public void saveRequestMissingV1() throws Exception {
        // given
        GameInfoCreateRequest request = GameInfoCreateRequest.builder()
                .name("경기이름")
                .date("날짜")
                .build();

        // when
        String json = objectMapper.writeValueAsString(request);

        // then
        mockMvc.perform(post("/api/v1/gameInfos/save")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.messages[0]")
                        .value(makeMessage("gameInfoCreateRequest", AnnotationMsg.NOT_BLANK, "location")))
                .andDo(print());
    }

   @Test
    @DisplayName("저장 - Request 여러개 필드 누락")
    public void saveRequestMissingV2() throws Exception {
        // given
        GameInfoCreateRequest request = GameInfoCreateRequest.builder()
                .date("날짜")
                .build();

        // when
        String json = objectMapper.writeValueAsString(request);

        // then
        mockMvc.perform(post("/api/v1/gameInfos/save")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.messages[0]")
                        .value(makeMessage("gameInfoCreateRequest", AnnotationMsg.NOT_BLANK, "name")))
                .andExpect(jsonPath("$.messages[1]")
                        .value(makeMessage("gameInfoCreateRequest", AnnotationMsg.NOT_BLANK, "location")))
                .andDo(print());
    }

    @Test
    @DisplayName("GameInfo 저장 실패")
    public void saveFail() throws Exception {
        // given
        GameInfoCreateRequest request = GameInfoCreateRequest.builder()
                .name("경기 이름 0")
                .date("12월 12일")
                .location("경기 위치 0")
                .build();

        // when
        String json = objectMapper.writeValueAsString(request);

        // expect
        mockMvc.perform(post("/api/v1/gameInfos/save")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.messages[0]")
                        .value(makeMessage("GameInfo", DUPLICATED_CODE.getMessage(), "name & date")))
                .andDo(print());
    }

    @Test
    @DisplayName("Id 로 검색")
    public void findById() throws Exception {

        mockMvc.perform(get("/api/v1/gameInfos/{Id}", gameInfo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("서울 vs 부산"))
                .andExpect(jsonPath("$.date").value("12월 12일"))
                .andExpect(jsonPath("$.location").value("경기장"))
                .andDo(print());
    }


    @Test
    @DisplayName("Id 로 검색 - 실패")
    public void findByIdFail() throws Exception {

        mockMvc.perform(get("/api/v1/gameInfos/{Id}", gameInfo.getId() + 10L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.messages[0]")
                        .value(makeMessage("GameInfo", NO_VALUE_CODE.getMessage(), "id")))
                .andDo(print());
    }

    @Test
    @DisplayName("검색 - 경기 이름")
    public void searchByName() throws Exception {

        GameInfoSearchRequest searchRequest = GameInfoSearchRequest.builder()
                .name("경기 이름")
                .build();

        String json = objectMapper.writeValueAsString(searchRequest);

        mockMvc.perform(post("/api/v1/gameInfos/search")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("경기 이름 14"))
                .andExpect(jsonPath("$[0].date").value("12월 11일"))
                .andExpect(jsonPath("$[0].location").value("경기 위치 14"))
                .andDo(print());
    }

    @Test
    @DisplayName("검색 - 경기 날짜")
    public void searchByDate() throws Exception {

        GameInfoSearchRequest searchRequest = GameInfoSearchRequest.builder()
                .date("12월 11일")
                .build();

        String json = objectMapper.writeValueAsString(searchRequest);

        mockMvc.perform(post("/api/v1/gameInfos/search")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("경기 이름 14"))
                .andExpect(jsonPath("$[0].date").value("12월 11일"))
                .andExpect(jsonPath("$[0].location").value("경기 위치 14"))
                .andDo(print());
    }

    @Test
    @DisplayName("검색 - 경기 위치")
    public void searchByLocation() throws Exception {

        GameInfoSearchRequest searchRequest = GameInfoSearchRequest.builder()
                .location("경기장")
                .build();

        String json = objectMapper.writeValueAsString(searchRequest);

        mockMvc.perform(post("/api/v1/gameInfos/search")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("서울 vs 부산"))
                .andExpect(jsonPath("$[0].date").value("12월 12일"))
                .andExpect(jsonPath("$[0].location").value("경기장"))
                .andDo(print());
    }

    @Test
    @DisplayName("검색 - 이름, 날짜, 위치  [ 성공 ]")
    public void searchByRequestSuccess() throws Exception {

        GameInfoSearchRequest searchRequest = GameInfoSearchRequest.builder()
                .name("서울 vs 부산")
                .date("12월 12일")
                .location("경기장")
                .build();

        String json = objectMapper.writeValueAsString(searchRequest);

        mockMvc.perform(post("/api/v1/gameInfos/search")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("서울 vs 부산"))
                .andExpect(jsonPath("$[0].date").value("12월 12일"))
                .andExpect(jsonPath("$[0].location").value("경기장"))
                .andDo(print());
    }

    @Test
    @DisplayName("검색 - 이름, 날짜, 위치  [ 결과 없음 ]")
    public void searchByRequestNoValue() throws Exception {

        GameInfoSearchRequest searchRequest = GameInfoSearchRequest.builder()
                .name("서울 vs 부산")
                .date("12월 12일")
                .location("경기장 2")
                .build();

        String json = objectMapper.writeValueAsString(searchRequest);

        mockMvc.perform(post("/api/v1/gameInfos/search")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").doesNotExist())
                .andExpect(jsonPath("$[0].date").doesNotExist())
                .andExpect(jsonPath("$[0].location").doesNotExist())
                .andDo(print());
    }

    @Test
    @DisplayName("수정 - 이름만 수정")
    public void modifyName() throws Exception {

        GameInfoModifyRequest searchRequest = GameInfoModifyRequest.builder()
                .name("부천 vs 서울")
                .date("12월 12일")
                .location("경기장")
                .build();

        String json = objectMapper.writeValueAsString(searchRequest);

        mockMvc.perform(put("/api/v1/gameInfos/{Id}", gameInfo.getId() )
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @DisplayName("저장 - Request 필드 누락")
    public void modifyRequestMissingV1() throws Exception {
        // given
        GameInfoModifyRequest request = GameInfoModifyRequest.builder()
                .name("경기 내용")
                .location("경기 위치")
                .build();

        // when
        String json = objectMapper.writeValueAsString(request);

        // then
        mockMvc.perform(put("/api/v1/gameInfos/{id}", gameInfo.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.messages[0]")
                        .value(makeMessage("gameInfoModifyRequest", AnnotationMsg.NOT_BLANK, "date")))
                .andDo(print());
    }


    @Test
    @DisplayName("수정 - GameInfo 없어서 예외 발생 ")
    public void modifyNoValue() throws Exception {

        GameInfoModifyRequest searchRequest = GameInfoModifyRequest.builder()
                .name("부천 vs 서울")
                .date("12월 12일")
                .location("경기장")
                .build();

        String json = objectMapper.writeValueAsString(searchRequest);

        mockMvc.perform(put("/api/v1/gameInfos/{Id}", gameInfo.getId() + 100L )
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.messages[0]")
                        .value(makeMessage("GameInfo", NO_VALUE_CODE.getMessage(), "id")))
                .andDo(print());
    }

    @Test
    @DisplayName("삭제")
    public void deleteGameInfo() throws Exception {

        mockMvc.perform(delete("/api/v1/gameInfos/{id}", gameInfo.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("삭제 - GameInfo 없어서 예외발생")
    public void deleteGameInfoNoValue() throws Exception {

        mockMvc.perform(delete("/api/v1/gameInfos/{id}", gameInfo.getId() + 100L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.messages[0]")
                        .value(makeMessage("GameInfo", NO_VALUE_CODE.getMessage(), "id")))
                .andDo(print());
    }



    public String makeMessage(String type, String message, String field) {
        return ("[" + type + "] " + message + " { 필드 : " + field + " }" );
    }

}
