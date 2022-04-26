package at.kk.msc.hcov.core.util;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

public class TestUtils {

  public static OntModel getMovieModel() {
    OntModel movieModel = ModelFactory.createOntologyModel();
    RDFDataMgr.read(movieModel, "src/test/resources/movie.ttl");
    return movieModel;
  }

}
