package at.kk.msc.hcov.core.service.processing.plugin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import at.kk.msc.hcov.core.util.mockdata.ResultMockData;
import at.kk.msc.hcov.sdk.crowdsourcing.processing.ProcessingResult;
import at.kk.msc.hcov.sdk.crowdsourcing.processing.RequiredKeyNotPresentException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RawDataProcessorPluginTest {

  RawDataProcessorPlugin target;

  @BeforeEach
  void setUp() {
    target = new RawDataProcessorPlugin();
  }

  @Test
  public void testProcess() throws RequiredKeyNotPresentException {
    // given
    List<ProcessingResult> givenData = ResultMockData.MOCKED_PROCESSING_RESULT_AS_FROM_DATA_PROCESSOR();

    // when
    List<ProcessingResult> actual = target.process(givenData);

    // then
    assertThat(actual).isEqualTo(ResultMockData.EXPECTED_PROCESSING_RESULT_RAW_RESULT_PROCESSOR());
  }

  @Test
  public void testProcess_KeyNotGiven() throws RequiredKeyNotPresentException {
    // given
    List<ProcessingResult> givenData = ResultMockData.EXPECTED_PROCESSING_RESULT_RAW_RESULT_PROCESSOR();

    // when
    assertThatThrownBy(() -> target.process(givenData))
        .isInstanceOf(RequiredKeyNotPresentException.class);
  }
}
