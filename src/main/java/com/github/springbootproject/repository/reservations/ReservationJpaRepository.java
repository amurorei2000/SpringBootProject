package com.github.springbootproject.repository.reservations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByPassenger_PassengerId(Integer passengerId);
}
