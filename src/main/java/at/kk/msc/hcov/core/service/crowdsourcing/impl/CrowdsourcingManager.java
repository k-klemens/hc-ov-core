package at.kk.msc.hcov.core.service.crowdsourcing.impl;

import at.kk.msc.hcov.core.persistence.metadata.ICrowdsourcingMetadataStore;
import at.kk.msc.hcov.core.service.crowdsourcing.ICrowdsourcingManager;
import at.kk.msc.hcov.core.service.crowdsourcing.exception.CrowdsourcingManagerException;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedQualityControlTask;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedTaskIdMappings;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedVerification;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedVerificationTask;
import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.plugin.IPluginLoader;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.IQualityControlProvider;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.exception.QualityControlTaskCreationFailedException;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model.QualityControlTask;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model.QualityControlTaskCreation;
import at.kk.msc.hcov.core.service.verificationtask.task.IVerificationTaskCreator;
import at.kk.msc.hcov.core.service.verificationtask.task.exception.VerificationTaskCreationFailedException;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.ICrowdsourcingConnectorPlugin;
import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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
  private IVerificationTaskCreator verificationTaskCreator;

  private IPluginLoader<Plugin<String>> pluginLoader;

  @Override
  public PublishedVerification createAndPublishVerification(VerificationTaskSpecification specification)
      throws PluginLoadingError, CrowdsourcingManagerException {
    List<VerificationTask> verificationTasks = createVerificationTasksOrThrow(specification);
    List<QualityControlTask> qualityControlTasks = createQualityControlQuestionsIfRequired(specification);

    PublishedTaskIdMappings publishedTaskIdMappings = publishTasksAndStoreMetadata(specification, verificationTasks, qualityControlTasks);

    return toPublishedVerification(publishedTaskIdMappings, verificationTasks, qualityControlTasks);
  }

  private List<VerificationTask> createVerificationTasksOrThrow(VerificationTaskSpecification specification)
      throws PluginLoadingError, CrowdsourcingManagerException {
    List<VerificationTask> verificationTasks;
    try {
      verificationTasks = verificationTaskCreator.createTasks(specification);
    } catch (VerificationTaskCreationFailedException e) {
      throw new CrowdsourcingManagerException("Error while creating the verificationTasks!", e);
    }
    return verificationTasks;
  }

  private PublishedTaskIdMappings publishTasksAndStoreMetadata(
      VerificationTaskSpecification specification,
      List<VerificationTask> verificationTasks,
      List<QualityControlTask> qualityControlTasks
  ) throws PluginLoadingError, CrowdsourcingManagerException {
    List<VerificationTask> tasksToBePublished = new ArrayList<>(verificationTasks);
    tasksToBePublished.addAll(qualityControlTasks);
    PublishedTaskIdMappings publishedTaskIdMappings = publishTasksUsingConnector(tasksToBePublished, specification);
    metadataStore.saveMetaData(specification, publishedTaskIdMappings);
    return publishedTaskIdMappings;
  }

  private PublishedTaskIdMappings publishTasksUsingConnector(
      List<VerificationTask> tasksToBePublished, VerificationTaskSpecification specification
  )
      throws PluginLoadingError, CrowdsourcingManagerException {
    ICrowdsourcingConnectorPlugin crowdsourcingConnectorPlugin = setupCrowdsourcingConnectorPlugin(specification);
    try {
      Map<UUID, String> idMap = crowdsourcingConnectorPlugin.publishTasks(tasksToBePublished);
      return toPublishedTaskIdMappings(idMap, specification);
    } catch (PluginConfigurationNotSetException e) {
      throw new CrowdsourcingManagerException("Could not publish tasks as configuration for the crowdsourcing connector is not set.", e);
    }
  }

  private PublishedTaskIdMappings toPublishedTaskIdMappings(Map<UUID, String> idMap, VerificationTaskSpecification specification) {
    Set<UUID> qualityControlIds = null;
    if (specification.getQualityControlTasksSpecification() != null) {
      qualityControlIds = specification.getQualityControlTasksSpecification()
          .getQualityControlTasks().stream()
          .map(QualityControlTaskCreation::getQualityControlModelElementId)
          .collect(Collectors.toSet());
    } else {
      qualityControlIds = new HashSet<>();
    }

    PublishedTaskIdMappings publishedTaskIdMappings = new PublishedTaskIdMappings(new HashMap<>(), new HashMap<>());
    for (Map.Entry<UUID, String> idMapEntry : idMap.entrySet()) {
      UUID interalId = idMapEntry.getKey();
      String crowdsourcingId = idMapEntry.getValue();
      if (qualityControlIds.contains(interalId)) {
        publishedTaskIdMappings.getQualityControlTaskIdMappings().put(interalId, crowdsourcingId);
      } else {
        publishedTaskIdMappings.getOntologyVerificationTaskIdMappings().put(interalId, crowdsourcingId);
      }
    }
    return publishedTaskIdMappings;
  }

  private PublishedVerification toPublishedVerification(
      PublishedTaskIdMappings publishedTaskIdMappings,
      List<VerificationTask> verificationTasks,
      List<QualityControlTask> qualityControlTasks
  ) {
    List<PublishedVerificationTask> publishedVerificationTasks = verificationTasks.stream()
        .map(
            verificationTask ->
                new PublishedVerificationTask(
                    verificationTask,
                    publishedTaskIdMappings.getOntologyVerificationTaskId(verificationTask.getOntologyElementId())
                )
        )
        .toList();

    List<PublishedQualityControlTask> publishedQualityTasks = qualityControlTasks.stream()
        .map(
            qualityControlTask ->
                new PublishedQualityControlTask(
                    qualityControlTask,
                    publishedTaskIdMappings.getQualityControlTaskId(qualityControlTask.getOntologyElementId())
                )
        )
        .toList();

    return new PublishedVerification(publishedVerificationTasks, publishedQualityTasks);
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
