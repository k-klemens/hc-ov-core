package at.kk.msc.hcov.core.endpoint.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationMetaDataDto {

  /**
   * Name of the verification.
   */
  private String verificationName;
  /**
   * Name of the ontology to be verified.
   */
  private String ontologyName;
  /**
   * Date on which the verification was published.
   */
  private LocalDateTime createdAt;
  /**
   * ID of the verification task plugin to be used to create the HITs.
   */
  private String verificationTaskPluginId;
  /**
   * Map holding arbitrary configuration values to be passed to the verification task plugin.
   */
  private Map<String, String> verificationTaskPluginConfiguration;
  /**
   * ID of the context provider task plugin to be used to create the HITs.
   */
  private String contextProviderPluginId;
  /**
   * Map holding arbitrary configuration values to be passed to the context provider plugin.
   */
  private Map<String, String> contextProviderConfiguration;
  /**
   * ID of the crowdsourcing connector plugin to be used to publish the HITs.
   */
  private String crowdsourcingConnectorPluginId;
  /**
   * Map holding arbitrary configuration values to be passed to the crowdsourcing connector plugin.
   */
  private Map<String, String> crowdsourcingConnectorPluginConfiguration;
  /**
   * List of IDs of the crowdsourcing processor plugins to be used to process the results from the crowdsourcing platform.
   */
  private List<String> processorPluginIds;
  /**
   * List of maps holding arbitrary configuration values of the processor plugins.
   */
  private List<Map<String, String>> processorPluginConfigurations;
  /**
   * Meta data about uality control tasks. Can be null / optional.
   */
  private List<QualityControlMetaDataDto> qualityControlMetaData;
  /**
   * Maps the extracted model element UUIDs to the external IDs provided by the crowdsourcing platform.
   */
  private Map<UUID, String> ontologyVerificationTaskIdMappings;

}
