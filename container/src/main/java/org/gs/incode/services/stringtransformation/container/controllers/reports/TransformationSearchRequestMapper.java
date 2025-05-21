package org.gs.incode.services.stringtransformation.container.controllers.reports;

import java.io.OutputStream;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.gs.incode.services.stringtransformation.application.usecases.dto.DownloadReportQuery;
import org.gs.incode.services.stringtransformation.dtos.TransformationSearchQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class TransformationSearchRequestMapper {
  public TransformationSearchQuery searchRequestToTransformationSearchQuery(
      TransformationSearchRequest transformationSearchRequest, Pageable pageable) {

    if (transformationSearchRequest == null) {
      throw new IllegalArgumentException("Request must not be null!");
    }
    if (pageable == null) {
      throw new IllegalArgumentException("Pageable must not be null!");
    }

    TransformationSearchQuery query = new TransformationSearchQuery();
    query.setFrom(transformationSearchRequest.getFrom().toInstant(ZoneOffset.UTC));
    query.setTo(transformationSearchRequest.getTo().toInstant(ZoneOffset.UTC));
    query.setPage(pageable.getPageNumber());
    query.setSize(pageable.getPageSize());
    return query;
  }

  public DownloadReportQuery transformationReportRequestToDownloadReportQuery(
      DownloadReportRequest downloadReportRequest, OutputStream stream) {

    ZonedDateTime from = downloadReportRequest.getDate().atStartOfDay(ZoneOffset.UTC);
    ZonedDateTime to = from.plus(1, ChronoUnit.DAYS);
    return new DownloadReportQuery(from, to, downloadReportRequest.getFormat(), stream);
  }
}
