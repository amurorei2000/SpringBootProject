package com.github.springbootproject.web.dto.items;

import com.github.springbootproject.repository.items.ItemEntity;
import com.github.springbootproject.web.dto.items.ItemBody;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Item {
    @Schema(description = "Item id", example = "1")
    private Integer id;

    @Schema(description = "Item 이름", example = "Dell XPS 15")
    private String name;

    @Schema(description = "Item 기기 타입", example = "Laptop")
    private String type;

    @Schema(description = "Item 가격", example = "125000")
    private Integer price;

    @Schema(description = "Item 스토어 아이디", example = "1")
    private Integer store_id;

    @Schema(description = "Item 재고량", example = "10")
    private Integer stock;

    private Spec spec;

    public Item(Integer id, String name, String type, Integer price, Integer store_id, Integer stock, Spec spec) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.store_id = store_id;
        this.stock = stock;
        this.spec = spec;
    }

    public Item(Integer id, String name, String type, Integer price, Integer store_id, Integer stock, String cpu, String capacity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.store_id = store_id;
        this.stock = stock;
        this.spec = new Spec(cpu, capacity);
    }

    public Item(Integer id, ItemBody itemBody) {
        this.id = id;
        this.name = itemBody.getName();
        this.type = itemBody.getType();
        this.price = itemBody.getPrice();
        this.store_id = null;
        this.stock = 0;
        this.spec = itemBody.getSpec();
    }

    public Item(ItemEntity itemEntity) {
        this.id = itemEntity.getId();
        this.name = itemEntity.getName();
        this.type = itemEntity.getType();
        this.price = itemEntity.getPrice();
        this.store_id = itemEntity.getStoreId();
        this.stock = itemEntity.getStock();
        this.spec = new Spec(itemEntity.getCpu(), itemEntity.getCapacity());
    }

}
