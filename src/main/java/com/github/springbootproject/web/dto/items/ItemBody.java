package com.github.springbootproject.web.dto.items;

import com.github.springbootproject.web.dto.items.Spec;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemBody {
    @Schema(description = "Item 이름", example = "Dell XPS 15")
    private String name;

    @Schema(description = "Item 기기 타입", example = "Laptop")
    private String type;

    @Schema(description = "Item 가격", example = "125000")
    private Integer price;

    private Spec spec;
}
