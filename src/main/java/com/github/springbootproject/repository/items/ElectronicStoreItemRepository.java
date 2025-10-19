package com.github.springbootproject.repository.items;

import java.util.List;
import java.util.Set;

public interface ElectronicStoreItemRepository {
    List<ItemEntity> findAllItems();

    Integer saveItem(ItemEntity itemEntity);

    ItemEntity findItemById(Integer id);

    void deleteById(Integer id);

    List<ItemEntity> findItemByIds(Set<Integer> idSet);


    ItemEntity updateItemEntity(Integer id, ItemEntity itemEntity);

    void updateItemStock(Integer itemId, Integer stock);
}
