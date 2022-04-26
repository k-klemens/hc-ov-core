package at.kk.msc.hcov.core.service.ontology.loading;

import at.kk.msc.hcov.core.persistence.triples.ITripleStoreRepository;
import java.io.FileNotFoundException;
import org.apache.jena.ontology.OntModel;

/**
 * Provides an interface to load ontologies not yet imported to an instance of {@link ITripleStoreRepository}.
 */
public interface IExternalOntologyLoader {

  /**
   * Loads an ontlogy from a provided path.
   *
   * @param path where to load the ontology from.
   * @return an {@link OntModel} object that contains the ontology.
   * @throws FileNotFoundException if the provided path is not found.
   */
  OntModel loadOntology(String path) throws FileNotFoundException;

}
