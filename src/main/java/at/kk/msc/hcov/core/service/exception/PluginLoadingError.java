package at.kk.msc.hcov.core.service.exception;

import at.kk.msc.hcov.core.service.plugin.IPluginLoader;

public class PluginLoadingError extends Exception {

  public PluginLoadingError(IPluginLoader.PluginType pluginType, String pluginId) {
    super("Could not load plugin of type='" + pluginType + "' with id='" + pluginId +
        "'! Make sure the plugin ID is spelled correctly and the platform has the plugin installed."
    );
  }
}
