package at.kk.msc.hcov.core.endpoint;

import at.kk.msc.hcov.core.endpoint.dto.VerificationMetaDataDto;
import at.kk.msc.hcov.core.persistence.metadata.exception.VerificationDoesNotExistException;
import at.kk.msc.hcov.core.persistence.metadata.impl.CrowdsourcingMetadataStore;
import at.kk.msc.hcov.core.persistence.model.VerificationMetaDataEntity;
import at.kk.msc.hcov.core.service.mapper.VerificationMetaDataMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@RestController
@RequestMapping("/metadata")
public class RestMetadataEndpoint {

  private CrowdsourcingMetadataStore crowdsourcingMetadataStore;
  private VerificationMetaDataMapper mapper;

  @Operation(summary = "Endpoint to obtain metadata for a published verification.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          content = @Content(schema = @Schema(implementation = VerificationMetaDataDto.class))
      ),
      @ApiResponse(responseCode = "404", description = "No verification for the given name found!"),
      @ApiResponse(responseCode = "500", description = "A problem when processing the request occured.")
  })
  @GetMapping("/{verification-name}")
  public ResponseEntity<VerificationMetaDataDto> getMetaData(@PathVariable("verification-name") String verificationName) {
    try {
      VerificationMetaDataEntity entity = crowdsourcingMetadataStore.getMetaData(verificationName);
      VerificationMetaDataDto dto = mapper.toDto(entity);
      return new ResponseEntity<>(dto, HttpStatus.OK);
    } catch (VerificationDoesNotExistException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }
}
