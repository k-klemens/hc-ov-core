package at.kk.msc.hcov.core.endpoint.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
