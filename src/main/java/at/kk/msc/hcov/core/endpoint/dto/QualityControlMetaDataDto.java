package at.kk.msc.hcov.core.endpoint.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor

public class QualityControlMetaDataDto {

  /**
   * QUality control model element.
   */
  private UUID qualityControlModelElementId;

  /**
   * Id for provided by the crowdsourcing platform.
   */
  private String crowdsourcingId;

  /**
   * Correct answer for the task.
   */
  private String answer;

}
