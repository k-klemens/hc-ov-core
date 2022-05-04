package at.kk.msc.hcov.core.service.processing.plugin;

import at.kk.msc.hcov.sdk.crowdsourcing.processing.IProcessorPlugin;
import at.kk.msc.hcov.sdk.crowdsourcing.processing.ProcessingResult;
import at.kk.msc.hcov.sdk.crowdsourcing.processing.RequiredKeyNotPresentException;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Processor plugin that consumes data as provided by the {@link at.kk.msc.hcov.core.service.processing.impl.DataProcessor} and outputs them without the
 * FROM_RAW key.
 */
@Component
public class RawDataProcessorPlugin implements IProcessorPlugin {

  private Map<String, Object> configuration;

  @Override
  public List<ProcessingResult> process(List<ProcessingResult> list) throws RequiredKeyNotPresentException {
    validateAllInputHasRequiredKeys(list);
    list.forEach(
        processingResult -> processingResult.getResultData().remove("FROM_RAW")
    );
    return list;
  }

  @Override
  public List<String> requiresInputResultDataKeys() {
    return List.of("FROM_RAW");
  }

  @Override
  public boolean supports(String s) {
    return "RAW_DATA_PROCESSOR".equalsIgnoreCase(s);
  }

  @Override
  public void setConfiguration(Map<String, Object> map) {
    this.configuration = map;
  }

  @Override
  public Map<String, Object> getConfiguration() {
    return configuration;
  }
}
