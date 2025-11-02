package com.github.springbootproject.service.mapper;

import com.github.springbootproject.repository.airlineTicket.AirlineTicket;
import com.github.springbootproject.web.dto.airline.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static com.github.springbootproject.web.dto.airline.Ticket.formatter;

@Mapper
public interface TicketMapper {

    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    @Mapping(source = "ticketId", target = "ticketId")
    @Mapping(source = "departureLocation", target = "depart")
    @Mapping(source = "arrivalLocation", target = "arrival")
    @Mapping(source = "departureAt", target = "departureTime", qualifiedByName = "convert")
    @Mapping(source = "returnAt", target = "returnTime", qualifiedByName = "convert")
    Ticket airlineTicketToTicket(AirlineTicket airlineTicket);

    @Named("convert")
    static String localDateTimeToString(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        return localDateTime.format(formatter);
    }

}
