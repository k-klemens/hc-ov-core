package at.kk.msc.hcov.core.service.processing.impl;

import at.kk.msc.hcov.core.persistence.metadata.ICrowdsourcingMetadataStore;
import at.kk.msc.hcov.core.persistence.metadata.exception.VerificationDoesNotExistException;
import at.kk.msc.hcov.core.persistence.model.ProcessorPluginConfigurationEntity;
import at.kk.msc.hcov.core.persistence.model.VerificationMetaDataEntity;
import at.kk.msc.hcov.core.service.crowdsourcing.ICrowdsourcingManager;
import at.kk.msc.hcov.core.service.crowdsourcing.exception.CrowdsourcingManagerException;
import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.plugin.IPluginLoader;
import at.kk.msc.hcov.core.service.processing.IDataProcessor;
import at.kk.msc.hcov.core.service.processing.exception.DataProcessorException;
import at.kk.msc.hcov.core.service.processing.model.DataProcessorResults;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.RawResult;
import at.kk.msc.hcov.sdk.crowdsourcing.processing.IProcessorPlugin;
import at.kk.msc.hcov.sdk.crowdsourcing.processing.ProcessingResult;
import at.kk.msc.hcov.sdk.crowdsourcing.processing.RequiredKeyNotPresentException;
import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.plugin.core.Plugin;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataProcessor implements IDataProcessor {

  private ICrowdsourcingMetadataStore metadataStore;
  private ICrowdsourcingManager crowdsourcingManager;

  private IPluginLoader<Plugin<String>> pluginLoader;

  @Override
  public List<DataProcessorResults> loadResultsAndProcess(String verificationName)
      throws PluginLoadingError, VerificationDoesNotExistException, DataProcessorException {
    VerificationMetaDataEntity metaData = loadMetaDataOrThrow(verificationName);

    List<DataProcessorResults> pluginResults = new ArrayList<>();
    List<ProcessingResult> currentProcessingData = getRawDataForVerificationAsProcessingResults(metaData);
    for (ProcessorPluginConfigurationEntity processorConfiguration : metaData.getProcessorPluginConfigurationEntities()) {
      DataProcessorResults dataProcessorResults = executeProcessorPlugin(processorConfiguration, currentProcessingData);
      currentProcessingData = dataProcessorResults.getProcessingResult();
      pluginResults.add(dataProcessorResults);
    }

    return pluginResults;
  }

  private DataProcessorResults executeProcessorPlugin(
      ProcessorPluginConfigurationEntity processorPluginConfiguration, List<ProcessingResult> input
  ) throws PluginLoadingError, DataProcessorException {
    String pluginId = processorPluginConfiguration.getPluginId();
    IProcessorPlugin processorPlugin =
        (IProcessorPlugin) pluginLoader.loadPluginOrThrow(IPluginLoader.PluginType.PROCESSOR, pluginId);
    processorPlugin.setConfiguration(processorPluginConfiguration.getProcessorPluginConfigurationAsMap());

    try {
      List<ProcessingResult> results = processorPlugin.process(input);
      return new DataProcessorResults(pluginId, results);
    } catch (RequiredKeyNotPresentException e) {
      throw new DataProcessorException("The given input data for the plugin " + pluginId +
          " does not contain one required key! Ensure that the order of specifying the execution of the processor plugins is correct!");
    } catch (PluginConfigurationNotSetException e) {
      throw new DataProcessorException("Required configuration for processor plugin '" + pluginId + "' not set!", e);
    }
  }

  private List<ProcessingResult> getRawDataForVerificationAsProcessingResults(VerificationMetaDataEntity metaData)
      throws DataProcessorException, VerificationDoesNotExistException {
    try {
      Map<UUID, List<RawResult>> rawResultsMap = crowdsourcingManager.getAllResultsForVerification(metaData.getVerificationName());
      return toProcessingResultList(rawResultsMap, metaData);
    } catch (CrowdsourcingManagerException | PluginLoadingError e) {
      throw new DataProcessorException("Error while obtaining results from the crowdsourcing platform!", e);
    }
  }

  private List<ProcessingResult> toProcessingResultList(Map<UUID, List<RawResult>> rawResultsMap, VerificationMetaDataEntity metaData) {
    Map<String, UUID> crowdsourcingIdMappings = metaData.getCrowdsourcingTaskIdMappings();
    return rawResultsMap.values().stream()
        .flatMap(Collection::stream)
        .map(
            rawResult -> new ProcessingResult(
                rawResult.getCrowdscouringId(),
                new HashMap<>(Map.of(
                    "FROM_RAW", "TRUE -> This list of processing data is obtained from a List of RawResults",
                    "EXTRACTED_ELEMENTS_ID", crowdsourcingIdMappings.get(rawResult.getCrowdscouringId()).toString(),
                    "RESULT_ID", rawResult.getResultId(),
                    "WORKER_ID", rawResult.getWorkerId(),
                    "ANSWER", rawResult.getAnswer()
                )),
                "Results as obtained from the crowdsourcing platform."
            )
        ).toList();
  }

  private VerificationMetaDataEntity loadMetaDataOrThrow(String verificationName) throws VerificationDoesNotExistException {
    return metadataStore.getMetaData(verificationName);
  }

}
