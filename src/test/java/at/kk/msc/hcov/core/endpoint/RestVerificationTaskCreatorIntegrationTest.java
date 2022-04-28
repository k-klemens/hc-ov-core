package at.kk.msc.hcov.core.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskResponseDto;
import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskSpecificationRequestDto;
import at.kk.msc.hcov.core.util.IntegrationTestPlugins;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(IntegrationTestPlugins.class)
public class RestVerificationTaskCreatorIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper jacksonObjectMapper;

  @AfterEach
  void tearDown() throws IOException {
    FileUtils.deleteDirectory(new File("src/test/resources/integration-filestore/"));
  }

  @Test
  void testCreateVerificationTasks() throws IOException, URISyntaxException {
    // given
    File movieOntologyFile = new File("src/test/resources/movie.owl.xml");
    // provide an ontology in the repo directory
    FileUtils.copyFile(movieOntologyFile, new File("src/test/resources/integration-filestore/.hcov-triplestore/movie/movie.owl.xml"));

    HttpHeaders jsonHeader = new HttpHeaders();
    jsonHeader.setContentType(MediaType.APPLICATION_JSON);

    VerificationTaskSpecificationRequestDto givenRequest = VerificationTaskSpecificationRequestDto.builder()
        .ontologyName("movie")
        .verificationName("movie-person-verification")
        .verificationTaskPluginId("MOVIE_VERIFICATION")
        .build();
    String givenRequestJson = jacksonObjectMapper.writeValueAsString(givenRequest);

    // when
    ResponseEntity<VerificationTaskResponseDto> responseEntity = restTemplate.exchange(
        new URI("http://localhost:" + port + "/verification-task/create"),
        HttpMethod.POST,
        new HttpEntity<>(givenRequestJson, jsonHeader),
        VerificationTaskResponseDto.class
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    // TODO check if the correct object is returned

  }


}