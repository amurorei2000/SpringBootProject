package com.github.springbootproject.repository.passenger;

import com.github.springbootproject.web.dto.airline.ReservationRequest;

import java.util.List;
import java.util.Optional;

public interface PassengerRepository {

    Optional<Passenger> findPassengerByUserId(Integer userId);

    List<Passenger> findPassengerByUserIds(List<Integer> userIds);
}
