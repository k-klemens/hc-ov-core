package at.kk.msc.hcov.core.service.templating;

import at.kk.msc.hcov.core.service.templating.model.ResolvedVariablesWrapper;
import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin;
import at.kk.msc.hcov.sdk.verificationtask.model.ProvidedContext;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTaskSpecification;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.jena.ontology.OntModel;

/**
 * Specifies interface which is capable of populating templates to create HTML documents to be used for the verifications.
 */
public interface ITemplatingService {

  /**
   * Creates HTML documents reflecting the verification tasks for all passed model elements.
   *
   * @param extractedModelElementsMap map containting the extracted model elements to for each a HTML document shall be created.
   * @param providedContextMap        map containing possible context objects to the model elements. Can be an empty map if the context is not needed.
   * @param verificationTaskPlugin    plugin used to create the verificaiton task to provide the template as well as the template variables.
   * @param specification             specification of the verification to obtain configuration values.
   * @return a Map of HTML documents reflecting the individual verification tasks keyed by the extracted model element ids.
   * @throws PluginConfigurationNotSetException if the verificationTaskPlugin requires a configuration the configuration was not set properly
   */
  Map<UUID, String> populateTemplates(
      Map<UUID, OntModel> extractedModelElementsMap,
      Map<UUID, ProvidedContext> providedContextMap,
      IVerificationTaskPlugin verificationTaskPlugin,
      VerificationTaskSpecification specification
  ) throws PluginConfigurationNotSetException;

  Map<UUID, String> populateTemplatesWithResolvedVariables(
      List<ResolvedVariablesWrapper> templateVariables,
      IVerificationTaskPlugin verificationTaskPlugin
  ) throws PluginConfigurationNotSetException;


}
