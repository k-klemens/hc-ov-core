package at.kk.msc.hcov.core.service.verificationtask.task;

import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.templating.model.ResolvedVariablesWrapper;
import at.kk.msc.hcov.core.service.verificationtask.task.exception.VerificationTaskCreationFailedException;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
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

  /**
   * Creates a set of {@link VerificationTask} object according to a given specification with given resolved template variables.
   * This method does not call any element extractors or context providers
   *
   * @param specification     of the verification.
   * @param resolvedVariables list of values for the templating variables.
   * @return a list of {@link VerificationTask} objects containing the verification tasks.
   * @throws VerificationTaskCreationFailedException if something went wrong during creation of the verification tasks.
   * @throws PluginLoadingError                      if a specified plugin could not be loaded.
   */
  List<VerificationTask> createTasksWithResolvedVariables(
      VerificationTaskSpecification specification,
      List<ResolvedVariablesWrapper> resolvedVariables
  )
      throws VerificationTaskCreationFailedException, PluginLoadingError;


}
