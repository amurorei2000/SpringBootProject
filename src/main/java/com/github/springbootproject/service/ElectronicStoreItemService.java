package com.github.springbootproject.service;

import com.github.springbootproject.repository.items.ElectronicStoreItemJpaRepository;
import com.github.springbootproject.repository.items.ElectronicStoreItemRepository;
import com.github.springbootproject.repository.items.ItemEntity;
import com.github.springbootproject.repository.storeSales.StoreSales;
import com.github.springbootproject.repository.storeSales.StoreSalesJpaRepository;
import com.github.springbootproject.repository.storeSales.StoreSalesRepository;
import com.github.springbootproject.service.exceptions.NotAcceptException;
import com.github.springbootproject.service.exceptions.NotFoundException;
import com.github.springbootproject.service.mapper.ItemMapper;
import com.github.springbootproject.web.dto.items.BuyOrder;
import com.github.springbootproject.web.dto.items.Item;
import com.github.springbootproject.web.dto.items.ItemBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElectronicStoreItemService {
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ElectronicStoreItemRepository electronicStoreItemRepository;
    private final ElectronicStoreItemJpaRepository electronicStoreItemJpaRepository;

    private final StoreSalesRepository storeSalesRepository;
    private final StoreSalesJpaRepository storeSalesJpaRepository;


    public List<Item> findAllItem() {
//        List<ItemEntity> itemEntities = electronicStoreItemRepository.findAllItems();
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAll();
        return itemEntities.stream()
                .map(ItemMapper.INSTANCE::itemEntityToItem)
                .collect(Collectors.toList());
    }


    public Integer saveItem(ItemBody itemBody) {
        ItemEntity itemEntity = ItemMapper.INSTANCE.idAndItemBodyToItemEntity(null, itemBody);
        ItemEntity itemEntityCreated;

        //        ItemEntity itemEntity = new ItemEntity(
//                null,   // auto increment
//                itemBody.getName(),
//                itemBody.getType(),
//                itemBody.getPrice(),
//                null,
//                0,
//                itemBody.getSpec().getCpu(),
//                itemBody.getSpec().getCapacity()
//        );

//        return electronicStoreItemRepository.saveItem(itemEntity);
        try {
            itemEntityCreated = electronicStoreItemJpaRepository.save(itemEntity);
        } catch(RuntimeException e) {
            throw new NotAcceptException("Item을 저장하는 도중에 Error 가 발생하였습니다.");
        }

        return itemEntityCreated.getId();
    }

    public Item findItemById(Integer id) {
//        ItemEntity itemEntity = electronicStoreItemRepository.findItemById(id)
        ItemEntity itemEntity = electronicStoreItemJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당하는 아이템이 없습니다."));

        return ItemMapper.INSTANCE.itemEntityToItem(itemEntity);
    }

    public List<Item> findItemsByIds(List<Integer> ids) {
//        Set<Integer> idSet = ids.stream().collect(Collectors.toSet());

//        List<ItemEntity> itemEntities = electronicStoreItemRepository.findAllItems();
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAll();

        if (itemEntities.isEmpty()) {
            throw new NotFoundException("아무 Items 들을 찾을 수 없습니다.");
        }

        return itemEntities.stream()
                .map(Item::new)
                .collect(Collectors.toList());
    }

    public void deleteItemByPathId(Integer id) {
//        electronicStoreItemRepository.deleteById(id);
        electronicStoreItemJpaRepository.deleteById(id);
    }

    @Transactional(transactionManager = "transactionManager1")
    public Item updateItem(Integer id, ItemBody itemBody) {
        ItemEntity itemEntityUpdated = electronicStoreItemJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 ID: " + id + "의 Item을 찾을 수 없습니다."));

//        ItemEntity itemEntity = ItemMapper.INSTANCE.idAndItemBodyToItemEntity(null, itemBody);

        // 조회한 값을 변경하면 JPA가 db의 값도 변경한다.
        itemEntityUpdated.setItemBody(itemBody);
        return ItemMapper.INSTANCE.itemEntityToItem(itemEntityUpdated);
    }

//    @Transactional(transactionManager="tm1")
    @Transactional(transactionManager="transactionManager1")
    public Integer buyItems(BuyOrder buyOrder) {
        // BuyOrder에서 상품 ID와 수량을 얻어낸다.
        Integer itemId = buyOrder.getItemId();
        Integer itemNums = buyOrder.getItemNums();

        // 상품을 조회하여 수량이 얼마나 있는지 확인한다.
//        ItemEntity itemEntity = electronicStoreItemRepository.findItemById(itemId)
        ItemEntity itemEntity = electronicStoreItemJpaRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("해당 ID: " + itemId + "의 Item을 찾을 수 없습니다."));

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

        if (successBuyItemNums == 4) {
            log.error("4개를 구매하는 건 허락되지 않습니다.");
            throw new RuntimeException("4개를 구매하는건 허락하지 않습니다.");
        }
        // 상품의 재고에 기존 계산한 재고를 구매하는 수량을 뺀다.
//        electronicStoreItemRepository.updateItemStock(itemId, itemEntity.getStock() - successBuyItemNums);
        itemEntity.setStock(itemEntity.getStock() - successBuyItemNums);


        // 상품 구매하는 수량 * 가격만큼 가계 매상으로 올린다.
//        StoreSales storeSales = storeSalesRepository.findStoreSalesById(itemEntity.getStoreId());
//        storeSalesRepository.updateSalesAmount(itemEntity.getStoreId(), storeSales.getAmount() + totalPrice);

        StoreSales storeSales = storeSalesJpaRepository.findById(itemEntity.getStoreId())
                        .orElseThrow(() -> new NotFoundException("요청하신 StoreId : " +  itemEntity.getStoreId() + "는 찾을 수 없습니다."));

        storeSales.setAmount(storeSales.getAmount() + totalPrice);

        return successBuyItemNums;
    }

    public List<Item> findItemsByTypes(List<String> types) {
        List<ItemEntity> storeSalesEntities = electronicStoreItemJpaRepository.findItemEntitiesByTypeIn(types);

        return storeSalesEntities.stream()
                .map(ItemMapper.INSTANCE::itemEntityToItem)
                .collect(Collectors.toList());
    }

    public List<Item> findItemsOrderByPrices(Integer maxValue) {
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findItemEntitiesByPriceLessThanEqualOrderByPriceAsc(maxValue);
        return  itemEntities.stream()
                .map(ItemMapper.INSTANCE::itemEntityToItem)
                .collect(Collectors.toList());
    }

    public Page<Item> findAllWithPageable(Pageable pageable) {
        Page<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAll(pageable);

        return itemEntities.map(ItemMapper.INSTANCE::itemEntityToItem);
    }

    public Page<Item> findAllWithPageable(List<String> types, Pageable pageable) {
        Page<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAllByTypeIn(types, pageable);

        return itemEntities.map(ItemMapper.INSTANCE::itemEntityToItem);
    }
}
