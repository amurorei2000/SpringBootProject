package com.github.springbootproject.repository.passenger;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "passengerId")
@AllArgsConstructor
@Builder
public class Passenger {
    private Integer passengerId;
    private Integer userId;
    private String passportNum;
}
