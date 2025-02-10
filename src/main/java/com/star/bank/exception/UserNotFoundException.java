package com.star.bank.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String id) {

    super(String.format("User with id %s not found", id));
  }

}
