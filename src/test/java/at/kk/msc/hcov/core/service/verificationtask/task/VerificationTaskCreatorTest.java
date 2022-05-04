package at.kk.msc.hcov.core.service.verificationtask.task;

import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskMockData.EXPECTED_TASKS_WITHOUT_CONTEXT;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskMockData.EXPECTED_TASKS_WITH_CONTEXT;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskMockData.EXPECTED_TEMPLATES_WITHOUT_CONTEXT;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskMockData.EXPECTED_TEMPLATES_WITH_CONTEXT;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskMockData.MOCKED_EXTRACTED_MODEL_ELEMENTS;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskMockData.MOCKED_PROVIDED_CONTEXTS;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskMockData.MOCKED_RESOLVED_VARIABLE_WRAPPERS_WITHOUT_CONTEXT;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_TASK_SPECIFICATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.kk.msc.hcov.core.persistence.triples.exception.OntologyNotFoundException;
import at.kk.msc.hcov.core.service.crowdsourcing.ICrowdsourcingManager;
import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.ontology.data.IDataProvider;
import at.kk.msc.hcov.core.service.plugin.IPluginLoader;
import at.kk.msc.hcov.core.service.templating.ITemplatingService;
import at.kk.msc.hcov.core.service.templating.model.ResolvedVariablesWrapper;
import at.kk.msc.hcov.core.service.verificationtask.task.exception.VerificationTaskCreationFailedException;
import at.kk.msc.hcov.core.service.verificationtask.task.impl.VerificationTaskCreator;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import at.kk.msc.hcov.core.util.ContextProviderPluginMock;
import at.kk.msc.hcov.core.util.VerificationTaskPluginMock;
import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import at.kk.msc.hcov.sdk.verificationtask.IContextProviderPlugin;
import at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.plugin.core.Plugin;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class VerificationTaskCreatorTest {

  @Mock
  IDataProvider dataProviderMock;

  @Mock
  ITemplatingService templatingServiceMock;

  @Mock
  IPluginLoader<Plugin<String>> pluginLoaderMock;

  @Mock
  ICrowdsourcingManager crowdsourcingManagerMock;

  IVerificationTaskPlugin verificationTaskPluginMock;
  IContextProviderPlugin contextProviderPluginMock;

  IVerificationTaskCreator target;

  @BeforeEach
  void setUp() throws PluginLoadingError, PluginConfigurationNotSetException, OntologyNotFoundException, IOException {
    verificationTaskPluginMock = new VerificationTaskPluginMock(true);
    contextProviderPluginMock = new ContextProviderPluginMock();

    target = new VerificationTaskCreator(dataProviderMock, templatingServiceMock, pluginLoaderMock);

    when(pluginLoaderMock.loadPluginOrThrow(eq(IPluginLoader.PluginType.VERIFICATION_TASK_CREATOR), eq("VERIFICATION_MOCK")))
        .thenReturn(verificationTaskPluginMock);
    when(pluginLoaderMock.loadPluginOrThrow(eq(IPluginLoader.PluginType.CONTEXT_PROVIDER), eq("CONTEXT_MOCK")))
        .thenReturn(contextProviderPluginMock);
  }

  @AfterEach
  void tearDown() {
    Mockito.reset(dataProviderMock, templatingServiceMock, pluginLoaderMock);
  }

  @Test
  public void testCreateTasks_withContext()
      throws VerificationTaskCreationFailedException, PluginLoadingError, PluginConfigurationNotSetException, OntologyNotFoundException,
      IOException {
    // given
    VerificationTaskSpecification givenVerificationTaskSpecification = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    when(templatingServiceMock.populateTemplates(
        eq(MOCKED_EXTRACTED_MODEL_ELEMENTS()),
        eq(MOCKED_PROVIDED_CONTEXTS()),
        eq(verificationTaskPluginMock),
        eq(givenVerificationTaskSpecification)
    )).thenReturn(EXPECTED_TEMPLATES_WITH_CONTEXT());

    when(dataProviderMock.extractAndStoreRequiredOntologyElements(
            eq(MOCKED_VERIFICATION_TASK_SPECIFICATION().getOntologyName()),
            eq(MOCKED_VERIFICATION_TASK_SPECIFICATION().getVerificationName()),
            eq(verificationTaskPluginMock.getElementExtractor())
        )
    ).thenReturn(MOCKED_EXTRACTED_MODEL_ELEMENTS());


    // when
    List<VerificationTask> actual = target.createTasks(givenVerificationTaskSpecification);

    // then
    verify(pluginLoaderMock, times(1))
        .loadPluginOrThrow(eq(IPluginLoader.PluginType.VERIFICATION_TASK_CREATOR), eq("VERIFICATION_MOCK"));
    verify(pluginLoaderMock, times(1))
        .loadPluginOrThrow(eq(IPluginLoader.PluginType.CONTEXT_PROVIDER), eq("CONTEXT_MOCK"));
    verify(dataProviderMock, times(1))
        .extractAndStoreRequiredOntologyElements(
            eq(givenVerificationTaskSpecification.getOntologyName()),
            eq(givenVerificationTaskSpecification.getVerificationName()),
            eq(verificationTaskPluginMock.getElementExtractor())
        );
    assertThat(((ContextProviderPluginMock) contextProviderPluginMock).gotCalled()).isTrue();
    assertThat(actual).containsExactlyInAnyOrderElementsOf(EXPECTED_TASKS_WITH_CONTEXT());
  }

  @ParameterizedTest
  @NullAndEmptySource
  public void testCreateTasks_withoutContext(String givenContextProvider)
      throws VerificationTaskCreationFailedException, PluginLoadingError, PluginConfigurationNotSetException, OntologyNotFoundException,
      IOException {
    // given
    VerificationTaskSpecification givenVerificationTaskSpecification = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    givenVerificationTaskSpecification.setContextProviderPluginId(givenContextProvider);
    givenVerificationTaskSpecification.setContextProviderConfiguration(null);
    when(templatingServiceMock.populateTemplates(
        eq(MOCKED_EXTRACTED_MODEL_ELEMENTS()),
        anyMap(),
        eq(verificationTaskPluginMock),
        eq(givenVerificationTaskSpecification)
    ))
        .thenReturn(EXPECTED_TEMPLATES_WITHOUT_CONTEXT());
    ((VerificationTaskPluginMock) verificationTaskPluginMock).setWithContext(false);


    when(dataProviderMock.extractAndStoreRequiredOntologyElements(
            eq(MOCKED_VERIFICATION_TASK_SPECIFICATION().getOntologyName()),
            eq(MOCKED_VERIFICATION_TASK_SPECIFICATION().getVerificationName()),
            eq(verificationTaskPluginMock.getElementExtractor())
        )
    ).thenReturn(MOCKED_EXTRACTED_MODEL_ELEMENTS());

    // when
    List<VerificationTask> actual = target.createTasks(givenVerificationTaskSpecification);

    // then
    verify(pluginLoaderMock, times(1))
        .loadPluginOrThrow(eq(IPluginLoader.PluginType.VERIFICATION_TASK_CREATOR), eq("VERIFICATION_MOCK"));
    verify(pluginLoaderMock, never())
        .loadPluginOrThrow(eq(IPluginLoader.PluginType.CONTEXT_PROVIDER), any());
    verify(dataProviderMock, times(1))
        .extractAndStoreRequiredOntologyElements(
            eq(givenVerificationTaskSpecification.getOntologyName()),
            eq(givenVerificationTaskSpecification.getVerificationName()),
            eq(verificationTaskPluginMock.getElementExtractor())
        );
    assertThat(((ContextProviderPluginMock) contextProviderPluginMock).gotCalled()).isFalse();
    assertThat(actual).containsExactlyInAnyOrderElementsOf(EXPECTED_TASKS_WITHOUT_CONTEXT());
  }

  @Test
  public void testCreateTasks_givenVerificationPluginNotFound_expectException()
      throws PluginLoadingError, OntologyNotFoundException, IOException {
    // given
    when(pluginLoaderMock.loadPluginOrThrow(eq(IPluginLoader.PluginType.VERIFICATION_TASK_CREATOR), eq("NOT_EXISTING"))).
        thenThrow(new PluginLoadingError(IPluginLoader.PluginType.VERIFICATION_TASK_CREATOR, "NOT_EXISTING"));
    VerificationTaskSpecification givenVerificationTaskSpecification = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    givenVerificationTaskSpecification.setVerificationTaskPluginId("NOT_EXISTING");

    // when - then
    assertThatThrownBy(() -> target.createTasks(givenVerificationTaskSpecification))
        .isInstanceOf(PluginLoadingError.class);


    // then
    verify(pluginLoaderMock, times(1))
        .loadPluginOrThrow(eq(IPluginLoader.PluginType.VERIFICATION_TASK_CREATOR), eq("NOT_EXISTING"));
    verify(pluginLoaderMock, never()).loadPluginOrThrow(eq(IPluginLoader.PluginType.CONTEXT_PROVIDER), any());
    verify(dataProviderMock, never()).extractAndStoreRequiredOntologyElements(any(), any(), any());
    assertThat(((ContextProviderPluginMock) contextProviderPluginMock).gotCalled()).isFalse();
  }

  @Test
  public void testCreateTasks_givenContextProviderPluginNotFound_expectException()
      throws PluginLoadingError, OntologyNotFoundException, IOException, PluginConfigurationNotSetException {
    // given
    when(pluginLoaderMock.loadPluginOrThrow(eq(IPluginLoader.PluginType.CONTEXT_PROVIDER), eq("NOT_EXISTING"))).
        thenThrow(new PluginLoadingError(IPluginLoader.PluginType.CONTEXT_PROVIDER, "NOT_EXISTING"));
    VerificationTaskSpecification givenVerificationTaskSpecification = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    givenVerificationTaskSpecification.setContextProviderPluginId("NOT_EXISTING");

    when(dataProviderMock.extractAndStoreRequiredOntologyElements(
            eq(MOCKED_VERIFICATION_TASK_SPECIFICATION().getOntologyName()),
            eq(MOCKED_VERIFICATION_TASK_SPECIFICATION().getVerificationName()),
            eq(verificationTaskPluginMock.getElementExtractor())
        )
    ).thenReturn(MOCKED_EXTRACTED_MODEL_ELEMENTS());

    // when - then
    assertThatThrownBy(() -> target.createTasks(givenVerificationTaskSpecification))
        .isInstanceOf(PluginLoadingError.class);


    // then
    verify(pluginLoaderMock, times(1))
        .loadPluginOrThrow(eq(IPluginLoader.PluginType.VERIFICATION_TASK_CREATOR), eq("VERIFICATION_MOCK"));
    verify(pluginLoaderMock, times(1)).loadPluginOrThrow(eq(IPluginLoader.PluginType.CONTEXT_PROVIDER), eq("NOT_EXISTING"));
    verify(dataProviderMock, times(1))
        .extractAndStoreRequiredOntologyElements(
            eq(givenVerificationTaskSpecification.getOntologyName()),
            eq(givenVerificationTaskSpecification.getVerificationName()),
            eq(verificationTaskPluginMock.getElementExtractor())
        );
    assertThat(((ContextProviderPluginMock) contextProviderPluginMock).gotCalled()).isFalse();
  }

  @ParameterizedTest
  @MethodSource("provideDataProviderExceptions")
  public void testCreateTasks_givenDataProviderThrowsException_expectVerificationTaskCreationFailedException(Exception givenException)
      throws PluginConfigurationNotSetException, OntologyNotFoundException, IOException {
    // given
    VerificationTaskSpecification givenSpecification = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    when(dataProviderMock.extractAndStoreRequiredOntologyElements(
            eq(MOCKED_VERIFICATION_TASK_SPECIFICATION().getOntologyName()),
            eq(MOCKED_VERIFICATION_TASK_SPECIFICATION().getVerificationName()),
            eq(verificationTaskPluginMock.getElementExtractor())
        )
    ).thenThrow(givenException);

    // when - then
    assertThatThrownBy(() -> target.createTasks(givenSpecification))
        .isInstanceOf(VerificationTaskCreationFailedException.class);
  }

  @Test
  public void testCreateTasks_givenTemplatingServiceThrows_expectVerifciationTaskCreationFailedException()
      throws PluginConfigurationNotSetException, OntologyNotFoundException, IOException {
    // given
    VerificationTaskSpecification givenVerificationTaskSpecification = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    when(templatingServiceMock.populateTemplates(
        eq(MOCKED_EXTRACTED_MODEL_ELEMENTS()),
        eq(MOCKED_PROVIDED_CONTEXTS()),
        eq(verificationTaskPluginMock),
        eq(givenVerificationTaskSpecification)
    )).thenThrow(new PluginConfigurationNotSetException());

    when(dataProviderMock.extractAndStoreRequiredOntologyElements(
            eq(MOCKED_VERIFICATION_TASK_SPECIFICATION().getOntologyName()),
            eq(MOCKED_VERIFICATION_TASK_SPECIFICATION().getVerificationName()),
            eq(verificationTaskPluginMock.getElementExtractor())
        )
    ).thenReturn(MOCKED_EXTRACTED_MODEL_ELEMENTS());

    // when - then
    assertThatThrownBy(() -> target.createTasks(givenVerificationTaskSpecification))
        .isInstanceOf(VerificationTaskCreationFailedException.class);
  }

  @Test
  void testCreateTasksWithResolvedVariables()
      throws PluginConfigurationNotSetException, VerificationTaskCreationFailedException, PluginLoadingError, OntologyNotFoundException,
      IOException {
    // given
    List<ResolvedVariablesWrapper> givenVariables = MOCKED_RESOLVED_VARIABLE_WRAPPERS_WITHOUT_CONTEXT();
    VerificationTaskSpecification givenVerificationTaskSpecification = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    givenVerificationTaskSpecification.setContextProviderPluginId(null);
    givenVerificationTaskSpecification.setContextProviderConfiguration(null);
    when(templatingServiceMock.populateTemplatesWithResolvedVariables(
        eq(givenVariables),
        eq(verificationTaskPluginMock)
    ))
        .thenReturn(EXPECTED_TEMPLATES_WITHOUT_CONTEXT());
    ((VerificationTaskPluginMock) verificationTaskPluginMock).setWithContext(false);

    // when
    List<VerificationTask> actual = target.createTasksWithResolvedVariables(
        givenVerificationTaskSpecification, givenVariables
    );

    // then
    verify(pluginLoaderMock, times(1)).loadPluginOrThrow(eq(IPluginLoader.PluginType.VERIFICATION_TASK_CREATOR), eq("VERIFICATION_MOCK"));
    verify(pluginLoaderMock, never()).loadPluginOrThrow(eq(IPluginLoader.PluginType.CONTEXT_PROVIDER), any());
    verify(dataProviderMock, never()).extractAndStoreRequiredOntologyElements(any(), any(), any());
    assertThat(((ContextProviderPluginMock) contextProviderPluginMock).gotCalled()).isFalse();
    assertThat(actual).containsExactlyInAnyOrderElementsOf(EXPECTED_TASKS_WITHOUT_CONTEXT());
  }

  private static Stream<Arguments> provideDataProviderExceptions() {
    return Stream.of(
        Arguments.of(new OntologyNotFoundException("TEST", null)),
        Arguments.of(new IOException())
    );
  }

}
