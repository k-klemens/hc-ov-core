package at.kk.msc.hcov.core.endpoint.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessingResultDto {

  /**
   * Crowdsourcing id for which task the result is for.
   */
  private String crowdsourcingId;

  /**
   * Key value pairs describing the calculated data.
   */
  private Map<String, String> resultData;

  /**
   * Description of the data.
   */
  private String resultDescription;

}
