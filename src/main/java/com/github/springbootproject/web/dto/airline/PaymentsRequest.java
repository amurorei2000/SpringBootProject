package com.github.springbootproject.web.dto.airline;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PaymentsRequest {
    List<Integer> user_ids;
    List<Integer> airline_ticket_ids;
}
