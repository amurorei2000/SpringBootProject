package com.github.springbootproject.web.controller;

import com.github.springbootproject.service.AirReservationService;
import com.github.springbootproject.service.exceptions.InvalidValueException;
import com.github.springbootproject.service.exceptions.NotAcceptException;
import com.github.springbootproject.service.exceptions.NotFoundException;
import com.github.springbootproject.web.dto.airline.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/air-reservation")
@RequiredArgsConstructor
@Slf4j
public class AirReservationController {
    private final AirReservationService airReservationService;

    @GetMapping("/tickets")
    @Operation(summary = "여행지 티켓 조회", description = "사용자가 선호하는 여행지를 조회하는 api")
    @ResponseStatus(HttpStatus.OK)
    public TicketResponse findAirlineTickets (
            @Parameter(name = "user-Id", description = "유저 ID", example = "1")
            @RequestParam("user-id") Integer userId,
            @Parameter(name = "airline-ticket-type", description = "항공권 타입", example = "왕복|편도")
            @RequestParam("airline-ticket-type") String ticketType) {
        log.info("선호하는 여행지를 찾고 있습니다.");

        List<Ticket> tickets = airReservationService.findUserFavoritePlaceTickets(userId, ticketType);
        log.info("여행지 정보를 찾았습니다.");

        return new TicketResponse(tickets);
    }

    @PostMapping("/reservations")
    @Operation(summary = "티켓 예약", description = "티켓 예약 정보를 db에 저장하는 api")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResult makeReservation(@RequestBody ReservationRequest reservationRequest) {
        return airReservationService.makeReservation(reservationRequest);
    }

    // 자신과 가족들의 항공권을 한 번에 결제하기
    @PostMapping("/payments")
    @Operation(summary = "티켓 일괄 예약", description = "자신과 가족들의 항공권을 일괄 예약하는 api")
    @ResponseStatus(HttpStatus.CREATED)
    public String makeReservations(@RequestBody PaymentsRequest paymentsRequest) {
        Integer count = airReservationService.makeReservations(paymentsRequest);
        return String.format("요청하신 결제 중 %d건 진행완료 되었습니다.", count);
    }

    @GetMapping("/flight-pageable")
    @Operation(summary = "항공권 페이지", description = "항공권 왕복|편도에 따라 Pagable")
    @ResponseStatus(HttpStatus.OK)
    public FlightResponse getFlightPageable(
            @Parameter(name = "type", description = "왕복|편도", example = "왕복")
            @RequestParam("type") String type,
            Pageable pageable) {

        return airReservationService.getFlightPageable(type, pageable);
    }

    @GetMapping("/username-arrival-location")
    @Operation(summary = "예약자 도착지 조회", description = "예약자의 항공편 목적지 리스트 조회")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getReservedArrivalLocations(
            @Parameter(name = "username", description = "예약자 이름")
            @RequestParam("username") String username) {

        return airReservationService.getReservedArrivalLocations(username);

    }

    @GetMapping("/users-sum-price")
    @Operation(summary = "예약자 비용 합계", description = "예약자의 예약한 항공편과 수수료의 합")
    @ResponseStatus(HttpStatus.OK)
    public Double getSumPriceAndCharge(
            @Parameter(name = "user-id", description = "예약자 아이디")
            @RequestParam("user-id") Integer userId) {

        return airReservationService.findUserFlightSumPrice(userId);
    }

}
