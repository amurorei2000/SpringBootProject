package com.github.springbootproject.repository.passenger;

import com.github.springbootproject.repository.items.ItemEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PassengerDao implements PassengerRepository {
    private JdbcTemplate jdbcTemplate;

    static RowMapper<Passenger> passengerRowMapper = ((rs, rowNum) ->
            new Passenger(
                    rs.getInt("passenger_id"),
                    rs.getInt("user_id"),
                    rs.getNString("passport_num")
            )
    );

    public PassengerDao(@Qualifier("jdbcTemplate2") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Passenger findPassengerByUserId(Integer userId) {
        return jdbcTemplate.queryForObject("SELECT * FROM passenger WHERE user_id = ?",
                passengerRowMapper, userId);
    }
}
