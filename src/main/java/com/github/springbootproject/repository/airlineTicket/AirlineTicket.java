package com.github.springbootproject.repository.airlineTicket;

import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "ticketId")
@Builder
public class AirlineTicket {
    private Integer ticketId;
    private String ticketType;
    private String departureLocation;
    private String arrivalLocation;
    private LocalDateTime departureAt;
    private LocalDateTime returnAt;
    private double tax;
    private double totalPrice;

    public AirlineTicket(Integer ticketId, String ticketType, String departureLocation, String arrivalLocation, Date departureAt, Date returnAt, double tax, double totalPrice) {
        this.ticketId = ticketId;
        this.ticketType = ticketType;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.departureAt = departureAt != null ? departureAt.toLocalDate().atStartOfDay() : null;
        this.returnAt = returnAt != null ? returnAt.toLocalDate().atStartOfDay() : null;
        this.tax = tax;
        this.totalPrice = totalPrice;
    }

}
