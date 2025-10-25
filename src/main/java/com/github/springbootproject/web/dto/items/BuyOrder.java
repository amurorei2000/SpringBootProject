package com.github.springbootproject.web.dto.items;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BuyOrder {
    private Integer itemId;
    private Integer itemNums;
}
