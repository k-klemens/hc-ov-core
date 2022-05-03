package at.kk.msc.hcov.core.service.mapper;

import at.kk.msc.hcov.core.endpoint.dto.VerificationMetaDataDto;
import at.kk.msc.hcov.core.persistence.model.ConfigurationEntity;
import at.kk.msc.hcov.core.persistence.model.ProcessorPluginConfigurationEntity;
import at.kk.msc.hcov.core.persistence.model.VerificationMetaDataEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface VerificationMetaDataMapper {

  @Mapping(source = "verificationTaskPluginConfiguration", target = "verificationTaskPluginConfiguration", qualifiedByName = "toConfigurationMap")
  @Mapping(source = "contextProviderConfiguration", target = "contextProviderConfiguration", qualifiedByName = "toConfigurationMap")
  @Mapping(source = "crowdsourcingConnectorPluginConfiguration", target = "crowdsourcingConnectorPluginConfiguration", qualifiedByName = "toConfigurationMap")
  @Mapping(source = "processorPluginConfigurationEntities", target = "processorPluginConfigurations", qualifiedByName = "toListOfConfigurationMaps")
  VerificationMetaDataDto toDto(VerificationMetaDataEntity entity);


  @Named("toConfigurationMap")
  default Map<String, String> toConfigurationMap(List<ConfigurationEntity> configurationEntities) {
    if (configurationEntities != null) {
      return configurationEntities.stream()
          .collect(Collectors.toMap(
                  ConfigurationEntity::getConfigurationKey,
                  ConfigurationEntity::getConfigurationValue
              )
          );
    }
    return null;
  }

  @Named("toListOfConfigurationMaps")
  default List<Map<String, String>> toListOfConfigurationMaps(List<ProcessorPluginConfigurationEntity> configurationEntities) {
    if (configurationEntities != null) {
      return configurationEntities.stream()
          .map(ProcessorPluginConfigurationEntity::getProcessorPluginConfiguration)
          .map(this::toConfigurationMap)
          .toList();
    }
    return null;
  }
}
