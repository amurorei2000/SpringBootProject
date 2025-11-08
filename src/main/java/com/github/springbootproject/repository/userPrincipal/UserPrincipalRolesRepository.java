package com.github.springbootproject.repository.userPrincipal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPrincipalRolesRepository extends JpaRepository<UserPrincipalRoles, Integer> {

    @Query("SELECT up FROM UserPrincipal up JOIN FETCH up.userPrincipalRoles upr JOIN FETCH upr.roles WHERE up.email = :email")
    public Optional<UserPrincipal> findByEmailFetchJoin (String email);
}
