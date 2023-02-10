package com.dwp.TicketApplication.Response;

import com.dwp.TicketApplication.entity.Purchaser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaserResponse {

  private boolean success;
  private String errorMessage;
  private Purchaser payload;

}
