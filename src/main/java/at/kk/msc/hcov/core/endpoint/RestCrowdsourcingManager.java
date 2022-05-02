package at.kk.msc.hcov.core.endpoint;

import at.kk.msc.hcov.core.endpoint.dto.PublishedVerificationDto;
import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskSpecificationRequestDto;
import at.kk.msc.hcov.core.service.crowdsourcing.ICrowdsourcingManager;
import at.kk.msc.hcov.core.service.crowdsourcing.exception.CrowdsourcingManagerException;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedVerification;
import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.mapper.PublishedVerificationMapper;
import at.kk.msc.hcov.core.service.mapper.VerificationTaskSpecificationMapper;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/crowdsourcing")
public class RestCrowdsourcingManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestCrowdsourcingManager.class);

  private VerificationTaskSpecificationMapper verificationTaskSpecificationMapper;
  private PublishedVerificationMapper publishedVerificationMapper;
  private ICrowdsourcingManager crowdsourcingManager;

  @Operation(summary = "Endpoint to create and publish verification tasks according to a given specification.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Returns the published verification tasks information.",
          content = @Content(schema = @Schema(implementation = PublishedVerificationDto.class))
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Something went wrong when processing the publish request."
      )
  })
  @PostMapping("/publish")
  public ResponseEntity<PublishedVerificationDto> createAndPublishVerification(
      @RequestBody @Valid VerificationTaskSpecificationRequestDto requestDto
  ) {
    try {
      VerificationTaskSpecification specification = verificationTaskSpecificationMapper.toServiceObject(requestDto);
      PublishedVerification publishedVerification = crowdsourcingManager.createAndPublishVerification(specification);
      return new ResponseEntity<>(publishedVerificationMapper.toDto(publishedVerification, specification), HttpStatus.OK);
    } catch (PluginLoadingError | CrowdsourcingManagerException e) {
      LOGGER.error("Error while creating and publishing task", e);
      throw new ResponseStatusException(500, e.getMessage(), e);
    }
  }
}
