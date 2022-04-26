package at.kk.msc.hcov.core.service.ontology.data.impl;

import at.kk.msc.hcov.core.persistence.triples.ITripleStoreRepository;
import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNameAlreadyInUseException;
import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNotFoundException;
import at.kk.msc.hcov.core.service.ontology.data.IDataProvider;
import at.kk.msc.hcov.core.service.ontology.loading.IExternalOntologyLoader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.apache.jena.ontology.OntModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataProvider implements IDataProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(DataProvider.class);

  private IExternalOntologyLoader ontologyLoader;
  private ITripleStoreRepository tripleStoreRepository;

  @Override
  public void uploadOntologyFromFile(String path, String ontologyName) throws IOException, OntologyNameAlreadyInUseException {
    OntModel loadedModel = ontologyLoader.loadOntology(path);
    tripleStoreRepository.persist(loadedModel, ontologyName);
  }

  @Override
  public Map<UUID, OntModel> extractAndStoreRequiredOntologyElements(
      String ontologyName, String verificationName, Function<OntModel, List<OntModel>> extractor
  ) throws OntologyNotFoundException, IOException {
    OntModel ontModel = loadOntModelFromRepositoryOrThrow(ontologyName);
    List<OntModel> extractedElements = extractor.apply(ontModel);
    return tripleStoreRepository.persistExtractedSubOntologies(ontologyName, verificationName, extractedElements);
  }

  private OntModel loadOntModelFromRepositoryOrThrow(String ontologyName) throws OntologyNotFoundException {
    try {
      return tripleStoreRepository.load(ontologyName);
    } catch (OntologyNotFoundException e) {
      LOGGER.error("Could not find ontology in triple store repository! Please upload the ontology the first using",
          e); //TODO specify the method to upload
      throw e;
    }
  }
}
