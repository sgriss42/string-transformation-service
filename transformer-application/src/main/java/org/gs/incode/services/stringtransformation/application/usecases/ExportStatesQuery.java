package org.gs.incode.services.stringtransformation.application.usecases;

import jakarta.validation.constraints.NotNull;
import java.io.OutputStream;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ExportStatesQuery {
  @NotNull private final ZonedDateTime from;
  @NotNull private final ZonedDateTime to;
  @NotNull private final String format;
  @NotNull private final OutputStream outputStream;

  public ExportStatesQuery(
      ZonedDateTime from, ZonedDateTime to, String format, OutputStream outputStream) {
    this.from = from;
    this.to = to;
    this.format = format;
    this.outputStream = outputStream;
  }
}
