package at.kk.msc.hcov.core.service.mapper;

import static at.kk.msc.hcov.core.util.VerificationTaskSpecificationMockData.EXPECTED_VERIFICATION_META_DATA_WITH_QUALITY_CONTROL;
import static at.kk.msc.hcov.core.util.VerificationTaskSpecificationMockData.EXPECTED_VERIFICATION_META_DATA_WITH_QUALITY_CONTROL_DTO;
import static org.assertj.core.api.Assertions.assertThat;

import at.kk.msc.hcov.core.endpoint.dto.VerificationMetaDataDto;
import at.kk.msc.hcov.core.persistence.model.VerificationMetaDataEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class VerificationMetaDataMapperTest {

  VerificationMetaDataMapper target = Mappers.getMapper(VerificationMetaDataMapper.class);

  @Test
  void testToDto() {
    // given
    VerificationMetaDataEntity givenEntity = EXPECTED_VERIFICATION_META_DATA_WITH_QUALITY_CONTROL();

    // when
    VerificationMetaDataDto actual = target.toDto(givenEntity);

    // then
    assertThat(actual).isEqualTo(EXPECTED_VERIFICATION_META_DATA_WITH_QUALITY_CONTROL_DTO());
  }
}
