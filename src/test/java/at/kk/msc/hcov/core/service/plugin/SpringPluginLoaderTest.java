package at.kk.msc.hcov.core.service.plugin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.plugin.impl.SpringPluginLoader;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.ICrowdsourcingConnectorPlugin;
import at.kk.msc.hcov.sdk.crowdsourcing.processing.IProcessorPlugin;
import at.kk.msc.hcov.sdk.verificationtask.IContextProviderPlugin;
import at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.plugin.core.Plugin;
import org.springframework.plugin.core.PluginRegistry;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SpringPluginLoaderTest {

  @Mock
  PluginRegistry<IVerificationTaskPlugin, String> verificationTaskPluginRepoMock;

  @Mock
  IVerificationTaskPlugin verificationTaskPluginMock;

  @Mock
  PluginRegistry<IContextProviderPlugin, String> contextProviderPluginRepoMock;

  @Mock
  IContextProviderPlugin contextProviderPluginMock;

  @Mock
  PluginRegistry<ICrowdsourcingConnectorPlugin, String> crowdsourcingConnectorPluginRepoMock;

  @Mock
  ICrowdsourcingConnectorPlugin crowdsourcingConnectorPluginMock;

  @Mock
  PluginRegistry<IProcessorPlugin, String> processorPluginRepoMock;

  @Mock
  IProcessorPlugin processorPluginMock;

  IPluginLoader<Plugin<String>> target;

  @BeforeEach
  void setUp() {
    when(verificationTaskPluginRepoMock.getPluginFor(eq("VERIFICATION"), any())).thenReturn(verificationTaskPluginMock);
    when(contextProviderPluginRepoMock.getPluginFor(eq("CONTEXT"), any())).thenReturn(contextProviderPluginMock);
    when(crowdsourcingConnectorPluginRepoMock.getPluginFor(eq("CROWDSOURCING"), any())).thenReturn(crowdsourcingConnectorPluginMock);
    when(processorPluginRepoMock.getPluginFor(eq("PROCESSOR"), any())).thenReturn(processorPluginMock);
    target = new SpringPluginLoader(
        verificationTaskPluginRepoMock, contextProviderPluginRepoMock, crowdsourcingConnectorPluginRepoMock, processorPluginRepoMock
    );
  }

  @ParameterizedTest
  @MethodSource("provideTestLoadPluginOrThrowData")
  public void testLoadPluginOrThrow(IPluginLoader.PluginType givenType, String givenId, Class expectedInstance) throws PluginLoadingError {
    // given - by methodsource

    // when
    Plugin<String> actual = target.loadPluginOrThrow(givenType, givenId);

    // then
    assertThat(actual)
        .isNotNull()
        .isInstanceOf(expectedInstance);
  }

  @Test
  public void testLoadPluginOrThrow_givenFaultyConfig_expectError() {
    // given - by methodsource
    when(verificationTaskPluginRepoMock.getPluginFor(eq("CONTEXT"), any()))
        .thenThrow(new PluginLoadingError(IPluginLoader.PluginType.VERIFICATION_TASK_CREATOR, "CONTEXT"));

    // when - then
    assertThatThrownBy(() -> target.loadPluginOrThrow(IPluginLoader.PluginType.VERIFICATION_TASK_CREATOR, "CONTEXT"))
        .isInstanceOf(PluginLoadingError.class);
  }

  private static Stream<Arguments> provideTestLoadPluginOrThrowData() {
    return Stream.of(
        Arguments.of(IPluginLoader.PluginType.VERIFICATION_TASK_CREATOR, "VERIFICATION", IVerificationTaskPlugin.class),
        Arguments.of(IPluginLoader.PluginType.CONTEXT_PROVIDER, "CONTEXT", IContextProviderPlugin.class),
        Arguments.of(IPluginLoader.PluginType.CROWDSOURCING_CONNECTOR, "CROWDSOURCING", ICrowdsourcingConnectorPlugin.class),
        Arguments.of(IPluginLoader.PluginType.PROCESSOR, "PROCESSOR", IProcessorPlugin.class)
    );
  }

}
