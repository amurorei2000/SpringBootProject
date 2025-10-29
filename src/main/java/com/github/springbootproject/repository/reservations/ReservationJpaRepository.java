package com.github.springbootproject.repository.reservations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Integer> {
    Reservation findByPassenger_PassengerId(Integer passengerId);
}
