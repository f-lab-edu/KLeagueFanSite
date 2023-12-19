package com.kleague.kleaguefinder.service;

import com.kleague.kleaguefinder.domain.Seat;
import com.kleague.kleaguefinder.exception.DuplicatedValueException;
import com.kleague.kleaguefinder.exception.NoValueException;
import com.kleague.kleaguefinder.repository.seat.SeatRepository;
import com.kleague.kleaguefinder.request.seat.SeatCreateRequest;
import com.kleague.kleaguefinder.request.seat.SeatModifyRequest;
import com.kleague.kleaguefinder.request.seat.SeatSearchRequest;
import com.kleague.kleaguefinder.response.SeatResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static com.kleague.kleaguefinder.domain.Category.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class SeatServiceTest {

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    SeatService seatService;

    Seat seatNo1;

    @BeforeEach
    public void setup() {
        seatNo1 = Seat.builder()
                .seatNumber("1")
                .category(A)
                .build();

        seatRepository.save(seatNo1);
    }

    @Test
    @DisplayName("Seat 저장 성공")
    public void saveSuccessV1() {
        // given
        SeatCreateRequest request = SeatCreateRequest.builder()
                .seatNumber("2")
                .category(A)
                .build();

        // when
        Long seatId = seatService.save(request);

        // then
        assertThat(seatId).isEqualTo(3L);
    }

    @Test
    @DisplayName("Seat 저장 실패 - 중복 값으로 인해 예외 발생")
    public void saveFailV1() {

        // when
        for(int i=2; i<27; i++){
            seatRepository.save(Seat.builder().seatNumber(String.valueOf(i)).category(A).build());
        }

        SeatCreateRequest request = SeatCreateRequest.builder()
                .seatNumber("5")
                .category(A)
                .build();

        // then
        assertThatThrownBy(() -> seatService.save(request)).isInstanceOf(DuplicatedValueException.class);
    }

    @Test
    @DisplayName("Seat 저장 성공 - 필드 중 한개 만 중복")
    public void saveSuccessV2() {
        // when
        SeatCreateRequest request = SeatCreateRequest.builder()
                .seatNumber("1")
                .category(B)
                .build();

        Long seatId = seatService.save(request);

        // then
        assertThat(seatId).isEqualTo(5L);

    }

    @Test
    @DisplayName("Seat Id 로 찾기 - 성공")
    public void findSeatById() {
        // given
        Long seatId = seatNo1.getId();

        // when
        SeatResponse seatResponse = seatService.findById(seatId);

        // then
        assertThat(seatResponse.getSeatNumber()).isEqualTo("1");
        assertThat(seatResponse.getCategory()).isEqualTo(A);
    }

    @Test
    @DisplayName("Seat Id 로 찾기 - 실패")
    public void findSeatByIdFail() {
        // given
        Long seatId = seatNo1.getId();

        // expected
        assertThatThrownBy(() -> seatService.findById(seatId + 30L)).isInstanceOf(NoValueException.class);
    }

    @Test
    @DisplayName("searchRequest 로 검색 - 검색 값 두개 다 있음")
    public void findSeatByRequestV1() {
        // given
        SeatSearchRequest request = SeatSearchRequest.builder()
                .seatNumber("1")
                .category(A)
                .build();

        // when
        List<SeatResponse> seatResponseList = seatService.findByRequest(request);

        // then
        assertThat(seatResponseList.get(0).getSeatNumber()).isEqualTo("1");
        assertThat(seatResponseList.get(0).getCategory()).isEqualTo(A);
    }

    @Test
    @DisplayName("searchRequest 로 검색 - 카테고리만 존재")
    public void findSeatByRequestV2() {
        // given
        SeatSearchRequest request = SeatSearchRequest.builder()
                .category(A)
                .build();

        // when
        List<SeatResponse> seatResponseList = seatService.findByRequest(request);

        // then
        assertThat(seatResponseList.get(0).getSeatNumber()).isEqualTo("1");
        assertThat(seatResponseList.get(0).getCategory()).isEqualTo(A);
    }

    @Test
    @DisplayName("searchRequest 로 검색")
    public void findSeatByRequestV3() {
        // given
        SeatSearchRequest request = SeatSearchRequest.builder()
                .category(B)
                .build();

        // when
        List<SeatResponse> seatResponseList = seatService.findByRequest(request);

        // then
        assertThat(seatResponseList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Seat seatNumber 수정 - 성공")
    public void modifySuccessV1() {
        // given
        SeatModifyRequest request = SeatModifyRequest.builder()
                .seatNumber("2")
                .category(A)
                .build();

        // when
        seatService.modify(seatNo1.getId(), request);

        // then
        assertThat(seatNo1.getSeatNumber()).isEqualTo("2");
    }

    @Test
    @DisplayName("Seat Category 수정 - 성공")
    public void modifySuccessV2() {
        // given
        SeatModifyRequest request = SeatModifyRequest.builder()
                .seatNumber("1")
                .category(B)
                .build();

        // when
        seatService.modify(seatNo1.getId(), request);

        // then
        assertThat(seatNo1.getSeatNumber()).isEqualTo("1");
        assertThat(seatNo1.getCategory()).isEqualTo(B);
    }

    @Test
    @DisplayName("Seat  수정 - 실패 [ 없는 값 조회 ]")
    public void modifyFail() {
        // given
        SeatModifyRequest request = SeatModifyRequest.builder()
                .seatNumber("1")
                .category(B)
                .build();

        // expected
        assertThatThrownBy(() -> seatService.modify(seatNo1.getId() + 10L, request))
                .isInstanceOf(NoValueException.class);
    }

    @Test
    @DisplayName("Seat 삭제 - 성공")
    public void deleteSuccess() {

        seatService.delete(1L);
        assertThat(seatRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Seat 삭제 - 실패 ")
    public void deleteFail() {

        assertThatThrownBy(() -> seatService.delete(10L)).isInstanceOf(NoValueException.class);

    }
}
