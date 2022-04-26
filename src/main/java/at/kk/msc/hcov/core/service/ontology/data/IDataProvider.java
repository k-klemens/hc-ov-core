package at.kk.msc.hcov.core.service.ontology.data;

import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNameAlreadyInUseException;
import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import org.apache.jena.ontology.OntModel;

public interface IDataProvider {

  /**
   * Method to upload an ontology from a file to the platform's triple store repository.
   *
   * @param path         to the ontology file.
   * @param ontologyName name of the ontology to be used for storing the ontology in the repository.
   * @throws IOException                       if some file handling errors occur.
   * @throws OntologyNameAlreadyInUseException if the ontology name is already known to the repository.
   */
  void uploadOntologyFromFile(String path, String ontologyName) throws IOException, OntologyNameAlreadyInUseException;

  /**
   * Method to Create sub-ontologies of the provided ontology from the repository. Each of the sub-ontologies corresponds to one HIT / unit of verification
   *
   * @param ontologyName     name of hte ontology to loaded from the repository.
   * @param verificationName name of the verfication the elements shall be extracted for.
   * @param extractor        function to create the elements needed for one verification task.
   * @return a Map of UUID,OntModel pairs, where each of the models contains the elements needed for one verification task.
   * @throws OntologyNotFoundException if the ontology is not already uploaded to the ontology (see uploadOntologyFromFile).
   * @throws IOException               if some file handling causes an exception.
   */
  Map<UUID, OntModel> extractAndStoreRequiredOntologyElements(
      String ontologyName,
      String verificationName,
      Function<OntModel, List<OntModel>> extractor
  ) throws OntologyNotFoundException, IOException;

}
