package org.gs.incode.services.stringtransformation.persistance.mapper;

import static org.gs.incode.services.stringtransformation.dtos.TransformerType.TO_UPPERCASE;
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
import org.gs.incode.services.stringtransformation.persistance.jpa.entity.JpaTransformerTask;
import org.gs.incode.services.stringtransformation.reporting.TransformationJobReport;
import org.gs.incode.services.stringtransformation.reporting.TransformationResult;
import org.gs.incode.services.stringtransformation.reporting.TransformationResultWithTransformers;
import org.gs.incode.services.stringtransformation.reporting.Transformer;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class JpaTransactionJobMapperTest {

  @Test
  void toPagedResponseDto() {
    JpaTransactionJobMapper mapper = Mappers.getMapper(JpaTransactionJobMapper.class);

    JpaTransactionJob jpaJob = new JpaTransactionJob();
    Page<JpaTransactionJob> page = new PageImpl<>(List.of(jpaJob), PageRequest.of(10, 100), 9999);
    PagedResponse<TransformationResult> pagedResponse = mapper.toPagedResponseDto(page);
    assertEquals(10, pagedResponse.getPage());
    assertEquals(100, pagedResponse.getSize());
    assertEquals(9999, pagedResponse.getTotalElements());
    assertEquals(1, pagedResponse.getContent().size());

    TransformationResult transformationResult = pagedResponse.getContent().get(0);
    assertNotNull(transformationResult);
  }

  @Test
  void toTransformationResult() {
    JpaTransactionJobMapper mapper = Mappers.getMapper(JpaTransactionJobMapper.class);
    JpaTransactionJob jpaJob = getJpaTransactionJob();
    TransformationResult transformationResult = mapper.toTransformationResult(jpaJob);
    assertEquals(jpaJob.getId().toString(), transformationResult.getId());
    assertEquals(jpaJob.getCompletedAt(), transformationResult.getCompletedAt());
    assertEquals(jpaJob.getCreatedAt(), transformationResult.getCreatedAt());
    assertEquals(jpaJob.getErrorMessage(), transformationResult.getErrorMessage());
    assertEquals(
        jpaJob.getIsJobCompletedSuccessfully(), transformationResult.isJobCompletedSuccessfully());
  }

  @Test
  void toTransformationResultWithTransformers() {
    JpaTransactionJobMapper mapper = Mappers.getMapper(JpaTransactionJobMapper.class);
    JpaTransactionJob jpaJob = getJpaTransactionJob();
    List<TransformationResultWithTransformers> transformationResults =
        mapper.toTransformationResultWithTransformerList(List.of(jpaJob));
    TransformationResultWithTransformers transformationResult = transformationResults.get(0);
    assertEquals(jpaJob.getId().toString(), transformationResult.getId());
    assertEquals(jpaJob.getCompletedAt(), transformationResult.getCompletedAt());
    assertEquals(jpaJob.getCreatedAt(), transformationResult.getCreatedAt());
    assertEquals(jpaJob.getErrorMessage(), transformationResult.getErrorMessage());
    assertEquals(
        jpaJob.getIsJobCompletedSuccessfully(), transformationResult.isJobCompletedSuccessfully());

    Transformer transformer = transformationResult.getTransformers().get(0);

    assertEquals(1, transformer.getId());
    assertEquals(TransformerType.TO_LOWERCASE, transformer.getType());
  }

  private JpaTransactionJob getJpaTransactionJob() {
    JpaTransactionJob jpaJob = new JpaTransactionJob();
    jpaJob.setId(UUID.randomUUID());
    jpaJob.setIsJobCompletedSuccessfully(true);
    JpaTransformerTask jpaTransformerTask = new JpaTransformerTask();
    jpaTransformerTask.setId(1);
    jpaTransformerTask.setJob(jpaJob);
    jpaTransformerTask.setParameters("");
    jpaTransformerTask.setType(TransformerType.TO_LOWERCASE);
    jpaJob.setTransformers(List.of(jpaTransformerTask));

    jpaJob.setCreatedAt(Instant.now());
    jpaJob.setErrorMessage("Error");
    jpaJob.setCompletedAt(Instant.now());
    return jpaJob;
  }

  @Test
  void toEntity() {
    JpaTransactionJobMapper mapper = Mappers.getMapper(JpaTransactionJobMapper.class);
    TransformationJobReport transformationJobReport = new TransformationJobReport();

    TransformerTaskConfig config = new TransformerTaskConfig(TO_UPPERCASE);
    config.regexp("regexp");
    config.replacement("replacement");
    transformationJobReport.initializeReport(new TransformationCommand("input", List.of(config)));
    transformationJobReport.success("result");

    JpaTransactionJob jpaTransactionJob = mapper.toEntity(transformationJobReport);
    assertEquals("input", jpaTransactionJob.getInput());
    assertEquals(transformationJobReport.getId(), jpaTransactionJob.getId());
    assertEquals(transformationJobReport.getResult(), jpaTransactionJob.getResult());
    assertEquals(
        transformationJobReport.getIsJobCompletedSuccessfully(),
        jpaTransactionJob.getIsJobCompletedSuccessfully());
    assertEquals(transformationJobReport.getCreatedAt(), jpaTransactionJob.getCreatedAt());
    assertEquals(transformationJobReport.getCompletedAt(), jpaTransactionJob.getCompletedAt());
    assertEquals(transformationJobReport.getErrorMessage(), jpaTransactionJob.getErrorMessage());

    assertEquals(1, jpaTransactionJob.getTransformers().size());
    assertEquals(0, jpaTransactionJob.getTransformers().get(0).getId());
    assertEquals(TO_UPPERCASE, jpaTransactionJob.getTransformers().get(0).getType());
    assertEquals(jpaTransactionJob, jpaTransactionJob.getTransformers().get(0).getJob());
    assertEquals(
        "{\"regexp\":\"regexp\",\"replacement\":\"replacement\"}",
        jpaTransactionJob.getTransformers().get(0).getParameters());
  }

  @Test
  void toEntity2() {
    JpaTransactionJobMapper mapper = Mappers.getMapper(JpaTransactionJobMapper.class);
    TransformationJobReport transformationJobReport = new TransformationJobReport();

    TransformerTaskConfig config = new TransformerTaskConfig(TO_UPPERCASE);
    config.regexp("regexp");
    config.replacement("replacement");
    transformationJobReport.initializeReport(new TransformationCommand("input", List.of(config)));
    transformationJobReport.failed("ERROR");

    JpaTransactionJob jpaTransactionJob = mapper.toEntity(transformationJobReport);
    assertEquals("input", jpaTransactionJob.getInput());
    assertEquals(transformationJobReport.getId(), jpaTransactionJob.getId());
    assertEquals(transformationJobReport.getResult(), jpaTransactionJob.getResult());
    assertEquals(
            transformationJobReport.getIsJobCompletedSuccessfully(),
            jpaTransactionJob.getIsJobCompletedSuccessfully());
    assertEquals(transformationJobReport.getCreatedAt(), jpaTransactionJob.getCreatedAt());
    assertEquals(transformationJobReport.getCompletedAt(), jpaTransactionJob.getCompletedAt());
    assertEquals(transformationJobReport.getErrorMessage(), jpaTransactionJob.getErrorMessage());

    assertEquals(1, jpaTransactionJob.getTransformers().size());
    assertEquals(0, jpaTransactionJob.getTransformers().get(0).getId());
    assertEquals(TO_UPPERCASE, jpaTransactionJob.getTransformers().get(0).getType());
    assertEquals(jpaTransactionJob, jpaTransactionJob.getTransformers().get(0).getJob());
    assertEquals(
            "{\"regexp\":\"regexp\",\"replacement\":\"replacement\"}",
            jpaTransactionJob.getTransformers().get(0).getParameters());
  }
}
