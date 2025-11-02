package com.github.springbootproject.web.dto.airline;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.springbootproject.repository.airlineTicket.AirlineTicket;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Ticket {
    private String depart;
    private String arrival;
    private String departureTime;
    private String returnTime;
    private Integer ticketId;

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//    public Ticket(AirlineTicket airlineTicket) {
//        this.depart = airlineTicket.getDepartureLocation();
//        this.arrival = airlineTicket.getArrivalLocation();
//        this.departureTime = airlineTicket.getDepartureAt().format(formatter);
//        this.returnTime = airlineTicket.getReturnAt().format(formatter);
//        this.ticketId = airlineTicket.getTicketId();
//    }

}
