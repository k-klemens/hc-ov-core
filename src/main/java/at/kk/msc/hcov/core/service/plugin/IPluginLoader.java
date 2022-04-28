package at.kk.msc.hcov.core.service.plugin;

import at.kk.msc.hcov.core.service.exception.PluginLoadingError;

/**
 * Interface to specify the how plugins can be loaded.
 *
 * @param <P> class describing the implementaiton mechanism of the plugin (e.g. by Spring using {@link org.springframework.plugin.core.Plugin})
 */
public interface IPluginLoader<P> {

  /**
   * Load a plugin if available or throws exception.
   *
   * @param type     the type of the plugin to load
   * @param pluginId the id of the plugin to load
   * @return the plugin if available
   * @throws PluginLoadingError if something went wrong when loading the plugin
   */
  P loadPluginOrThrow(PluginType type, String pluginId) throws PluginLoadingError;

  enum PluginType {
    VERIFICATION_TASK_CREATOR, CONTEXT_PROVIDER, CROWDSOURCING_CONNECTOR, PROCESSOR
  }
}
