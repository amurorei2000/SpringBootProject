package com.github.springbootproject.repository.airlineTicket;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirlineTicketRepository {
    List<AirlineTicket> findAllAirlineTicketsWithPlaceAndTicketType(String likePlace, String ticketType);

    List<AirlineTicketAndFlightInfo> findAllAirlineTicketAndFlightInfo(Integer airlineTicketId);
}
