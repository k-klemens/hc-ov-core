package at.kk.msc.hcov.core.persistence.model;

import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessorPluginConfigurationEntity {

  public ProcessorPluginConfigurationEntity(Map<String, String> processorPluginConfiguration) {
    this.processorPluginConfiguration = processorPluginConfiguration;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ElementCollection
  @MapKeyColumn
  @Column(name = "PROCESSOR_PLUGIN_CONFIG")
  @CollectionTable(name = "PROCESSOR_PLUGIN_CONFIG_MAPPING")
  private Map<String, String> processorPluginConfiguration;
}
