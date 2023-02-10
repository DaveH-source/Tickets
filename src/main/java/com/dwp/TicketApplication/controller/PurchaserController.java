package com.dwp.TicketApplication.controller;

import com.dwp.TicketApplication.Request.TicketTypeRequest;
import com.dwp.TicketApplication.entity.Purchaser;
import com.dwp.TicketApplication.Response.PurchaserResponse;
import com.dwp.TicketApplication.exception.InvalidTicketRequestException;
import com.dwp.TicketApplication.service.TicketServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PurchaserController implements PurchaserContract {

  @Autowired
  private TicketServiceImpl ticketService;

  @PostMapping("/buy-tickets")
  public ResponseEntity<PurchaserResponse> addPurchaser(@RequestBody TicketTypeRequest ticketTypeRequest)
      throws InvalidTicketRequestException {
    try {
      return new ResponseEntity<PurchaserResponse>(new PurchaserResponse(true, null, ticketService.savePurchaser(ticketTypeRequest)),
          HttpStatus.CREATED);
    } catch (InvalidTicketRequestException itre) {
      return new ResponseEntity<PurchaserResponse>(new PurchaserResponse(false, itre.getMessage(), null), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/list-purchasers")
  public ResponseEntity<List<Purchaser>> findAllPurchasers() {
    return new ResponseEntity<>(ticketService.getPurchasers(), HttpStatus.OK);
  }

  @GetMapping("/purchasers-by-id/{id}")
  public ResponseEntity<Purchaser> findPurchaserById(@PathVariable int id) {
    return new ResponseEntity<>(ticketService.getPurchaserById(id), HttpStatus.OK);
  }

  @GetMapping("/purchasers-by-name/{username}")
  public ResponseEntity<List<Purchaser>> findPurchaserByUsername(@PathVariable String username) {
    return new ResponseEntity<>(ticketService.getPurchaserByUsername(username), HttpStatus.OK);
  }
}
