package com.github.springbootproject.web.dto.items;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Spec {
    @Schema(description = "아이템 CPU", example = "Google Tensor")
    private String cpu;

    @Schema(description = "아이디 용량 스펙", example = "25GB")
    private String capacity;
}
