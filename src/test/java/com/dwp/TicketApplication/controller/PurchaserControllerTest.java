package com.dwp.TicketApplication.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dwp.TicketApplication.Request.TicketTypeRequest;
import com.dwp.TicketApplication.Response.PurchaserResponse;
import com.dwp.TicketApplication.entity.Purchaser;
import com.dwp.TicketApplication.exception.InvalidTicketRequestException;
import com.dwp.TicketApplication.service.TicketServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaserControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TicketServiceImpl ticketService;

  @Test
  void shouldBeAbleToBuyTicketsSuccessfully() throws Exception{
 Purchaser purchaser = new Purchaser(1, "Dave", 5, 7, 2, 12, 170, new Date());
    PurchaserResponse response = new PurchaserResponse(true, "", purchaser);
    doReturn(purchaser).when(ticketService).savePurchaser(any());

    mockMvc.perform(post("/buy-tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(purchaser)))

        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success", Matchers.is(true)));
  }

  @Test
  void failToBuyTicketsWhenNoUserSupplied() throws Exception{

    TicketTypeRequest request = TicketTypeRequest.builder().username(null).numAdultTickets(2).numChildTickets(2).numInfantTickets(1).build();
    doThrow(new InvalidTicketRequestException("No user details provided."))
        .when(ticketService)
        .savePurchaser(request);

    mockMvc.perform(post("/buy-tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(request)))

        .andExpect(status().isBadRequest())
        .andExpect(content().string("{\"success\":false,\"errorMessage\":\"No user details provided.\",\"payload\":null}"));
  }

  @Test
  void failToBuyTicketsWhenNumberOfTicketsGreaterThanTwenty() throws Exception{

    TicketTypeRequest request = TicketTypeRequest.builder().username("Bob").numAdultTickets(20).numChildTickets(2).numInfantTickets(1).build();
    doThrow(new InvalidTicketRequestException("A maximum of 20 tickets can be purchased at a time."))
        .when(ticketService)
        .savePurchaser(request);

    mockMvc.perform(post("/buy-tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(request)))

        .andExpect(status().isBadRequest())
        .andExpect(content().string("{\"success\":false,\"errorMessage\":\"A maximum of 20 tickets can be purchased at a time.\",\"payload\":null}"));
  }

  @Test
  void shouldFindAllPurchasedTickets() throws Exception {

    List<Purchaser> purchasers = new ArrayList<>();
    purchasers.add(new Purchaser(1, "Dave", 5, 7, 2, 12, 170, new Date()));
    purchasers.add(new Purchaser(2, "Pauline", 2, 3, 0, 5, 70, new Date()));

    when(ticketService.getPurchasers()).thenReturn(purchasers);
    mockMvc.perform(
            get("/list-purchasers")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(2)));

    verify(ticketService, times(1))
        .getPurchasers();
  }

  @Test
  void shouldFindPurchasedTicketsById() throws Exception {

    Purchaser purchaser = new Purchaser(1, "Dave", 5, 7, 2, 12, 170, new Date());
    when(ticketService.getPurchaserById(1)).thenReturn(purchaser);
    mockMvc.perform(
            get("/purchasers-by-id/1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username", Matchers.is("Dave")));

    verify(ticketService, times(1))
        .getPurchaserById(1);
  }

  @Test
  void shouldFindPurchasedTicketsByUsername() throws Exception {
    List<Purchaser> purchasers = new ArrayList<>();
    purchasers.add(new Purchaser(1, "Dave", 5, 7, 2, 12, 170, new Date()));
    purchasers.add(new Purchaser(2, "Pauline", 2, 3, 0, 5, 70, new Date()));

    when(ticketService.getPurchaserByUsername(isA(String.class))).thenReturn(purchasers);

    mockMvc.perform(
            get("/purchasers-by-name/\"Pauline\"")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[1]username", Matchers.is("Pauline")));

      verify(ticketService, times(1))
        .getPurchaserByUsername(isA(String.class));
  }

  static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}