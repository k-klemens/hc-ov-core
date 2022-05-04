package at.kk.msc.hcov.core.util.mockdata;

import at.kk.msc.hcov.core.endpoint.dto.DataProcessorResultsDto;
import at.kk.msc.hcov.core.endpoint.dto.ProcessingResultDto;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.RawResult;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProcessingIntegrationTestMockData {

  public static Map<UUID, String> MOCKED_MOVIES_TASK_ID_MAPPINGS() {
    return Map.of(
        UUID.fromString("521073c6-8310-413e-a07a-0c3fc50b91bf"), "WriterExternalId",
        UUID.fromString("4e2bae67-bd63-4bb7-b53f-e8f01ff82f69"), "PersonExternalId",
        UUID.fromString("c25aec9e-2b10-4318-a21c-b0f6ac1caaf5"), "MovieDirectorExternalId",
        UUID.fromString("2ed75e19-2485-486a-8722-5386dd447cf5"), "ActorExternalId"
    );
  }

  public static Map<String, UUID> MOCKED_MOVIES_CROWDSOUCRING_ID_MAPPINGS() {
    return Map.of(
        "WriterExternalId", UUID.fromString("521073c6-8310-413e-a07a-0c3fc50b91bf"),
        "PersonExternalId", UUID.fromString("4e2bae67-bd63-4bb7-b53f-e8f01ff82f69"),
        "MovieDirectorExternalId", UUID.fromString("c25aec9e-2b10-4318-a21c-b0f6ac1caaf5"),
        "ActorExternalId", UUID.fromString("2ed75e19-2485-486a-8722-5386dd447cf5")
    );
  }

  public static Map<String, List<RawResult>> getMockedMovieRawResultMap() {
    return Map.of(
        "WriterExternalId", List.of(
            new RawResult("WriterResultId1", "WriterExternalId", "W1", "A"),
            new RawResult("WriterResultId2", "WriterExternalId", "W2", "A"),
            new RawResult("WriterResultId3", "WriterExternalId", "W3", "B"),
            new RawResult("WriterResultId4", "WriterExternalId", "W4", "A"),
            new RawResult("WriterResultId5", "WriterExternalId", "W5", "A")
        ),
        "PersonExternalId", List.of(
            new RawResult("PersonResultId1", "PersonExternalId", "W1", "A"),
            new RawResult("PersonResultId2", "PersonExternalId", "W2", "B"),
            new RawResult("PersonResultId3", "PersonExternalId", "W3", "B"),
            new RawResult("PersonResultId4", "PersonExternalId", "W4", "B"),
            new RawResult("PersonResultId5", "PersonExternalId", "W5", "C")
        ),
        "MovieDirectorExternalId", List.of(
            new RawResult("MovieDirectorResultId", "MovieDirectorExternalId", "W1", "C"),
            new RawResult("MovieDirectorResultId", "MovieDirectorExternalId", "W2", "B"),
            new RawResult("MovieDirectorResultId", "MovieDirectorExternalId", "W3", "B"),
            new RawResult("MovieDirectorResultId", "MovieDirectorExternalId", "W4", "C"),
            new RawResult("MovieDirectorResultId", "MovieDirectorExternalId", "W5", "C")
        ),
        "ActorExternalId", List.of(
            new RawResult("ActorResultId", "ActorExternalId", "W1", "A"),
            new RawResult("ActorResultId", "ActorExternalId", "W2", "A"),
            new RawResult("ActorResultId", "ActorExternalId", "W3", "A"),
            new RawResult("ActorResultId", "ActorExternalId", "W4", "A"),
            new RawResult("ActorResultId", "ActorExternalId", "W5", "A")
        )
    );
  }

  public static DataProcessorResultsDto EXPECTED_DATA_PROCESSOR_RESULTS_MOVIES_DTO() {
    List<ProcessingResultDto> processingResultDtos = getMockedMovieRawResultMap().values().stream()
        .flatMap(Collection::stream)
        .map(
            rawResult -> new ProcessingResultDto(
                rawResult.getCrowdscouringId(),
                new HashMap<>(Map.of(
                    "EXTRACTED_ELEMENTS_ID", MOCKED_MOVIES_CROWDSOUCRING_ID_MAPPINGS().get(rawResult.getCrowdscouringId()).toString(),
                    "RESULT_ID", rawResult.getResultId(),
                    "WORKER_ID", rawResult.getWorkerId(),
                    "ANSWER", rawResult.getAnswer()
                )),
                "Results as obtained from the crowdsourcing platform."
            )
        ).toList();

    return new DataProcessorResultsDto("RAW_DATA_PROCESSOR", processingResultDtos);

  }
}
