package at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model;

import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class wrapping the information needed to create quality control tasks.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityControlTaskCreation {

  /**
   * UUID of the quality control tasks model elements.
   */
  private UUID qualityControlModelElementId;

  /**
   * Map of key-value pairs containing the values for the templating variables.
   */
  private Map<String, Object> resolvedVariables;

  /**
   * Correct answer to the given quality control task.
   */
  private String answer;
}
