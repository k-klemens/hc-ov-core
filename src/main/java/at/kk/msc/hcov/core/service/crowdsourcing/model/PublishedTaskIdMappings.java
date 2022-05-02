package at.kk.msc.hcov.core.service.crowdsourcing.model;


import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class holding a mapping between ontology elements id and crowdsourcing task id.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishedTaskIdMappings {

  /**
   * Mappings for ontology verification tasks.
   */
  Map<UUID, String> ontologyVerificationTaskIdMappings;

  /**
   * Mappings for quality control tasks.
   */
  Map<UUID, String> qualityControlTaskIdMappings;

  public String getOntologyVerificationTaskId(UUID elementId) {
    return ontologyVerificationTaskIdMappings.get(elementId);
  }

  public String getQualityControlTaskId(UUID elementId) {
    return qualityControlTaskIdMappings.get(elementId);
  }

}