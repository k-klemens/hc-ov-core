package at.kk.msc.hcov.core.endpoint.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UploadOntologyRequestDto {

  /**
   * Name of the ontology to be uploaded.
   */
  @NotBlank
  private String ontologyName;

  /**
   * Local file path of the ontology to be uploaded.
   */
  @NotBlank
  private String filePathToOntology;

}
