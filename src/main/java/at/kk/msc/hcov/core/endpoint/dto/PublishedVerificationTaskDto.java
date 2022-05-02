package at.kk.msc.hcov.core.endpoint.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class PublishedVerificationTaskDto {
  

  /**
   * Name of the verification.
   */
  private String verificationName;
  /**
   * Name of the ontology to be verified.
   */
  private String ontologyName;
  /**
   * UUID of the extracted ontology element typically provided by an DataProvider.
   */
  private UUID ontologyElementId;
  /**
   * String containing a populated HTML template of the task to be uploaded to the crowdsourcing platform.
   */
  private String taskHtml;

  /**
   * ID of the published task providded by the crowdsourcing platform.
   */
  private String crowdsourcingId;

}
