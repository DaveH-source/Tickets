package com.dwp.TicketApplication.service;

import com.dwp.TicketApplication.Request.TicketTypeRequest;
import com.dwp.TicketApplication.entity.Purchaser;
import com.dwp.TicketApplication.exception.InvalidTicketRequestException;
import com.dwp.TicketApplication.repository.PurchaserRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {

  @Value("${purchaser.adult.ticket.price}")
  private int adultTicketPrice;

  @Value("${purchaser.child.ticket.price}")
  private int childTicketPrice;

  @Value("${purchaser.infant.ticket.price}")
  private int infantTicketPrice;

  @Value("${purchaser.max.qty}")
  private int ticketMaxQty;
  @Autowired
  private PurchaserRepository repository;

  public Purchaser savePurchaser(TicketTypeRequest ticketTypeRequest) throws InvalidTicketRequestException{
    boolean validTicketRequest = true;
    Purchaser purchaser = null;
      isValidTicketRequest(ticketTypeRequest);
      purchaser = mapPurchaseEntity(ticketTypeRequest);

      /* EXTERNAL Approach
      Call out to external seat reservation service
      RestTemplate restTemplate = new RestTemplate();
      URI seatUri = new URI("http://seatreservation");
      restTemplate.postForObject(seatUri, purchaser, Purchaser.class);

      Call out to external payment service
      RestTemplate restTemplate = new RestTemplate();
      URI payUri = new URI("http://payment");
      restTemplate.postForObject(payUri, purchaser, Purchaser.class); */

      /* INTERNAL Approach
      Need services created and autowired then call directly.
      ticketPaymentService.makePayment(calculateTotalTicketPrice(purchaser));
      seatReservationService.reserveSeats(calculateSeatsRequired(purchaser)); */

      return repository.save(purchaser);

  }

  private Purchaser mapPurchaseEntity(TicketTypeRequest ticketTypeRequest) {
    Purchaser purchaser = new Purchaser();
    purchaser.setUsername(ticketTypeRequest.getUsername());
    purchaser.setNumAdultTickets(ticketTypeRequest.getNumAdultTickets());
    purchaser.setNumChildTickets(ticketTypeRequest.getNumChildTickets());
    purchaser.setNumInfantTickets(ticketTypeRequest.getNumInfantTickets());
    purchaser.setNumSeatsReserved(calculateSeatsRequired(purchaser));
    purchaser.setTotalTicketCost(calculateTotalTicketPrice(purchaser));
    purchaser.setCreatedDate(new Date());
    return purchaser;
  }

  public List<Purchaser> getPurchasers() {
    return repository.findAll();
  }

  public Purchaser getPurchaserById(int id) {
    return repository.findById(id).orElse(null);
  }

  public List<Purchaser> getPurchaserByUsername(String username) {
    return repository.findByUsername(username);
      }

  private void isValidTicketRequest(TicketTypeRequest ticketTypeRequest) throws InvalidTicketRequestException {
    // At least 1 adult ticket must be purchased
    if (ticketTypeRequest.getNumAdultTickets() < 1) {
      throw new InvalidTicketRequestException(
          "At least 1 adult ticket purchase must be made.");
    }
    // An actual user must be provided
    if (ticketTypeRequest.getUsername() == null) {
      throw new InvalidTicketRequestException(
          "No user details provided.");
    }

    // Max of 20 tickets can be purchased at a time.
    if (ticketTypeRequest.getNumAdultTickets() + ticketTypeRequest.getNumChildTickets()
        + ticketTypeRequest.getNumInfantTickets()
        > ticketMaxQty) {
      throw new InvalidTicketRequestException(
          "A maximum of 20 tickets can be purchased at a time.");
    }

    // Must be a greater or equal number of adults vs infants
    if (ticketTypeRequest.getNumAdultTickets() < ticketTypeRequest.getNumInfantTickets()) {
      throw new InvalidTicketRequestException(
          "Infants cannot exceed number of adults, not enough knees !");
    }
  }

  private int calculateSeatsRequired(Purchaser purchaser) {
    // Infants do not require a seat.
    return purchaser.getNumAdultTickets() + purchaser.getNumChildTickets();
  }

  private int calculateTotalTicketPrice(Purchaser purchaser) {
    return purchaser.getNumAdultTickets() * adultTicketPrice
        + purchaser.getNumChildTickets() * childTicketPrice
        + purchaser.getNumInfantTickets() * infantTicketPrice;

  }
}
