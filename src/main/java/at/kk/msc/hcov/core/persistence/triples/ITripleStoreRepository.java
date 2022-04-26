package at.kk.msc.hcov.core.persistence.triples;

import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNameAlreadyInUseException;
import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
   * Persists extracted sub-ontologies (= data for one hit / units of verification) of one ontology used for a verification.
   *
   * @param ontologyName      name of the ontology to be verified.
   * @param verificationName  name of the verification conducted.
   * @param extractedElements elements extracted from the ontology
   * @return a Map of UUIDs and OntModels, where the uuids are used for storing the OntModels.
   * @throws OntologyNotFoundException if no ontology with the given name is found in the repository.
   */
  Map<UUID, OntModel> persistExtractedSubOntologies(
      String ontologyName, String verificationName, List<OntModel> extractedElements
  ) throws OntologyNotFoundException, IOException;

  /**
   * Loads an existing ontology with the given name from the triple store.
   *
   * @param name the name of the ontology to be loaded.
   * @return an {@link OntModel} object of the given ontology.
   * @throws OntologyNotFoundException if no ontology with the given name is found in the repository.
   */
  OntModel load(String name) throws OntologyNotFoundException;

}
