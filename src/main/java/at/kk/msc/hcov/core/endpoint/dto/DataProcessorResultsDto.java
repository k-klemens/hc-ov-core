package at.kk.msc.hcov.core.endpoint.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DataProcessorResultsDto {

  /**
   * ID of the {@link at.kk.msc.hcov.sdk.crowdsourcing.processing.IProcessorPlugin} used to create the results.
   */
  private String pluginId;

  /**
   * List of {@link ProcessingResultDto} objects returned by the exectued plugin.
   */
  private List<ProcessingResultDto> processingResult;

}
