package at.kk.msc.hcov.core.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import at.kk.msc.hcov.core.endpoint.dto.DataProcessorResultsDto;
import at.kk.msc.hcov.core.endpoint.dto.DataProcessorResultsWrapperDto;
import at.kk.msc.hcov.core.persistence.model.VerificationMetaDataEntity;
import at.kk.msc.hcov.core.persistence.repository.VerificationMetaDataRepository;
import at.kk.msc.hcov.core.util.IntegrationTestPlugins;
import at.kk.msc.hcov.core.util.mockdata.ProcessingIntegrationTestMockData;
import at.kk.msc.hcov.core.util.mockdata.VerificationTaskSpecificationMockData;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(IntegrationTestPlugins.class)
public class RestDataProcessorIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;


  @Autowired
  VerificationMetaDataRepository metaDataRepository;

  @Test
  public void testGetResults() throws URISyntaxException {
    // given
    String givenVerificationName = "testGetMetaData";
    VerificationMetaDataEntity givenEntity =
        VerificationTaskSpecificationMockData.EXPECTED_VERIFICATION_META_DATA_WITHOUT_QUALITY_CONTROL();
    givenEntity.setOntologyVerificationTaskIdMappings(
        Map.of(
            UUID.fromString("521073c6-8310-413e-a07a-0c3fc50b91bf"), "WriterExternalId",
            UUID.fromString("4e2bae67-bd63-4bb7-b53f-e8f01ff82f69"), "PersonExternalId",
            UUID.fromString("c25aec9e-2b10-4318-a21c-b0f6ac1caaf5"), "MovieDirectorExternalId",
            UUID.fromString("2ed75e19-2485-486a-8722-5386dd447cf5"), "ActorExternalId"
        )
    );
    givenEntity.setVerificationName(givenVerificationName);
    metaDataRepository.save(givenEntity);

    // when
    ResponseEntity<DataProcessorResultsWrapperDto> responseEntity = restTemplate.getForEntity(
        new URI("http://localhost:" + port + "/data-processor/" + givenVerificationName),
        DataProcessorResultsWrapperDto.class
    );


    // then
    assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    List<DataProcessorResultsDto> actualList = responseEntity.getBody().getDataProcessorResultsDtos();
    assertThat(actualList).hasSize(1);
    DataProcessorResultsDto actual = actualList.get(0);
    DataProcessorResultsDto expected = ProcessingIntegrationTestMockData.EXPECTED_DATA_PROCESSOR_RESULTS_MOVIES_DTO();
    assertThat(actual.getPluginId()).isEqualTo(expected.getPluginId());

    assertThat(actual.getProcessingResult()).containsExactlyInAnyOrderElementsOf(expected.getProcessingResult());
  }

  @Test
  public void testGetResults_expectVerificationNotFound() throws URISyntaxException {
    // given
    String givenVerificationName = "NOT_FOUND";


    // when
    ResponseEntity<DataProcessorResultsWrapperDto> responseEntity = restTemplate.getForEntity(
        new URI("http://localhost:" + port + "/data-processor/" + givenVerificationName),
        DataProcessorResultsWrapperDto.class
    );


    // then
    assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
  }


}
