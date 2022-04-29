package at.kk.msc.hcov.core.service.templating;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import at.kk.msc.hcov.core.service.templating.impl.ThymeleafTemplatingService;
import at.kk.msc.hcov.core.service.templating.model.ResolvedVariablesWrapper;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import at.kk.msc.hcov.core.util.VerificationTaskMockData;
import at.kk.msc.hcov.core.util.VerificationTaskPluginMock;
import at.kk.msc.hcov.core.util.VerificationTaskSpecificationMockData;
import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin;
import at.kk.msc.hcov.sdk.verificationtask.model.ProvidedContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.jena.ontology.OntModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;

public class ThymeleafTemplatingServiceTest {


  ITemplatingService target;

  @BeforeEach
  void setUp() {
    ITemplateEngine templateEngine = new TemplateEngine();
    target = new ThymeleafTemplatingService(templateEngine);
  }

  @Test
  void testPopulateTemplate_withContext() throws PluginConfigurationNotSetException {
    // given - for more information see VerificationTaskPluginMock
    Map<UUID, OntModel> givenExtractedModelElements = VerificationTaskMockData.MOCKED_EXTRACTED_MODEL_ELEMENTS();
    Map<UUID, ProvidedContext> givenProvidedContexts = VerificationTaskMockData.MOCKED_PROVIDED_CONTEXTS();
    VerificationTaskSpecification givenTaskSpecification = VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_TASK_SPECIFICATION();
    IVerificationTaskPlugin givenVerificationTaskPlugin = new VerificationTaskPluginMock(true);


    // when
    Map<UUID, String> actual = target.populateTemplates(
        givenExtractedModelElements, givenProvidedContexts, givenVerificationTaskPlugin, givenTaskSpecification
    );

    // then
    assertThat(actual).containsAllEntriesOf(VerificationTaskMockData.EXPECTED_TEMPLATES_WITH_CONTEXT());
  }

  @Test
  void testPopulateTemplate_withoutContext() throws PluginConfigurationNotSetException {
    // given - for more information see VerificationTaskPluginMock
    Map<UUID, OntModel> givenExtractedModelElements = VerificationTaskMockData.MOCKED_EXTRACTED_MODEL_ELEMENTS();
    Map<UUID, ProvidedContext> givenProvidedContexts = new HashMap<>();
    VerificationTaskSpecification givenTaskSpecification = VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_TASK_SPECIFICATION();
    IVerificationTaskPlugin givenVerificationTaskPlugin = new VerificationTaskPluginMock(false);


    // when
    Map<UUID, String> actual = target.populateTemplates(
        givenExtractedModelElements, givenProvidedContexts, givenVerificationTaskPlugin, givenTaskSpecification
    );

    // then
    assertThat(actual).containsAllEntriesOf(VerificationTaskMockData.EXPECTED_TEMPLATES_WITHOUT_CONTEXT());
  }

  @Test
  void testPopulateTemplate_faultyContextMap_expectException() {
    // given - see VerificationTaskPluginMock
    Map<UUID, OntModel> givenExtractedModelElements = VerificationTaskMockData.MOCKED_EXTRACTED_MODEL_ELEMENTS();
    Map<UUID, ProvidedContext> givenProvidedContexts = VerificationTaskMockData.MOCKED_PROVIDED_CONTEXTS();
    givenProvidedContexts.remove(VerificationTaskMockData.FIRST_MOCK_UUID);
    VerificationTaskSpecification givenTaskSpecification = VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_TASK_SPECIFICATION();
    IVerificationTaskPlugin givenVerificationTaskPlugin = new VerificationTaskPluginMock(false);

    // when - then
    assertThatThrownBy(
        () -> target.populateTemplates(
            givenExtractedModelElements, givenProvidedContexts, givenVerificationTaskPlugin, givenTaskSpecification
        )
    ).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void testPopulateTemplatesWithResolvedVariables() throws PluginConfigurationNotSetException {
    // given - for more information see VerificationTaskPluginMock
    List<ResolvedVariablesWrapper> givenResolvedVariables = VerificationTaskMockData.MOCKED_RESOLVED_VARIABLE_WRAPPERS_WITHOUT_CONTEXT();
    IVerificationTaskPlugin givenVerificationTaskPlugin = new VerificationTaskPluginMock(false);

    // when
    Map<UUID, String> actual = target.populateTemplatesWithResolvedVariables(
        givenResolvedVariables,
        givenVerificationTaskPlugin
    );

    // then
    assertThat(actual).containsAllEntriesOf(VerificationTaskMockData.EXPECTED_TEMPLATES_WITHOUT_CONTEXT());
  }

}
