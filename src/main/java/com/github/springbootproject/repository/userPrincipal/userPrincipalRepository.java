package com.github.springbootproject.repository.userPrincipal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userPrincipalRepository extends JpaRepository<UserPrincipal, Integer> {

    @Query("SELECT up FROM UserPrincipal up JOIN FETCH up.userPrincipalRoles upr JOIN FETCH up.user u")
    Optional<UserPrincipal> findByEmailFetchJoin(String email);

    boolean existsUserPrincipalByEmail(String email);
}
