package at.kk.msc.hcov.core.util.mockdata;

import static at.kk.msc.hcov.core.util.mockdata.PublishedTaskMockData.FIRST_CS_ID;
import static at.kk.msc.hcov.core.util.mockdata.PublishedTaskMockData.SECOND_CS_ID;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskMockData.FIRST_MOCK_UUID;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskMockData.SECOND_MOCK_UUID;

import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.RawResult;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ResultMockData {

  public static Map<String, List<RawResult>> MOCKED_RAW_RESULT_MAP() {
    return Map.of(
        FIRST_CS_ID, List.of(
            new RawResult(FIRST_CS_ID + "-R1", FIRST_CS_ID, "W1", "Y"),
            new RawResult(FIRST_CS_ID + "-R2", FIRST_CS_ID, "W2", "Y"),
            new RawResult(FIRST_CS_ID + "-R3", FIRST_CS_ID, "W3", "Y"),
            new RawResult(FIRST_CS_ID + "-R4", FIRST_CS_ID, "W4", "Y"),
            new RawResult(FIRST_CS_ID + "-R5", FIRST_CS_ID, "W5", "Y")
        ),
        SECOND_CS_ID, List.of(
            new RawResult(SECOND_CS_ID + "-R1", SECOND_CS_ID, "W1", "Y"),
            new RawResult(SECOND_CS_ID + "-R2", SECOND_CS_ID, "W2", "Y"),
            new RawResult(SECOND_CS_ID + "-R3", SECOND_CS_ID, "W3", "N"),
            new RawResult(SECOND_CS_ID + "-R4", SECOND_CS_ID, "W4", "N"),
            new RawResult(SECOND_CS_ID + "-R5", SECOND_CS_ID, "W5", "Y")
        )
    );
  }

  public static Map<UUID, List<RawResult>> EXPECTED_RAW_RESULT_MAP() {
    return Map.of(
        FIRST_MOCK_UUID, List.of(
            new RawResult(FIRST_CS_ID + "-R1", FIRST_CS_ID, "W1", "Y"),
            new RawResult(FIRST_CS_ID + "-R2", FIRST_CS_ID, "W2", "Y"),
            new RawResult(FIRST_CS_ID + "-R3", FIRST_CS_ID, "W3", "Y"),
            new RawResult(FIRST_CS_ID + "-R4", FIRST_CS_ID, "W4", "Y"),
            new RawResult(FIRST_CS_ID + "-R5", FIRST_CS_ID, "W5", "Y")
        ),
        SECOND_MOCK_UUID, List.of(
            new RawResult(SECOND_CS_ID + "-R1", SECOND_CS_ID, "W1", "Y"),
            new RawResult(SECOND_CS_ID + "-R2", SECOND_CS_ID, "W2", "Y"),
            new RawResult(SECOND_CS_ID + "-R3", SECOND_CS_ID, "W3", "N"),
            new RawResult(SECOND_CS_ID + "-R4", SECOND_CS_ID, "W4", "N"),
            new RawResult(SECOND_CS_ID + "-R5", SECOND_CS_ID, "W5", "Y")
        )
    );
  }
}
