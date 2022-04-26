package at.kk.msc.hcov.core.persistence.triples.exception;

import java.io.FileNotFoundException;

public class OntologyNotFoundException extends Exception {

  public OntologyNotFoundException(String name, FileNotFoundException fileNotFoundException) {
    super("Ontology '" + name + "' does not exists in the triple store!", fileNotFoundException);
  }

}
