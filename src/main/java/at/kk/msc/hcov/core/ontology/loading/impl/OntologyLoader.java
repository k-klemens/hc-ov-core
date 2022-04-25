package at.kk.msc.hcov.core.ontology.loading.impl;

import at.kk.msc.hcov.core.ontology.loading.IOntologyLoader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.stereotype.Component;

@Component
public class OntologyLoader implements IOntologyLoader {

  @Override
  public OntModel loadOntology(String path) throws FileNotFoundException {
    OntModel ontModel = ModelFactory.createOntologyModel();

    InputStream inputStream = new FileInputStream(path);
    ontModel.read(inputStream, null);

    return ontModel;
  }

}
