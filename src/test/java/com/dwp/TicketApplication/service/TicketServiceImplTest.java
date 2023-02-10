package com.dwp.TicketApplication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dwp.TicketApplication.Request.TicketTypeRequest;
import com.dwp.TicketApplication.entity.Purchaser;
import com.dwp.TicketApplication.exception.InvalidTicketRequestException;
import com.dwp.TicketApplication.repository.PurchaserRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

  @InjectMocks
  TicketServiceImpl ticketService;

  @Mock
  PurchaserRepository repository;

  //  @Value("${purchaser.max.qty}")
  private Integer purchaserMaxQty;

  @Test
  void contextLoads() {
    purchaserMaxQty = (Integer) ReflectionTestUtils.getField(ticketService, "ticketMaxQty");

    assertEquals(20, purchaserMaxQty);
  }

  @BeforeEach
  public void init() {
//    ReflectionTestUtils.setField(ticketService, "purchaserMaxQty", 20);}
    ReflectionTestUtils.setField(ticketService, "ticketMaxQty", 20);
  }

  @Test
  void testShouldBeAbleToPurchaseTickets() throws InvalidTicketRequestException {

    Purchaser purchaserBefore = new Purchaser(0, "Fred", 3, 4, 1, 7, 0, new Date());
    Purchaser purchaserAfter = new Purchaser(1, "Fred", 3, 4, 1, 7, 100, new Date());

    when(repository.save(purchaserBefore)).thenReturn(purchaserAfter);

    Purchaser buyer = ticketService.savePurchaser(
        TicketTypeRequest.builder().username("Fred").numAdultTickets(3).numChildTickets(4)
            .numInfantTickets(1).build());

    assertEquals(4, buyer.getNumChildTickets());
    assertEquals("Fred", buyer.getUsername());
    verify(repository, times(1)).save(purchaserBefore);
  }

  @Test
  void failToPurchaseTicketsTooManyInfants() throws InvalidTicketRequestException {
    Exception exception = assertThrows(InvalidTicketRequestException.class, () -> {
      ticketService.savePurchaser(TicketTypeRequest.builder().username("Roger").numAdultTickets(1).numChildTickets(4).numInfantTickets(2).build());
    });

    String expectedMessage = "Infants cannot exceed number of adults, not enough knees !";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }


  @Test
  void failToPurchaseTicketsNoAdults() throws InvalidTicketRequestException {
    Exception exception = assertThrows(InvalidTicketRequestException.class, () -> {
      ticketService.savePurchaser(TicketTypeRequest.builder().username("Roger").numAdultTickets(0).numChildTickets(4).numInfantTickets(2).build());
    });

    String expectedMessage = "At least 1 adult ticket purchase must be made.";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void testFindAllPurchasers() {
    List<Purchaser> purchasers = new ArrayList<>();
    Purchaser buyerOne = new Purchaser(1, "Ethan", 1, 1, 1, 2, 30, new Date());
    Purchaser buyerTwo = new Purchaser(2, "Eve", 4, 8, 2, 16, 160, new Date());
    purchasers.add(buyerOne);
    purchasers.add(buyerTwo);

    when(repository.findAll()).thenReturn(purchasers);

    List<Purchaser> purchasersList = ticketService.getPurchasers();

    assertEquals(2, purchasersList.size());
    verify(repository, times(1)).findAll();
  }

  @Test
  void testFindPurchasersById() {
    Purchaser buyerOne = new Purchaser(1, "Ethan", 1, 1, 1, 2, 30, new Date());
    when(repository.findById(anyInt())).thenReturn(Optional.of(buyerOne));

    Purchaser p = ticketService.getPurchaserById(1);

    assertEquals("Ethan", p.getUsername());
    verify(repository, times(1)).findById(anyInt());
  }

  @Test
  void testFindPurchasersByUserName() {
    List<Purchaser> purchasers = new ArrayList<>();
    Purchaser buyerOne = new Purchaser(1, "Ethan", 1, 1, 1, 2, 30, new Date());
    purchasers.add(buyerOne);
    when(repository.findByUsername(anyString())).thenReturn(purchasers);

    List<Purchaser> p = ticketService.getPurchaserByUsername("Ethan");

    assertEquals(1, p.size());
    assertEquals("Ethan", p.get(0).getUsername());
    verify(repository, times(1)).findByUsername(anyString());
  }
}