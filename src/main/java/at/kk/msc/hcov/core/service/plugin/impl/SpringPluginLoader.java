package at.kk.msc.hcov.core.service.plugin.impl;

import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.plugin.IPluginLoader;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.ICrowdsourcingConnectorPlugin;
import at.kk.msc.hcov.sdk.crowdsourcing.processing.IProcessorPlugin;
import at.kk.msc.hcov.sdk.verificationtask.IContextProviderPlugin;
import at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import org.springframework.plugin.core.Plugin;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class SpringPluginLoader implements IPluginLoader<Plugin<String>> {

  private PluginRegistry<IVerificationTaskPlugin, String> verificationTaskPluginRegistry;
  private PluginRegistry<IContextProviderPlugin, String> contextProviderPluginRegistry;
  private PluginRegistry<ICrowdsourcingConnectorPlugin, String> crowdsourcingConnectorPluginRegistry;
  private PluginRegistry<IProcessorPlugin, String> processorPluginRegistry;


  @Override
  public Plugin<String> loadPluginOrThrow(IPluginLoader.PluginType type, String pluginId) throws PluginLoadingError {
    Supplier<PluginLoadingError> exceptionSupplier = () -> new PluginLoadingError(type, pluginId);
    return switch (type) {
      case VERIFICATION_TASK_CREATOR -> verificationTaskPluginRegistry.getPluginFor(pluginId, exceptionSupplier);
      case CONTEXT_PROVIDER -> contextProviderPluginRegistry.getPluginFor(pluginId, exceptionSupplier);
      case CROWDSOURCING_CONNECTOR -> crowdsourcingConnectorPluginRegistry.getPluginFor(pluginId, exceptionSupplier);
      case PROCESSOR -> processorPluginRegistry.getPluginFor(pluginId, exceptionSupplier);
    };
  }

}
