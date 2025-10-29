package com.github.springbootproject.repository.passenger;

import com.github.springbootproject.web.dto.airline.ReservationRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository {

    Optional<Passenger> findPassengerByUserId(Integer userId);

    List<Passenger> findPassengerByUserIds(List<Integer> userIds);
}
