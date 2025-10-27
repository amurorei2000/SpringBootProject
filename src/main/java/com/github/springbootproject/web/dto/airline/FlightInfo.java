package com.github.springbootproject.web.dto.airline;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class FlightInfo {
    @Schema(description = "flightId", example = "1")
    private Integer flightId;

    @Schema(description = "departAt", example = "2025-10-28T12:32:00")
    private LocalDateTime departAt;

    @Schema(description = "arrivalAt", example = "2025-10-28T13:00:00")
    private LocalDateTime arrivalAt;

    @Schema(description = "departureLocation", example = "서울")
    private String departureLocation;

    @Schema(description = "arrivalLocation", example = "런던")
    private String arrivalLocation;

}
