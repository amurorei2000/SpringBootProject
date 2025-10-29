package com.github.springbootproject.repository.reservations;

import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository {
    Boolean saveReservation(Reservation reservation);

    int updateStatus(Reservation reservation);
}
