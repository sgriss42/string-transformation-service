package org.gs.incode.services.stringtransformation.exceptions;

public class StringTransformationException extends TransformationServiceException {
  public StringTransformationException(String msg) {
    super(msg);
  }

  public StringTransformationException(String msg, Throwable throwable) {
    super(msg, throwable);
  }
}
