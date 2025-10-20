package com.github.springbootproject.repository.reservations;

public interface ReservationRepository {
    Boolean saveReservation(Reservation reservation);

    int updateStatus(Reservation reservation);
}
