package at.kk.msc.hcov.core.service.ontology.loading.impl;

import at.kk.msc.hcov.core.service.ontology.loading.IOntologyLoader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OntologyLoader implements IOntologyLoader {

  private static final Logger LOGGER = LoggerFactory.getLogger(OntologyLoader.class);

  @Override
  public OntModel loadOntology(String path) throws FileNotFoundException {
    LOGGER.info("Started loading ontology from: {}", path);
    OntModel ontModel = ModelFactory.createOntologyModel();

    InputStream inputStream = new FileInputStream(path);
    ontModel.read(inputStream, null);
    LOGGER.info("Loaded ontology from: {}", path);
    
    return ontModel;
  }

}
