package at.kk.msc.hcov.core.service.templating.impl;

import at.kk.msc.hcov.core.service.templating.ITemplatingService;
import at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin;
import at.kk.msc.hcov.sdk.verificationtask.model.Context;
import java.util.Map;
import java.util.UUID;
import org.apache.jena.ontology.OntModel;
import org.springframework.stereotype.Service;

@Service
public class ThymeleafTemplatingService implements ITemplatingService {


  @Override
  public Map<UUID, String> populateTemplates(IVerificationTaskPlugin verificationTaskPlugin, Map<UUID, OntModel> extractedModelElementsMap,
                                             Map<UUID, Context> providedContextMap) {
    return null;
  }
}
