package at.kk.msc.hcov.core.util;


import at.kk.msc.hcov.core.util.mockdata.PublishedTaskMockData;
import at.kk.msc.hcov.core.util.mockdata.ResultMockData;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.ICrowdsourcingConnectorPlugin;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.HitStatus;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.RawResult;
import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;

@Getter
public class CrowdsourcingConnectorPluginMock implements ICrowdsourcingConnectorPlugin {

  private Map<String, Object> configuration;

  private Map<String, HitStatus> mockedHitStatusMap;

  private boolean calledPublishTasks = false;
  private List<VerificationTask> mostRecentVerificationTaskListProvided;

  private boolean calledGetStatusForHits = false;
  private List<String> mostRecentGetStatusForHitsList;

  private boolean calledGetResultsForHits = false;

  @Override
  public Map<UUID, String> publishTasks(List<VerificationTask> list) throws PluginConfigurationNotSetException {
    // store mock information when method is called for verification / assertions
    this.mostRecentVerificationTaskListProvided = list;
    calledPublishTasks = true;

    validateConfigurationSetOrThrow();
    if (list.size() == 4) { // MOCK DATA size 4 -> with quality control
      return PublishedTaskMockData.MOCKED_ID_MAPPINGS_WITH_QUALITY_CONTROL();
    }
    return PublishedTaskMockData.MOCKED_ID_MAPPINGS_WITHOUT_QUALITY_CONTROL();
  }

  @Override
  public Map<String, HitStatus> getStatusForHits(List<String> list) throws PluginConfigurationNotSetException {
    calledGetStatusForHits = true;
    mostRecentGetStatusForHitsList = list;
    return getMockedHitStatusMap();
  }

  @Override
  public Map<String, List<RawResult>> getResultsForHits(List<String> list) throws PluginConfigurationNotSetException {
    calledGetResultsForHits = true;
    return ResultMockData.MOCKED_RAW_RESULT_MAP();
  }

  public void setHitStatusMapMockData(Map<String, HitStatus> mockedHitStatusMap) {
    this.mockedHitStatusMap = mockedHitStatusMap;
  }

  @Override
  public void validateConfigurationSetOrThrow() throws PluginConfigurationNotSetException {
    ICrowdsourcingConnectorPlugin.super.validateConfigurationSetOrThrow();
    if (!configuration.containsKey("REQUIRED_CONFIGURATION")) {
      throw new PluginConfigurationNotSetException("Configuration with key REQUIRED_CONFIGURATION not set!");
    }
  }

  @Override
  public void setConfiguration(Map<String, Object> map) {
    configuration = map;
  }

  @Override
  public Map<String, Object> getConfiguration() {
    return configuration;
  }

  @Override
  public boolean supports(String s) {
    return "CROWDSOURCING_MOCK".equalsIgnoreCase(s);
  }

}
