package com.github.springbootproject.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity,Integer> {
}
