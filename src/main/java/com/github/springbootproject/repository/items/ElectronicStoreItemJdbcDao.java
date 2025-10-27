package com.github.springbootproject.repository.items;

import com.github.springbootproject.repository.storeSales.StoreSales;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class ElectronicStoreItemJdbcDao implements ElectronicStoreItemRepository {

    private final JdbcTemplate jdbcTemplate;

    static RowMapper<ItemEntity> itemEntityRowMapper = ((rs, rowNum) ->
                new ItemEntity.ItemEntityBuilder()
                        .id(rs.getInt("id"))
                        .name(rs.getNString("name"))
                        .type(rs.getNString("type"))
                        .price(rs.getInt("price"))
                        .storeSales(new StoreSales(rs.getInt("store_id"), "", 10))
                        .stock(rs.getInt("stock"))
                        .cpu(rs.getNString("cpu"))
                        .capacity(rs.getNString("capacity"))
                        .build()
            );

    public ElectronicStoreItemJdbcDao(@Qualifier("jdbcTemplate1") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ItemEntity> findAllItems() {
        return jdbcTemplate.query("SELECT * FROM item", itemEntityRowMapper);
    }

    @Override
    public Integer saveItem(ItemEntity itemEntity) {
        jdbcTemplate.update("INSERT INTO item(name, type, price, cpu, capacity) " +
                                "VALUES (?, ?, ?, ?, ?)",
                itemEntity.getName(),
                itemEntity.getType(),
                itemEntity.getPrice(),
                itemEntity.getStoreSales().getId(),
                itemEntity.getStock(),
                itemEntity.getCpu(),
                itemEntity.getCapacity()
        );

        ItemEntity foundedItem = jdbcTemplate.queryForObject("SELECT * FROM item WHERE name = ?",
                itemEntityRowMapper, itemEntity.getName());

        return foundedItem.getId();
    }

    @Override
    public Optional<ItemEntity> findItemById(Integer id) {

        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM item WHERE id = ?",
                itemEntityRowMapper, id));
    }

    @Override
    public void deleteById(Integer id) {
        jdbcTemplate.update("DELETE FROM item WHERE id = ?", id);
    }

    @Override
    public List<ItemEntity> findItemByIds(Set<Integer> idSet) {
        String placeHolder = idSet.stream()
                .map(id -> "?")
                .collect(Collectors.joining(", "));

        return jdbcTemplate.query("SELECT * FROM item WHERE id IN (" +
                placeHolder + ")",
                itemEntityRowMapper, idSet.toArray());
    }

    @Override
    public ItemEntity updateItemEntity(Integer id, ItemEntity itemEntity) {
        jdbcTemplate.update("UPDATE item " +
                        "SET name=?, type=?, price=?, cpu=?, capacity=? " +
                        " WHERE id=?",
                itemEntity.getName(), itemEntity.getType(), itemEntity.getPrice(),
                itemEntity.getCpu(), itemEntity.getCapacity(), id);

        return jdbcTemplate.queryForObject("SELECT * " +
                "FROM item " +
                "WHERE id = ?",
                itemEntityRowMapper, id);
    }

    @Override
    public void updateItemStock(Integer itemId, Integer stock) {
        jdbcTemplate.update("UPDATE item SET stock=? WHERE id=?", stock, itemId);
    }


}
