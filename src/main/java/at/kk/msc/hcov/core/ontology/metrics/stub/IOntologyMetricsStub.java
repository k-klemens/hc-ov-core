package at.kk.msc.hcov.core.ontology.metrics.stub;

import at.kk.msc.hcov.core.ontology.metrics.IOntologyMetrics;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class IOntologyMetricsStub implements IOntologyMetrics {

  @Override
  public Map<String, String> calculateMetrics() {
    return new HashMap<>();
  }

}
