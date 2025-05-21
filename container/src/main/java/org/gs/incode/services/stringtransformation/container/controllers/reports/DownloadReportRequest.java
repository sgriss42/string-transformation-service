package org.gs.incode.services.stringtransformation.container.controllers.reports;

import io.swagger.v3.oas.annotations.media.Schema;
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
  @NotNull
  @Schema(
      description = "Date of the report to download.",
      example = "2024-12-31",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate date;

  @NotNull
  @Schema(
      description = "File format of the report (e.g., csv, txt).",
      example = "csv",
      allowableValues = "csv, txt",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String format;

  @Schema(hidden = true)
  public String name() {
    return "report-%s.%s".formatted(date, format);
  }
}
