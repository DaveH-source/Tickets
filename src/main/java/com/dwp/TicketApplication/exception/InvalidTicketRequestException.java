package com.dwp.TicketApplication.exception;

public class InvalidTicketRequestException extends Exception{
  public InvalidTicketRequestException(String errorMessage) {
    super(errorMessage);
  }
}
