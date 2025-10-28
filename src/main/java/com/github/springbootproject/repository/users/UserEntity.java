package com.github.springbootproject.repository.users;

import com.github.springbootproject.repository.passenger.Passenger;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "userId")
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name", length = 20)
    private String userName;

    @Column(name = "like_travel_place", length = 30)
    private String likeTravelPlace;

    @Column(name = "phone_num", length = 30)
    private String phoneNum;

    @OneToOne(mappedBy = "user")
    private Passenger passenger;
}
