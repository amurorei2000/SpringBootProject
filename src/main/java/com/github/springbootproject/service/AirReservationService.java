package com.github.springbootproject.service;

import com.github.springbootproject.repository.airlineTicket.AirlineTicket;
import com.github.springbootproject.repository.airlineTicket.AirlineTicketAndFlightInfo;
import com.github.springbootproject.repository.airlineTicket.AirlineTicketJpaRepository;
import com.github.springbootproject.repository.airlineTicket.AirlineTicketRepository;
import com.github.springbootproject.repository.flight.Flight;
import com.github.springbootproject.repository.flight.FlightJpaRepository;
import com.github.springbootproject.repository.passenger.Passenger;
import com.github.springbootproject.repository.passenger.PassengerJpaRepository;
import com.github.springbootproject.repository.passenger.PassengerRepository;
import com.github.springbootproject.repository.reservations.Reservation;
import com.github.springbootproject.repository.reservations.ReservationJpaRepository;
import com.github.springbootproject.repository.reservations.ReservationRepository;
import com.github.springbootproject.repository.users.UserEntity;
import com.github.springbootproject.repository.users.UserJpaRepository;
import com.github.springbootproject.repository.users.UserRepository;
import com.github.springbootproject.service.exceptions.InvalidValueException;
import com.github.springbootproject.service.exceptions.NotAcceptException;
import com.github.springbootproject.service.exceptions.NotFoundException;
import com.github.springbootproject.web.dto.airline.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirReservationService {
//    private final UserRepository userRepository;
//    private final AirlineTicketRepository airlineTicketRepository;
//    private final PassengerRepository passengerRepository;
//    private final ReservationRepository reservationRepository;
    private final UserJpaRepository userJpaRepository;
    private final AirlineTicketJpaRepository airlineTicketJpaRepository;
    private final PassengerJpaRepository passengerJpaRepository;
    private final ReservationJpaRepository reservationJpaRepository;
    private final FlightJpaRepository flightJpaRepository;

    public List<Ticket> findUserFavoritePlaceTickets(Integer userId, String ticketType) {
        // 유저를 userId로 가져와서 선호하는 여행지 도출
        Set<String> ticketTypeSet = new HashSet<>(Arrays.asList("편도", "왕복"));

        if (!ticketTypeSet.contains(ticketType)) {
            throw new InvalidValueException("해당 TicketType " + ticketType + "은 지원하지 않습니다.");
        }

        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 ID: " + userId + " 유저를 찾을 수 없습니다."));
        String likePlace = userEntity.getLikeTravelPlace();

        // 선호하는 여행지와 ticketType으로 AirlineTicket table 질의해서 필요한 AirlineTicket 정보를 가져온다.
        List<AirlineTicket> airlineTickets = airlineTicketJpaRepository.findAllByArrivalLocationAndTicketType(likePlace, ticketType);


        if (airlineTickets.isEmpty()) {
            throw new NotFoundException("해당 likePalce: " + likePlace + "와 TicketType: " + ticketType + "에 해당하는 항공편을 찾을 수 없습니다.");
        }

        // 이 둘의 정보를 조합해서 Ticket DTO를 만든다.
        return airlineTickets.stream()
                .map(ticket -> new Ticket())
                .collect(Collectors.toList());
    }

    @Transactional(transactionManager="transactionManager2")
    public ReservationResult makeReservation(ReservationRequest reservationRequest) {
        // passenger 정보 가져오기
        Passenger passenger = passengerJpaRepository.findPassengerByUser_UserId(reservationRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("요청하신 userId " + reservationRequest.getUserId() + "에 해당하는 Passenger를 찾을 수 없습니다."));

        // price 등의 정보 가져오기
        Integer airlineTicketId = reservationRequest.getAirlineTicketId();
        AirlineTicket airlineTicket = airlineTicketJpaRepository.findById(airlineTicketId)
                .orElseThrow(() -> new NotFoundException("airlineTicket을 찾을 수 없습니다."));

        List<Flight> flightList = airlineTicket.getFlightList();

        if (flightList.isEmpty()) {
            throw new NotFoundException("AirlineTicket Id" + airlineTicketId + " 에 해당하는 항공편과 항공권을 찾을 수 없습니다.");
        }

        // reservation 생성
        Boolean isSuccess = false;
        Reservation reservation = new Reservation(passenger, airlineTicket);

        try {
            reservationJpaRepository.save(reservation);
            isSuccess = true;
        } catch (RuntimeException e) {
            throw new NotAcceptException("Reservation이 등록되는 과정이 거부되었습니다.");
        }

        // ReservationResult DTO 만들기
        List<Integer> prices = flightList.stream()
                .map(Flight::getFlightPrice)
                .map(Double::intValue)
                .collect(Collectors.toList());

        List<Integer> charges = flightList.stream()
                .map(Flight::getCharge)
                .map(Double::intValue)
                .collect(Collectors.toList());

        Integer tax = airlineTicket.getTax().intValue();

        Integer totalPrice = airlineTicket.getTotalPrice().intValue();

        return new ReservationResult(
                prices, charges, tax, totalPrice, isSuccess
        );
    }

    @Transactional(transactionManager = "transactionManager2")
    public Integer makeReservations(PaymentsRequest paymentsRequest) {

        List<Integer> userIds = paymentsRequest.getUser_ids();
        List<Integer> ticketIds = paymentsRequest.getAirline_ticket_ids();
        List<AirlineTicket> airlineTickets =  ticketIds.stream()
                .map(ticketId -> airlineTicketJpaRepository.findById(ticketId).get())
                .toList();

        if (userIds.isEmpty() || ticketIds.isEmpty()) {
            return 0;
        }

        if (userIds.size() != ticketIds.size()) {
            return 0;
        }

        // Passneger 리스트 가져오기
        List<Passenger> passengers = userIds.stream()
                .map(passenger -> passengerJpaRepository.findById(passenger).get())
                .toList();

        // reservation 생성
        Integer reservationCount = 0;

        for (int i = 0; i< passengers.size(); i++) {
            Reservation reservation = new Reservation(passengers.get(i), airlineTickets.get(i));
            reservationJpaRepository.save(reservation);
            reservationCount++;
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

    @Transactional(transactionManager = "transactionManager2")
    public List<String> getReservedArrivalLocations(String username) {

        // 유저 이름으로 패신저 아이디 조회 - users
        UserEntity user = userJpaRepository.findByUserName(username);
        Passenger passenger = passengerJpaRepository.findPassengerByUser_UserId(user.getUserId())
                .orElseThrow(() -> new NotFoundException("해당 사용자 이름 " + username + "에 해당하는 passenger가 없습니다."));

        // 패신저 아이디로 항공권 티켓 조회 - passenger
        List<Reservation> reservations = reservationJpaRepository.findByPassenger_PassengerId(passenger.getPassengerId());
        List<AirlineTicket> airlineTickets = reservations.stream().map(Reservation::getAirlineTicket).toList();

        // 항공권 티켓 아이디로 도착지 조회 - airline_ticket
        List<String> locations = airlineTickets.stream()
                .map(AirlineTicket::getArrivalLocation)
                .toList();

        locations.forEach(location -> log.info("locations: " + locations));


        return locations;
    }`}
