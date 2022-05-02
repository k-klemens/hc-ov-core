package at.kk.msc.hcov.core.service.mapper;

import at.kk.msc.hcov.core.endpoint.dto.PublishedVerificationDto;
import at.kk.msc.hcov.core.endpoint.dto.PublishedVerificationTaskDto;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedQualityControlTask;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedVerification;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedVerificationTask;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PublishedVerificationMapper {

  private PublishedVerificationTaskMapper verificationTaskMapper;

  public PublishedVerificationDto toDto(PublishedVerification publishedVerification, VerificationTaskSpecification specification) {
    PublishedVerificationTaskDto samplePublishedVerificationTaskDto = null;
    if (publishedVerification.getOntologyVerificationTasks() != null && publishedVerification.getOntologyVerificationTasks().size() >= 1) {
      samplePublishedVerificationTaskDto = verificationTaskMapper.toDto(publishedVerification.getOntologyVerificationTasks().get(0));
    }

    Map<UUID, String> qualityControlTaskMapping = new HashMap<>();
    if (publishedVerification.getQualityControlTasks() != null) {
      qualityControlTaskMapping = publishedVerification.getQualityControlTasks().stream().collect(
          Collectors.toMap(
              PublishedQualityControlTask::getOntologyElementId,
              PublishedQualityControlTask::getCrowdsourcingId
          )
      );
    }

    return new PublishedVerificationDto(
        specification.getVerificationName(),
        specification.getOntologyName(),
        samplePublishedVerificationTaskDto,
        publishedVerification.getOntologyVerificationTasks().stream().collect(
            Collectors.toMap(
                PublishedVerificationTask::getOntologyElementId,
                PublishedVerificationTask::getCrowdsourcingId
            )
        ),
        qualityControlTaskMapping
    );
  }
}
