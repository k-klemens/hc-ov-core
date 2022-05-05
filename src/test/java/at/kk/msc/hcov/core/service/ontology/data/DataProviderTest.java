package at.kk.msc.hcov.core.service.ontology.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.kk.msc.hcov.core.persistence.triples.ITripleStoreRepository;
import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNameAlreadyInUseException;
import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNotFoundException;
import at.kk.msc.hcov.core.service.ontology.data.impl.DataProvider;
import at.kk.msc.hcov.core.service.ontology.loading.IExternalOntologyLoader;
import at.kk.msc.hcov.core.util.TestUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.jena.ontology.OntModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DataProviderTest {

  IDataProvider target;

  @Mock
  IExternalOntologyLoader ontologyLoaderMock;

  @Mock
  ITripleStoreRepository tripleStoreRepositoryMock;

  @BeforeEach
  void setUp() {
    target = new DataProvider(ontologyLoaderMock, tripleStoreRepositoryMock);
  }

  @Test
  public void testUploadOntologyFromFile() throws IOException, OntologyNameAlreadyInUseException {
    // given
    String givenPath = "src/test/resources/movie.owl.xml";
    String givenOntologyName = "movie";
    OntModel mockedOntModel = TestUtils.getMovieModel();
    when(ontologyLoaderMock.loadOntology(givenPath)).thenReturn(mockedOntModel);

    // when
    target.uploadOntologyFromFile(givenPath, givenOntologyName);

    // then
    verify(ontologyLoaderMock, times(1)).loadOntology(eq(givenPath));
    verify(tripleStoreRepositoryMock, times(1)).persist(eq(mockedOntModel), eq(givenOntologyName));
  }

  @Test
  public void testUploadOntologyFromFile_givenOntologyFileNotFound() throws IOException, OntologyNameAlreadyInUseException {
    // given
    String givenPath = "src/test/resources/movie2.ttl";
    String givenOntologyName = "movie2";
    OntModel mockedOntModel = TestUtils.getMovieModel();
    when(ontologyLoaderMock.loadOntology(eq(givenPath))).thenThrow(new FileNotFoundException("Could not find file"));

    // when - then
    assertThatThrownBy(() -> target.uploadOntologyFromFile(givenPath, givenOntologyName))
        .isInstanceOf(FileNotFoundException.class);

    // then
    verify(ontologyLoaderMock, times(1)).loadOntology(eq(givenPath));
    verify(tripleStoreRepositoryMock, times(0)).persist(eq(mockedOntModel), eq(givenOntologyName));
  }

  @Test
  public void testUploadOntologyFromFile_givenOntologyNameAlreadInUse() throws IOException, OntologyNameAlreadyInUseException {
    // given
    String givenPath = "src/test/resources/movie.owl.xml";
    String givenOntologyName = "movie";
    OntModel mockedOntModel = TestUtils.getMovieModel();
    when(ontologyLoaderMock.loadOntology(givenPath)).thenReturn(mockedOntModel);
    doThrow(new OntologyNameAlreadyInUseException(givenOntologyName, new File(givenPath)))
        .when(tripleStoreRepositoryMock).persist(eq(mockedOntModel), eq(givenOntologyName));

    // when - then
    assertThatThrownBy(() -> target.uploadOntologyFromFile(givenPath, givenOntologyName))
        .isInstanceOf(OntologyNameAlreadyInUseException.class);

    // then
    verify(ontologyLoaderMock, times(1)).loadOntology(eq(givenPath));
    verify(tripleStoreRepositoryMock, times(1)).persist(eq(mockedOntModel), eq(givenOntologyName));
  }

  @Test
  public void testExtractAndSaveRequiredOntologyElements() throws OntologyNotFoundException, IOException {
    // given
    String givenOntologyName = "movie";
    String givenVerificationName = "movie verification";
    List<OntModel> givenSampleMovieModelExtractedElements = TestUtils.getSampleMovieModelExtractedElements();

    when(tripleStoreRepositoryMock.load("movie"))
        .thenReturn(TestUtils.getMovieModel());
    when(tripleStoreRepositoryMock.persistExtractedSubOntologies(eq(givenOntologyName), eq(givenVerificationName), any()))
        .thenReturn(givenSampleMovieModelExtractedElements.stream().collect(Collectors.toMap(
            t -> UUID.randomUUID(),
            t -> t
        )));

    // when
    Map<UUID, OntModel> actual = target.extractAndStoreRequiredOntologyElements(
        givenOntologyName,
        givenVerificationName,
        TestUtils.getSampleMovieModelExtrator(),
        true
    );

    // then
    assertThat(actual).hasSize(4);
    assertThat(actual.entrySet()).allMatch(entry -> entry.getValue().listClasses().toList().size() == 1);
    verify(tripleStoreRepositoryMock, times(1)).load(eq(givenOntologyName));
    verify(tripleStoreRepositoryMock, times(1)).persistExtractedSubOntologies(
        eq(givenOntologyName), eq(givenVerificationName), any()
    );
  }

  @Test
  public void testExtractAndSaveRequiredOntologyElements_givenExtractModelElementsFalse() throws OntologyNotFoundException, IOException {
    // given
    String givenOntologyName = "movie";
    String givenVerificationName = "movie verification";

    when(tripleStoreRepositoryMock.load("movie"))
        .thenReturn(TestUtils.getMovieModel());

    // when
    Map<UUID, OntModel> actual = target.extractAndStoreRequiredOntologyElements(
        givenOntologyName,
        givenVerificationName,
        TestUtils.getSampleMovieModelExtrator(),
        false
    );

    // then
    assertThat(actual).hasSize(4);
    assertThat(actual.entrySet()).allMatch(entry -> entry.getValue().listClasses().toList().size() == 1);
    verify(tripleStoreRepositoryMock, times(1)).load(eq(givenOntologyName));
    verify(tripleStoreRepositoryMock, never()).persistExtractedSubOntologies(
        any(), any(), any()
    );
  }

}
