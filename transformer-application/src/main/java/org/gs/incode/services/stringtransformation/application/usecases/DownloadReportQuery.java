package org.gs.incode.services.stringtransformation.application.usecases;

import jakarta.validation.constraints.NotNull;
import java.io.OutputStream;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.ToString;

@ToString(exclude = "outputStream")
@Getter
public class DownloadReportQuery {
  @NotNull private final ZonedDateTime from;
  @NotNull private final ZonedDateTime to;
  @NotNull private final String format;
  @NotNull private final OutputStream outputStream;

  public DownloadReportQuery(
      ZonedDateTime from, ZonedDateTime to, String format, OutputStream outputStream) {
    this.from = from;
    this.to = to;
    this.format = format.toUpperCase();
    this.outputStream = outputStream;
  }
}
