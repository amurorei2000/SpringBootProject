package com.github.springbootproject.repository.storeSales;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class StoreSalesJdbcDao implements StoreSalesRepository{

    private JdbcTemplate jdbcTemplate;

    public StoreSalesJdbcDao(@Qualifier("jdbcTemplate2") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    static RowMapper<StoreSales> storeSalesRowMapper = ((rs, rowNum) ->
       new StoreSales(
               rs.getInt("id"),
               rs.getNString("store_name"),
               rs.getInt("amount")
       )
    );

    @Override
    public void updateSalesAmount(Integer storeId, Integer amount) {
        jdbcTemplate.update("UPDATE store_sales SET amount=? WHERE id=?", amount, storeId);
    }

    @Override
    public StoreSales findStoreSalesById(Integer storeId) {
        return jdbcTemplate.queryForObject("SELECT * FROM store_sales WHERE id=?",
                storeSalesRowMapper, storeId);
    }
}
