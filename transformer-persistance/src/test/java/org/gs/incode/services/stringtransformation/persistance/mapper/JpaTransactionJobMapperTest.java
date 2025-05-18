package org.gs.incode.services.stringtransformation.persistance.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.gs.incode.services.stringtransformation.dtos.PagedResponse;
import org.gs.incode.services.stringtransformation.dtos.TransformationCommand;
import org.gs.incode.services.stringtransformation.dtos.TransformerTaskConfig;
import org.gs.incode.services.stringtransformation.dtos.TransformerType;
import org.gs.incode.services.stringtransformation.persistance.jpa.entity.JpaTransactionJob;
import org.gs.incode.services.stringtransformation.reporting.TransformationJobReport;
import org.gs.incode.services.stringtransformation.reporting.TransformationResult;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class JpaTransactionJobMapperTest {

  @Test
  void toPagedResponseDto() {
    JpaTransactionJobMapper mapper = Mappers.getMapper(JpaTransactionJobMapper.class);

    JpaTransactionJob entity = new JpaTransactionJob();
    Page<JpaTransactionJob> page = new PageImpl<>(List.of(entity), PageRequest.of(10, 100), 9999);
    PagedResponse<TransformationResult> pagedResponse = mapper.toPagedResponseDto(page);
    assertEquals(10, pagedResponse.getPage());
    assertEquals(100, pagedResponse.getSize());
    assertEquals(9999, pagedResponse.getTotalElements());
    assertEquals(1, pagedResponse.getContent().size());

    TransformationResult transformationResult = pagedResponse.getContent().get(0);
    assertNotNull(transformationResult);
  }

  @Test
  void toDto() {
    JpaTransactionJobMapper mapper = Mappers.getMapper(JpaTransactionJobMapper.class);
    JpaTransactionJob e1 = new JpaTransactionJob();
    e1.setId(UUID.randomUUID());
    e1.setIsJobCompletedSuccessfully(true);

    e1.setCreatedAt(Instant.now());
    e1.setErrorMessage("Error");
    e1.setCompletedAt(Instant.now());
    TransformationResult transformationResult = mapper.toDto(e1);
    assertEquals(e1.getId().toString(), transformationResult.getId());
    assertEquals(e1.getCompletedAt(), transformationResult.getCompletedAt());
    assertEquals(e1.getCreatedAt(), transformationResult.getCreatedAt());
    assertEquals(e1.getErrorMessage(), transformationResult.getErrorMessage());
    assertEquals(
        e1.getIsJobCompletedSuccessfully(), transformationResult.isJobCompletedSuccessfully());
  }

  @Test
  void toEntity() {
    JpaTransactionJobMapper mapper = Mappers.getMapper(JpaTransactionJobMapper.class);
    TransformationJobReport transformationJobReport = new TransformationJobReport();

    TransformerTaskConfig config = new TransformerTaskConfig(TransformerType.TO_UPPERCASE);
    config.regexp("regexp");
    config.replacement("replacement");
    transformationJobReport.initializeReport(new TransformationCommand("input", List.of(config)));
    transformationJobReport.success("result");

    JpaTransactionJob jpaTransactionJob = mapper.toEntity(transformationJobReport);

    assertEquals(transformationJobReport.getId(), jpaTransactionJob.getId());
    assertEquals(transformationJobReport.getResult(), jpaTransactionJob.getResult());
    assertEquals(
        transformationJobReport.getIsJobCompletedSuccessfully(),
        jpaTransactionJob.getIsJobCompletedSuccessfully());
    assertEquals(transformationJobReport.getCreatedAt(), jpaTransactionJob.getCreatedAt());
    assertEquals(transformationJobReport.getCompletedAt(), jpaTransactionJob.getCompletedAt());
    assertEquals(transformationJobReport.getErrorMessages(), jpaTransactionJob.getErrorMessage());

    assertEquals(1, jpaTransactionJob.getTransformers().size());
    assertEquals(0, jpaTransactionJob.getTransformers().get(0).getId());
    assertEquals(jpaTransactionJob, jpaTransactionJob.getTransformers().get(0).getJob());
    assertEquals(
        "{\"regexp\":\"regexp\",\"replacement\":\"replacement\"}",
        jpaTransactionJob.getTransformers().get(0).getParameters());
  }
}
