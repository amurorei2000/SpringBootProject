package com.github.springbootproject.repository.reservations;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Integer> {
}
