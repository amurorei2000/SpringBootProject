package com.github.springbootproject.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
@Slf4j
class AirReservationControllerSpringTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Find Airline Tickets 성공")
    void findAirlineTicketsCase1() throws Exception {
        // given
        Integer userId = 5;
        String ticketType = "왕복";

        // when & then
        String content = mockMvc.perform(
                get("/v1/api/air-reservation/tickets")
                        .param("user-id", userId.toString())
                        .param("airline-ticket-type", ticketType)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        log.info("결과: " + content);
    }

    @Test
    @DisplayName("Find Airline Tickets 실패")
    void findAirlineTicketsCase2() throws Exception {
        // given
        Integer userId = 5;
        String ticketType = "왕";

        // when & then
        String content = mockMvc.perform(
                        get("/v1/api/air-reservation/tickets")
                                .param("user-id", userId.toString())
                                .param("airline-ticket-type", ticketType)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        log.info("결과: " + content);
    }
}