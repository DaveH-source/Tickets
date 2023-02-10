package com.dwp.TicketApplication.service;

import com.dwp.TicketApplication.Request.TicketTypeRequest;
import com.dwp.TicketApplication.entity.Purchaser;
import com.dwp.TicketApplication.exception.InvalidTicketRequestException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface TicketService {

  /**
   * Used to purchase tickets and reserve seats
   * @param ticketTypeRequest
   * @return
   * @throws InvalidTicketRequestException
   */
  Purchaser savePurchaser(TicketTypeRequest ticketTypeRequest) throws InvalidTicketRequestException;

  /**
   * List all historical ticket purchases
   * @return
   */
  List<Purchaser> getPurchasers();

  /**
   * List purchases made for a particular Id
   * @param id
   * @return
   */
  Purchaser getPurchaserById(int id);

  /**
   * List ticket purchases for a particular user
   * @param username
   * @return
   */
  List<Purchaser> getPurchaserByUsername(String username);

}
