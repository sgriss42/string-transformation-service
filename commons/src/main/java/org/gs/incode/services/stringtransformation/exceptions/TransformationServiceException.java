package org.gs.incode.services.stringtransformation.exceptions;

public class TransformationServiceException extends RuntimeException {

  public TransformationServiceException(String msg) {
    super(msg);
  }

  public TransformationServiceException(String msg, Throwable throwable) {
    super(msg, throwable);
  }
}
