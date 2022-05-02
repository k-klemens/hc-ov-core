package at.kk.msc.hcov.core.persistence.model.creator;

import at.kk.msc.hcov.core.persistence.model.ProcessorPluginConfigurationEntity;
import at.kk.msc.hcov.core.persistence.model.QualityControlMetaDataEntity;
import at.kk.msc.hcov.core.persistence.model.VerificationMetaDataEntity;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedTaskIdMappings;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model.QualityControlTasksSpecification;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class VerificationMetaDataCreator {

  public VerificationMetaDataEntity toEntity(
      VerificationTaskSpecification serviceObject,
      PublishedTaskIdMappings publishedTaskIdMappings
  ) {
    VerificationMetaDataEntity entity = new VerificationMetaDataEntity();

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

  List<ProcessorPluginConfigurationEntity> toProcessorPluginConfigurationEntities(List<Map<String, Object>> mapList) {
    return mapList != null ? mapList.stream()
        .map(this::toStringMap)
        .map(ProcessorPluginConfigurationEntity::new)
        .collect(Collectors.toList()) : null;
  }

  Map<String, String> toStringMap(Map<String, Object> objectMap) {
    Map<String, String> returnMap = new HashMap<>();
    if (objectMap != null) {
      for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
        returnMap.put(entry.getKey(), entry.getValue().toString());
      }
    }
    return returnMap;
  }

  List<QualityControlMetaDataEntity> toQualityControlMetaDataEntities(
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