package com.github.springbootproject.repository.flight;

import com.github.springbootproject.repository.airlineTicket.AirlineTicket;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private String flightId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id",  nullable = false)
    private AirlineTicket ticket;

    @Column(name = "departure_at")
    private LocalDateTime departureAt;

    @Column(name = "arrival_at")
    private LocalDateTime arrivalAt;

    @Column(name = "departure_loc")
    private String departureLocation;

    @Column(name = "arrival_loc")
    private String arrival_loc;

    @Column(name = "flight price")
    private String flightPrice;


}
