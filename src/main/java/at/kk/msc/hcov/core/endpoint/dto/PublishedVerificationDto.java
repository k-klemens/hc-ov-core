package at.kk.msc.hcov.core.endpoint.dto;

import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PublishedVerificationDto {

  /**
   * Name of the verification.
   */
  private String verificationName;
  /**
   * Name of the ontology to be verified.
   */
  private String ontologyName;

  /**
   * Containts the fully rendered first Verification Task task html. Only the first Verification template is returned due to space considerations.
   */
  private PublishedVerificationTaskDto samplePublishedVerificationTask;

  /**
   * Maps the extracted model element UUIDs to the external IDs provided by the crowdsourcing platform.
   */
  private Map<UUID, String> verificationTaskIdMappings;

  /**
   * Maps the provided quality control element UUID to the external IDs provided by the crowdsourcing platform.
   */
  private Map<UUID, String> qualitiyControlTaskIdMappings;

}
