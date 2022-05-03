package at.kk.msc.hcov.core.service.crowdsourcing;

import at.kk.msc.hcov.core.persistence.metadata.exception.VerificationDoesNotExistException;
import at.kk.msc.hcov.core.service.crowdsourcing.exception.CrowdsourcingManagerException;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedVerification;
import at.kk.msc.hcov.core.service.crowdsourcing.model.VerificationProgress;
import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;

/**
 * Interfaces specifying operations to be supported for connections to crowdsourcing platforms.
 */
public interface ICrowdsourcingManager {

  /**
   * Creates a set of verification tasks using a {@link at.kk.msc.hcov.core.service.verificationtask.task.IVerificationTaskCreator and
   * publishes the given set of verification tasks according to the given specification on a crowdsourcing platform and stores the metadata
   * using a {@link at.kk.msc.hcov.core.persistence.metadata.ICrowdsourcingMetadataStore}.
   * Depending on the specification this also includes creating quality control questions using a IQualityControlgProvider.
   *
   * @param specification {@link VerificationTaskSpecification} specifying the configuration for the crowdsourcing tasks.
   * @return a PublishedVerification object describing the verification as well as the tasks created.
   * @throws PluginLoadingError            if loading a required plugins failed
   * @throws CrowdsourcingManagerException if creation of the quality control tasks failed.
   */
  PublishedVerification createAndPublishVerification(VerificationTaskSpecification specification)
      throws PluginLoadingError, CrowdsourcingManagerException;

  /**
   * Uses a {@link at.kk.msc.hcov.sdk.crowdsourcing.platform.ICrowdsourcingConnectorPlugin} and the {@link at.kk.msc.hcov.core.persistence.metadata.ICrowdsourcingMetadataStore}
   * to obtain the status / progress of a published verification.
   *
   * @param verificationName name of the verification to obtain the status.
   * @return a {@link VerificationProgress} object describing the status of the verification
   * @throws if the request cannot be processed.
   */
  VerificationProgress getStatusOfVerification(String verificationName)
      throws CrowdsourcingManagerException, PluginLoadingError, VerificationDoesNotExistException;

}
