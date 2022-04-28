package at.kk.msc.hcov.core.service.templating;

import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin;
import at.kk.msc.hcov.sdk.verificationtask.model.ProvidedContext;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTaskSpecification;
import java.util.Map;
import java.util.UUID;
import org.apache.jena.ontology.OntModel;

public interface ITemplatingService {

  Map<UUID, String> populateTemplates(
      Map<UUID, OntModel> extractedModelElementsMap,
      Map<UUID, ProvidedContext> providedContextMap,
      IVerificationTaskPlugin verificationTaskPlugin,
      VerificationTaskSpecification specification
  ) throws PluginConfigurationNotSetException;


}
