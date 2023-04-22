package com.vnptt.tms.exception;

/**
 * Returns in case no result can be found in the database SQL
 */
public class ResourceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ResourceNotFoundException(String msg) {
    super(msg);
  }
}