package at.kk.msc.hcov.core.util;

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
                new QualityControlMetaDataEntity(QualityControlTaskMockData.FIRST_QC_MOCK_UUID, "FIRST-QC-CS-ID", "FIRST-ANSWER"),
                new QualityControlMetaDataEntity(QualityControlTaskMockData.SECOND_QC_MOCK_UUID, "SECOND-QC-CS-ID", "SECOND-ANSWER")
            )
        )
        .ontologyVerificationTaskIdMappings(PublishedTaskMockData.MOCKED_ID_MAPPINGS_WITHOUT_QUALITY_CONTROL())
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
        .ontologyVerificationTaskIdMappings(PublishedTaskMockData.MOCKED_ID_MAPPINGS_WITHOUT_QUALITY_CONTROL())
        .build();
  }
}
