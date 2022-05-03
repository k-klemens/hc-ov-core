package at.kk.msc.hcov.core.util;

import static at.kk.msc.hcov.core.util.PublishedTaskMockData.FIRST_CS_ID;
import static at.kk.msc.hcov.core.util.PublishedTaskMockData.SECOND_CS_ID;

import at.kk.msc.hcov.core.endpoint.dto.TaskProgressDetailDto;
import at.kk.msc.hcov.core.endpoint.dto.VerificationProgressDto;
import at.kk.msc.hcov.core.service.crowdsourcing.model.TaskProgressDetail;
import at.kk.msc.hcov.core.service.crowdsourcing.model.VerificationProgress;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.HitStatus;
import java.util.List;
import java.util.Map;

public class VerificationProgressMockData {

  public static Map<String, HitStatus> MOCKED_HIT_STATUS_MAP_ALL_COMPLETED() {
    return Map.of(
        FIRST_CS_ID, new HitStatus(FIRST_CS_ID, 5, 5),
        SECOND_CS_ID, new HitStatus(SECOND_CS_ID, 5, 5)
    );
  }

  public static Map<String, HitStatus> MOCKED_HIT_STATUS_MAP_ALL_IN_PROGRESS() {
    return Map.of(
        FIRST_CS_ID, new HitStatus(FIRST_CS_ID, 5, 3),
        SECOND_CS_ID, new HitStatus(SECOND_CS_ID, 5, 1)
    );
  }

  public static Map<String, HitStatus> MOCKED_HIT_STATUS_MAP_MIXED_IN_PROGRESS() {
    return Map.of(
        FIRST_CS_ID, new HitStatus(FIRST_CS_ID, 5, 3),
        SECOND_CS_ID, new HitStatus(SECOND_CS_ID, 5, 5)
    );
  }

  public static VerificationProgress EXPECTED_VERIFICATION_PROGRESS_ALL_COMPLETED() {
    VerificationProgress verificationProgress = new VerificationProgress();
    verificationProgress.setVerificationName(VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_NAME);
    verificationProgress.setCreatedAt(TimeProviderMock.MOCK_TIME);
    verificationProgress.setStatus(VerificationProgress.Status.ALL_TASKS_COMPLETED);
    verificationProgress.setTotalHits(2);
    verificationProgress.setCompletedHits(2);
    verificationProgress.setOpenHits(0);
    verificationProgress.setTaskProgressDetails(
        List.of(
            new TaskProgressDetail(VerificationTaskMockData.FIRST_MOCK_UUID, FIRST_CS_ID, 5, 5, 0),
            new TaskProgressDetail(VerificationTaskMockData.SECOND_MOCK_UUID, SECOND_CS_ID, 5, 5, 0)
        )
    );
    return verificationProgress;
  }

  public static VerificationProgress EXPECTED_VERIFICATION_PROGRESS_ALL_IN_PROGRESS() {
    VerificationProgress verificationProgress = new VerificationProgress();
    verificationProgress.setVerificationName(VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_NAME);
    verificationProgress.setCreatedAt(TimeProviderMock.MOCK_TIME);
    verificationProgress.setStatus(VerificationProgress.Status.PUBLISHED);
    verificationProgress.setTotalHits(2);
    verificationProgress.setCompletedHits(0);
    verificationProgress.setOpenHits(2);
    verificationProgress.setTaskProgressDetails(
        List.of(
            new TaskProgressDetail(VerificationTaskMockData.FIRST_MOCK_UUID, FIRST_CS_ID, 5, 3, 2),
            new TaskProgressDetail(VerificationTaskMockData.SECOND_MOCK_UUID, SECOND_CS_ID, 5, 1, 4)
        )
    );
    return verificationProgress;
  }

  public static VerificationProgress EXPECTED_VERIFICATION_PROGRESS_MIXED_IN_PROGRESS() {
    VerificationProgress verificationProgress = new VerificationProgress();
    verificationProgress.setVerificationName(VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_NAME);
    verificationProgress.setCreatedAt(TimeProviderMock.MOCK_TIME);
    verificationProgress.setStatus(VerificationProgress.Status.PUBLISHED);
    verificationProgress.setTotalHits(2);
    verificationProgress.setCompletedHits(1);
    verificationProgress.setOpenHits(1);
    verificationProgress.setTaskProgressDetails(
        List.of(
            new TaskProgressDetail(VerificationTaskMockData.FIRST_MOCK_UUID, FIRST_CS_ID, 5, 3, 2),
            new TaskProgressDetail(VerificationTaskMockData.SECOND_MOCK_UUID, SECOND_CS_ID, 5, 5, 0)
        )
    );
    return verificationProgress;
  }

  public static VerificationProgressDto EXPECTED_VERIFICATION_PROGRESS_MIXED_IN_PROGRESS_DTO() {
    VerificationProgressDto verificationProgress = new VerificationProgressDto();
    verificationProgress.setVerificationName(VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_NAME);
    verificationProgress.setCreatedAt(TimeProviderMock.MOCK_TIME);
    verificationProgress.setStatus(VerificationProgress.Status.PUBLISHED);
    verificationProgress.setTotalHits(2);
    verificationProgress.setCompletedHits(1);
    verificationProgress.setOpenHits(1);
    verificationProgress.setTaskProgressDetails(
        List.of(
            new TaskProgressDetailDto(VerificationTaskMockData.FIRST_MOCK_UUID, FIRST_CS_ID, 5, 3, 2),
            new TaskProgressDetailDto(VerificationTaskMockData.SECOND_MOCK_UUID, SECOND_CS_ID, 5, 5, 0)
        )
    );
    return verificationProgress;
  }
}
