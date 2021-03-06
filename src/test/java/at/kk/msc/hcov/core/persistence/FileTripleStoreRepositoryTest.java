package at.kk.msc.hcov.core.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNameAlreadyInUseException;
import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNotFoundException;
import at.kk.msc.hcov.core.persistence.triples.impl.FileTripleStoreRepository;
import at.kk.msc.hcov.core.service.ontology.loading.impl.FileOntologyLoader;
import at.kk.msc.hcov.core.util.TestUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.jena.ext.com.google.common.io.Files;
import org.apache.jena.ontology.OntModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileTripleStoreRepositoryTest {

  FileTripleStoreRepository target;

  String mockedBasePath = "src/test/resources/filestorage";
  String storagePath = "src/test/resources/filestorage/.hcov-triplestore";

  @BeforeEach
  void setUp() throws IOException {
    target = new FileTripleStoreRepository(mockedBasePath, new FileOntologyLoader());
  }

  @AfterEach
  void tearDown() throws IOException {
    FileUtils.deleteDirectory(new File(mockedBasePath));
  }

  @Test
  void testBasePathIsSetCorrectly_givenBlank() throws IOException {
    // given
    String basePath = "   ";

    // when
    target = new FileTripleStoreRepository(basePath, null);

    // then
    assertThat(target.getStorageBasePath()).isEqualTo(System.getProperty("user.home"));
  }

  @Test
  void testBasePathIsSetCorrectly_givenPath() throws IOException {
    // given
    String givenBasePath = "src/test/resources/kk";

    // when
    target = new FileTripleStoreRepository(givenBasePath, null);

    // then
    assertThat(target.getStorageBasePath()).isEqualTo("src/test/resources/kk");
    FileUtils.deleteDirectory(new File(givenBasePath));
  }

  @Test
  void testStorageDirectoryIsCreated() {
    // given
    // see mockedBasePath

    // when
    // see setUp()

    // then
    assertThat(new File(mockedBasePath + File.separator + ".hcov-triplestore"))
        .exists()
        .isDirectory();
  }

  @Test
  void testPersist_givenAlreadyUsedName_expectException() throws IOException {
    // given
    String givenAlreadyInUseOntologyName = "already_existing";
    File file = createOntologyFile(givenAlreadyInUseOntologyName);
    Files.createParentDirs(file);
    file.createNewFile();

    // when - then
    assertThatThrownBy(() -> target.persist(null, givenAlreadyInUseOntologyName))
        .isInstanceOf(OntologyNameAlreadyInUseException.class);
  }

  @Test
  void testPersist() throws IOException, OntologyNameAlreadyInUseException {
    // given
    String givenName = "movie-test";
    OntModel givenModel = TestUtils.getMovieModel();

    // when
    target.persist(givenModel, givenName);

    // then
    assertThat(createOntologyFile(givenName))
        .exists()
        .isNotEmpty();
  }

  @Test
  void testLoad() throws IOException, OntologyNotFoundException {
    // given
    File pizzaOntoloyFile = new File("src/test/resources/pizza.owl.xml");
    // provide an ontology in the repo directory
    FileUtils.copyFile(pizzaOntoloyFile, new File(storagePath + File.separator + "pizza", "pizza.owl.xml"));

    // when
    OntModel actual = target.load("pizza");

    // then
    assertThat(actual.size())
        .isPositive();
    assertThat(actual.listClasses().toList().size()).isEqualTo(331);
    assertThat(actual.listAllOntProperties().toList().size()).isEqualTo(30);
  }

  @Test
  void testLoad_givenNameNotLoadedYet() {
    //given
    String nonExistingOntologyName = "new_one";

    // when - then
    assertThatThrownBy(() -> target.load(nonExistingOntologyName))
        .isInstanceOf(OntologyNotFoundException.class);
  }

  @Test
  void testPersistExtractedSubOntologies() throws IOException, OntologyNotFoundException {
    // given
    String givenOntologyName = "pizza";
    String givenVerificationName = "Topping-Verification";
    File pizzaOntoloyFile = new File("src/test/resources/pizza.owl.xml");
    // provide an ontology in the repo directory
    FileUtils.copyFile(pizzaOntoloyFile, new File(storagePath + File.separator + givenOntologyName, "pizza.owl.xml"));
    List<OntModel> givenSubModels = TestUtils.getPizzaSubModels();

    // when
    Map<UUID, OntModel> actual = target.persistExtractedSubOntologies(givenOntologyName, givenVerificationName, givenSubModels);

    // then
    File expectedLocation = new File(storagePath + File.separator + givenOntologyName + File.separator + givenVerificationName);
    assertThat(expectedLocation).exists();

    assertThat(actual).hasSize(23);
    List<File> expectedCreatedFiles =
        actual.entrySet().stream().map(entry -> new File(expectedLocation, entry.getKey() + ".owl.xml")).toList();
    assertThat(expectedCreatedFiles).allMatch(File::exists);
  }

  @Test
  void testPersistExtractedSubOntologies_givenOntologyDoesNotExist() {
    // given
    String givenOntologyName = "not_existent";
    String givenVerificationName = "verification1";

    // when - then
    assertThatThrownBy(() -> target.persistExtractedSubOntologies(givenOntologyName, givenVerificationName, List.of()))
        .isInstanceOf(OntologyNotFoundException.class);
  }

  private File createOntologyFile(String name) {
    return new File(storagePath + File.separator + name, name + ".owl.xml");
  }
}
