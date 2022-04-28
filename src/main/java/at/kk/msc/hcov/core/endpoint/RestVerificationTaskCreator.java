package at.kk.msc.hcov.core.endpoint;

import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskResponseDto;
import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskSpecificationRequestDto;
import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.mapper.VerificationTaskSpecificationMapper;
import at.kk.msc.hcov.core.service.verificationtask.IVerificationTaskCreator;
import at.kk.msc.hcov.core.service.verificationtask.exception.VerificationTaskCreationFailedException;
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

  @Operation(summary = "Endpoint to create verification tasks according to a given specification. This interface does not upload or persist the tasks.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Returns the created verification tasks.",
          content = @Content(schema = @Schema(implementation = VerificationTaskResponseDto.class))
      ),
      @ApiResponse(
          responseCode = "520",
          description = "Something went wrong when processing the task creation request."
      ),
      @ApiResponse(
          responseCode = "521",
          description = "One of the request plugins is not available with the platform installation."
      )
  })
  @PostMapping("/create")
  public ResponseEntity<VerificationTaskResponseDto> createVerificationTasks(
      @RequestBody @Valid VerificationTaskSpecificationRequestDto requestDto
  ) {
    try {
      List<VerificationTask> verificationTasks = verificationTaskCreator.createTasks(
          specificationMapper.toServiceObject(requestDto)
      );
    } catch (VerificationTaskCreationFailedException e) {
      throw new ResponseStatusException(HttpStatus.valueOf(520), e.getMessage(), e);
    } catch (PluginLoadingError e) {
      throw new ResponseStatusException(HttpStatus.valueOf(521), e.getMessage(), e);
    }
    return null;
  }
}
