package at.kk.msc.hcov.core.endpoint;

import at.kk.msc.hcov.core.endpoint.dto.UploadOntologyRequestDto;
import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNameAlreadyInUseException;
import at.kk.msc.hcov.core.service.ontology.data.IDataProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.io.IOException;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@RestController
@RequestMapping("/provider")
public class RestOntologyProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestOntologyProvider.class);

  private IDataProvider dataProvider;

  @Operation(summary = "Endpoint to upload and persist and ontology on the platform.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The ontology was uploaded successfully."),
      @ApiResponse(responseCode = "409", description = "The ontology name is already used for this or another ontology."),
      @ApiResponse(responseCode = "500", description = "A problem when processing the ontology upload request occured.")
  })
  @PostMapping("/upload")
  public ResponseEntity<Void> postUploadOntology(@Valid @RequestBody UploadOntologyRequestDto request) {
    try {
      dataProvider.uploadOntologyFromFile(request.getFilePathToOntology(), request.getOntologyName());
    } catch (IOException e) {
      LOGGER.error("Error when uploading ontology!", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          "Something went wrong when processing the location of the ontology.", e);
    } catch (OntologyNameAlreadyInUseException e) {
      LOGGER.error("Ontology name already exists!", e);
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          "An ontology with the name '" + request.getOntologyName() + "' already exists. " +
              "Either delete the ontology from the repository or use a different name!");
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
