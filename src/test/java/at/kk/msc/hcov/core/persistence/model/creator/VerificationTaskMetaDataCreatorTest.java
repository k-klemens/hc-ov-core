package at.kk.msc.hcov.core.persistence.model.creator;

import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskSpecificationMockData.EXPECTED_VERIFICATION_META_DATA_WITHOUT_QUALITY_CONTROL;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskSpecificationMockData.EXPECTED_VERIFICATION_META_DATA_WITH_QUALITY_CONTROL;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_TASK_SPECIFICATION;
import static org.assertj.core.api.Assertions.assertThat;

import at.kk.msc.hcov.core.persistence.model.VerificationMetaDataEntity;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedTaskIdMappings;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import at.kk.msc.hcov.core.util.TimeProviderMock;
import at.kk.msc.hcov.core.util.mockdata.PublishedTaskMockData;
import at.kk.msc.hcov.core.util.mockdata.QualityControlTasksSpecificationMockData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class VerificationTaskMetaDataCreatorTest {

  VerificationMetaDataCreator target;

  @BeforeEach
  void setUp() {
    target = new VerificationMetaDataCreator(new TimeProviderMock());
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
    VerificationMetaDataEntity actual = target.toEntity(
        givenServiceObject, givenPublishedTaskIdMappings
    );

    // then
    assertThat(actual).isEqualTo(EXPECTED_VERIFICATION_META_DATA_WITH_QUALITY_CONTROL());
  }

  @Test
  void testToEntity_givenNoQualityControl() {
    // given
    VerificationTaskSpecification givenServiceObject = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    PublishedTaskIdMappings givenPublishedTaskIdMappings = PublishedTaskMockData.MOCKED_PUBLISHED_TASK_ID_MAPPINGS_WITH_QUALITY_CONTROL();

    // when
    VerificationMetaDataEntity actual = target.toEntity(
        givenServiceObject, givenPublishedTaskIdMappings
    );

    // then
    assertThat(actual).isEqualTo(EXPECTED_VERIFICATION_META_DATA_WITHOUT_QUALITY_CONTROL());
  }
}
