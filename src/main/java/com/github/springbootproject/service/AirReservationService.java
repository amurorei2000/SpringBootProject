package com.github.springbootproject.service;

import com.github.springbootproject.repository.airlineTicket.AirlineTicket;
import com.github.springbootproject.repository.airlineTicket.AirlineTicketAndFlightInfo;
import com.github.springbootproject.repository.airlineTicket.AirlineTicketJpaRepository;
import com.github.springbootproject.repository.airlineTicket.AirlineTicketRepository;
import com.github.springbootproject.repository.passenger.Passenger;
import com.github.springbootproject.repository.passenger.PassengerRepository;
import com.github.springbootproject.repository.reservations.Reservation;
import com.github.springbootproject.repository.reservations.ReservationRepository;
import com.github.springbootproject.repository.users.UserEntity;
import com.github.springbootproject.repository.users.UserRepository;
import com.github.springbootproject.service.exceptions.InvalidValueException;
import com.github.springbootproject.service.exceptions.NotAcceptException;
import com.github.springbootproject.service.exceptions.NotFoundException;
import com.github.springbootproject.web.dto.airline.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirReservationService {
    private final UserRepository userRepository;
    private final AirlineTicketRepository airlineTicketRepository;
    private final PassengerRepository passengerRepository;
    private final ReservationRepository reservationRepository;
    private final AirlineTicketJpaRepository airlineTicketJpaRepository;

    public List<Ticket> findUserFavoritePlaceTickets(Integer userId, String ticketType) {
        // 유저를 userId로 가져와서 선호하는 여행지 도출
        Set<String> ticketTypeSet = new HashSet<>(Arrays.asList("편도", "왕복"));

        if (!ticketTypeSet.contains(ticketType)) {
            throw new InvalidValueException("해당 TicketType " + ticketType + "은 지원하지 않습니다.");
        }

        UserEntity userEntity = userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("해당 ID: " + userId + " 유저를 찾을 수 없습니다."));
        String likePlace = userEntity.getLikeTravelPlace();

        // 선호하는 여행지와 ticketType으로 AirlineTicket table 질의해서 필요한 AirlineTicket table 질의해서 필요한 정보를 가져온다.
        List<AirlineTicket> airlineTickets = airlineTicketRepository.findAllAirlineTicketsWithPlaceAndTicketType(likePlace,ticketType);

        if (airlineTickets.isEmpty()) {
            throw new NotFoundException("해당 likePalce: " + likePlace + "와 TicketType: " + ticketType + "에 해당하는 항공편을 찾을 수 없습니다.");
        }

        // 이 둘의 정보를 조합해서 Ticket DTO를 만든다.
        return airlineTickets.stream()
                .map(Ticket::new)
                .collect(Collectors.toList());
    }

    @Transactional(transactionManager="tm2")
    public ReservationResult makeReservation(ReservationRequest reservationRequest) {
        // passenger 정보 가져오기
        Passenger passenger = passengerRepository.findPassengerByUserId(reservationRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("요청하신 userId " + reservationRequest.getUserId() + "에 해당하는 Passenger를 찾을 수 없습니다."));
        Integer passengerId = passenger.getUserId();

        // price 등의 정보 가져오기
        Integer airlineTicketId = reservationRequest.getAirlineTicketId();
        List<AirlineTicketAndFlightInfo> airlineTicketAndFlightInfo = airlineTicketRepository.findAllAirlineTicketAndFlightInfo(airlineTicketId);

        if (airlineTicketAndFlightInfo.isEmpty()) {
            throw new NotFoundException("AirlineTicket Id" + airlineTicketId + " 에 해당하는 항공편과 항공권을 찾을 수 없습니다.");
        }

        // reservation 생성
        Boolean isSuccess = false;
        Reservation reservation = new Reservation(passengerId, reservationRequest.getAirlineTicketId());

        try {
            isSuccess = reservationRepository.saveReservation(reservation);
        } catch (RuntimeException e) {
            throw new NotAcceptException("Reservation이 등록되는 과정이 거부되었습니다.");
        }

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

    public FlightResponse getFlightPageable(String type, Pageable pageable) {
        Page<AirlineTicket> airlineTickets = airlineTicketJpaRepository.findAllByTicketType(type, pageable);

        List<FlightInfo> flightInfos = airlineTickets.stream()
                .map(ticket -> new FlightInfo(
                        ticket.getTicketId(),
                        ticket.getDepartureAt(),
                        ticket.getReturnAt(),
                        ticket.getDepartureLocation(),
                        ticket.getArrivalLocation()
                ))
                .toList();

        return new FlightResponse(flightInfos);
    }

    public List<String> getReservedArrivalLocations(String username) {

        // 유저 이름으로 패신저 아이디 조회 - users

        // 패신저 아이디로 항공권 티켓 조회 - passenger

        // 항공권 티켓 아이디로 도착지 조회 - airline_ticket

        return Arrays.asList("파리", "런던");
    }
}
