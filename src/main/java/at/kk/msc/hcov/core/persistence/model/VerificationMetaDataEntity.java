package at.kk.msc.hcov.core.persistence.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
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

  private LocalDateTime createdAt;

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

  @Transient
  public Map<String, UUID> getCrowdsourcingTaskIdMappings() {
    return
        ontologyVerificationTaskIdMappings.entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getValue,
                    Map.Entry::getKey
                )
            );
  }

  @Transient
  public Map<String, Object> getCrowdsourcingConfigurationAsMap() {
    if (crowdsourcingConnectorPluginConfiguration != null) {
      return crowdsourcingConnectorPluginConfiguration.stream()
          .collect(
              Collectors.toMap(
                  ConfigurationEntity::getConfigurationKey,
                  ConfigurationEntity::getConfigurationValue
              )
          );
    }
    return null;
  }
}

