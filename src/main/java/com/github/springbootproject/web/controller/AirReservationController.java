package com.github.springbootproject.web.controller;

import com.github.springbootproject.service.AirReservationService;
import com.github.springbootproject.web.dto.airline.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/air-reservation")
public class AirReservationController {
    AirReservationService airReservationService;

    public AirReservationController(AirReservationService airReservationService) {
        this.airReservationService = airReservationService;
    }

    @GetMapping("/tickets")
    public TicketResponse findAirlineTickets (@RequestParam("user-id") Integer userId, @RequestParam("airline-ticket-type") String ticketType) {
        List<Ticket> tickets = airReservationService.findUserFavoritePlaceTickets(userId, ticketType);

        return new TicketResponse(tickets);
    }

    @PostMapping("/reservations")
    public ReservationResult makeReservation(@RequestBody ReservationRequest reservationRequest) {
        return airReservationService.makeReservation(reservationRequest);
    }

    // 자신과 가족들의 항공권을 한 번에 결제하기
    @PostMapping("/payments")
    public String makeReservations(@RequestBody PaymentsRequest paymentsRequest) {
        Integer count = airReservationService.makeReservations(paymentsRequest);
        return String.format("요청하신 결제 중 %d건 진행완료 되었습니다.", count);
    }
}
