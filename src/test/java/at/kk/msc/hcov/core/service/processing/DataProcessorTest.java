package at.kk.msc.hcov.core.service.processing;

import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskSpecificationMockData.EXPECTED_VERIFICATION_META_DATA_WITH_QUALITY_CONTROL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.kk.msc.hcov.core.persistence.metadata.ICrowdsourcingMetadataStore;
import at.kk.msc.hcov.core.persistence.metadata.exception.VerificationDoesNotExistException;
import at.kk.msc.hcov.core.persistence.model.VerificationMetaDataEntity;
import at.kk.msc.hcov.core.service.crowdsourcing.ICrowdsourcingManager;
import at.kk.msc.hcov.core.service.crowdsourcing.exception.CrowdsourcingManagerException;
import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.plugin.IPluginLoader;
import at.kk.msc.hcov.core.service.processing.exception.DataProcessorException;
import at.kk.msc.hcov.core.service.processing.impl.DataProcessor;
import at.kk.msc.hcov.core.service.processing.model.DataProcessorResults;
import at.kk.msc.hcov.core.service.processing.plugin.RawDataProcessorPlugin;
import at.kk.msc.hcov.core.util.mockdata.ResultMockData;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.plugin.core.Plugin;

@ExtendWith(MockitoExtension.class)
public class DataProcessorTest {

  @Mock
  ICrowdsourcingMetadataStore metaDataStoreMock;

  @Mock
  ICrowdsourcingManager crowdsourcingManagerMock;

  @Mock
  IPluginLoader<Plugin<String>> pluginLoaderMock;


  IDataProcessor target;

  @BeforeEach
  void setUp() {
    target = new DataProcessor(metaDataStoreMock, crowdsourcingManagerMock, pluginLoaderMock);
  }

  @Test
  void testLoadResultsAndProcess()
      throws VerificationDoesNotExistException, PluginLoadingError, CrowdsourcingManagerException, DataProcessorException {
    // given
    VerificationMetaDataEntity givenMetaData = EXPECTED_VERIFICATION_META_DATA_WITH_QUALITY_CONTROL();
    String givenVerificationName = givenMetaData.getVerificationName();
    when(metaDataStoreMock.getMetaData(eq(givenVerificationName)))
        .thenReturn(givenMetaData);
    when(crowdsourcingManagerMock.getAllResultsForVerification(eq(givenVerificationName)))
        .thenReturn(ResultMockData.EXPECTED_RAW_RESULT_MAP());
    when(pluginLoaderMock.loadPluginOrThrow(eq(IPluginLoader.PluginType.PROCESSOR), eq("RAW_DATA_PROCESSOR")))
        .thenReturn(new RawDataProcessorPlugin());

    // when
    List<DataProcessorResults> actualList = target.loadResultsAndProcess(givenVerificationName);

    // then
    verify(metaDataStoreMock, times(1)).getMetaData(eq(givenVerificationName));
    verify(crowdsourcingManagerMock, times(1)).getAllResultsForVerification(eq(givenVerificationName));
    verify(pluginLoaderMock, times(1)).loadPluginOrThrow(eq(IPluginLoader.PluginType.PROCESSOR), eq("RAW_DATA_PROCESSOR"));

    assertThat(actualList).hasSize(1);
    DataProcessorResults actual = actualList.get(0);
    DataProcessorResults expected = ResultMockData.EXPECTED_DATA_PROCESSOR_RESULTS_ONLY_RAW().get(0);
    assertThat(actual.getPluginId()).isEqualTo(expected.getPluginId());
    assertThat(actual.getProcessingResult()).containsExactlyInAnyOrderElementsOf(expected.getProcessingResult());
  }

  @Test
  void testLoadResultsAndProcess_givenCrowdsourcingManagerThrows_expectException()
      throws VerificationDoesNotExistException, PluginLoadingError, CrowdsourcingManagerException, DataProcessorException {
    // given
    VerificationMetaDataEntity givenMetaData = EXPECTED_VERIFICATION_META_DATA_WITH_QUALITY_CONTROL();
    String givenVerificationName = givenMetaData.getVerificationName();
    when(metaDataStoreMock.getMetaData(eq(givenVerificationName)))
        .thenReturn(givenMetaData);
    when(crowdsourcingManagerMock.getAllResultsForVerification(eq(givenVerificationName)))
        .thenThrow(new PluginLoadingError(IPluginLoader.PluginType.PROCESSOR, "MOCKED"));


    // when
    assertThatThrownBy(() -> target.loadResultsAndProcess(givenVerificationName))
        .isInstanceOf(DataProcessorException.class);
    // then
    verify(metaDataStoreMock, times(1)).getMetaData(eq(givenVerificationName));
    verify(crowdsourcingManagerMock, times(1)).getAllResultsForVerification(eq(givenVerificationName));
    verify(pluginLoaderMock, times(0)).loadPluginOrThrow(any(), any());
  }

  public static void assertThatProcessorResultsAreEqual(DataProcessorResults actual, DataProcessorResults expected) {
    assertThat(actual.getPluginId()).isEqualTo(expected.getPluginId());

  }
}
