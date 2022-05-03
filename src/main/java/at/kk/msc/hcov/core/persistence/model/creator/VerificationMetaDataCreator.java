package at.kk.msc.hcov.core.persistence.model.creator;

import at.kk.msc.hcov.core.persistence.model.ConfigurationEntity;
import at.kk.msc.hcov.core.persistence.model.ProcessorPluginConfigurationEntity;
import at.kk.msc.hcov.core.persistence.model.QualityControlMetaDataEntity;
import at.kk.msc.hcov.core.persistence.model.VerificationMetaDataEntity;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedTaskIdMappings;
import at.kk.msc.hcov.core.service.util.ITimeProvider;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model.QualityControlTasksSpecification;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class VerificationMetaDataCreator {

  private ITimeProvider timeProvider;

  public VerificationMetaDataEntity toEntity(
      VerificationTaskSpecification serviceObject,
      PublishedTaskIdMappings publishedTaskIdMappings
  ) {
    VerificationMetaDataEntity entity = new VerificationMetaDataEntity();

    entity.setVerificationName(serviceObject.getVerificationName());
    entity.setOntologyName(serviceObject.getOntologyName());
    entity.setCreatedAt(timeProvider.now());
    entity.setVerificationTaskPluginId(serviceObject.getVerificationTaskPluginId());
    entity.setVerificationTaskPluginConfiguration(toConfigurationEntities(serviceObject.getVerificationTaskPluginConfiguration()));
    entity.setContextProviderPluginId(serviceObject.getContextProviderPluginId());
    entity.setContextProviderConfiguration(toConfigurationEntities(serviceObject.getContextProviderConfiguration()));
    entity.setCrowdsourcingConnectorPluginId(serviceObject.getCrowdsourcingConnectorPluginId());
    entity.setCrowdsourcingConnectorPluginConfiguration(
        toConfigurationEntities(serviceObject.getCrowdsourcingConnectorPluginConfiguration()));
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
        .map(this::toConfigurationEntities)
        .map(ProcessorPluginConfigurationEntity::new)
        .collect(Collectors.toList()) : null;
  }

  List<ConfigurationEntity> toConfigurationEntities(Map<String, Object> objectMap) {
    List<ConfigurationEntity> configurationEntities = null;
    if (objectMap != null) {
      configurationEntities = new ArrayList<>();
      for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
        configurationEntities.add(new ConfigurationEntity(entry.getKey(), entry.getValue().toString()));
      }
    }
    return configurationEntities;
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