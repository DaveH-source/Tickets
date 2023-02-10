package com.dwp.TicketApplication.controller;

import com.dwp.TicketApplication.Request.TicketTypeRequest;
import com.dwp.TicketApplication.entity.Purchaser;
import com.dwp.TicketApplication.Response.PurchaserResponse;
import com.dwp.TicketApplication.exception.InvalidTicketRequestException;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface PurchaserContract {

  /**
   * Used to buy tickets
   *
   * @param purchaser
   * @return purchaser
   */
  ResponseEntity<PurchaserResponse> addPurchaser(TicketTypeRequest ticketTypeRequest) throws InvalidTicketRequestException;

  /**
   * Retrieve all purchasers of tickets
   *
   * @return purchaser
   */
  ResponseEntity<List<Purchaser>> findAllPurchasers();

  /**
   * Retrieve all tickets for a particular Id
   *
   * @param id
   * @return
   */
  ResponseEntity<Purchaser> findPurchaserById(int id);

  /**
   * Retrieve all tickets for a particular user
   *
   * @param username
   * @return
   */
  ResponseEntity<List<Purchaser>> findPurchaserByUsername(String username);
}
