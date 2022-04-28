package at.kk.msc.hcov.core.service.mapper;

import static at.kk.msc.hcov.core.util.VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_TASK_SPECIFICATION;
import static at.kk.msc.hcov.core.util.VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_TASK_SPECIFICATION_DTO;
import static org.assertj.core.api.Assertions.assertThat;

import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskSpecificationRequestDto;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTaskSpecification;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class VerificationTaskSpecificationMapperTest {

  VerificationTaskSpecificationMapper target = Mappers.getMapper(VerificationTaskSpecificationMapper.class);

  @Test
  void testToServiceObject() {
    // given
    VerificationTaskSpecificationRequestDto requestDto = MOCKED_VERIFICATION_TASK_SPECIFICATION_DTO();

    // when
    VerificationTaskSpecification actual = target.toServiceObject(requestDto);

    // then
    assertThat(actual).isEqualTo(MOCKED_VERIFICATION_TASK_SPECIFICATION());
  }
}
