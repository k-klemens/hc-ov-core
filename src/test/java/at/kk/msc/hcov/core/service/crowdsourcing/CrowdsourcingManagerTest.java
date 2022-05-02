package at.kk.msc.hcov.core.service.crowdsourcing;


import static at.kk.msc.hcov.core.util.PublishedTaskMockData.MOCKED_PUBLISHED_TASK_ID_MAPPINGS_WITHOUT_QUALITY_CONTROL;
import static at.kk.msc.hcov.core.util.PublishedTaskMockData.MOCKED_PUBLISHED_TASK_ID_MAPPINGS_WITH_QUALITY_CONTROL;
import static at.kk.msc.hcov.core.util.QualityControlTasksSpecificationMockData.MOCKED_QUALITY_CONTROL_TASK_SPECIFICATION;
import static at.kk.msc.hcov.core.util.VerificationTaskMockData.EXPECTED_TASKS_WITH_CONTEXT;
import static at.kk.msc.hcov.core.util.VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_TASK_SPECIFICATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.kk.msc.hcov.core.persistence.metadata.ICrowdsourcingMetadataStore;
import at.kk.msc.hcov.core.service.crowdsourcing.exception.CrowdsourcingManagerException;
import at.kk.msc.hcov.core.service.crowdsourcing.impl.CrowdsourcingManager;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedVerification;
import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.plugin.IPluginLoader;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.IQualityControlProvider;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.exception.QualityControlTaskCreationFailedException;
import at.kk.msc.hcov.core.service.verificationtask.task.IVerificationTaskCreator;
import at.kk.msc.hcov.core.service.verificationtask.task.exception.VerificationTaskCreationFailedException;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import at.kk.msc.hcov.core.util.CrowdsourcingConnectorPluginMock;
import at.kk.msc.hcov.core.util.PublishedVerificationMockData;
import at.kk.msc.hcov.core.util.QualityControlTaskMockData;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.ICrowdsourcingConnectorPlugin;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.plugin.core.Plugin;

@ExtendWith(MockitoExtension.class)
public class CrowdsourcingManagerTest {

  @Mock
  IPluginLoader<Plugin<String>> pluginLoaderMock;

  @Mock
  IQualityControlProvider qualityControlProviderMock;

  @Mock
  ICrowdsourcingMetadataStore metadataStoreMock;

  @Mock
  IVerificationTaskCreator verificationTaskCreatorMock;

  ICrowdsourcingConnectorPlugin crowdsourcingConnectorPluginMock;

  ICrowdsourcingManager target;

  @BeforeEach
  void setUp() {
    crowdsourcingConnectorPluginMock = new CrowdsourcingConnectorPluginMock();
    target = new CrowdsourcingManager(qualityControlProviderMock, metadataStoreMock, verificationTaskCreatorMock, pluginLoaderMock);
  }

  @Test
  void testCreateAndPublishVerification_withQualityControl()
      throws PluginLoadingError, QualityControlTaskCreationFailedException, CrowdsourcingManagerException,
      VerificationTaskCreationFailedException {
    // given
    List<VerificationTask> givenVerificationTasks = EXPECTED_TASKS_WITH_CONTEXT();
    VerificationTaskSpecification givenSpecification = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    givenSpecification.setQualityControlTasksSpecification(MOCKED_QUALITY_CONTROL_TASK_SPECIFICATION());
    givenSpecification.setCrowdsourcingConnectorPluginConfiguration(Map.of("REQUIRED_CONFIGURATION", "is_set"));
    when(pluginLoaderMock.loadPluginOrThrow(eq(IPluginLoader.PluginType.CROWDSOURCING_CONNECTOR), eq("CROWDSOURCING_MOCK")))
        .thenReturn(crowdsourcingConnectorPluginMock);
    when(verificationTaskCreatorMock.createTasks(eq(givenSpecification))).thenReturn(givenVerificationTasks);
    when(qualityControlProviderMock.createQualityControlTasks(eq(givenSpecification)))
        .thenReturn(QualityControlTaskMockData.EXPECTED_QC_TASKS_WITH_CONTEXT());

    // when
    PublishedVerification actual = target.createAndPublishVerification(givenSpecification);

    // then
    verify(pluginLoaderMock, times(1))
        .loadPluginOrThrow(eq(IPluginLoader.PluginType.CROWDSOURCING_CONNECTOR), eq("CROWDSOURCING_MOCK"));
    verify(verificationTaskCreatorMock, times(1))
        .createTasks(eq(givenSpecification));
    verify(qualityControlProviderMock, times(1))
        .createQualityControlTasks(eq(givenSpecification));

    CrowdsourcingConnectorPluginMock connectorPluginMock = (CrowdsourcingConnectorPluginMock) crowdsourcingConnectorPluginMock;
    assertThat(connectorPluginMock.isCalledPublishTasks()).isTrue();
    assertThat(connectorPluginMock.getMostRecentVerificationTaskListProvided())
        .containsExactlyInAnyOrderElementsOf(
            Stream.concat(givenVerificationTasks.stream(), QualityControlTaskMockData.EXPECTED_QC_TASKS_WITH_CONTEXT().stream()).toList()
        );

    verify(metadataStoreMock, times(1))
        .saveMetaData(eq(givenSpecification), eq(MOCKED_PUBLISHED_TASK_ID_MAPPINGS_WITH_QUALITY_CONTROL()));

    assertThat(actual).isEqualTo(PublishedVerificationMockData.MOCKED_PUBLISHED_VERIFICATION_WITH_QUALITY_CONTROL());
  }


  @Test
  void testCreateAndPublishVerification_withoutQualityControl()
      throws PluginLoadingError, QualityControlTaskCreationFailedException, CrowdsourcingManagerException,
      VerificationTaskCreationFailedException {
    // given
    List<VerificationTask> givenVerificationTasks = EXPECTED_TASKS_WITH_CONTEXT();
    VerificationTaskSpecification givenSpecification = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    givenSpecification.setCrowdsourcingConnectorPluginConfiguration(Map.of("REQUIRED_CONFIGURATION", "is_set"));
    when(verificationTaskCreatorMock.createTasks(eq(givenSpecification))).thenReturn(givenVerificationTasks);
    when(pluginLoaderMock.loadPluginOrThrow(eq(IPluginLoader.PluginType.CROWDSOURCING_CONNECTOR), eq("CROWDSOURCING_MOCK")))
        .thenReturn(crowdsourcingConnectorPluginMock);

    // when
    PublishedVerification actual = target.createAndPublishVerification(givenSpecification);

    // then
    verify(pluginLoaderMock, times(1))
        .loadPluginOrThrow(eq(IPluginLoader.PluginType.CROWDSOURCING_CONNECTOR), eq("CROWDSOURCING_MOCK"));
    verify(verificationTaskCreatorMock, times(1))
        .createTasks(eq(givenSpecification));
    verify(qualityControlProviderMock, never()).createQualityControlTasks(any());

    CrowdsourcingConnectorPluginMock connectorPluginMock = (CrowdsourcingConnectorPluginMock) crowdsourcingConnectorPluginMock;
    assertThat(connectorPluginMock.isCalledPublishTasks()).isTrue();
    assertThat(connectorPluginMock.getMostRecentVerificationTaskListProvided()).containsExactlyInAnyOrderElementsOf(givenVerificationTasks);

    verify(metadataStoreMock, times(1))
        .saveMetaData(eq(givenSpecification), eq(MOCKED_PUBLISHED_TASK_ID_MAPPINGS_WITHOUT_QUALITY_CONTROL()));
    assertThat(actual).isEqualTo(PublishedVerificationMockData.MOCKED_PUBLISHED_VERIFICATION_WITHOUT_QUALITY_CONTROL());
  }

  @Test
  void testCreateAndPublishVerification_givenRequiredConfigurationNotSet_expectException()
      throws PluginLoadingError, QualityControlTaskCreationFailedException, CrowdsourcingManagerException,
      VerificationTaskCreationFailedException {
    // given
    List<VerificationTask> givenVerificationTasks = EXPECTED_TASKS_WITH_CONTEXT();
    VerificationTaskSpecification givenSpecification = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    givenSpecification.setCrowdsourcingConnectorPluginConfiguration(Map.of("NOT_REQUIRED_CONFIGURATION", "is_set"));
    when(verificationTaskCreatorMock.createTasks(eq(givenSpecification))).thenReturn(givenVerificationTasks);
    when(pluginLoaderMock.loadPluginOrThrow(eq(IPluginLoader.PluginType.CROWDSOURCING_CONNECTOR), eq("CROWDSOURCING_MOCK")))
        .thenReturn(crowdsourcingConnectorPluginMock);

    // when
    assertThatThrownBy(() -> target.createAndPublishVerification(givenSpecification))
        .isInstanceOf(CrowdsourcingManagerException.class);

    // then
    verify(pluginLoaderMock, times(1))
        .loadPluginOrThrow(eq(IPluginLoader.PluginType.CROWDSOURCING_CONNECTOR), eq("CROWDSOURCING_MOCK"));
    verify(verificationTaskCreatorMock, times(1))
        .createTasks(eq(givenSpecification));
    verify(qualityControlProviderMock, never()).createQualityControlTasks(any());

    CrowdsourcingConnectorPluginMock connectorPluginMock = (CrowdsourcingConnectorPluginMock) crowdsourcingConnectorPluginMock;
    assertThat(connectorPluginMock.isCalledPublishTasks()).isTrue();
    assertThat(connectorPluginMock.getMostRecentVerificationTaskListProvided()).containsExactlyInAnyOrderElementsOf(givenVerificationTasks);

    verify(metadataStoreMock, never()).saveMetaData(any(), any());
  }


  @Test
  void testCreateAndPublishVerification_givenQualityControlThrows_expectException()
      throws PluginLoadingError, QualityControlTaskCreationFailedException,
      VerificationTaskCreationFailedException {
    // given
    List<VerificationTask> givenVerificationTasks = EXPECTED_TASKS_WITH_CONTEXT();
    VerificationTaskSpecification givenSpecification = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    givenSpecification.setQualityControlTasksSpecification(MOCKED_QUALITY_CONTROL_TASK_SPECIFICATION());
    givenSpecification.setCrowdsourcingConnectorPluginConfiguration(Map.of("REQUIRED_CONFIGURATION", "is_set"));
    when(verificationTaskCreatorMock.createTasks(eq(givenSpecification))).thenReturn(givenVerificationTasks);
    when(qualityControlProviderMock.createQualityControlTasks(eq(givenSpecification)))
        .thenThrow(new QualityControlTaskCreationFailedException(new Exception()));

    // when
    assertThatThrownBy(() -> target.createAndPublishVerification(givenSpecification))
        .isInstanceOf(CrowdsourcingManagerException.class);

    // then
    verify(pluginLoaderMock, never())
        .loadPluginOrThrow(any(), any());
    verify(verificationTaskCreatorMock, times(1))
        .createTasks(eq(givenSpecification));
    verify(qualityControlProviderMock, times(1)).createQualityControlTasks(eq(givenSpecification));

    CrowdsourcingConnectorPluginMock connectorPluginMock = (CrowdsourcingConnectorPluginMock) crowdsourcingConnectorPluginMock;
    assertThat(connectorPluginMock.isCalledPublishTasks()).isFalse();

    verify(metadataStoreMock, never()).saveMetaData(any(), any());
  }

  @Test
  void testCreateAndPublishVerification_givenTaskCreatorThrows_expectException()
      throws PluginLoadingError, QualityControlTaskCreationFailedException,
      VerificationTaskCreationFailedException {
    // given
    List<VerificationTask> givenVerificationTasks = EXPECTED_TASKS_WITH_CONTEXT();
    VerificationTaskSpecification givenSpecification = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    givenSpecification.setQualityControlTasksSpecification(MOCKED_QUALITY_CONTROL_TASK_SPECIFICATION());
    givenSpecification.setCrowdsourcingConnectorPluginConfiguration(Map.of("REQUIRED_CONFIGURATION", "is_set"));
    when(verificationTaskCreatorMock.createTasks(eq(givenSpecification))).thenThrow(
        new VerificationTaskCreationFailedException(new Exception()));

    // when
    assertThatThrownBy(() -> target.createAndPublishVerification(givenSpecification))
        .isInstanceOf(CrowdsourcingManagerException.class);

    // then
    verify(pluginLoaderMock, never())
        .loadPluginOrThrow(any(), any());
    verify(verificationTaskCreatorMock, times(1))
        .createTasks(eq(givenSpecification));
    verify(qualityControlProviderMock, never()).createQualityControlTasks(any());

    CrowdsourcingConnectorPluginMock connectorPluginMock = (CrowdsourcingConnectorPluginMock) crowdsourcingConnectorPluginMock;
    assertThat(connectorPluginMock.isCalledPublishTasks()).isFalse();

    verify(metadataStoreMock, never()).saveMetaData(any(), any());
  }
}
