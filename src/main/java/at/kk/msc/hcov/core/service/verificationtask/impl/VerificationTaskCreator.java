package at.kk.msc.hcov.core.service.verificationtask.impl;

import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNotFoundException;
import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.ontology.data.IDataProvider;
import at.kk.msc.hcov.core.service.plugin.IPluginLoader;
import at.kk.msc.hcov.core.service.templating.ITemplatingService;
import at.kk.msc.hcov.core.service.verificationtask.IVerificationTaskCreator;
import at.kk.msc.hcov.core.service.verificationtask.exception.VerificationTaskCreationFailedException;
import at.kk.msc.hcov.sdk.verificationtask.IContextProviderPlugin;
import at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin;
import at.kk.msc.hcov.sdk.verificationtask.model.Context;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTaskSpecification;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import org.apache.jena.ontology.OntModel;
import org.springframework.stereotype.Component;

@Component
public class VerificationTaskCreator implements IVerificationTaskCreator {

  private IDataProvider dataProvider;
  private ITemplatingService templatingService;

  private IPluginLoader pluginLoader;

  @Override
  public List<VerificationTask> createTasks(VerificationTaskSpecification specification)
      throws VerificationTaskCreationFailedException, PluginLoadingError {
    IVerificationTaskPlugin verificationTaskPlugin = (IVerificationTaskPlugin) pluginLoader.loadPluginOrThrow(
        IPluginLoader.PluginType.VERIFICATION_TASK_CREATOR, specification.getVerificationTaskPluginId()
    );

    Map<UUID, OntModel> extractedModelElements = extractModelElementsOrThrow(verificationTaskPlugin, specification);
    Map<UUID, Context> providedContexts = extractContextIfNeeded(extractedModelElements, specification);

    // TODO: care for representation + eventual possibility to upload the pictures somewhere -->
    Map<UUID, String> hitQuestions = templatingService.populateTemplates(verificationTaskPlugin, extractedModelElements, providedContexts);

    return null;
  }

  private Map<UUID, Context> extractContextIfNeeded(
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
    Function<OntModel, List<OntModel>> elementExtractor =
        verificationTaskPlugin.getElementExtractor(specification.getVerificationTaskPluginConfiguration());
    try {
      return dataProvider.extractAndStoreRequiredOntologyElements(
          specification.getOntologyName(), specification.getVerificationName(), elementExtractor
      );
    } catch (OntologyNotFoundException | IOException e) {
      throw new VerificationTaskCreationFailedException(e);
    }
  }


}
