package at.kk.msc.hcov.core.service.mapper;

import static at.kk.msc.hcov.core.util.VerificationTaskSpecificationMockData.EXPECTED_VERIFICATION_TASK_SPECIFICATION_WITH_QUALITY_CONTROL;
import static at.kk.msc.hcov.core.util.VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_TASK_SPECIFICATION;
import static at.kk.msc.hcov.core.util.VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_TASK_SPECIFICATION_DTO;
import static org.assertj.core.api.Assertions.assertThat;

import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskSpecificationRequestDto;
import at.kk.msc.hcov.core.persistence.model.VerificationTaskSpecificationEntity;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedTaskIdMappings;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import at.kk.msc.hcov.core.util.PublishedTaskMockData;
import at.kk.msc.hcov.core.util.QualityControlTasksSpecificationMockData;
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

  @Test
  void testToServiceObject_withQualityControl() {
    // given
    VerificationTaskSpecificationRequestDto requestDto = MOCKED_VERIFICATION_TASK_SPECIFICATION_DTO();
    requestDto.setQualityControlTasksSpecification(
        QualityControlTasksSpecificationMockData.MOCKED_QUALITY_CONTROL_TASK_SPECIFICATION_DTO()
    );

    // when
    VerificationTaskSpecification actual = target.toServiceObject(requestDto);

    // then
    VerificationTaskSpecification expected = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    expected.setQualityControlTasksSpecification(
        QualityControlTasksSpecificationMockData.MOCKED_QUALITY_CONTROL_TASK_SPECIFICATION()
    );
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testToEntity() {
    // given
    VerificationTaskSpecification givenServiceObject = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    givenServiceObject.setQualityControlTasksSpecification(
        QualityControlTasksSpecificationMockData.MOCKED_QUALITY_CONTROL_TASK_SPECIFICATION()
    );
    PublishedTaskIdMappings givenPublishedTaskIdMappings = PublishedTaskMockData.MOCKED_PUBLISHED_TASK_ID_MAPPINGS_WITH_QUALITY_CONTROL();

    // when
    VerificationTaskSpecificationEntity actual = target.toEntity(
        givenServiceObject, givenPublishedTaskIdMappings
    );

    // then
    assertThat(actual).isEqualTo(EXPECTED_VERIFICATION_TASK_SPECIFICATION_WITH_QUALITY_CONTROL());
  }
}
