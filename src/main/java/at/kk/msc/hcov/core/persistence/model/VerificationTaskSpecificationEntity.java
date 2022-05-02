package at.kk.msc.hcov.core.persistence.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationTaskSpecificationEntity {

  @Id
  private String verificationName;
  private String ontologyName;

  private String verificationTaskPluginId;
  @ElementCollection
  @MapKeyColumn
  @Column(name = "VERIFICATION_TASK_PLUGIN_CONFIG")
  @CollectionTable(name = "VERIFICATION_TASK_PLUGIN_CONFIG_MAPPING")
  private Map<String, String> verificationTaskPluginConfiguration;


  private String contextProviderPluginId;
  @ElementCollection
  @MapKeyColumn
  @Column(name = "CONTEXT_PROVIDER_PLUGIN_CONFIG")
  @CollectionTable(name = "CONTEXT_PROVIDER_PLUGIN_CONFIG_MAPPING")
  private Map<String, String> contextProviderConfiguration;


  private String crowdsourcingConnectorPluginId;
  @ElementCollection
  @MapKeyColumn
  @Column(name = "CONTEXT_PROVIDER_PLUGIN_CONFIG")
  @CollectionTable(name = "CONTEXT_PROVIDER_PLUGIN_CONFIG_MAPPING")
  private Map<String, String> crowdsourcingConnectorPluginConfiguration;


  @ElementCollection
  private List<String> processorPluginIds;
  @OneToMany
  private List<ProcessorPluginConfigurationEntity> processorPluginConfigurationEntities;

  @OneToMany
  private List<QualityControlMetaDataEntity> qualityControlMetaData;

  @ElementCollection
  @MapKeyColumn
  @Column(name = "ONTOLOGY_VERIFICATION_CS_ID")
  @CollectionTable(name = "ONTOLOGY_VERIFICATION_TASK_ID_MAPPING")
  private Map<UUID, String> ontologyVerificationTaskIdMappings;
}

