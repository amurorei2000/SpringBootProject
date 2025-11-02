package com.github.springbootproject.repository.reservations;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FlightPriceAndCharge {
    private Double flightPrice;
    private Double charge;
}
