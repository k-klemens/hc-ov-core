package at.kk.msc.hcov.core.service.ontology.loading;

import java.io.FileNotFoundException;
import org.apache.jena.ontology.OntModel;

/**
 * Provides an interface to load ontologies not yet imported to an instance of {@link at.kk.msc.hcov.core.persistence.triples.ITripleStore}.
 */
public interface IExternalOntologyLoader {

  OntModel loadOntology(String path) throws FileNotFoundException;

}
