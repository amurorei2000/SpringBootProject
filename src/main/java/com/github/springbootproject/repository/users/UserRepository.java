package com.github.springbootproject.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
//    Optional<UserEntity> findUserById(Integer userId);
//    Optional<UserEntity> findByUserName(String userName);
}
