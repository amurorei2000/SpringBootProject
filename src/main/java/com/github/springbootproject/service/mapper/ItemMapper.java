package com.github.springbootproject.service.mapper;

import com.github.springbootproject.repository.items.ItemEntity;
import com.github.springbootproject.web.dto.items.Item;
import com.github.springbootproject.web.dto.items.ItemBody;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {

    // 싱글톤
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    // 메소드
    @Mapping(target = "spec.cpu", source = "cpu")
    @Mapping(target = "spec.capacity", source = "capacity")
    Item itemEntityToItem(ItemEntity itemEntity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "itemBody.spec.cpu", target = "cpu")
    @Mapping(source = "itemBody.spec.capacity", target = "capacity")
    @Mapping(source = "itemBody.name", target = "name")
    @Mapping(source = "itemBody.type", target = "type")
    @Mapping(source = "itemBody.price", target = "price")
    @Mapping(target = "storeSales", ignore = true)
    @Mapping(constant = "0", target = "stock")
    ItemEntity idAndItemBodyToItemEntity(Integer id, ItemBody itemBody);
}
