package at.kk.msc.hcov.core.endpoint.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UploadOntologyRequestDto {

  @NotBlank
  private String ontologyName;
  
  @NotBlank
  private String filePathToOntology;

}
