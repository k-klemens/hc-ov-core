package at.kk.msc.hcov.core.service.ontology.metrics;

import java.util.Map;
import org.apache.jena.ontology.OntModel;

/**
 * Interface for implementing classes that supply general information about ontologies.
 */
public interface IOntologyMetrics {


  /**
   * Calculates the targeted metrics of an ontology.
   *
   * @param ontModel the ontology for which the metric shall be created.
   * @return a Map containing the metric names and values.
   */
  Map<String, String> calculateMetrics(OntModel ontModel);

}
