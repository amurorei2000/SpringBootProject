package com.github.springbootproject.web.dto.airline;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {
    private Integer userId;
    private Integer airlineTicketId;
}
