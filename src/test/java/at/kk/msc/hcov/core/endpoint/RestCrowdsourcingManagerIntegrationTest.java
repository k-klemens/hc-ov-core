package at.kk.msc.hcov.core.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import at.kk.msc.hcov.core.endpoint.dto.PublishedVerificationDto;
import at.kk.msc.hcov.core.endpoint.dto.QualityControlTaskCreationDto;
import at.kk.msc.hcov.core.endpoint.dto.QualityControlTasksSpecificationDto;
import at.kk.msc.hcov.core.endpoint.dto.TaskProgressDetailDto;
import at.kk.msc.hcov.core.endpoint.dto.VerificationProgressDto;
import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskSpecificationRequestDto;
import at.kk.msc.hcov.core.persistence.model.ConfigurationEntity;
import at.kk.msc.hcov.core.persistence.model.QualityControlMetaDataEntity;
import at.kk.msc.hcov.core.persistence.model.VerificationMetaDataEntity;
import at.kk.msc.hcov.core.persistence.repository.VerificationMetaDataRepository;
import at.kk.msc.hcov.core.service.crowdsourcing.model.VerificationProgress;
import at.kk.msc.hcov.core.util.IntegrationTestPlugins;
import at.kk.msc.hcov.core.util.TimeProviderMock;
import at.kk.msc.hcov.core.util.VerificationTaskSpecificationMockData;
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
public class RestCrowdsourcingManagerIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper jacksonObjectMapper;

  @Autowired
  VerificationMetaDataRepository metaDataRepository;

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
        .verificationName("movie-person-verification-1")
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
    assertThat(actual.getVerificationName()).isEqualTo("movie-person-verification-1");
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

    assertThat(metaDataRepository.existsById("movie-person-verification-1")).isTrue();
    VerificationMetaDataEntity acutalEntity = metaDataRepository.findById("movie-person-verification-1").get();
    assertThat(acutalEntity.getOntologyName()).isEqualTo("movie");
    assertThat(acutalEntity.getVerificationName()).isEqualTo("movie-person-verification-1");
    assertThat(acutalEntity.getVerificationTaskPluginId()).isEqualTo("MOVIE_VERIFICATION");
    assertThat(acutalEntity.getVerificationTaskPluginConfiguration())
        .hasSize(1)
        .contains(new ConfigurationEntity("CONTEXT_ENABLED", "true"));
    assertThat(acutalEntity.getContextProviderPluginId()).isEqualTo("MOVIE_VERIFICATION_CONTEXT");
    assertThat(acutalEntity.getContextProviderConfiguration()).isNullOrEmpty();
    assertThat(acutalEntity.getCrowdsourcingConnectorPluginId()).isEqualTo("CROWDSOURCING_MOCK");
    assertThat(acutalEntity.getCrowdsourcingConnectorPluginConfiguration())
        .hasSize(1)
        .contains(new ConfigurationEntity("REQUIRED_CONFIGURATION", "true"));
    assertThat(acutalEntity.getQualityControlMetaData())
        .hasSize(1)
        .contains(new QualityControlMetaDataEntity(
            UUID.fromString("f7e924fc-a94d-4ca1-bbb8-19b254bf83aa"),
            "QualityControlExternalId",
            "Correct Answer")
        );
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

  @Test
  public void testGetVerificationProgress() throws URISyntaxException {
    // given
    String givenVerificationName = "testGetVerificationProgress_VERIFICATION";
    VerificationMetaDataEntity givenEntity =
        VerificationTaskSpecificationMockData.EXPECTED_VERIFICATION_META_DATA_WITHOUT_QUALITY_CONTROL();
    givenEntity.setVerificationName(givenVerificationName);

    givenEntity.setOntologyVerificationTaskIdMappings(
        Map.of(
            UUID.fromString("ca38ed48-c756-420d-b4a8-170b05d064f5"), "WriterExternalId",
            UUID.fromString("417d5f0d-20d7-49e6-92f2-e4a3bb2a49b5"), "PersonExternalId",
            UUID.fromString("00725aab-7c10-4ef8-837d-70571ae67fa3"), "ActorExternalId",
            UUID.fromString("1855cf64-9033-46e5-9776-7b5c0ab713d7"), "MovieDirectorExternalId"
        )
    );
    metaDataRepository.save(givenEntity);

    // when
    ResponseEntity<VerificationProgressDto> responseEntity = restTemplate.getForEntity(
        new URI("http://localhost:" + port + "/crowdsourcing/progress/" + givenVerificationName),
        VerificationProgressDto.class
    );

    // then
    assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

    VerificationProgressDto expected = new VerificationProgressDto();
    expected.setVerificationName(givenVerificationName);
    expected.setCreatedAt(TimeProviderMock.MOCK_TIME);
    expected.setStatus(VerificationProgress.Status.ALL_TASKS_COMPLETED);
    expected.setTotalHits(4);
    expected.setCompletedHits(4);
    expected.setOpenHits(0);
    expected.setTaskProgressDetails(
        List.of(
            new TaskProgressDetailDto(UUID.fromString("ca38ed48-c756-420d-b4a8-170b05d064f5"), "WriterExternalId", 5, 5, 0),
            new TaskProgressDetailDto(UUID.fromString("417d5f0d-20d7-49e6-92f2-e4a3bb2a49b5"), "PersonExternalId", 5, 5, 0),
            new TaskProgressDetailDto(UUID.fromString("00725aab-7c10-4ef8-837d-70571ae67fa3"), "ActorExternalId", 5, 5, 0),
            new TaskProgressDetailDto(UUID.fromString("1855cf64-9033-46e5-9776-7b5c0ab713d7"), "MovieDirectorExternalId", 5, 5, 0)
        )
    );

    assertThat(responseEntity.getBody()).isNotNull();
    VerificationProgressDto actual = responseEntity.getBody();
    assertThatVerificationProgressDtoAreEqual(actual, expected);
  }

  public static void assertThatVerificationProgressDtoAreEqual(VerificationProgressDto actual, VerificationProgressDto expected) {
    assertThat(actual.getVerificationName()).isEqualTo(expected.getVerificationName());
    assertThat(actual.getCreatedAt()).isEqualTo(expected.getCreatedAt());
    assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
    assertThat(actual.getTotalHits()).isEqualTo(expected.getTotalHits());
    assertThat(actual.getCompletedHits()).isEqualTo(expected.getCompletedHits());
    assertThat(actual.getOpenHits()).isEqualTo(expected.getOpenHits());
    assertThat(actual.getTaskProgressDetails()).containsExactlyInAnyOrderElementsOf(expected.getTaskProgressDetails());
  }

}
