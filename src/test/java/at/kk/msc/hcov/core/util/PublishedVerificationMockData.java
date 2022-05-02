package at.kk.msc.hcov.core.util;

import static at.kk.msc.hcov.core.util.PublishedTaskMockData.MOCKED_PUBLISHED_TASK_ID_MAPPINGS_WITH_QUALITY_CONTROL;

import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedQualityControlTask;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedVerification;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedVerificationTask;
import java.util.ArrayList;
import java.util.List;

public class PublishedVerificationMockData {

  public static PublishedVerification MOCKED_PUBLISHED_VERIFICATION_WITH_QUALITY_CONTROL() {
    List<PublishedVerificationTask> publishedVerificationTasks = VerificationTaskMockData.EXPECTED_TASKS_WITH_CONTEXT().stream()
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

  public static PublishedVerification MOCKED_PUBLISHED_VERIFICATION_WITHOUT_QUALITY_CONTROL() {
    List<PublishedVerificationTask> publishedVerificationTasks = VerificationTaskMockData.EXPECTED_TASKS_WITH_CONTEXT().stream()
        .map(vT -> new PublishedVerificationTask(
                vT, MOCKED_PUBLISHED_TASK_ID_MAPPINGS_WITH_QUALITY_CONTROL().getOntologyVerificationTaskId(vT.getOntologyElementId())
            )
        ).toList();


    return new PublishedVerification(publishedVerificationTasks, new ArrayList<>());
  }
}
