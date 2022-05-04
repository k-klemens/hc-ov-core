package at.kk.msc.hcov.core.persistence.model;

import java.util.List;
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

  public ProcessorPluginConfigurationEntity(List<ConfigurationEntity> processorPluginConfiguration) {
    this.processorPluginConfiguration = processorPluginConfiguration;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ElementCollection
  @CollectionTable(name = "PROCESSOR_PLUGIN_CONFIG_MAPPING")
  private List<ConfigurationEntity> processorPluginConfiguration;
}
