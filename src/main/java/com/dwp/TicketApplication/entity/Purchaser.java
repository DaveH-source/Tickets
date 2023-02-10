package com.dwp.TicketApplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PURCHASER")
@EqualsAndHashCode(exclude = {"createdDate"})
public class Purchaser {

  @Id
  @GeneratedValue
  @Column(name = "ID")
  private int id;
  @Column(name = "USER_NAME")
  private String username;
  @Column(name = "ADULT_TICKET_QTY")
  private int numAdultTickets;
  @Column(name = "CHILD_TICKET_QTY")
  private int numChildTickets;
  @Column(name = "INFANT_TICKET_QTY")
  private int numInfantTickets;
  @Column(name = "TOTAL_SEATS_RESERVED")
  private int numSeatsReserved;
  @Column(name = "TOTAL_COST")
  private int totalTicketCost;
  @Column(name= "CREATED_DATE")
  private Date createdDate;

}
