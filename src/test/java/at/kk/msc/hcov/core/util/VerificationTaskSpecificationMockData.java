package at.kk.msc.hcov.core.util;

import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTaskSpecification;
import java.util.ArrayList;
import java.util.HashMap;

public class VerificationTaskSpecificationMockData {

  public static final VerificationTaskSpecification MOCKED_VERIFICATION_TASK_SPECIFICATION() {
    return VerificationTaskSpecification.builder()
        .verificationName("MOCK-Verification")
        .ontologyName("ONTOLOGY-TEST-NAME")
        .verificationTaskPluginId("VERIFICATION_MOCK")
        .verificationTaskPluginConfiguration(new HashMap<>())
        .contextProviderPluginId("CONTEXT_MOCK")
        .contextProviderConfiguration(new HashMap<>())
        .crowdsourcingConnectorPluginId("CROWDSOUCRING_MOCK")
        .crowdsourcingConnectorPluginConfiguration(new HashMap<>())
        .processorPluginIds(new ArrayList<>())
        .processorPluginConfigurations(new ArrayList<>())
        .build();
  }
}
