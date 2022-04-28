package at.kk.msc.hcov.core.endpoint.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerificationTaskResponseDto {

  /**
   * Name of the verification.
   */
  private String verificationName;
  /**
   * Name of the ontology to be verified.
   */
  private String ontologyName;
  /**
   * Generated verification tasks.
   */
  private List<VerificationTaskDto> verificationTasks;

}