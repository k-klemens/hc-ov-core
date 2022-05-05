package at.kk.msc.hcov.core;

import at.kk.msc.hcov.sdk.crowdsourcing.platform.ICrowdsourcingConnectorPlugin;
import at.kk.msc.hcov.sdk.crowdsourcing.processing.IProcessorPlugin;
import at.kk.msc.hcov.sdk.verificationtask.IContextProviderPlugin;
import at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.plugin.core.config.EnablePluginRegistries;

@SpringBootApplication(scanBasePackages = {"at.kk.msc.hcov.core", "at.kk.msc.hcov.plugin"})
@EnablePluginRegistries(
    {IVerificationTaskPlugin.class, IContextProviderPlugin.class, ICrowdsourcingConnectorPlugin.class, IProcessorPlugin.class}
)
public class PlatformCore {

  public static void main(String[] args) {
    SpringApplication.run(PlatformCore.class, args);
  }

}
