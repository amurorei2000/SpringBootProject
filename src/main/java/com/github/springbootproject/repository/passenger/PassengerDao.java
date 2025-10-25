package com.github.springbootproject.repository.passenger;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class PassengerDao implements PassengerRepository {
    private JdbcTemplate jdbcTemplate;

    static RowMapper<Passenger> passengerRowMapper = ((rs, rowNum) ->
            new Passenger.PassengerBuilder()
                    .passengerId(rs.getInt("passenger_id"))
                    .userId(rs.getInt("user_id"))
                    .passportNum(rs.getNString("passport_num"))
                    .build()
    );

    public PassengerDao(@Qualifier("jdbcTemplate2") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Passenger> findPassengerByUserId(Integer userId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM passenger WHERE user_id = ?",
                    passengerRowMapper, userId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Passenger> findPassengerByUserIds(List<Integer> userIds) {
        String idPlaceholder = String.join(", ", Collections.nCopies(userIds.size(), "?"));

        return jdbcTemplate.query("SELECT * FROM passenger WHERE user_id in (" + idPlaceholder + ")",
                passengerRowMapper, userIds.toArray());
    }
}
