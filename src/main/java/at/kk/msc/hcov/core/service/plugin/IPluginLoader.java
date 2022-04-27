package at.kk.msc.hcov.core.service.plugin;

import at.kk.msc.hcov.core.service.exception.PluginLoadingError;

public interface IPluginLoader<P> {

  P loadPluginOrThrow(PluginType type, String pluginId) throws PluginLoadingError;

  enum PluginType {
    VERIFICATION_TASK_CREATOR, CONTEXT_PROVIDER, CROWDSOURCING_CONNECTOR, PROCESSOR
  }
}
