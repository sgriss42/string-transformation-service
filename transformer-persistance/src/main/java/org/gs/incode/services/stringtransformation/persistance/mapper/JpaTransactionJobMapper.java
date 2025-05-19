package org.gs.incode.services.stringtransformation.persistance.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.gs.incode.services.stringtransformation.dtos.PagedResponse;
import org.gs.incode.services.stringtransformation.dtos.TransformerTaskConfig;
import org.gs.incode.services.stringtransformation.persistance.jpa.entity.JpaTransactionJob;
import org.gs.incode.services.stringtransformation.persistance.jpa.entity.JpaTransformerTask;
import org.gs.incode.services.stringtransformation.reporting.TransformationJobReport;
import org.gs.incode.services.stringtransformation.reporting.TransformationResult;
import org.gs.incode.services.stringtransformation.reporting.TransformationResultWithTransformers;
import org.gs.incode.services.stringtransformation.reporting.Transformer;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface JpaTransactionJobMapper {

  TransformationResult toTransformationResult(JpaTransactionJob jpaTransactionJob);

  @Mapping(source = "number", target = "page")
  PagedResponse<TransformationResult> toPagedResponseDto(Page<JpaTransactionJob> jpaTransactionJob);

  TransformationResultWithTransformers toTransformationResultWithTransformers(
      JpaTransactionJob jpaTransactionJob);

  List<TransformationResultWithTransformers> toTransformationResultWithTransformerList(
      List<JpaTransactionJob> list);

  List<Transformer> toTransformerList(List<JpaTransformerTask> list);

  JpaTransactionJob toEntity(TransformationJobReport report);

  @AfterMapping
  default void commandToTransformer(
      @MappingTarget JpaTransactionJob entity, TransformationJobReport report) {
    Type typeObject = new TypeToken<HashMap<String, String>>() {}.getType();
    Gson gson = new Gson();
    List<TransformerTaskConfig> transformerTaskConfigs =
        report.getCommand().getTransformerTaskConfigs();
    List<JpaTransformerTask> transformers = new ArrayList<>();
    for (int i = 0; i < transformerTaskConfigs.size(); ++i) {
      JpaTransformerTask jpaTransformerTask = new JpaTransformerTask();
      jpaTransformerTask.setId(i);
      jpaTransformerTask.setJob(entity);
      String gsonData = gson.toJson(transformerTaskConfigs.get(i).getParameters(), typeObject);
      jpaTransformerTask.setParameters(gsonData);
      transformers.add(jpaTransformerTask);
    }
    entity.setTransformers(transformers);
  }
}
