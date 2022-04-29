package at.kk.msc.hcov.core.service.templating.model;

import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResolvedVariablesWrapper {

  /**
   * UUID of the extracted model elements.
   */
  private UUID extractedModelElementsId;
  /**
   * Map of key-value pairs containing the values for the templating variables.
   */
  private Map<String, Object> resolvedVariables;

}
