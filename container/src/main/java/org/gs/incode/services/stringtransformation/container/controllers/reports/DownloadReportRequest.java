package org.gs.incode.services.stringtransformation.container.controllers.reports;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DownloadReportRequest {
  @NotNull LocalDate date;
  @NotNull String format;

  public String name() {
    return "report-%s.%s".formatted(date, format);
  }
}
