package at.kk.msc.hcov.core.endpoint.dto;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationTaskSpecificationRequestDto {


  /**
   * Name of the verification.
   */
  @NotBlank
  private String verificationName;
  /**
   * Name of the ontology to be verified.
   */
  @NotBlank
  private String ontologyName;

  /**
   * ID of the verification task plugin to be used to create the HITs.
   */
  @NotBlank
  private String verificationTaskPluginId;
  /**
   * Map holding arbitrary configuration values to be passed to the verification task plugin.
   */
  private Map<String, Object> verificationTaskPluginConfiguration;

  /**
   * ID of the context provider task plugin to be used to create the HITs.
   */
  private String contextProviderPluginId;
  /**
   * Map holding arbitrary configuration values to be passed to the context provider plugin.
   */
  private Map<String, Object> contextProviderConfiguration;

  /**
   * ID of the crowdsourcing connector plugin to be used to publish the HITs.
   */
  private String crowdsourcingConnectorPluginId;
  /**
   * Map holding arbitrary configuration values to be passed to the crowdsourcing connector plugin.
   */
  private Map<String, Object> crowdsourcingConnectorPluginConfiguration;

  /**
   * List of IDs of the crowdsourcing processor plugins to be used to process the results from the crowdsourcing platform.
   */
  private List<String> processorPluginIds;
  /**
   * List of maps holding arbitrary configuration values of the processor plugins.
   */
  private List<Map<String, Object>> processorPluginConfigurations;

  /**
   * Specification on how to create quality control tasks. Can be null / optional.
   */
  private QualityControlTasksSpecificationDto qualityControlTasksSpecification;

}
