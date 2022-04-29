package at.kk.msc.hcov.core.util;

import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import at.kk.msc.hcov.sdk.verificationtask.IContextProviderPlugin;
import at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin;
import at.kk.msc.hcov.sdk.verificationtask.model.ProvidedContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.apache.jena.ontology.OntModel;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class IntegrationTestPlugins {

  @Bean
  public IVerificationTaskPlugin sampleMovieVerificationTaskPlugin() {
    return new IVerificationTaskPlugin() {

      private Map<String, Object> configuration;

      @Override
      public Function<OntModel, List<OntModel>> getElementExtractor() throws PluginConfigurationNotSetException {
        return TestUtils.getSampleMovieModelExtrator();
      }

      @Override
      public BiFunction<OntModel, ProvidedContext, Map<String, Object>> getTemplateVariableValueResolver()
          throws PluginConfigurationNotSetException {
        return (ontModel, providedContext) -> {
          Map<String, Object> templateValues = new HashMap<>();
          String subClassName = ontModel.listClasses().toList().get(0).getLocalName();
          templateValues.put("subclass", subClassName);
          if (configuration != null && (boolean) configuration.getOrDefault("CONTEXT_ENABLED", false)) {
            templateValues.put("context", providedContext.getContextString());
          }
          return templateValues;
        };
      }

      @Override
      public String getTemplate() throws PluginConfigurationNotSetException {
        if (!configuration.containsKey("CONTEXT_ENABLED")) {
          return """
              <html>
                <body>    
                  <h1>Verify the following fact:</h1>          
                  <p>Is <span th:text=\"${subclass}\"/> a type of Person?</p> 
                  <input type="radio" id="yes" name="answer" value="Yes">
                  <label for="yes">Yes</label><br>
                  <input type="radio" id="no" name="answer" value="No">
                  <label for="no">No</label><br>
                </body>
              </html>          
              """;
        } else if ((boolean) configuration.get("CONTEXT_ENABLED")) {
          return """
              <html>
                <body>    
                  <h1>Verify the following fact:</h1>          
                  <p>Is <span th:text=\"${subclass}\"/> from <span th:text=\"${context}\"/> a type of Person?</p> 
                  <input type="radio" id="yes" name="answer" value="Yes">
                  <label for="yes">Yes</label><br>
                  <input type="radio" id="no" name="answer" value="No">
                  <label for="no">No</label><br>
                </body>
              </html>          
              """;
        }
        return "";
      }

      @Override
      public void setConfiguration(Map<String, Object> map) {
        configuration = new HashMap<>(map);
      }

      @Override
      public Map<String, Object> getConfiguration() {
        return configuration;
      }

      @Override
      public boolean supports(String s) {
        return "MOVIE_VERIFICATION".equalsIgnoreCase(s);
      }
    };
  }

  @Bean
  public IContextProviderPlugin sampleMovieVerificationContextProviderPlugin() {
    return new IContextProviderPlugin() {
      @Override
      public ProvidedContext provideContextFor(UUID uuid, OntModel ontModel, Map<String, Object> map) {
        String subClassName = ontModel.listClasses().toList().get(0).getLocalName();
        return new ProvidedContext(uuid, subClassName + "-Context");
      }

      @Override
      public boolean supports(String s) {
        return "MOVIE_VERIFICATION_CONTEXT".equalsIgnoreCase(s);
      }
    };
  }
}
