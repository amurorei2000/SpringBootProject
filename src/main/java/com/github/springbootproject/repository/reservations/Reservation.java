package com.github.springbootproject.repository.reservations;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Reservation {

    private Integer reservationId;
    private Integer passengerId;
    private Integer airlineTicketId;
    private String reservationStatus;
    private LocalDateTime reserveAt;

    public Reservation(Integer passengerId, Integer airlineTicketId) {
        this.passengerId = passengerId;
        this.airlineTicketId = airlineTicketId;
        this.reservationStatus = "대기";
        this.reserveAt = LocalDateTime.now();
    }

}
