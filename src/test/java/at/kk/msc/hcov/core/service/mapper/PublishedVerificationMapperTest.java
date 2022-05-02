package at.kk.msc.hcov.core.service.mapper;

import static at.kk.msc.hcov.core.util.PublishedVerificationMockData.EXPECTED_PUBLISHED_VERIFICATION_DTO_WITH_QUALITY_CONTROL;
import static at.kk.msc.hcov.core.util.PublishedVerificationMockData.MOCKED_PUBLISHED_VERIFICATION_WITH_QUALITY_CONTROL;

import at.kk.msc.hcov.core.endpoint.dto.PublishedVerificationDto;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedVerification;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import at.kk.msc.hcov.core.util.VerificationTaskSpecificationMockData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class PublishedVerificationMapperTest {

  PublishedVerificationTaskMapper publishedVerificationTaskMapper = Mappers.getMapper(PublishedVerificationTaskMapper.class);

  PublishedVerificationMapper target = new PublishedVerificationMapper(publishedVerificationTaskMapper);

  @Test
  public void testToDto() {
    // given
    VerificationTaskSpecification givenSpecification = VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_TASK_SPECIFICATION();
    PublishedVerification givenServiceObject = MOCKED_PUBLISHED_VERIFICATION_WITH_QUALITY_CONTROL();

    // when
    PublishedVerificationDto actual = target.toDto(givenServiceObject, givenSpecification);

    // then
    Assertions.assertThat(actual).isEqualTo(EXPECTED_PUBLISHED_VERIFICATION_DTO_WITH_QUALITY_CONTROL());
  }
}
