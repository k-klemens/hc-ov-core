package at.kk.msc.hcov.core.util;

import at.kk.msc.hcov.sdk.crowdsourcing.platform.ICrowdsourcingConnectorPlugin;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.HitStatus;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.RawResult;
import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import at.kk.msc.hcov.sdk.verificationtask.IContextProviderPlugin;
import at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin;
import at.kk.msc.hcov.sdk.verificationtask.model.ProvidedContext;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.apache.jena.ontology.OntModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  @Bean
  public ICrowdsourcingConnectorPlugin sampleCrowdsourcingConnectorPlugin() {
    return new ICrowdsourcingConnectorPlugin() {
      private static final Logger LOGGER = LoggerFactory.getLogger("sampleCrowdsourcingConnectorPlugin");
      private Map<String, Object> configuration;

      @Override
      public Map<UUID, String> publishTasks(List<VerificationTask> list) throws PluginConfigurationNotSetException {
        validateConfigurationSetOrThrow();

        Map<UUID, String> returnMap = new HashMap<>();
        for (VerificationTask task : list) {
          if (task.getTaskHtml().contains("<span>Writer</span>")) {
            returnMap.put(task.getOntologyElementId(), "WriterExternalId");
          } else if (task.getTaskHtml().contains("<span>Person</span>")) {
            returnMap.put(task.getOntologyElementId(), "PersonExternalId");
          } else if (task.getTaskHtml().contains("<span>MovieDirector</span>")) {
            returnMap.put(task.getOntologyElementId(), "MovieDirectorExternalId");
          } else if (task.getTaskHtml().contains("<span>Actor</span>")) {
            returnMap.put(task.getOntologyElementId(), "ActorExternalId");
          } else if (task.getTaskHtml().contains("<span>QualityControl</span>")) {
            returnMap.put(task.getOntologyElementId(), "QualityControlExternalId");
          }
          LOGGER.info("Published task for element id {}", task.getOntologyElementId());
        }
        return returnMap;
      }

      @Override
      public Map<String, HitStatus> getStatusForHits(List<String> list) throws PluginConfigurationNotSetException {
        return Map.of(
            "WriterExternalId", new HitStatus("WriterExternalId", 5, 5),
            "PersonExternalId", new HitStatus("PersonExternalId", 5, 5),
            "MovieDirectorExternalId", new HitStatus("MovieDirectorExternalId", 5, 5),
            "ActorExternalId", new HitStatus("ActorExternalId", 5, 5)
        );
      }

      @Override
      public Map<String, List<RawResult>> getResultsForHits(List<String> list) throws PluginConfigurationNotSetException {
        return Map.of(
            "WriterExternalId", List.of(
                new RawResult("WriterResultId1", "WriterExternalId", "W1", "A"),
                new RawResult("WriterResultId2", "WriterExternalId", "W2", "A"),
                new RawResult("WriterResultId3", "WriterExternalId", "W3", "B"),
                new RawResult("WriterResultId4", "WriterExternalId", "W4", "A"),
                new RawResult("WriterResultId5", "WriterExternalId", "W5", "A")
            ),
            "PersonExternalId", List.of(
                new RawResult("PersonResultId1", "PersonExternalId", "W1", "A"),
                new RawResult("PersonResultId2", "PersonExternalId", "W2", "B"),
                new RawResult("PersonResultId3", "PersonExternalId", "W3", "B"),
                new RawResult("PersonResultId4", "PersonExternalId", "W4", "B"),
                new RawResult("PersonResultId5", "PersonExternalId", "W5", "C")
            ),
            "MovieDirectorExternalId", List.of(
                new RawResult("MovieDirectorResultId", "MovieDirectorExternalId", "W1", "C"),
                new RawResult("MovieDirectorResultId", "MovieDirectorExternalId", "W2", "B"),
                new RawResult("MovieDirectorResultId", "MovieDirectorExternalId", "W3", "B"),
                new RawResult("MovieDirectorResultId", "MovieDirectorExternalId", "W4", "C"),
                new RawResult("MovieDirectorResultId", "MovieDirectorExternalId", "W5", "C")
            ),
            "ActorExternalId", List.of(
                new RawResult("ActorResultId", "ActorExternalId", "W1", "A"),
                new RawResult("ActorResultId", "ActorExternalId", "W2", "A"),
                new RawResult("ActorResultId", "ActorExternalId", "W3", "A"),
                new RawResult("ActorResultId", "ActorExternalId", "W4", "A"),
                new RawResult("ActorResultId", "ActorExternalId", "W5", "A")
            )
        );
      }

      @Override
      public void validateConfigurationSetOrThrow() throws PluginConfigurationNotSetException {
        ICrowdsourcingConnectorPlugin.super.validateConfigurationSetOrThrow();
        if (!configuration.containsKey("REQUIRED_CONFIGURATION")) {
          throw new PluginConfigurationNotSetException("Configuration with key REQUIRED_CONFIGURATION not set!");
        }
      }

      @Override
      public void setConfiguration(Map<String, Object> map) {
        configuration = map;
      }

      @Override
      public Map<String, Object> getConfiguration() {
        return configuration;
      }

      @Override
      public boolean supports(String s) {
        return "CROWDSOURCING_MOCK".equalsIgnoreCase(s);
      }
    };
  }
}
