package at.kk.msc.hcov.core.endpoint.dto;

import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class representing on quality control task.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QualityControlTaskCreationDto {

  /**
   * Given UUID of the quality control task elements.
   * This is required as the elements are typcially not provided by any data provider.
   */
  private UUID qualityControlModelElementId;
  /**
   * Set of values for the variables used by the template to be used.
   */
  private Map<String, Object> resolvedVariables;
  /**
   * Correct answer to the given quality control task.
   */
  private String answer;

}
