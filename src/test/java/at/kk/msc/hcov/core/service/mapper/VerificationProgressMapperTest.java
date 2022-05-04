package at.kk.msc.hcov.core.service.mapper;

import static at.kk.msc.hcov.core.util.mockdata.VerificationProgressMockData.EXPECTED_VERIFICATION_PROGRESS_MIXED_IN_PROGRESS;
import static at.kk.msc.hcov.core.util.mockdata.VerificationProgressMockData.EXPECTED_VERIFICATION_PROGRESS_MIXED_IN_PROGRESS_DTO;
import static org.assertj.core.api.Assertions.assertThat;

import at.kk.msc.hcov.core.endpoint.dto.VerificationProgressDto;
import at.kk.msc.hcov.core.service.crowdsourcing.model.VerificationProgress;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class VerificationProgressMapperTest {

  VerificationProgressMapper target = Mappers.getMapper(VerificationProgressMapper.class);

  @Test
  public void testToDto() {
    // given
    VerificationProgress givenServiceLayerObject = EXPECTED_VERIFICATION_PROGRESS_MIXED_IN_PROGRESS();

    // when
    VerificationProgressDto actual = target.toDto(givenServiceLayerObject);

    // then
    assertThat(actual).isEqualTo(EXPECTED_VERIFICATION_PROGRESS_MIXED_IN_PROGRESS_DTO());
  }
}
