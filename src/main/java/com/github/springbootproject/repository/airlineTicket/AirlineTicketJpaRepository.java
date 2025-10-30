package com.github.springbootproject.repository.airlineTicket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AirlineTicketJpaRepository extends JpaRepository<AirlineTicket, Integer> {
    Page<AirlineTicket> findAllByTicketType(String ticketType, Pageable pageable);

    List<AirlineTicket> findAllByArrivalLocationAndTicketType(String arrivalLocation, String ticketType);
}
