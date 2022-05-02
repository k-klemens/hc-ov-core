package at.kk.msc.hcov.core.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import at.kk.msc.hcov.core.endpoint.dto.PublishedVerificationDto;
import at.kk.msc.hcov.core.endpoint.dto.QualityControlTaskCreationDto;
import at.kk.msc.hcov.core.endpoint.dto.QualityControlTasksSpecificationDto;
import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskSpecificationRequestDto;
import at.kk.msc.hcov.core.util.IntegrationTestPlugins;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Condition;
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
public class RestCrowdsourcingManagerTest {

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
  void testCreateVerificationTasks_givenContextProviderEnabled() throws IOException, URISyntaxException {
    // given
    File movieOntologyFile = new File("src/test/resources/movie.owl.xml");
    // provide an ontology in the repo directory
    FileUtils.copyFile(movieOntologyFile, new File("src/test/resources/integration-filestore/.hcov-triplestore/movie/movie.owl.xml"));

    HttpHeaders jsonHeader = new HttpHeaders();
    jsonHeader.setContentType(MediaType.APPLICATION_JSON);

    QualityControlTasksSpecificationDto givenQualityControlTasksSpecificationDto = QualityControlTasksSpecificationDto.builder()
        .qualityControlTasks(
            List.of(
                new QualityControlTaskCreationDto(
                    UUID.fromString("f7e924fc-a94d-4ca1-bbb8-19b254bf83aa"),
                    Map.of("subclass", "QualityControl", "context", "QualityControl-Context"),
                    "Correct Answer"
                )
            )
        ).build();

    VerificationTaskSpecificationRequestDto givenRequest = VerificationTaskSpecificationRequestDto.builder()
        .ontologyName("movie")
        .verificationName("movie-person-verification")
        .verificationTaskPluginId("MOVIE_VERIFICATION")
        .verificationTaskPluginConfiguration(Map.of("CONTEXT_ENABLED", true))
        .contextProviderPluginId("MOVIE_VERIFICATION_CONTEXT")
        .crowdsourcingConnectorPluginId("CROWDSOURCING_MOCK")
        .crowdsourcingConnectorPluginConfiguration(Map.of("REQUIRED_CONFIGURATION", true))
        .qualityControlTasksSpecification(givenQualityControlTasksSpecificationDto)
        .build();
    String givenRequestJson = jacksonObjectMapper.writeValueAsString(givenRequest);

    // when
    ResponseEntity<PublishedVerificationDto> responseEntity = restTemplate.exchange(
        new URI("http://localhost:" + port + "/crowdsourcing/publish"),
        HttpMethod.POST,
        new HttpEntity<>(givenRequestJson, jsonHeader),
        PublishedVerificationDto.class
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

    PublishedVerificationDto actual = responseEntity.getBody();
    assertThat(actual).isNotNull();
    assertThat(actual.getVerificationName()).isEqualTo("movie-person-verification");
    assertThat(actual.getOntologyName()).isEqualTo("movie");
    assertThat(actual.getSamplePublishedVerificationTask()).isNotNull();

    assertThat(actual.getVerificationTaskIdMappings())
        .hasSize(4)
        .hasValueSatisfying(new Condition<>(s -> s.equals("WriterExternalId"), ""))
        .hasValueSatisfying(new Condition<>(s -> s.equals("PersonExternalId"), ""))
        .hasValueSatisfying(new Condition<>(s -> s.equals("MovieDirectorExternalId"), ""))
        .hasValueSatisfying(new Condition<>(s -> s.equals("ActorExternalId"), ""));

    assertThat(actual.getQualitiyControlTaskIdMappings())
        .hasSize(1)
        .containsKey(UUID.fromString("f7e924fc-a94d-4ca1-bbb8-19b254bf83aa"));
  }

  @Test
  void testCreateVerificationTasks_givenWrongPlugin_expect500() throws IOException, URISyntaxException {
    // given
    File movieOntologyFile = new File("src/test/resources/movie.owl.xml");
    // provide an ontology in the repo directory
    FileUtils.copyFile(movieOntologyFile, new File("src/test/resources/integration-filestore/.hcov-triplestore/movie/movie.owl.xml"));

    HttpHeaders jsonHeader = new HttpHeaders();
    jsonHeader.setContentType(MediaType.APPLICATION_JSON);

    QualityControlTasksSpecificationDto givenQualityControlTasksSpecificationDto = QualityControlTasksSpecificationDto.builder()
        .qualityControlTasks(
            List.of(
                new QualityControlTaskCreationDto(
                    UUID.fromString("f7e924fc-a94d-4ca1-bbb8-19b254bf83aa"),
                    Map.of("subclass", "QualityControl", "context", "QualityControl-Context"),
                    "Correct Answer"
                )
            )
        ).build();

    VerificationTaskSpecificationRequestDto givenRequest = VerificationTaskSpecificationRequestDto.builder()
        .ontologyName("movie")
        .verificationName("movie-person-verification")
        .verificationTaskPluginId("MOVIE_VERIFICATION_NEW")
        .verificationTaskPluginConfiguration(Map.of("CONTEXT_ENABLED", true))
        .contextProviderPluginId("MOVIE_VERIFICATION_CONTEXT")
        .crowdsourcingConnectorPluginId("CROWDSOURCING_MOCK")
        .crowdsourcingConnectorPluginConfiguration(Map.of("REQUIRED_CONFIGURATION", true))
        .qualityControlTasksSpecification(givenQualityControlTasksSpecificationDto)
        .build();
    String givenRequestJson = jacksonObjectMapper.writeValueAsString(givenRequest);

    // when
    ResponseEntity<PublishedVerificationDto> responseEntity = restTemplate.exchange(
        new URI("http://localhost:" + port + "/crowdsourcing/publish"),
        HttpMethod.POST,
        new HttpEntity<>(givenRequestJson, jsonHeader),
        PublishedVerificationDto.class
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
