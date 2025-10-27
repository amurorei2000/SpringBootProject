package com.github.springbootproject.repository.airlineTicket;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "ticketId")
@Builder
@Entity
@Table(name = "airline_ticket")
public class AirlineTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Integer ticketId;

    @Column(name = "ticket_type", length = 5)
    private String ticketType;

    @Column(name = "departure_loc", length = 20)
    private String departureLocation;

    @Column(name = "arrival_loc", length = 20)
    private String arrivalLocation;

    @Column(name = "departure_at")
    private LocalDateTime departureAt;

    @Column(name = "return_at")
    private LocalDateTime returnAt;

    @Column(name = "tax")
    private double tax;

    @Column(name = "total_price")
    private double totalPrice;

}
