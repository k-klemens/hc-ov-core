package at.kk.msc.hcov.core.persistence.triples;

import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNameAlreadyInUseException;
import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNotFoundException;
import java.io.IOException;
import org.apache.jena.ontology.OntModel;

/**
 * Interface to connect triple stores which are used to persist ontologies with a given name and load them.
 */
public interface ITripleStoreRepository {

  /**
   * Persists a given ontology model with a given name.
   *
   * @param ontModel the ontology to persist.
   * @param name     the unique name of the ontology.
   * @throws IOException
   * @throws OntologyNameAlreadyInUseException if a triple store already has an ontology stored with the given name.
   */
  void persist(OntModel ontModel, String name) throws IOException, OntologyNameAlreadyInUseException;

  /**
   * Loads an existing ontology with the given name from the triple store.
   *
   * @param name the name of the ontology to be loaded.
   * @return an {@link OntModel} object of the given ontology.
   * @throws OntologyNotFoundException if no ontology with the given name is found in the repository.
   */
  OntModel load(String name) throws OntologyNotFoundException;

}
