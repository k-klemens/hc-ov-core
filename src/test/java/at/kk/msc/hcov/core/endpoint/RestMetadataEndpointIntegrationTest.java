package at.kk.msc.hcov.core.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import at.kk.msc.hcov.core.endpoint.dto.VerificationMetaDataDto;
import at.kk.msc.hcov.core.persistence.model.VerificationMetaDataEntity;
import at.kk.msc.hcov.core.persistence.repository.VerificationMetaDataRepository;
import at.kk.msc.hcov.core.util.VerificationTaskSpecificationMockData;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RestMetadataEndpointIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;


  @Autowired
  VerificationMetaDataRepository metaDataRepository;

  @Test
  public void testGetMetaData() throws URISyntaxException {
    // given
    String givenVerificationName = "NEW_NEW_VERIFICATION";
    VerificationMetaDataEntity givenEntity = VerificationTaskSpecificationMockData.EXPECTED_VERIFICATION_META_DATA_WITH_QUALITY_CONTROL();
    givenEntity.setVerificationName(givenVerificationName);
    metaDataRepository.save(givenEntity);

    // when
    ResponseEntity<VerificationMetaDataDto> responseEntity = restTemplate.getForEntity(
        new URI("http://localhost:" + port + "/metadata/NEW_NEW_VERIFICATION"),
        VerificationMetaDataDto.class
    );


    // then
    assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    VerificationMetaDataDto expected = VerificationTaskSpecificationMockData.EXPECTED_VERIFICATION_META_DATA_WITH_QUALITY_CONTROL_DTO();
    expected.setVerificationName("NEW_NEW_VERIFICATION");
    assertThat(responseEntity.getBody()).isEqualTo(expected);
  }

  @Test
  public void testGetMetaData_givenVerificationNotFound_expect404() throws URISyntaxException {
    // given

    // when
    ResponseEntity<VerificationMetaDataDto> responseEntity = restTemplate.getForEntity(
        new URI("http://localhost:" + port + "/metadata/NEW_NEW_VERIFICATION12"),
        VerificationMetaDataDto.class
    );


    // then
    assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
  }
}
