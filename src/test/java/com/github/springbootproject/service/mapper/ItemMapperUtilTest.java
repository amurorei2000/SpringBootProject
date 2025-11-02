package com.github.springbootproject.service.mapper;

import com.github.springbootproject.repository.items.ItemEntity;
import com.github.springbootproject.repository.storeSales.StoreSales;
import com.github.springbootproject.web.dto.items.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
class ItemMapperUtilTest {

    @Test
    @DisplayName("ItemEntity의 itemEntityToItem 메소드 테스트")
    void itemEntityToItem() {
        // given
        ItemEntity itemEntity = ItemEntity.builder()
                .id(1)
                .name("name")
                .type("type")
                .price(1000)
                .stock(0)
                .cpu("CPU 1")
                .capacity("5G")
                .storeSales(new StoreSales())
                .build();

        // when
        Item item = ItemMapper.INSTANCE.itemEntityToItem(itemEntity);

        // then
        log.info("만들어진 item: " + item);
        assertEquals(itemEntity.getPrice(), item.getPrice());
        assertEquals(itemEntity.getId(), item.getId());
        assertEquals(itemEntity.getCpu(), item.getSpec().getCpu());
        assertEquals(itemEntity.getCapacity(), item.getSpec().getCapacity());
    }
}