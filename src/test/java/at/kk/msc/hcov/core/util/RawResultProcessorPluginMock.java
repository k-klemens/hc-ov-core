package at.kk.msc.hcov.core.util;

import static at.kk.msc.hcov.core.util.mockdata.PublishedTaskMockData.FIRST_CS_ID;
import static at.kk.msc.hcov.core.util.mockdata.PublishedTaskMockData.SECOND_CS_ID;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskMockData.FIRST_MOCK_UUID;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskMockData.SECOND_MOCK_UUID;

import at.kk.msc.hcov.sdk.crowdsourcing.processing.IProcessorPlugin;
import at.kk.msc.hcov.sdk.crowdsourcing.processing.ProcessingResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RawResultProcessorPluginMock implements IProcessorPlugin {

  @Override
  public List<ProcessingResult> process(List<ProcessingResult> list) {
    return List.of(
        new ProcessingResult(
            FIRST_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", FIRST_MOCK_UUID.toString(),
                "CROWDSOURCING_ID", FIRST_CS_ID,
                "RESULT_ID", FIRST_CS_ID + "-R1",
                "WORKER", "W1",
                "ANSWER", "Y"
            ),
            "Raw results as obtained from the crowdsourcing platform"
        ),
        new ProcessingResult(
            FIRST_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", FIRST_MOCK_UUID.toString(),
                "CROWDSOURCING_ID", FIRST_CS_ID,
                "RESULT_ID", FIRST_CS_ID + "-R2",
                "WORKER", "W2",
                "ANSWER", "Y"
            ),
            "Raw results as obtained from the crowdsourcing platform"
        ),
        new ProcessingResult(
            FIRST_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", FIRST_MOCK_UUID.toString(),
                "CROWDSOURCING_ID", FIRST_CS_ID,
                "RESULT_ID", FIRST_CS_ID + "-R3",
                "WORKER", "W3",
                "ANSWER", "Y"
            ),
            "Raw results as obtained from the crowdsourcing platform"
        ),
        new ProcessingResult(
            FIRST_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", FIRST_MOCK_UUID.toString(),
                "CROWDSOURCING_ID", FIRST_CS_ID,
                "RESULT_ID", FIRST_CS_ID + "-R4",
                "WORKER", "W4",
                "ANSWER", "Y"
            ),
            "Raw results as obtained from the crowdsourcing platform"
        ),
        new ProcessingResult(
            FIRST_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", FIRST_MOCK_UUID.toString(),
                "CROWDSOURCING_ID", FIRST_CS_ID,
                "RESULT_ID", FIRST_CS_ID + "-R5",
                "WORKER", "W5",
                "ANSWER", "Y"
            ),
            "Raw results as obtained from the crowdsourcing platform"
        ),
        new ProcessingResult(
            SECOND_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", SECOND_MOCK_UUID.toString(),
                "CROWDSOURCING_ID", SECOND_CS_ID,
                "RESULT_ID", SECOND_CS_ID + "-R1",
                "WORKER", "W1",
                "ANSWER", "Y"
            ),
            "Raw results as obtained from the crowdsourcing platform"
        ),
        new ProcessingResult(
            SECOND_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", SECOND_MOCK_UUID.toString(),
                "CROWDSOURCING_ID", SECOND_CS_ID,
                "RESULT_ID", SECOND_CS_ID + "-R2",
                "WORKER", "W2",
                "ANSWER", "Y"
            ),
            "Raw results as obtained from the crowdsourcing platform"
        ),
        new ProcessingResult(
            SECOND_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", SECOND_MOCK_UUID.toString(),
                "CROWDSOURCING_ID", SECOND_CS_ID,
                "RESULT_ID", SECOND_CS_ID + "-R3",
                "WORKER", "W3",
                "ANSWER", "N"
            ),
            "Raw results as obtained from the crowdsourcing platform"
        ),
        new ProcessingResult(
            SECOND_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", SECOND_MOCK_UUID.toString(),
                "CROWDSOURCING_ID", SECOND_CS_ID,
                "RESULT_ID", SECOND_CS_ID + "-R4",
                "WORKER", "W4",
                "ANSWER", "N"
            ),
            "Raw results as obtained from the crowdsourcing platform"
        ),
        new ProcessingResult(
            SECOND_CS_ID,
            Map.of(
                "EXTRACTED_ELEMENTS_ID", SECOND_MOCK_UUID.toString(),
                "CROWDSOURCING_ID", SECOND_CS_ID,
                "RESULT_ID", SECOND_CS_ID + "-R5",
                "WORKER", "W5",
                "ANSWER", "Y"
            ),
            "Raw results as obtained from the crowdsourcing platform"
        )
    );
  }

  @Override
  public List<String> requiresInputResultDataKeys() {
    // This raw result processor does not require any input to be present just the results obtained from the crowdsourcing manager.
    return new ArrayList<>();
  }

  @Override
  public boolean supports(String s) {
    return "RAW_RESULT_PROCESSOR".equalsIgnoreCase(s);
  }

  @Override
  public void setConfiguration(Map<String, Object> map) {

  }

  @Override
  public Map<String, Object> getConfiguration() {
    return null;
  }
}
