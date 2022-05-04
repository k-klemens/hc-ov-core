package at.kk.msc.hcov.core.endpoint;

import at.kk.msc.hcov.core.endpoint.dto.DataProcessorResultsDto;
import at.kk.msc.hcov.core.endpoint.dto.DataProcessorResultsWrapperDto;
import at.kk.msc.hcov.core.persistence.metadata.exception.VerificationDoesNotExistException;
import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.mapper.DataProcessorResultsMapper;
import at.kk.msc.hcov.core.service.processing.IDataProcessor;
import at.kk.msc.hcov.core.service.processing.exception.DataProcessorException;
import at.kk.msc.hcov.core.service.processing.model.DataProcessorResults;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
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
@RequestMapping("/data-processor")
public class RestDataProcessor {

  IDataProcessor dataProcessor;

  DataProcessorResultsMapper mapper;

  @Operation(summary = "Endpoint to obtain the results for a verification.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = DataProcessorResultsDto.class)))
      ),
      @ApiResponse(responseCode = "404", description = "No verification for the given name found!"),
      @ApiResponse(responseCode = "500", description = "A problem when processing the request occured.")
  })
  @GetMapping("/{verification-name}")
  public ResponseEntity<DataProcessorResultsWrapperDto> getResults(@PathVariable("verification-name") String verificationName) {
    try {
      List<DataProcessorResults> results = dataProcessor.loadResultsAndProcess(verificationName);
      List<DataProcessorResultsDto> dtos = mapper.toDto(results);
      return new ResponseEntity<>(new DataProcessorResultsWrapperDto(dtos), HttpStatus.OK);
    } catch (VerificationDoesNotExistException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    } catch (PluginLoadingError | DataProcessorException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

}
