package com.github.springbootproject.web.dto.items;

import com.github.springbootproject.repository.items.ItemEntity;
import com.github.springbootproject.repository.storeSales.StoreSales;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class StoreInfo {

    private Integer id;
    private String storeName;
    private Integer amount;
    private List<String> itemNames;

    public StoreInfo(StoreSales storeSales) {
        this.id = storeSales.getId();
        this.storeName = storeSales.getStoreName();
        this.amount = storeSales.getAmount();
        this.itemNames = storeSales.getItemEntities().stream()
                .map(ItemEntity::getName)
                .toList();
    }
}
