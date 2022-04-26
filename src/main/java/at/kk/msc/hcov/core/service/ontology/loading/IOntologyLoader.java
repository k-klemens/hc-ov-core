package at.kk.msc.hcov.core.service.ontology.loading;

import java.io.FileNotFoundException;
import org.apache.jena.ontology.OntModel;

public interface IOntologyLoader {

  OntModel loadOntology(String path) throws FileNotFoundException;

}
