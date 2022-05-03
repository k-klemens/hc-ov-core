package at.kk.msc.hcov.core.util;

import static at.kk.msc.hcov.core.util.PublishedTaskMockData.FIRST_QC_CS_ID;
import static at.kk.msc.hcov.core.util.PublishedTaskMockData.MOCKED_ID_MAPPINGS_WITHOUT_QUALITY_CONTROL;
import static at.kk.msc.hcov.core.util.PublishedTaskMockData.SECOND_QC_CS_ID;

import at.kk.msc.hcov.core.endpoint.dto.QualityControlMetaDataDto;
import at.kk.msc.hcov.core.endpoint.dto.VerificationMetaDataDto;
import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskSpecificationRequestDto;
import at.kk.msc.hcov.core.persistence.model.ConfigurationEntity;
import at.kk.msc.hcov.core.persistence.model.ProcessorPluginConfigurationEntity;
import at.kk.msc.hcov.core.persistence.model.QualityControlMetaDataEntity;
import at.kk.msc.hcov.core.persistence.model.VerificationMetaDataEntity;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerificationTaskSpecificationMockData {

  public static final String MOCKED_VERIFICATION_NAME = "MOCK-Verification";
  public static final String MOCKED_ONTOLOGY_NAME = "ONTOLOGY-TEST-NAME";

  public static final VerificationTaskSpecification MOCKED_VERIFICATION_TASK_SPECIFICATION() {
    return VerificationTaskSpecification.builder()
        .verificationName(MOCKED_VERIFICATION_NAME)
        .ontologyName(MOCKED_ONTOLOGY_NAME)
        .verificationTaskPluginId("VERIFICATION_MOCK")
        .verificationTaskPluginConfiguration(new HashMap<>())
        .contextProviderPluginId("CONTEXT_MOCK")
        .contextProviderConfiguration(new HashMap<>())
        .crowdsourcingConnectorPluginId("CROWDSOURCING_MOCK")
        .crowdsourcingConnectorPluginConfiguration(new HashMap<>())
        .processorPluginIds(List.of("FirstProcessor"))
        .processorPluginConfigurations(List.of(Map.of("P1_Config", "IS_SET")))
        .build();
  }

  public static VerificationTaskSpecificationRequestDto MOCKED_VERIFICATION_TASK_SPECIFICATION_DTO() {
    return VerificationTaskSpecificationRequestDto.builder()
        .verificationName(MOCKED_VERIFICATION_NAME)
        .ontologyName(MOCKED_ONTOLOGY_NAME)
        .verificationTaskPluginId("VERIFICATION_MOCK")
        .verificationTaskPluginConfiguration(new HashMap<>())
        .contextProviderPluginId("CONTEXT_MOCK")
        .contextProviderConfiguration(new HashMap<>())
        .crowdsourcingConnectorPluginId("CROWDSOURCING_MOCK")
        .crowdsourcingConnectorPluginConfiguration(new HashMap<>())
        .processorPluginIds(List.of("FirstProcessor"))
        .processorPluginConfigurations(List.of(Map.of("P1_Config", "IS_SET")))
        .build();
  }

  public static VerificationMetaDataEntity EXPECTED_VERIFICATION_META_DATA_WITH_QUALITY_CONTROL() {
    return VerificationMetaDataEntity.builder()
        .verificationName(MOCKED_VERIFICATION_NAME)
        .ontologyName(MOCKED_ONTOLOGY_NAME)
        .createdAt(TimeProviderMock.MOCK_TIME)
        .verificationTaskPluginId("VERIFICATION_MOCK")
        .verificationTaskPluginConfiguration(new ArrayList<>())
        .contextProviderPluginId("CONTEXT_MOCK")
        .contextProviderConfiguration(new ArrayList<>())
        .crowdsourcingConnectorPluginId("CROWDSOURCING_MOCK")
        .crowdsourcingConnectorPluginConfiguration(new ArrayList<>())
        .processorPluginIds(List.of("FirstProcessor"))
        .processorPluginConfigurationEntities(
            List.of(new ProcessorPluginConfigurationEntity(List.of(new ConfigurationEntity("P1_Config", "IS_SET"))))
        )
        .qualityControlMetaData(
            List.of(
                new QualityControlMetaDataEntity(QualityControlTaskMockData.FIRST_QC_MOCK_UUID, FIRST_QC_CS_ID,
                    "FIRST-ANSWER"),
                new QualityControlMetaDataEntity(QualityControlTaskMockData.SECOND_QC_MOCK_UUID, SECOND_QC_CS_ID,
                    "SECOND-ANSWER")
            )
        )
        .ontologyVerificationTaskIdMappings(MOCKED_ID_MAPPINGS_WITHOUT_QUALITY_CONTROL())
        .build();
  }

  public static VerificationMetaDataDto EXPECTED_VERIFICATION_META_DATA_WITH_QUALITY_CONTROL_DTO() {
    return VerificationMetaDataDto.builder()
        .verificationName(MOCKED_VERIFICATION_NAME)
        .ontologyName(MOCKED_ONTOLOGY_NAME)
        .createdAt(TimeProviderMock.MOCK_TIME)
        .verificationTaskPluginId("VERIFICATION_MOCK")
        .verificationTaskPluginConfiguration(new HashMap<>())
        .contextProviderPluginId("CONTEXT_MOCK")
        .contextProviderConfiguration(new HashMap<>())
        .crowdsourcingConnectorPluginId("CROWDSOURCING_MOCK")
        .crowdsourcingConnectorPluginConfiguration(new HashMap<>())
        .processorPluginIds(List.of("FirstProcessor"))
        .processorPluginConfigurations(
            List.of(Map.of("P1_Config", "IS_SET"))
        )
        .qualityControlMetaData(
            List.of(
                new QualityControlMetaDataDto(QualityControlTaskMockData.FIRST_QC_MOCK_UUID, FIRST_QC_CS_ID,
                    "FIRST-ANSWER"),
                new QualityControlMetaDataDto(QualityControlTaskMockData.SECOND_QC_MOCK_UUID, SECOND_QC_CS_ID,
                    "SECOND-ANSWER")
            )
        )
        .ontologyVerificationTaskIdMappings(MOCKED_ID_MAPPINGS_WITHOUT_QUALITY_CONTROL())
        .build();
  }

  public static VerificationMetaDataEntity EXPECTED_VERIFICATION_META_DATA_WITHOUT_QUALITY_CONTROL() {
    return VerificationMetaDataEntity.builder()
        .verificationName(MOCKED_VERIFICATION_NAME)
        .ontologyName(MOCKED_ONTOLOGY_NAME)
        .verificationTaskPluginId("VERIFICATION_MOCK")
        .verificationTaskPluginConfiguration(new ArrayList<>())
        .contextProviderPluginId("CONTEXT_MOCK")
        .contextProviderConfiguration(new ArrayList<>())
        .crowdsourcingConnectorPluginId("CROWDSOURCING_MOCK")
        .crowdsourcingConnectorPluginConfiguration(new ArrayList<>())
        .processorPluginIds(List.of("FirstProcessor"))
        .processorPluginConfigurationEntities(
            List.of(new ProcessorPluginConfigurationEntity(List.of(new ConfigurationEntity("P1_Config", "IS_SET"))))
        )
        .ontologyVerificationTaskIdMappings(MOCKED_ID_MAPPINGS_WITHOUT_QUALITY_CONTROL())
        .build();
  }
}
