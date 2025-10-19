package com.github.springbootproject.repository.passenger;

public interface PassengerRepository {

    Passenger findPassengerByUserId(Integer userId);
}
