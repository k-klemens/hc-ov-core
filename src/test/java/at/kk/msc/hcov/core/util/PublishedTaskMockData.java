package at.kk.msc.hcov.core.util;

import static at.kk.msc.hcov.core.util.QualityControlTaskMockData.FIRST_QC_MOCK_UUID;
import static at.kk.msc.hcov.core.util.QualityControlTaskMockData.SECOND_QC_MOCK_UUID;
import static at.kk.msc.hcov.core.util.VerificationTaskMockData.EXPECTED_TASKS_WITHOUT_CONTEXT;
import static at.kk.msc.hcov.core.util.VerificationTaskMockData.EXPECTED_TASKS_WITHOUT_CONTEXT_DTOS;
import static at.kk.msc.hcov.core.util.VerificationTaskMockData.EXPECTED_TASKS_WITH_CONTEXT_DTOS;
import static at.kk.msc.hcov.core.util.VerificationTaskMockData.FIRST_MOCK_UUID;
import static at.kk.msc.hcov.core.util.VerificationTaskMockData.SECOND_MOCK_UUID;

import at.kk.msc.hcov.core.endpoint.dto.PublishedVerificationTaskDto;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedTaskIdMappings;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedVerificationTask;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PublishedTaskMockData {

  public static Map<UUID, String> MOCKED_ID_MAPPINGS_WITHOUT_QUALITY_CONTROL() {
    Map<UUID, String> taskIdMappings = new HashMap<>();
    taskIdMappings.put(FIRST_MOCK_UUID, "FIRST-CS-ID");
    taskIdMappings.put(SECOND_MOCK_UUID, "SECOND-CS-ID");

    return taskIdMappings;
  }

  public static Map<UUID, String> MOCKED_ID_MAPPINGS_WITH_QUALITY_CONTROL() {
    Map<UUID, String> taskIdMappings = new HashMap<>();
    taskIdMappings.put(FIRST_MOCK_UUID, "FIRST-CS-ID");
    taskIdMappings.put(SECOND_MOCK_UUID, "SECOND-CS-ID");

    taskIdMappings.put(FIRST_QC_MOCK_UUID, "FIRST-QC-CS-ID");
    taskIdMappings.put(SECOND_QC_MOCK_UUID, "SECOND-QC-CS-ID");

    return taskIdMappings;
  }

  public static PublishedTaskIdMappings MOCKED_PUBLISHED_TASK_ID_MAPPINGS_WITHOUT_QUALITY_CONTROL() {
    PublishedTaskIdMappings taskIdMappings = new PublishedTaskIdMappings(new HashMap<>(), new HashMap<>());
    taskIdMappings.getOntologyVerificationTaskIdMappings().put(FIRST_MOCK_UUID, "FIRST-CS-ID");
    taskIdMappings.getOntologyVerificationTaskIdMappings().put(SECOND_MOCK_UUID, "SECOND-CS-ID");

    return taskIdMappings;
  }

  public static PublishedTaskIdMappings MOCKED_PUBLISHED_TASK_ID_MAPPINGS_WITH_QUALITY_CONTROL() {
    PublishedTaskIdMappings taskIdMappings = new PublishedTaskIdMappings(new HashMap<>(), new HashMap<>());
    taskIdMappings.getOntologyVerificationTaskIdMappings().put(FIRST_MOCK_UUID, "FIRST-CS-ID");
    taskIdMappings.getOntologyVerificationTaskIdMappings().put(SECOND_MOCK_UUID, "SECOND-CS-ID");

    taskIdMappings.getQualityControlTaskIdMappings().put(FIRST_QC_MOCK_UUID, "FIRST-QC-CS-ID");
    taskIdMappings.getQualityControlTaskIdMappings().put(SECOND_QC_MOCK_UUID, "SECOND-QC-CS-ID");

    return taskIdMappings;
  }


  public static List<PublishedVerificationTaskDto> EXPECTED_PUBLISHED_TASKS_WITHOUT_CONTEXT_DTOS() {
    return
        EXPECTED_TASKS_WITHOUT_CONTEXT_DTOS()
            .stream().map(
                verificationTaskDto -> new PublishedVerificationTaskDto(
                    verificationTaskDto.getVerificationName(),
                    verificationTaskDto.getOntologyName(),
                    verificationTaskDto.getOntologyElementId(),
                    verificationTaskDto.getTaskHtml(),
                    getExternalCrowdsourcingId(verificationTaskDto.getOntologyElementId())
                )
            )
            .toList();
  }

  public static List<PublishedVerificationTask> EXPECTED_PUBLISHED_TASKS_WITHOUT_CONTEXT() {
    return
        EXPECTED_TASKS_WITHOUT_CONTEXT()
            .stream().map(
                verificationTask -> new PublishedVerificationTask(
                    verificationTask.getVerificationName(),
                    verificationTask.getOntologyName(),
                    verificationTask.getOntologyElementId(),
                    verificationTask.getTaskHtml(),
                    getExternalCrowdsourcingId(verificationTask.getOntologyElementId())
                )
            )
            .toList();
  }

  public static List<PublishedVerificationTaskDto> EXPECTED_PUBLISHED_TASKS_DTOS_WITH_CONTEXT() {
    return
        EXPECTED_TASKS_WITH_CONTEXT_DTOS()
            .stream().map(
                verificationTask -> new PublishedVerificationTaskDto(
                    verificationTask.getVerificationName(),
                    verificationTask.getOntologyName(),
                    verificationTask.getOntologyElementId(),
                    verificationTask.getTaskHtml(),
                    getExternalCrowdsourcingId(verificationTask.getOntologyElementId())
                )
            )
            .toList();
  }

  private static String getExternalCrowdsourcingId(UUID extracedModelElements) {
    if (extracedModelElements.equals(FIRST_MOCK_UUID)) {
      return "FIRST-CS-ID";
    } else if (extracedModelElements.equals(SECOND_MOCK_UUID)) {
      return "SECOND-CS-ID";
    }
    return null;
  }

}
