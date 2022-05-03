package at.kk.msc.hcov.core.persistence.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationMetaDataEntity {

  @Id
  private String verificationName;
  private String ontologyName;

  private String verificationTaskPluginId;
  @ElementCollection
  @LazyCollection(LazyCollectionOption.FALSE)
  //@CollectionTable(name = "VERIFICATION_TASK_PLUGIN_CONFIG_MAPPING", joinColumns = @JoinColumn(name = "verification_name"))
  private List<ConfigurationEntity> verificationTaskPluginConfiguration;


  private String contextProviderPluginId;
  @ElementCollection
  @LazyCollection(LazyCollectionOption.FALSE)
  //@CollectionTable(name = "CONTEXT_PROVIDER_PLUGIN_CONFIG_MAPPING", joinColumns = @JoinColumn(name = "verification_name"))
  private List<ConfigurationEntity> contextProviderConfiguration;


  private String crowdsourcingConnectorPluginId;
  @ElementCollection
  @LazyCollection(LazyCollectionOption.FALSE)
  //@CollectionTable(name = "CROWDSOURCING_CONNECTOR_PLUGIN_CONFIG_MAPPING", joinColumns = @JoinColumn(name = "verification_name"))
  private List<ConfigurationEntity> crowdsourcingConnectorPluginConfiguration;


  @ElementCollection()
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<String> processorPluginIds;

  @OneToMany(cascade = CascadeType.ALL)
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<ProcessorPluginConfigurationEntity> processorPluginConfigurationEntities;

  @OneToMany(cascade = CascadeType.ALL)
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<QualityControlMetaDataEntity> qualityControlMetaData;

  @ElementCollection
  @LazyCollection(LazyCollectionOption.FALSE)
  @MapKeyColumn(nullable = true)
  @Column(name = "ONTOLOGY_VERIFICATION_CS_ID")
  @CollectionTable(name = "ONTOLOGY_VERIFICATION_TASK_ID_MAPPING", joinColumns = @JoinColumn(name = "verification_name"))
  private Map<UUID, String> ontologyVerificationTaskIdMappings;
}

