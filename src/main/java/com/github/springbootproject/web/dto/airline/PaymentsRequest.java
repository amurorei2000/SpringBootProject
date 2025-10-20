package com.github.springbootproject.web.dto.airline;

import java.util.List;

public class PaymentsRequest {
    List<Integer> user_ids;
    List<Integer> airline_ticket_ids;

    public PaymentsRequest() {
    }

    public List<Integer> getUser_ids() {
        return user_ids;
    }


    public List<Integer> getAirline_ticket_ids() {
        return airline_ticket_ids;
    }

}
