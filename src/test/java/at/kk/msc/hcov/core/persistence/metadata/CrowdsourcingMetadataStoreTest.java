package at.kk.msc.hcov.core.persistence.metadata;

import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskSpecificationMockData.EXPECTED_VERIFICATION_META_DATA_WITHOUT_QUALITY_CONTROL;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_TASK_SPECIFICATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import at.kk.msc.hcov.core.persistence.metadata.impl.CrowdsourcingMetadataStore;
import at.kk.msc.hcov.core.persistence.model.VerificationMetaDataEntity;
import at.kk.msc.hcov.core.persistence.model.creator.VerificationMetaDataCreator;
import at.kk.msc.hcov.core.persistence.repository.VerificationMetaDataRepository;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedTaskIdMappings;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import at.kk.msc.hcov.core.util.mockdata.PublishedTaskMockData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CrowdsourcingMetadataStoreTest {

  @Mock
  VerificationMetaDataCreator verificationMetaDataCreatorMock;

  @Mock
  VerificationMetaDataRepository verificationMetaDataRepositoryMock;

  ICrowdsourcingMetadataStore target;

  @BeforeEach
  void setUp() {
    target = new CrowdsourcingMetadataStore(verificationMetaDataRepositoryMock, verificationMetaDataCreatorMock);
  }

  @Test
  void testSaveMetaData() {
    // given
    VerificationTaskSpecification givenSpecification = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    PublishedTaskIdMappings givenPublishedTaskIdMappings =
        PublishedTaskMockData.MOCKED_PUBLISHED_TASK_ID_MAPPINGS_WITHOUT_QUALITY_CONTROL();

    when(verificationMetaDataCreatorMock.toEntity(eq(givenSpecification), eq(givenPublishedTaskIdMappings)))
        .thenReturn(EXPECTED_VERIFICATION_META_DATA_WITHOUT_QUALITY_CONTROL());
    when(verificationMetaDataRepositoryMock.save(eq(EXPECTED_VERIFICATION_META_DATA_WITHOUT_QUALITY_CONTROL())))
        .thenReturn(EXPECTED_VERIFICATION_META_DATA_WITHOUT_QUALITY_CONTROL());

    // when
    VerificationMetaDataEntity actual = target.saveMetaData(givenSpecification, givenPublishedTaskIdMappings);

    // then
    assertThat(actual).isEqualTo(EXPECTED_VERIFICATION_META_DATA_WITHOUT_QUALITY_CONTROL());
  }


}
