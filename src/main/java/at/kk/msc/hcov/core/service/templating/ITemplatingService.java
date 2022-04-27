package at.kk.msc.hcov.core.service.templating;

import at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin;
import at.kk.msc.hcov.sdk.verificationtask.model.Context;
import java.util.Map;
import java.util.UUID;
import org.apache.jena.ontology.OntModel;

public interface ITemplatingService {
  
  Map<UUID, String> populateTemplates(
      IVerificationTaskPlugin verificationTaskPlugin, Map<UUID, OntModel> extractedModelElementsMap, Map<UUID, Context> providedContextMap
  );


}
