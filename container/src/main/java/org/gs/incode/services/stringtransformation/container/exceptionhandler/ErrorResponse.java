package org.gs.incode.services.stringtransformation.container.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class ErrorResponse {
  private final String code;
  private final String message;
}
