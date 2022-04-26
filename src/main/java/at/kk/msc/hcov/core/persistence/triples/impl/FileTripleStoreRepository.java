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
    File pathToOntology = createFileInstanceOrThrowIfExists(name);
    try (OutputStream fileWriter = new FileOutputStream(pathToOntology, false)) {
      LOGGER.info("Started writing ontology {} to {}. This might take some time ...", name, pathToOntology.getAbsolutePath());
      RDFDataMgr.write(fileWriter, ontModel, Lang.RDFXML);
      LOGGER.info("Successfully persisted ontology {}", name);
    } catch (IOException e) {
      LOGGER.error("Error persiting ontology {} at {}!", name, pathToOntology.getAbsolutePath());
      throw e;
    }
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

  private File createFileInstanceOrThrowIfExists(String name) throws OntologyNameAlreadyInUseException {
    File pathToOntology = new File(createStorePathFromName(name));
    if (pathToOntology.exists()) {
      throw new OntologyNameAlreadyInUseException(name, pathToOntology);
    }
    return pathToOntology;
  }

  private String createStorePathFromName(String name) {
    return storageDirectory + File.separator + name + ".owl.xml";
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
