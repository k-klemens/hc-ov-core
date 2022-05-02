package at.kk.msc.hcov.core.persistence.metadata;

import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.PublishedTask;
import java.util.List;

/**
 * Interface specifying operations to be supported for storing metadata of a published verification.
 */
public interface ICrowdsourcingMetadataStore {

  /**
   * Saves the metadata of a verification. Including general setup of the verification and a mapping between internally used IDs and
   * IDs provided by the crowdsourcing platform for the tasks. Populated question / task / hit templates are not persisted due to space
   * considerations.
   *
   * @param verificationTaskSpecification specification of the published verification tasks.
   * @param publishedTasks                list of tasks published on the crowdsourcing platform.
   */
  void saveMetaData(VerificationTaskSpecification verificationTaskSpecification, List<PublishedTask> publishedTasks);


}
