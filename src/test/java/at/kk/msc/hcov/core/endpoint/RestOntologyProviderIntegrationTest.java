package at.kk.msc.hcov.core.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RestOntologyProviderIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @AfterEach
  void tearDown() throws IOException {
    FileUtils.deleteDirectory(new File("src/test/resources/integration-filestore/"));
  }

  @Test
  public void testPostUploadOntology() throws JSONException, URISyntaxException {
    // given
    HttpHeaders jsonHeader = new HttpHeaders();
    jsonHeader.setContentType(MediaType.APPLICATION_JSON);

    JSONObject givenRequest = new JSONObject();
    givenRequest.put("ontologyName", "movie");
    givenRequest.put("filePathToOntology", "src/test/resources/movie.owl.xml");

    // when
    ResponseEntity<Void> responseEntity = restTemplate.exchange(
        new URI("http://localhost:" + port + "/provider/upload"),
        HttpMethod.POST,
        new HttpEntity<>(givenRequest.toString(), jsonHeader),
        Void.class
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(new File("src/test/resources/integration-filestore/.hcov-triplestore/movie/movie.owl.xml")).exists();
  }

  @Test
  public void testPostUploadOntology_givenOntologyAlreadyExists_expect409Conflict() throws JSONException, URISyntaxException, IOException {
    // given
    File movieOntologyFile = new File("src/test/resources/movie.owl.xml");
    // provide an ontology in the repo directory
    FileUtils.copyFile(movieOntologyFile, new File("src/test/resources/integration-filestore/.hcov-triplestore/movie/movie.owl.xml"));

    HttpHeaders jsonHeader = new HttpHeaders();
    jsonHeader.setContentType(MediaType.APPLICATION_JSON);

    JSONObject givenRequest = new JSONObject();
    givenRequest.put("ontologyName", "movie");
    givenRequest.put("filePathToOntology", "src/test/resources/movie.owl.xml");

    // when
    ResponseEntity<Void> responseEntity = restTemplate.exchange(
        new URI("http://localhost:" + port + "/provider/upload"),
        HttpMethod.POST,
        new HttpEntity<>(givenRequest.toString(), jsonHeader),
        Void.class
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
  }

  @ParameterizedTest
  @MethodSource("provideTestPostUploadOntologyFaultyData")
  public void testPostUploadOntology_givenFaultyData_expect400(String givenFaultyJson) throws URISyntaxException {
    // given
    HttpHeaders jsonHeader = new HttpHeaders();
    jsonHeader.setContentType(MediaType.APPLICATION_JSON);

    // when
    ResponseEntity<Void> responseEntity = restTemplate.exchange(
        new URI("http://localhost:" + port + "/provider/upload"),
        HttpMethod.POST,
        new HttpEntity<>(givenFaultyJson, jsonHeader),
        Void.class
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  private static Stream<Arguments> provideTestPostUploadOntologyFaultyData() {
    return Stream.of(
        Arguments.of("{\"ontologyName\":\"\",\"filePathToOntology\":\"src\\/test\\/resources\\/movie.ttl\"}"),
        Arguments.of("{\"ontologyName\":\"movie\",\"filePathToOntology\":\"\"}")
    );
  }

}
