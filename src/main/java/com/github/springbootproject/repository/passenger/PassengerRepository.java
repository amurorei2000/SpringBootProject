package com.github.springbootproject.repository.passenger;

import com.github.springbootproject.web.dto.airline.ReservationRequest;

import java.util.List;

public interface PassengerRepository {

    Passenger findPassengerByUserId(Integer userId);

    List<Passenger> findPassengerByUserIds(List<Integer> userIds);
}
