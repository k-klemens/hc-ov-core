package at.kk.msc.hcov.core.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import at.kk.msc.hcov.core.endpoint.dto.PublishedVerificationTaskDto;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedVerificationTask;
import at.kk.msc.hcov.core.util.mockdata.PublishedTaskMockData;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class PublishedVerificationTaskMapperTest {

  PublishedVerificationTaskMapper target = Mappers.getMapper(PublishedVerificationTaskMapper.class);

  @Test
  void testToDto() {
    // given
    PublishedVerificationTask serviceObjects = PublishedTaskMockData.EXPECTED_PUBLISHED_TASKS_WITHOUT_CONTEXT().get(0);

    // when
    PublishedVerificationTaskDto actual = target.toDto(serviceObjects);

    // then
    assertThat(actual).isEqualTo(PublishedTaskMockData.EXPECTED_PUBLISHED_TASKS_WITHOUT_CONTEXT_DTOS().get(0));
  }

}
