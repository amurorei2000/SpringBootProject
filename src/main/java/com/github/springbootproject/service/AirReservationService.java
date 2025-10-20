package com.github.springbootproject.service;

import com.github.springbootproject.repository.airlineTicket.AirlineTicket;
import com.github.springbootproject.repository.airlineTicket.AirlineTicketAndFlightInfo;
import com.github.springbootproject.repository.airlineTicket.AirlineTicketRepository;
import com.github.springbootproject.repository.passenger.Passenger;
import com.github.springbootproject.repository.passenger.PassengerRepository;
import com.github.springbootproject.repository.reservations.Reservation;
import com.github.springbootproject.repository.reservations.ReservationRepository;
import com.github.springbootproject.repository.users.UserEntity;
import com.github.springbootproject.repository.users.UserRepository;
import com.github.springbootproject.web.dto.airline.PaymentsRequest;
import com.github.springbootproject.web.dto.airline.ReservationRequest;
import com.github.springbootproject.web.dto.airline.ReservationResult;
import com.github.springbootproject.web.dto.airline.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirReservationService {
    private final UserRepository userRepository;
    private final AirlineTicketRepository airlineTicketRepository;
    private final PassengerRepository passengerRepository;
    private final ReservationRepository reservationRepository;

    public List<Ticket> findUserFavoritePlaceTickets(Integer userId, String ticketType) {
        // 유저를 userId로 가져와서 선호하는 여행지 도출
        UserEntity userEntity = userRepository.findUserById(userId);
        String likePlace = userEntity.getLikeTravelPlace();

        // 선호하는 여행지와 ticketType으로 AirlineTicket table 질의해서 필요한 AirlineTicket table 질의해서 필요한 정보를 가져온다.
        List<AirlineTicket> airlineTickets = airlineTicketRepository.findAllAirlineTicketsWithPlaceAndTicketType(likePlace,ticketType);

        // 이 둘의 정보를 조합해서 Ticket DTO를 만든다.
        return airlineTickets.stream()
                .map(Ticket::new)
                .collect(Collectors.toList());
    }

    @Transactional(transactionManager="tm2")
    public ReservationResult makeReservation(ReservationRequest reservationRequest) {
        // passenger 정보 가져오기
        Passenger passenger = passengerRepository.findPassengerByUserId(reservationRequest.getUserId());
        Integer passengerId = passenger.getUserId();

        // price 등의 정보 가져오기
        List<AirlineTicketAndFlightInfo> airlineTicketAndFlightInfo = airlineTicketRepository.findAllAirlineTicketAndFlightInfo(reservationRequest.getAirlineTicketId());

        // reservation 생성
        Reservation reservation = new Reservation(passengerId, reservationRequest.getAirlineTicketId());
        Boolean isSuccess = reservationRepository.saveReservation(reservation);

        // ReservationResult DTO 만들기
        List<Integer> prices = airlineTicketAndFlightInfo.stream()
                .map(AirlineTicketAndFlightInfo::getPrice)
                .collect(Collectors.toList());

        List<Integer> charges = airlineTicketAndFlightInfo.stream()
                .map(AirlineTicketAndFlightInfo::getCharge)
                .collect(Collectors.toList());

        Integer tax = airlineTicketAndFlightInfo.stream()
                .map(AirlineTicketAndFlightInfo::getTax)
                .findFirst().get();

        Integer totalPrice = airlineTicketAndFlightInfo.stream()
                .map(AirlineTicketAndFlightInfo::getTotalPrice)
                .findFirst().get();

        return new ReservationResult(
                prices, charges, tax, totalPrice, isSuccess
        );
    }

    @Transactional(transactionManager = "tm2")
    public Integer makeReservations(PaymentsRequest paymentsRequest) {

        List<Integer> userIds = paymentsRequest.getUser_ids();
        List<Integer> ticketIds = paymentsRequest.getAirline_ticket_ids();

        if (userIds.isEmpty() || ticketIds.isEmpty()) {
            return 0;
        }

        if (userIds.size() != ticketIds.size()) {
            return 0;
        }

        // Passneger 리스트 가져오기
        List<Passenger> passengers = passengerRepository.findPassengerByUserIds(paymentsRequest.getUser_ids());

        // reservation 생성
        Integer reservationCount = 0;

        for (int i = 0; i< passengers.size(); i++) {
            Reservation reservation = new Reservation(passengers.get(i).getPassengerId(), ticketIds.get(i));
            reservationCount += reservationRepository.updateStatus(reservation);
        }

        // 예약된 수 반환
        return reservationCount;
    }
}
