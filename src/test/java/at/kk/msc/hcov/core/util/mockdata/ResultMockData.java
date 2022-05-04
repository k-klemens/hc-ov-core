package at.kk.msc.hcov.core.util.mockdata;

import static at.kk.msc.hcov.core.util.mockdata.PublishedTaskMockData.FIRST_CS_ID;
import static at.kk.msc.hcov.core.util.mockdata.PublishedTaskMockData.SECOND_CS_ID;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskMockData.FIRST_MOCK_UUID;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskMockData.SECOND_MOCK_UUID;

import at.kk.msc.hcov.core.service.processing.model.DataProcessorResults;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.RawResult;
import at.kk.msc.hcov.sdk.crowdsourcing.processing.ProcessingResult;
import java.util.HashMap;
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

  public static List<ProcessingResult> MOCKED_PROCESSING_RESULT_AS_FROM_DATA_PROCESSOR() {

    return List.of(
        new ProcessingResult(
            FIRST_CS_ID,
            new HashMap<>(Map.of(
                "FROM_RAW", "TRUE -> This list of processing data is obtained from a List of RawResults",
                "EXTRACTED_ELEMENTS_ID", FIRST_MOCK_UUID.toString(),
                "RESULT_ID", FIRST_CS_ID + "-R1",
                "WORKER_ID", "W1",
                "ANSWER", "Y"
            )),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            FIRST_CS_ID,
            new HashMap<>(Map.of(
                "FROM_RAW", "TRUE -> This list of processing data is obtained from a List of RawResults",
                "EXTRACTED_ELEMENTS_ID", FIRST_MOCK_UUID.toString(),
                "RESULT_ID", FIRST_CS_ID + "-R2",
                "WORKER_ID", "W2",
                "ANSWER", "Y"
            )),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            FIRST_CS_ID,
            new HashMap<>(Map.of(
                "FROM_RAW", "TRUE -> This list of processing data is obtained from a List of RawResults",
                "EXTRACTED_ELEMENTS_ID", FIRST_MOCK_UUID.toString(),
                "RESULT_ID", FIRST_CS_ID + "-R3",
                "WORKER_ID", "W3",
                "ANSWER", "Y"
            )),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            FIRST_CS_ID,
            new HashMap<>(Map.of(
                "FROM_RAW", "TRUE -> This list of processing data is obtained from a List of RawResults",
                "EXTRACTED_ELEMENTS_ID", FIRST_MOCK_UUID.toString(),
                "RESULT_ID", FIRST_CS_ID + "-R4",
                "WORKER_ID", "W4",
                "ANSWER", "Y"
            )),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            FIRST_CS_ID,
            new HashMap<>(Map.of(
                "FROM_RAW", "TRUE -> This list of processing data is obtained from a List of RawResults",
                "EXTRACTED_ELEMENTS_ID", FIRST_MOCK_UUID.toString(),
                "RESULT_ID", FIRST_CS_ID + "-R5",
                "WORKER_ID", "W5",
                "ANSWER", "Y"
            )),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            SECOND_CS_ID,
            new HashMap<>(Map.of(
                "FROM_RAW", "TRUE -> This list of processing data is obtained from a List of RawResults",
                "EXTRACTED_ELEMENTS_ID", SECOND_MOCK_UUID.toString(),
                "RESULT_ID", SECOND_CS_ID + "-R1",
                "WORKER_ID", "W1",
                "ANSWER", "Y"
            )),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            SECOND_CS_ID,
            new HashMap<>(Map.of(
                "FROM_RAW", "TRUE -> This list of processing data is obtained from a List of RawResults",
                "EXTRACTED_ELEMENTS_ID", SECOND_MOCK_UUID.toString(),
                "RESULT_ID", SECOND_CS_ID + "-R2",
                "WORKER_ID", "W2",
                "ANSWER", "Y"
            )),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            SECOND_CS_ID,
            new HashMap<>(Map.of(
                "FROM_RAW", "TRUE -> This list of processing data is obtained from a List of RawResults",
                "EXTRACTED_ELEMENTS_ID", SECOND_MOCK_UUID.toString(),
                "RESULT_ID", SECOND_CS_ID + "-R3",
                "WORKER_ID", "W3",
                "ANSWER", "N"
            )),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            SECOND_CS_ID,
            new HashMap<>(Map.of(
                "FROM_RAW", "TRUE -> This list of processing data is obtained from a List of RawResults",
                "EXTRACTED_ELEMENTS_ID", SECOND_MOCK_UUID.toString(),
                "RESULT_ID", SECOND_CS_ID + "-R4",
                "WORKER_ID", "W4",
                "ANSWER", "N"
            )),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            SECOND_CS_ID,
            new HashMap<>(Map.of(
                "FROM_RAW", "TRUE -> This list of processing data is obtained from a List of RawResults",
                "EXTRACTED_ELEMENTS_ID", SECOND_MOCK_UUID.toString(),
                "RESULT_ID", SECOND_CS_ID + "-R5",
                "WORKER_ID", "W5",
                "ANSWER", "Y"
            )),
            "Results as obtained from the crowdsourcing platform."
        )
    );
  }

  public static List<ProcessingResult> EXPECTED_PROCESSING_RESULT_RAW_RESULT_PROCESSOR() {
    return List.of(
        new ProcessingResult(
            FIRST_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", FIRST_MOCK_UUID.toString(),
                "RESULT_ID", FIRST_CS_ID + "-R1",
                "WORKER_ID", "W1",
                "ANSWER", "Y"
            ),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            FIRST_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", FIRST_MOCK_UUID.toString(),
                "RESULT_ID", FIRST_CS_ID + "-R2",
                "WORKER_ID", "W2",
                "ANSWER", "Y"
            ),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            FIRST_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", FIRST_MOCK_UUID.toString(),
                "RESULT_ID", FIRST_CS_ID + "-R3",
                "WORKER_ID", "W3",
                "ANSWER", "Y"
            ),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            FIRST_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", FIRST_MOCK_UUID.toString(),
                "RESULT_ID", FIRST_CS_ID + "-R4",
                "WORKER_ID", "W4",
                "ANSWER", "Y"
            ),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            FIRST_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", FIRST_MOCK_UUID.toString(),
                "RESULT_ID", FIRST_CS_ID + "-R5",
                "WORKER_ID", "W5",
                "ANSWER", "Y"
            ),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            SECOND_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", SECOND_MOCK_UUID.toString(),
                "RESULT_ID", SECOND_CS_ID + "-R1",
                "WORKER_ID", "W1",
                "ANSWER", "Y"
            ),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            SECOND_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", SECOND_MOCK_UUID.toString(),
                "RESULT_ID", SECOND_CS_ID + "-R2",
                "WORKER_ID", "W2",
                "ANSWER", "Y"
            ),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            SECOND_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", SECOND_MOCK_UUID.toString(),
                "RESULT_ID", SECOND_CS_ID + "-R3",
                "WORKER_ID", "W3",
                "ANSWER", "N"
            ),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            SECOND_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", SECOND_MOCK_UUID.toString(),
                "RESULT_ID", SECOND_CS_ID + "-R4",
                "WORKER_ID", "W4",
                "ANSWER", "N"
            ),
            "Results as obtained from the crowdsourcing platform."
        ),
        new ProcessingResult(
            SECOND_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", SECOND_MOCK_UUID.toString(),
                "RESULT_ID", SECOND_CS_ID + "-R5",
                "WORKER_ID", "W5",
                "ANSWER", "Y"
            ),
            "Results as obtained from the crowdsourcing platform."
        )
    );
  }

  public static List<DataProcessorResults> EXPECTED_DATA_PROCESSOR_RESULTS_ONLY_RAW() {
    return List.of(
        new DataProcessorResults("RAW_DATA_PROCESSOR", EXPECTED_PROCESSING_RESULT_RAW_RESULT_PROCESSOR())
    );
  }
}
