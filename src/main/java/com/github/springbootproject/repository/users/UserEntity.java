package com.github.springbootproject.repository.users;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "userId")
@AllArgsConstructor
@Builder
public class UserEntity {
    private Integer userId;
    private String userName;
    private String likeTravelPlace;
    private String phoneNum;
}
