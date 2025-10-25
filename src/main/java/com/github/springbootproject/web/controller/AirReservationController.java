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
}
