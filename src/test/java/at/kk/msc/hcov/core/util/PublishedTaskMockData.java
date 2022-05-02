package at.kk.msc.hcov.core.util;

import static at.kk.msc.hcov.core.util.VerificationTaskMockData.FIRST_MOCK_UUID;
import static at.kk.msc.hcov.core.util.VerificationTaskMockData.SECOND_MOCK_UUID;

import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.PublishedTask;
import java.util.List;

public class PublishedTaskMockData {
  
  public static List<PublishedTask> MOCKED_PUBLISHED_TASKS() {
    return List.of(
        new PublishedTask("FIRST-CS-ID", FIRST_MOCK_UUID),
        new PublishedTask("SECOND-CS-ID", SECOND_MOCK_UUID)
    );
  }

}
