package at.kk.msc.hcov.core.service.ontology.metrics.stub;

import at.kk.msc.hcov.core.service.ontology.metrics.IOntologyMetrics;
import java.util.HashMap;
import java.util.Map;
import org.apache.jena.ontology.OntModel;
import org.springframework.stereotype.Component;

@Component
public class IOntologyMetricsStub implements IOntologyMetrics {

  @Override
  public Map<String, String> calculateMetrics(OntModel ontModel) {
    return new HashMap<>();
  }

}
