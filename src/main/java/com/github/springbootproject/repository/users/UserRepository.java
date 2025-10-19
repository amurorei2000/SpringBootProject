package com.github.springbootproject.repository.users;

public interface UserRepository {
    UserEntity findUserById(Integer userId);
}
