package at.kk.msc.hcov.core.service.verificationtask;

import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.verificationtask.exception.VerificationTaskCreationFailedException;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTaskSpecification;
import java.util.List;

/**
 * Interface to create verification tasks.
 */
public interface IVerificationTaskCreator {

  /**
   * Creates a set of {@link VerificationTask} objects according to a given specification.
   *
   * @param specification of the verification.
   * @return a list of {@link VerificationTask} objects containing the verification tasks.
   * @throws VerificationTaskCreationFailedException if something went wrong during creation of the verification tasks.
   * @throws PluginLoadingError                      if a specified plugin could not be loaded.
   */
  List<VerificationTask> createTasks(VerificationTaskSpecification specification)
      throws VerificationTaskCreationFailedException, PluginLoadingError;


}
