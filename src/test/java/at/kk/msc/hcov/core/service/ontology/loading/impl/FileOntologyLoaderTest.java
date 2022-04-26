package at.kk.msc.hcov.core.service.ontology.loading.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import at.kk.msc.hcov.core.service.ontology.loading.IExternalOntologyLoader;
import java.io.File;
import java.io.FileNotFoundException;
import org.apache.jena.ext.com.google.common.collect.Iterators;
import org.apache.jena.ontology.OntModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileOntologyLoaderTest {

  IExternalOntologyLoader target;

  @BeforeEach
  void setUp() {
    target = new FileOntologyLoader();
  }

  @Test
  void testLoadOntology() throws FileNotFoundException {
    // given
    String givenPathToOntology = new File("src/test/resources/pizza.owl.xml").getAbsolutePath();

    // when
    OntModel actual = target.loadOntology(givenPathToOntology);

    // then
    assertThat(actual).isNotNull();
    assertThat(Iterators.size(actual.listClasses())).isPositive();
    assertThat(Iterators.size(actual.listAllOntProperties())).isPositive();
  }

  @Test
  void testLoadOntology_givenNonExistentPath() {
    // given
    String givenFaultyPath = "src/test/resources/non-existent.owl.xml";

    // when - then
    assertThatThrownBy(() -> target.loadOntology(givenFaultyPath)).isInstanceOf(FileNotFoundException.class);
  }

}
