package com.github.springbootproject.repository.passenger;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "passengerId")
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "passenger")
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passenger_id")
    private Integer passengerId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "passport_num", length = 50)
    private String passportNum;
}
