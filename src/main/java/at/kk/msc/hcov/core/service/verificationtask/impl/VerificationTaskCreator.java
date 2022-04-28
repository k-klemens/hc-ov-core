package at.kk.msc.hcov.core.service.verificationtask.impl;

import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNotFoundException;
import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.ontology.data.IDataProvider;
import at.kk.msc.hcov.core.service.plugin.IPluginLoader;
import at.kk.msc.hcov.core.service.templating.ITemplatingService;
import at.kk.msc.hcov.core.service.verificationtask.IVerificationTaskCreator;
import at.kk.msc.hcov.core.service.verificationtask.exception.VerificationTaskCreationFailedException;
import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import at.kk.msc.hcov.sdk.verificationtask.IContextProviderPlugin;
import at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin;
import at.kk.msc.hcov.sdk.verificationtask.model.ProvidedContext;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTaskSpecification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.apache.jena.ontology.OntModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class VerificationTaskCreator implements IVerificationTaskCreator {

  private static final Logger LOGGER = LoggerFactory.getLogger(VerificationTaskCreator.class);

  private IDataProvider dataProvider;
  private ITemplatingService templatingService;

  private IPluginLoader pluginLoader;

  @Override
  public List<VerificationTask> createTasks(VerificationTaskSpecification specification)
      throws VerificationTaskCreationFailedException, PluginLoadingError {
    String verificationName = specification.getVerificationName();
    String ontologyName = specification.getOntologyName();
    IVerificationTaskPlugin verificationTaskPlugin = (IVerificationTaskPlugin) pluginLoader.loadPluginOrThrow(
        IPluginLoader.PluginType.VERIFICATION_TASK_CREATOR, specification.getVerificationTaskPluginId()
    );
    verificationTaskPlugin.setConfiguration(
        specification.getVerificationTaskPluginConfiguration() == null ?
            new HashMap<>() : specification.getVerificationTaskPluginConfiguration()
    );

    Map<UUID, OntModel> extractedModelElements = extractModelElementsOrThrow(verificationTaskPlugin, specification);
    LOGGER.debug("Extracted model elements of size: {}", extractedModelElements.size());
    Map<UUID, ProvidedContext> providedContexts = extractContextIfNeeded(extractedModelElements, specification);
    LOGGER.debug("Extracted context of size: {}", providedContexts.size());

    // TODO: care for representation + eventual possibility to upload the pictures somewhere --> should be done by the task creator plugin
    Map<UUID, String> questionHtmlDocuments = createHtmlDocumentsOrThrow(
        extractedModelElements, providedContexts, verificationTaskPlugin, specification
    );

    List<VerificationTask> verificationTasks = new ArrayList<>();
    for (Map.Entry<UUID, String> questionHtmlEntry : questionHtmlDocuments.entrySet()) {
      UUID currentModelElementId = questionHtmlEntry.getKey();
      String currentQuestionHtmlDocument = questionHtmlEntry.getValue();
      VerificationTask verificationTask = new VerificationTask();
      verificationTask.setVerificationName(verificationName);
      verificationTask.setOntologyName(ontologyName);
      verificationTask.setOntologyElementId(currentModelElementId);
      verificationTask.setTaskHtml(currentQuestionHtmlDocument);
      verificationTasks.add(verificationTask);
    }
    return verificationTasks;
  }

  private Map<UUID, String> createHtmlDocumentsOrThrow(
      Map<UUID, OntModel> extractedModelElements,
      Map<UUID, ProvidedContext> providedContexts,
      IVerificationTaskPlugin verificationTaskPlugin,
      VerificationTaskSpecification specification
  ) throws VerificationTaskCreationFailedException {
    try {
      return templatingService.populateTemplates(
          extractedModelElements, providedContexts, verificationTaskPlugin, specification
      );
    } catch (PluginConfigurationNotSetException e) {
      throw new VerificationTaskCreationFailedException(e);
    }
  }

  private Map<UUID, ProvidedContext> extractContextIfNeeded(
      Map<UUID, OntModel> extractedModelElements, VerificationTaskSpecification specification
  ) throws PluginLoadingError {
    if (specification.getContextProviderPluginId() != null && !specification.getContextProviderPluginId().isBlank()) {
      IContextProviderPlugin contextProviderPlugin = (IContextProviderPlugin) pluginLoader.loadPluginOrThrow(
          IPluginLoader.PluginType.CONTEXT_PROVIDER, specification.getContextProviderPluginId()
      );
      return contextProviderPlugin.provideContextFor(extractedModelElements, specification.getContextProviderConfiguration());
    }
    return new HashMap<>();
  }

  private Map<UUID, OntModel> extractModelElementsOrThrow(
      IVerificationTaskPlugin verificationTaskPlugin, VerificationTaskSpecification specification
  ) throws VerificationTaskCreationFailedException {

    try {
      Function<OntModel, List<OntModel>> elementExtractor =
          verificationTaskPlugin.getElementExtractor();
      return dataProvider.extractAndStoreRequiredOntologyElements(
          specification.getOntologyName(), specification.getVerificationName(), elementExtractor
      );
    } catch (OntologyNotFoundException | IOException | PluginConfigurationNotSetException e) {
      throw new VerificationTaskCreationFailedException(e);
    }
  }


}