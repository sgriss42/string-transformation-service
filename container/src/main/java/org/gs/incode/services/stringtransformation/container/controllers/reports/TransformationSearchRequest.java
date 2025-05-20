package org.gs.incode.services.stringtransformation.container.controllers.reports;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.gs.incode.services.stringtransformation.container.controllers.reports.validation.DateIntervalValidation;

@Getter
@Setter
@ToString
@NoArgsConstructor
@DateIntervalValidation(message = "from should be before to!")
public class TransformationSearchRequest {
  @NotNull LocalDateTime from;
  @NotNull LocalDateTime to;
}
