package com.github.springbootproject.service;

import com.github.springbootproject.repository.items.ElectronicStoreItemRepository;
import com.github.springbootproject.repository.items.ItemEntity;
import com.github.springbootproject.repository.storeSales.StoreSales;
import com.github.springbootproject.repository.storeSales.StoreSalesRepository;
import com.github.springbootproject.web.dto.BuyOrder;
import com.github.springbootproject.web.dto.Item;
import com.github.springbootproject.web.dto.ItemBody;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ElectronicStoreItemService {
    private ElectronicStoreItemRepository electronicStoreItemRepository;
    private StoreSalesRepository storeSalesRepository;

    public ElectronicStoreItemService(ElectronicStoreItemRepository electronicStoreItemRepository, StoreSalesRepository storeSalesRepository) {
        this.electronicStoreItemRepository = electronicStoreItemRepository;
        this.storeSalesRepository = storeSalesRepository;
    }


    public List<Item> findAllItem() {
        List<ItemEntity> itemEntities = electronicStoreItemRepository.findAllItems();
        return itemEntities.stream()
                .map(Item::new)
                .collect(Collectors.toList());
    }


    public Integer saveItem(ItemBody itemBody) {
        ItemEntity itemEntity = new ItemEntity(
                null,   // auto increment
                itemBody.getName(),
                itemBody.getType(),
                itemBody.getPrice(),
                null,
                0,
                itemBody.getSpec().getCpu(),
                itemBody.getSpec().getCapacity()
        );

        return electronicStoreItemRepository.saveItem(itemEntity);
    }

    public Item findItemById(Integer id) {
        ItemEntity foundedItem = electronicStoreItemRepository.findItemById(id);
        return new Item(foundedItem);
    }

    public List<Item> findItemsByIds(List<Integer> ids) {
        Set<Integer> idSet = ids.stream().collect(Collectors.toSet());

        List<ItemEntity> foundedEntities = electronicStoreItemRepository.findItemByIds(idSet);

        return foundedEntities.stream()
                .map(Item::new)
                .collect(Collectors.toList());
    }

    public void deleteItemByPathId(Integer id) {
        electronicStoreItemRepository.deleteById(id);
    }

    public Item updateItem(Integer id, ItemBody itemBody) {
        ItemEntity itemEntity = new ItemEntity(id,
                itemBody.getName(),
                itemBody.getType(),
                itemBody.getPrice(),
                null,
                0,
                itemBody.getSpec().getCpu(),
                itemBody.getSpec().getCapacity());

        ItemEntity itemEntityUpdated = electronicStoreItemRepository.updateItemEntity(id, itemEntity);
        return new Item(itemEntityUpdated);
    }

    @Transactional(transactionManager="tm1")
    public Integer buyItems(BuyOrder buyOrder) {
        // BuyOrder에서 상품 ID와 수량을 얻어낸다.
        Integer itemId = buyOrder.getItemId();
        Integer itemNums = buyOrder.getItemNums();

        // 상품을 조회하여 수량이 얼마나 있는지 확인한다.
        ItemEntity itemEntity = electronicStoreItemRepository.findItemById(itemId);

        // 단, 재고가 아예 없거나 매장을 찾을 수 없으면 살 수 없다.
        if (itemEntity.getStoreId() == null) throw new RuntimeException("매장을 찾을 수 없습니다.");
        if (itemEntity.getStock() <= 0) throw new RuntimeException("상품의 재고가 없습니다.");

        Integer successBuyItemNums;
        if (itemNums > itemEntity.getStock()) {
            successBuyItemNums = itemEntity.getStock();
        } else {
            successBuyItemNums = itemNums;
        }

        // 상품의 수량과 가격을 가지고 계산하여 총 가격을 구한다.
        Integer totalPrice = successBuyItemNums * itemEntity.getPrice();

        // 상품의 재고에 기존 계산한 재고를 구매하는 수량을 뺀다.
        electronicStoreItemRepository.updateItemStock(itemId, itemEntity.getStock() - successBuyItemNums);

        if (successBuyItemNums == 4) throw new RuntimeException("4개를 구매하는건 허락하지 않습니다.");

        // 상품 구매하는 수량 * 가격만큼 가계 매상으로 올린다.
        StoreSales storeSales = storeSalesRepository.findStoreSalesById(itemEntity.getStoreId());
        storeSalesRepository.updateSalesAmount(itemEntity.getStoreId(), storeSales.getAmount() + totalPrice);

        return successBuyItemNums;
    }
}
