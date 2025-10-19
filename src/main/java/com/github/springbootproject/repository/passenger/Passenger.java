package com.github.springbootproject.repository.passenger;

import java.util.Objects;

public class Passenger {
    private Integer passengerId;
    private Integer userId;
    private String passportNum;

    public Passenger(Integer passengerId, Integer userId, String passportNum) {
        this.passengerId = passengerId;
        this.userId = userId;
        this.passportNum = passportNum;
    }

    public Integer getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Integer passengerId) {
        this.passengerId = passengerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPassportNum() {
        return passportNum;
    }

    public void setPassportNum(String passportNum) {
        this.passportNum = passportNum;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return Objects.equals(passengerId, passenger.passengerId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(passengerId);
    }
}
