package at.kk.msc.hcov.core.persistence.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ProcessorPluginConfigurationEntity {

  public ProcessorPluginConfigurationEntity(String pluginId, List<ConfigurationEntity> processorPluginConfiguration) {
    this.pluginId = pluginId;
    this.processorPluginConfiguration = processorPluginConfiguration;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String pluginId;

  @ElementCollection
  @CollectionTable(name = "PROCESSOR_PLUGIN_CONFIG_MAPPING")
  private List<ConfigurationEntity> processorPluginConfiguration;

  public Map<String, Object> getProcessorPluginConfigurationAsMap() {
    if (processorPluginConfiguration != null) {
      return processorPluginConfiguration.stream()
          .collect(Collectors.toMap(
                  ConfigurationEntity::getConfigurationKey,
                  ConfigurationEntity::getConfigurationValue
              )
          );
    }
    return new HashMap<>();
  }
}
