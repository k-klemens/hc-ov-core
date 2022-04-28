package at.kk.msc.hcov.core.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskDto;
import at.kk.msc.hcov.core.util.VerificationTaskMockData;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class VerificationTaskMapperTest {

  VerificationTaskMapper target = Mappers.getMapper(VerificationTaskMapper.class);

  @Test
  void testToDto() {
    // given
    List<VerificationTask> serviceObjects = VerificationTaskMockData.EXPECTED_TASKS_WITHOUT_CONTEXT();

    // when
    List<VerificationTaskDto> actual = target.toDto(serviceObjects);

    // then
    assertThat(actual).containsExactlyInAnyOrderElementsOf(VerificationTaskMockData.EXPECTED_TASKS_WITHOUT_CONTEXT_DTOS());
  }

}
