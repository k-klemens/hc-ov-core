package at.kk.msc.hcov.core.service.templating;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import at.kk.msc.hcov.core.service.templating.impl.ThymeleafTemplatingService;
import at.kk.msc.hcov.core.util.VerificationTaskPluginMock;
import at.kk.msc.hcov.core.util.VerificationTaskPluginMockData;
import at.kk.msc.hcov.core.util.VerificationTaskSpecificationMockData;
import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin;
import at.kk.msc.hcov.sdk.verificationtask.model.ProvidedContext;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTaskSpecification;
import java.util.HashMap;
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
    Map<UUID, OntModel> givenExtractedModelElements = VerificationTaskPluginMockData.MOCKED_EXTRACTED_MODEL_ELEMENTS;
    Map<UUID, ProvidedContext> givenProvidedContexts = VerificationTaskPluginMockData.MOCKED_PROVIDED_CONTEXTS;
    VerificationTaskSpecification givenTaskSpecification = VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_TASK_SPECIFICATION;
    IVerificationTaskPlugin givenVerificationTaskPlugin = new VerificationTaskPluginMock(true);


    // when
    Map<UUID, String> actual = target.populateTemplates(
        givenExtractedModelElements, givenProvidedContexts, givenVerificationTaskPlugin, givenTaskSpecification
    );

    // then
    assertThat(actual).containsAllEntriesOf(VerificationTaskPluginMockData.EXPECTED_WITH_CONTEXT);
  }

  @Test
  void testPopulateTemplate_withoutContext() throws PluginConfigurationNotSetException {
    // given - for more information see VerificationTaskPluginMock
    Map<UUID, OntModel> givenExtractedModelElements = VerificationTaskPluginMockData.MOCKED_EXTRACTED_MODEL_ELEMENTS;
    Map<UUID, ProvidedContext> givenProvidedContexts = new HashMap<>();
    VerificationTaskSpecification givenTaskSpecification = VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_TASK_SPECIFICATION;
    IVerificationTaskPlugin givenVerificationTaskPlugin = new VerificationTaskPluginMock(false);


    // when
    Map<UUID, String> actual = target.populateTemplates(
        givenExtractedModelElements, givenProvidedContexts, givenVerificationTaskPlugin, givenTaskSpecification
    );

    // then
    assertThat(actual).containsAllEntriesOf(VerificationTaskPluginMockData.EXPECTED_WITHOUT_CONTEXT);
  }

  @Test
  void testPopulateTemplate_faultyContextMap_expectException() {
    // given - see VerificationTaskPluginMock
    Map<UUID, OntModel> givenExtractedModelElements = VerificationTaskPluginMockData.MOCKED_EXTRACTED_MODEL_ELEMENTS;
    Map<UUID, ProvidedContext> givenProvidedContexts = VerificationTaskPluginMockData.MOCKED_PROVIDED_CONTEXTS;
    givenProvidedContexts.remove(VerificationTaskPluginMockData.FIRST_MOCK_UUID);
    VerificationTaskSpecification givenTaskSpecification = VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_TASK_SPECIFICATION;
    IVerificationTaskPlugin givenVerificationTaskPlugin = new VerificationTaskPluginMock(false);

    // when - then
    assertThatThrownBy(
        () -> target.populateTemplates(
            givenExtractedModelElements, givenProvidedContexts, givenVerificationTaskPlugin, givenTaskSpecification
        )
    ).isInstanceOf(IllegalArgumentException.class);
  }

}
