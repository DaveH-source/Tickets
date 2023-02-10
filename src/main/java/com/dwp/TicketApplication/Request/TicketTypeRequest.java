package com.dwp.TicketApplication.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
public final class TicketTypeRequest {

  private String username;
  private int numAdultTickets;
  private int numChildTickets;
  private int numInfantTickets;

}

