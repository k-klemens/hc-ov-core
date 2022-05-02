package at.kk.msc.hcov.core.service.mapper;

import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskSpecificationRequestDto;
import at.kk.msc.hcov.core.persistence.model.ProcessorPluginConfigurationEntity;
import at.kk.msc.hcov.core.persistence.model.QualityControlMetaDataEntity;
import at.kk.msc.hcov.core.persistence.model.VerificationTaskSpecificationEntity;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedTaskIdMappings;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model.QualityControlTasksSpecification;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class VerificationTaskSpecificationMapper {

  public abstract VerificationTaskSpecification toServiceObject(VerificationTaskSpecificationRequestDto dto);

  public VerificationTaskSpecificationEntity toEntity(
      VerificationTaskSpecification serviceObject,
      PublishedTaskIdMappings publishedTaskIdMappings
  ) {
    VerificationTaskSpecificationEntity entity = new VerificationTaskSpecificationEntity();

    entity.setVerificationName(serviceObject.getVerificationName());
    entity.setOntologyName(serviceObject.getOntologyName());
    entity.setVerificationTaskPluginId(serviceObject.getVerificationTaskPluginId());
    entity.setVerificationTaskPluginConfiguration(toStringMap(serviceObject.getVerificationTaskPluginConfiguration()));
    entity.setContextProviderPluginId(serviceObject.getContextProviderPluginId());
    entity.setContextProviderConfiguration(toStringMap(serviceObject.getContextProviderConfiguration()));
    entity.setCrowdsourcingConnectorPluginId(serviceObject.getCrowdsourcingConnectorPluginId());
    entity.setCrowdsourcingConnectorPluginConfiguration(toStringMap(serviceObject.getCrowdsourcingConnectorPluginConfiguration()));
    entity.setProcessorPluginIds(serviceObject.getProcessorPluginIds());
    entity.setProcessorPluginConfigurationEntities(
        toProcessorPluginConfigurationEntities(serviceObject.getProcessorPluginConfigurations())
    );
    entity.setQualityControlMetaData(
        toQualityControlMetaDataEntities(
            serviceObject.getQualityControlTasksSpecification(), publishedTaskIdMappings.getQualityControlTaskIdMappings()
        )
    );
    entity.setOntologyVerificationTaskIdMappings(publishedTaskIdMappings.getOntologyVerificationTaskIdMappings());

    return entity;
  }

  private List<ProcessorPluginConfigurationEntity> toProcessorPluginConfigurationEntities(List<Map<String, Object>> mapList) {
    return mapList.stream()
        .map(this::toStringMap)
        .map(ProcessorPluginConfigurationEntity::new)
        .collect(Collectors.toList());
  }

  private Map<String, String> toStringMap(Map<String, Object> objectMap) {
    Map<String, String> returnMap = new HashMap<>();
    for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
      returnMap.put(entry.getKey(), entry.getValue().toString());
    }
    return returnMap;
  }

  private List<QualityControlMetaDataEntity> toQualityControlMetaDataEntities(
      QualityControlTasksSpecification specification, Map<UUID, String> qualityTaskIdMappings
  ) {
    return specification.getQualityControlTasks().stream()
        .map(
            qualityControlTaskCreation ->
                new QualityControlMetaDataEntity(
                    qualityControlTaskCreation.getQualityControlModelElementId(),
                    qualityTaskIdMappings.get(qualityControlTaskCreation.getQualityControlModelElementId()),
                    qualityControlTaskCreation.getAnswer()
                )
        )
        .toList();
  }

}
