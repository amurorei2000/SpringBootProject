package com.github.springbootproject.repository.reservations;

import com.github.springbootproject.repository.airlineTicket.AirlineTicket;
import com.github.springbootproject.repository.passenger.Passenger;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Integer reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_ticekt_id")
    private AirlineTicket airlineTicket;

    @Column(name = "reservation_status")
    private String reservationStatus;

    @Column(name = "reserve_at")
    private LocalDateTime reserveAt;

    public Reservation(Passenger passenger, AirlineTicket airlineTicket) {
        this.passenger = passenger;
        this.airlineTicket = airlineTicket;
        this.reservationStatus = "대기";
        this.reserveAt = LocalDateTime.now();
    }

}
