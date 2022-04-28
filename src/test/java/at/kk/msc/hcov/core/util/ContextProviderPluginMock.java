package at.kk.msc.hcov.core.util;

import at.kk.msc.hcov.sdk.verificationtask.IContextProviderPlugin;
import at.kk.msc.hcov.sdk.verificationtask.model.ProvidedContext;
import java.util.Map;
import java.util.UUID;
import org.apache.jena.ontology.OntModel;

public class ContextProviderPluginMock implements IContextProviderPlugin {
  private boolean gotCalled;

  public boolean gotCalled() {
    return gotCalled;
  }

  @Override
  public ProvidedContext provideContextFor(UUID uuid, OntModel ontModel, Map<String, Object> map) {
    gotCalled = true;
    if (uuid.equals(VerificationTaskMockData.FIRST_MOCK_UUID)) {
      return new ProvidedContext(uuid, "FIRST-CONTEXT");
    } else if (uuid.equals(VerificationTaskMockData.SECOND_MOCK_UUID)) {
      return new ProvidedContext(uuid, "SECOND-CONTEXT");
    }
    return new ProvidedContext(uuid, "Some-mocked-context");
  }

  @Override
  public boolean supports(String s) {
    return "CONTEXT_MOCK".equals(s);
  }
}
