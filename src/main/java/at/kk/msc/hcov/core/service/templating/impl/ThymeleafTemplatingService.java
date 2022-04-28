package at.kk.msc.hcov.core.service.templating.impl;

import at.kk.msc.hcov.core.service.templating.ITemplatingService;
import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin;
import at.kk.msc.hcov.sdk.verificationtask.model.ProvidedContext;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTaskSpecification;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.apache.jena.ontology.OntModel;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class ThymeleafTemplatingService implements ITemplatingService {

  private ITemplateEngine thymeleafTemplateEngine;

  @Override
  public Map<UUID, String> populateTemplates(
      Map<UUID, OntModel> extractedModelElementsMap,
      Map<UUID, ProvidedContext> providedContextMap,
      IVerificationTaskPlugin verificationTaskPlugin,
      VerificationTaskSpecification specification
  ) throws PluginConfigurationNotSetException {
    validateProvidedContextMapOrThrow(providedContextMap, extractedModelElementsMap);

    Map<UUID, String> populatedTemplates = new HashMap<>();
    for (Map.Entry<UUID, OntModel> extractedModelElements : extractedModelElementsMap.entrySet()) {
      UUID currentElementId = extractedModelElements.getKey();
      OntModel currentModelElements = extractedModelElements.getValue();
      ProvidedContext currentProvidedContext = providedContextMap.getOrDefault(currentElementId, new ProvidedContext(currentElementId));

      String populatedTemplate = populateTemplate(verificationTaskPlugin, currentModelElements, currentProvidedContext);
      populatedTemplates.put(currentElementId, populatedTemplate);
    }
    return populatedTemplates;
  }

  private String populateTemplate(
      IVerificationTaskPlugin verificationTaskPlugin,
      OntModel currentModelElements,
      ProvidedContext providedContext
  ) throws PluginConfigurationNotSetException {
    Context thymeleafContext = new org.thymeleaf.context.Context();
    thymeleafContext.setVariables(
        verificationTaskPlugin.getTemplateVariableValueResolver().apply(currentModelElements, providedContext)
    );
    return thymeleafTemplateEngine.process(
        verificationTaskPlugin.getTemplate(), thymeleafContext
    );
  }

  private void validateProvidedContextMapOrThrow(
      Map<UUID, ProvidedContext> providedContextMap,
      Map<UUID, OntModel> extractedModelElementsMap
  ) {
    if (!providedContextMap.isEmpty() && providedContextMap.size() != extractedModelElementsMap.size()) {
      throw new IllegalArgumentException(
          "Non empty context map is provided which has a different size than the extracted model element map!"
      );
    }
  }
}
