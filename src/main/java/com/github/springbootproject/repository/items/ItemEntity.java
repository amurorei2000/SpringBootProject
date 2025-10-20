package com.github.springbootproject.repository.items;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Builder
public class ItemEntity {
    private Integer id;
    private String name;
    private String type;
    private Integer price;
    private Integer storeId;
    private Integer stock;
    private String cpu;
    private String capacity;

}
