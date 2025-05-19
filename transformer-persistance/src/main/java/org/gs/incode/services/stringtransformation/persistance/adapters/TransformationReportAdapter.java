package org.gs.incode.services.stringtransformation.persistance.adapters;

import java.time.Instant;
import java.util.List;
import org.gs.incode.services.stringtransformation.dtos.PagedResponse;
import org.gs.incode.services.stringtransformation.dtos.TransformationSearchQuery;
import org.gs.incode.services.stringtransformation.persistance.jpa.entity.JpaTransactionJob;
import org.gs.incode.services.stringtransformation.persistance.jpa.repository.TransformationJobRepository;
import org.gs.incode.services.stringtransformation.persistance.mapper.JpaTransactionJobMapper;
import org.gs.incode.services.stringtransformation.reporting.TransformationJobReport;
import org.gs.incode.services.stringtransformation.reporting.TransformationResult;
import org.gs.incode.services.stringtransformation.reporting.TransformationResultWithTransformers;
import org.gs.incode.services.stringtransformation.reporting.ports.TransformationReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransformationReportAdapter implements TransformationReportRepository {

  private final TransformationJobRepository repository;
  private final JpaTransactionJobMapper mapper;

  public TransformationReportAdapter(
      TransformationJobRepository repository, JpaTransactionJobMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional
  @Override
  public void save(TransformationJobReport report) {
    JpaTransactionJob entity = mapper.toEntity(report);
    repository.save(entity);
  }

  @Transactional(readOnly = true)
  @Override
  public PagedResponse<TransformationResult> findAll(TransformationSearchQuery query) {
    Page<JpaTransactionJob> page =
        repository.findAllByCompletedAtBetween(
            query.getFrom(), query.getTo(), PageRequest.of(query.getPage(), query.getSize()));

    List<TransformationResult> transformationResults =
        page.stream().map(mapper::toTransformationResult).toList();
    return new PagedResponse<>(
        transformationResults, page.getTotalPages(), page.getNumber(), page.getSize());
  }

  @Override
  public List<TransformationResultWithTransformers> findAllForTimeRange(Instant from, Instant to) {
    List<JpaTransactionJob> list = repository.findAllByCompletedAtBetween(from, to);
    return mapper.toTransformationResultWithTransformerList(list);
  }
}
