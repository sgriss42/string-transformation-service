package org.gs.incode.services.stringtransformation.persistance.exceptions;

import java.util.Set;
import org.gs.incode.services.stringtransformation.exceptions.TransformationServiceException;

public class UnsupportedExporter extends TransformationServiceException {
  public UnsupportedExporter(String format, Set<String> exporters) {
    super(
        "%s is unsupported exporter. Available exporters: %s"
            .formatted(format, String.join(",", exporters)));
  }
}
