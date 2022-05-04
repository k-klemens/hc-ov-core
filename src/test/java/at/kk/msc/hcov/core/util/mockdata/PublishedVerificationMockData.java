package at.kk.msc.hcov.core.util.mockdata;

import static at.kk.msc.hcov.core.util.mockdata.PublishedTaskMockData.EXPECTED_PUBLISHED_TASKS_DTOS_WITH_CONTEXT;
import static at.kk.msc.hcov.core.util.mockdata.PublishedTaskMockData.MOCKED_PUBLISHED_TASK_ID_MAPPINGS_WITH_QUALITY_CONTROL;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskMockData.EXPECTED_TASKS_WITH_CONTEXT;

import at.kk.msc.hcov.core.endpoint.dto.PublishedVerificationDto;
import at.kk.msc.hcov.core.endpoint.dto.PublishedVerificationTaskDto;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedQualityControlTask;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedVerification;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedVerificationTask;
import java.util.ArrayList;
import java.util.List;

public class PublishedVerificationMockData {

  public static PublishedVerification MOCKED_PUBLISHED_VERIFICATION_WITH_QUALITY_CONTROL() {
    List<PublishedVerificationTask> publishedVerificationTasks = EXPECTED_TASKS_WITH_CONTEXT().stream()
        .map(vT -> new PublishedVerificationTask(
                vT, MOCKED_PUBLISHED_TASK_ID_MAPPINGS_WITH_QUALITY_CONTROL().getOntologyVerificationTaskId(vT.getOntologyElementId())
            )
        ).toList();

    List<PublishedQualityControlTask> qualityControlTasks = QualityControlTaskMockData.EXPECTED_QC_TASKS_WITH_CONTEXT().stream()
        .map(qT -> new PublishedQualityControlTask(
                qT, MOCKED_PUBLISHED_TASK_ID_MAPPINGS_WITH_QUALITY_CONTROL().getQualityControlTaskId(qT.getOntologyElementId())
            )
        ).toList();

    return new PublishedVerification(publishedVerificationTasks, qualityControlTasks);
  }

  public static PublishedVerificationDto EXPECTED_PUBLISHED_VERIFICATION_DTO_WITH_QUALITY_CONTROL() {
    PublishedVerificationTaskDto publishedVerificationTasks = EXPECTED_PUBLISHED_TASKS_DTOS_WITH_CONTEXT().get(0);

    return new PublishedVerificationDto(
        VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_NAME,
        VerificationTaskSpecificationMockData.MOCKED_ONTOLOGY_NAME,
        publishedVerificationTasks,
        MOCKED_PUBLISHED_TASK_ID_MAPPINGS_WITH_QUALITY_CONTROL().getOntologyVerificationTaskIdMappings(),
        MOCKED_PUBLISHED_TASK_ID_MAPPINGS_WITH_QUALITY_CONTROL().getQualityControlTaskIdMappings()
    );
  }

  public static PublishedVerification MOCKED_PUBLISHED_VERIFICATION_WITHOUT_QUALITY_CONTROL() {
    List<PublishedVerificationTask> publishedVerificationTasks = EXPECTED_TASKS_WITH_CONTEXT().stream()
        .map(vT -> new PublishedVerificationTask(
                vT, MOCKED_PUBLISHED_TASK_ID_MAPPINGS_WITH_QUALITY_CONTROL().getOntologyVerificationTaskId(vT.getOntologyElementId())
            )
        ).toList();


    return new PublishedVerification(publishedVerificationTasks, new ArrayList<>());
  }
}
