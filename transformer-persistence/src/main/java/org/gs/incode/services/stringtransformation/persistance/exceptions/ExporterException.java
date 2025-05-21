package org.gs.incode.services.stringtransformation.persistance.exceptions;

import org.gs.incode.services.stringtransformation.exceptions.TransformationServiceException;

public class ExporterException extends TransformationServiceException {
  public ExporterException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
