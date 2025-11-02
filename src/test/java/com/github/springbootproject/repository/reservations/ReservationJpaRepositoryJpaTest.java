package com.github.springbootproject.repository.reservations;

import com.github.springbootproject.repository.airlineTicket.AirlineTicket;
import com.github.springbootproject.repository.airlineTicket.AirlineTicketJpaRepository;
import com.github.springbootproject.repository.passenger.Passenger;
import com.github.springbootproject.repository.passenger.PassengerJpaRepository;
import com.github.springbootproject.repository.passenger.PassengerRepository;
import com.github.springbootproject.web.dto.airline.ReservationRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest // slice test -> Jpa 사용하고 있는 slice test
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
@Slf4j
class ReservationJpaRepositoryJpaTest {

    @Autowired
    private PassengerJpaRepository passengerJpaRepository;

    @Autowired
    private AirlineTicketJpaRepository airlineTicketJpaRepository;

    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    @Test
    @DisplayName("ReservationJpaRepository로 항공편 가격과 수수료 검색")
    void findFlightPriceAndCharge() {
        // given
        Integer userId = 10;

        // when
        List<FlightPriceAndCharge> findFlightPriceAndCharges = reservationJpaRepository.findFlightPriceAndCharge(userId);


        // then
        log.info("결과: " + findFlightPriceAndCharges);
    }

    @Test
    @DisplayName("Reservation 예약 진행")
    void saveReservation() {
        // given
        Integer userId = 10;
        Integer ticketId = 5;

        Passenger passenger = passengerJpaRepository.findById(userId).get();
        AirlineTicket airlineTicket = airlineTicketJpaRepository.findById(ticketId).get();

        // when
        Reservation reservation = new Reservation(passenger, airlineTicket);
        Reservation res = reservationJpaRepository.save(reservation);

        // then
        log.info("결과: " + res);
        assertEquals(res.getPassenger(), passenger);
        assertEquals(res.getAirlineTicket(), airlineTicket);
    }
}