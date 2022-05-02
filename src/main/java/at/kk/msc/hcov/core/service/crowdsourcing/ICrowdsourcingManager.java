package at.kk.msc.hcov.core.service.crowdsourcing;

import at.kk.msc.hcov.core.service.crowdsourcing.exception.CrowdsourcingManagerException;
import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.PublishedTask;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.List;

/**
 * Interfaces specifying operations to be supported for connections to crowdsourcing platforms.
 */
public interface ICrowdsourcingManager {

  /**
   * Publishes the given set of verification tasks according to the given specification on a crowdsourcing platform and stores the metadata
   * using a {@link at.kk.msc.hcov.core.persistence.metadata.ICrowdsourcingMetadataStore}.
   *
   * @param verificationTasks a List of {@link VerificationTask} objects (=HITs) to be published.
   * @param specification     @apiNote {@link VerificationTaskSpecification} specifying the configuration for the crowdsourcing tasks.
   * @return a list of the {@link PublishedTask} objects
   * @throws PluginLoadingError            if loading a required plugins failed
   * @throws CrowdsourcingManagerException if creation of the quality control tasks failed.
   */
  List<PublishedTask> publishCrowdsourcingTasks(
      List<VerificationTask> verificationTasks, VerificationTaskSpecification specification
  ) throws PluginLoadingError, CrowdsourcingManagerException;
}
