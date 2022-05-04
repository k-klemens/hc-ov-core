package at.kk.msc.hcov.core.util;

import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskMockData.FIRST_MOCK_EXTRACTED_ELEMENTS;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskMockData.SECOND_MOCK_EXTRACTED_ELEMENTS;

import at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin;
import at.kk.msc.hcov.sdk.verificationtask.model.ProvidedContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.apache.jena.ontology.OntModel;

public class VerificationTaskPluginMock implements IVerificationTaskPlugin {

  /* MOCKED METHODS FROM THE PLUGIN TO BE USED WITH THE TEST DATA ABOVE */
  /**
   * withContext controls which template is being returned, necessary for testing and mocking
   */
  private boolean withContext;

  public VerificationTaskPluginMock(boolean withContext) {
    this.withContext = withContext;
  }

  @Override
  public Function<OntModel, List<OntModel>> getElementExtractor() {
    return ontModel -> {
      List<OntModel> extractedModelElements = new ArrayList<>();
      extractedModelElements.add(FIRST_MOCK_EXTRACTED_ELEMENTS);
      extractedModelElements.add(SECOND_MOCK_EXTRACTED_ELEMENTS);
      return extractedModelElements;
    };
  }

  @Override
  public BiFunction<OntModel, ProvidedContext, Map<String, Object>> getTemplateVariableValueResolver() {
    return (ontModel, context) -> {
      Map<String, Object> templatingVariables = new HashMap<>();

      templatingVariables.put("ontologyName", "ONTOLOGY-TEST-NAME");
      templatingVariables.put("element", ontModel == FIRST_MOCK_EXTRACTED_ELEMENTS ? "FIRST-ELEMENT" : "SECOND-ELEMENT");
      if (withContext) {
        templatingVariables.put("context", context.getContextString());
      }

      return templatingVariables;
    };
  }

  @Override
  public String getTemplate() {
    if (withContext) {
      return "Ontology with name <span th:text=\"${ontologyName}\"/> from <span th:text=\"${context}\"/> requires verification for <span th:text=\"${element}\"/>  is verified";
    } else {
      return "Ontology with name <span th:text=\"${ontologyName}\"/> requires verification for <span th:text=\"${element}\"/>  is verified";
    }
  }

  @Override
  public boolean supports(String s) {
    return "VERIFICATION_MOCK".equals(s);
  }

  @Override
  public void setConfiguration(Map<String, Object> map) {
    // not required for mock
  }

  @Override
  public Map<String, Object> getConfiguration() {
    return new HashMap<>();
  }

  public void setWithContext(boolean withContext) {
    this.withContext = withContext;
  }

}
