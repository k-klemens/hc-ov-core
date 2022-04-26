package at.kk.msc.hcov.core.persistence.triples.impl;

import at.kk.msc.hcov.core.persistence.triples.ITripleStoreRepository;
import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNameAlreadyInUseException;
import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNotFoundException;
import at.kk.msc.hcov.core.service.ontology.loading.IExternalOntologyLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Implementation of a triple store that stores ontologies in RDFXML
 */
@Repository
public class FileTripleStoreRepository implements ITripleStoreRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileTripleStoreRepository.class);
  @Getter
  private String storageBasePath;
  private File storageDirectory;

  private IExternalOntologyLoader fileOntologyLoader;

  public FileTripleStoreRepository(
      @Value("${fileTripleStore.storageBasePath:}") String storageBasePath,
      IExternalOntologyLoader fileOntologyLoader
  ) throws IOException {
    setStorageBasePathOrDefault(storageBasePath);
    setUpStorageDirectory();
    this.fileOntologyLoader = fileOntologyLoader;
  }

  @Override
  public void persist(OntModel ontModel, String name) throws IOException, OntologyNameAlreadyInUseException {
    File pathToOntology = createRequiredStorageDirsOrThrowIfExists(name);
    persistOntModel(pathToOntology, ontModel, name);
  }

  @Override
  public Map<UUID, OntModel> persistExtractedSubOntologies(String ontologyName, String verificationName, List<OntModel> extractedElements)
      throws OntologyNotFoundException, IOException {
    File storeDir = checkIfStoreDirExistsOrElseThorw(ontologyName);

    File verificationStoreDir = new File(storeDir, verificationName);
    verificationStoreDir.mkdirs();

    Map<UUID, OntModel> returnMap = extractedElements.stream()
        .collect(Collectors.toMap(o -> UUID.randomUUID(), o -> o));

    for (Map.Entry<UUID, OntModel> subModelEntry : returnMap.entrySet()) {
      UUID subOntologyUUID = subModelEntry.getKey();
      File subOntologyFile = new File(verificationStoreDir, subOntologyUUID + ".owl.xml");
      persistOntModel(subOntologyFile, subModelEntry.getValue(), "SubOntology_" + subOntologyUUID + "_Of_" + ontologyName);
    }

    return returnMap;
  }

  @Override
  public OntModel load(String name) throws OntologyNotFoundException {
    String pathToOntology = createStorePathFromName(name);
    try {
      return fileOntologyLoader.loadOntology(pathToOntology);
    } catch (FileNotFoundException e) {
      throw new OntologyNotFoundException(name, e);
    }
  }

  private void persistOntModel(File path, OntModel ontModel, String name) throws IOException {
    try (OutputStream fileWriter = new FileOutputStream(path, false)) {
      LOGGER.info("Started writing ontology {} to {}. This might take some time ...", name, path.getAbsolutePath());
      RDFDataMgr.write(fileWriter, ontModel, Lang.RDFXML);
      LOGGER.info("Successfully persisted ontology {}", name);
    } catch (IOException e) {
      LOGGER.error("Error persisting ontology {} at {}!", name, path.getAbsolutePath());
      throw e;
    }
  }

  private File createRequiredStorageDirsOrThrowIfExists(String name) throws OntologyNameAlreadyInUseException {
    File pathToStoreDir = new File(createStoreDirFromName(name));
    if (pathToStoreDir.exists()) {
      throw new OntologyNameAlreadyInUseException(name, pathToStoreDir);
    } else {
      pathToStoreDir.mkdirs();
    }
    return new File(pathToStoreDir, name + ".owl.xml");
  }

  private File checkIfStoreDirExistsOrElseThorw(String name) throws OntologyNotFoundException {
    File storeDir = new File(createStoreDirFromName(name));
    if (!storeDir.exists()) {
      throw new OntologyNotFoundException(name, new FileNotFoundException(storeDir.getAbsolutePath()));
    }
    return storeDir;
  }

  private String createStorePathFromName(String name) {
    return createStoreDirFromName(name) + File.separator + name + ".owl.xml";
  }

  private String createStoreDirFromName(String name) {
    return storageDirectory + File.separator + name;
  }

  private void setStorageBasePathOrDefault(String storagePath) {
    if (storagePath == null || storagePath.isBlank()) {
      this.storageBasePath = System.getProperty("user.home");
      LOGGER.info("Property fileTripleStore.storagePath not provided or empty using {} instead.", this.storageBasePath);
    } else {
      this.storageBasePath = storagePath;
    }
  }

  private void setUpStorageDirectory() throws IOException {
    this.storageDirectory = new File(this.storageBasePath + File.separator + ".hcov-triplestore");
    storageDirectory.mkdirs();
    if (!storageDirectory.exists()) {
      throw new IOException("Could not create storage directory for triple store at " + storageDirectory.getAbsolutePath() + "!");
    }
  }
}
