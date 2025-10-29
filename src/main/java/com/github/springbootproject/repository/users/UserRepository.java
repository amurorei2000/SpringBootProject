package com.github.springbootproject.repository.users;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    Optional<UserEntity> findUserById(Integer userId);
}
