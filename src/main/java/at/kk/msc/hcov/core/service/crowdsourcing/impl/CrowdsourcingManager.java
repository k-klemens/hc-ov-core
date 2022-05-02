package at.kk.msc.hcov.core.service.crowdsourcing.impl;

import at.kk.msc.hcov.core.persistence.metadata.ICrowdsourcingMetadataStore;
import at.kk.msc.hcov.core.service.crowdsourcing.ICrowdsourcingManager;
import at.kk.msc.hcov.core.service.crowdsourcing.exception.CrowdsourcingManagerException;
import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.plugin.IPluginLoader;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.IQualityControlProvider;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.exception.QualityControlTaskCreationFailedException;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model.QualityControlTask;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.ICrowdsourcingConnectorPlugin;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.PublishedTask;
import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.plugin.core.Plugin;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CrowdsourcingManager implements ICrowdsourcingManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(CrowdsourcingManager.class);

  private IQualityControlProvider qualityControlProvider;
  private ICrowdsourcingMetadataStore metadataStore;

  private IPluginLoader<Plugin<String>> pluginLoader;

  @Override
  public List<PublishedTask> publishCrowdsourcingTasks(List<VerificationTask> verificationTasks,
                                                       VerificationTaskSpecification specification)
      throws PluginLoadingError, CrowdsourcingManagerException {
    List<QualityControlTask> qualityControlTasks = createQualityControlQuestionsIfRequired(specification);

    List<VerificationTask> tasksToBePublished = new ArrayList<>(verificationTasks);
    tasksToBePublished.addAll(qualityControlTasks);

    List<PublishedTask> publishedTasks = publishTasksUsingConnector(tasksToBePublished, specification);
    metadataStore.saveMetaData(specification, publishedTasks);

    return publishedTasks;
  }

  private List<PublishedTask> publishTasksUsingConnector(
      List<VerificationTask> tasksToBePublished, VerificationTaskSpecification specification
  )
      throws PluginLoadingError, CrowdsourcingManagerException {
    ICrowdsourcingConnectorPlugin crowdsourcingConnectorPlugin = setupCrowdsourcingConnectorPlugin(specification);
    try {
      return crowdsourcingConnectorPlugin.publishTasks(tasksToBePublished);
    } catch (PluginConfigurationNotSetException e) {
      throw new CrowdsourcingManagerException("Could not publish tasks as configuration for the crowdsourcing connector is not set.", e);
    }
  }

  private List<QualityControlTask> createQualityControlQuestionsIfRequired(VerificationTaskSpecification specification)
      throws PluginLoadingError, CrowdsourcingManagerException {
    List<QualityControlTask> qualityControlTasks = null;
    if (specification.getQualityControlTasksSpecification() != null) {
      try {
        qualityControlTasks = qualityControlProvider.createQualityControlTasks(specification);
      } catch (QualityControlTaskCreationFailedException e) {
        throw new CrowdsourcingManagerException("Error during creation of the quality control tasks.", e);
      }
    }
    return qualityControlTasks == null ? new ArrayList<>() : qualityControlTasks;
  }

  private ICrowdsourcingConnectorPlugin setupCrowdsourcingConnectorPlugin(VerificationTaskSpecification specification)
      throws PluginLoadingError {
    ICrowdsourcingConnectorPlugin crowdsourcingConnectorPlugin = (ICrowdsourcingConnectorPlugin) pluginLoader.loadPluginOrThrow(
        IPluginLoader.PluginType.CROWDSOURCING_CONNECTOR, specification.getCrowdsourcingConnectorPluginId()
    );
    crowdsourcingConnectorPlugin.setConfiguration(
        specification.getCrowdsourcingConnectorPluginConfiguration()
    );
    return crowdsourcingConnectorPlugin;
  }

}
