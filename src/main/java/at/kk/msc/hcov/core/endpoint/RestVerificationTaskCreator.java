package at.kk.msc.hcov.core.endpoint;

import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskResponseDto;
import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskSpecificationRequestDto;
import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.mapper.VerificationTaskMapper;
import at.kk.msc.hcov.core.service.mapper.VerificationTaskSpecificationMapper;
import at.kk.msc.hcov.core.service.verificationtask.task.IVerificationTaskCreator;
import at.kk.msc.hcov.core.service.verificationtask.task.exception.VerificationTaskCreationFailedException;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@RestController
@RequestMapping("/verification-task")
public class RestVerificationTaskCreator {

  private IVerificationTaskCreator verificationTaskCreator;

  private VerificationTaskSpecificationMapper specificationMapper;
  private VerificationTaskMapper verificationTaskMapper;

  @Operation(summary = "Endpoint to create verification tasks according to a given specification. This interface does not upload or persist the tasks.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Returns the created verification tasks.",
          content = @Content(schema = @Schema(implementation = VerificationTaskResponseDto.class))
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Something went wrong when processing the task creation request."
      )
  })
  @PostMapping("/create")
  public ResponseEntity<VerificationTaskResponseDto> createVerificationTasks(
      @RequestBody @Valid VerificationTaskSpecificationRequestDto requestDto
  ) {
    try {
      List<VerificationTask> verificationTasks = verificationTaskCreator.createTasks(
          specificationMapper.toServiceObject(requestDto), false
      );
      return new ResponseEntity<>(
          VerificationTaskResponseDto.builder()
              .ontologyName(requestDto.getOntologyName())
              .verificationName(requestDto.getVerificationName())
              .verificationTasks(verificationTaskMapper.toDto(verificationTasks))
              .build(),
          HttpStatus.OK
      );
    } catch (VerificationTaskCreationFailedException | PluginLoadingError e) {
      throw new ResponseStatusException(500, e.getMessage(), e);
    }
  }
}
