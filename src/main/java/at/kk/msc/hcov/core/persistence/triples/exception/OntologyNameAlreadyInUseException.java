package at.kk.msc.hcov.core.persistence.triples.exception;

import java.io.File;

public class OntologyNameAlreadyInUseException extends Exception {

  public OntologyNameAlreadyInUseException(String name, File file) {
    super("Ontology name " + name + " already in use at " + file.getAbsolutePath() + "!");
  }
}
