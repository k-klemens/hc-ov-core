package at.kk.msc.hcov.core.service.processing.model;

import at.kk.msc.hcov.sdk.crowdsourcing.processing.ProcessingResult;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
/**
 * Describes the result of one plugin execution by the DataProcessor
 */
public class DataProcessorResults {

  /**
   * ID of the {@link at.kk.msc.hcov.sdk.crowdsourcing.processing.IProcessorPlugin} used to create the results.
   */
  private String pluginId;

  /**
   * List of {@link ProcessingResult} objects returned by the exectued plugin.
   */
  private List<ProcessingResult> processingResult;
}
