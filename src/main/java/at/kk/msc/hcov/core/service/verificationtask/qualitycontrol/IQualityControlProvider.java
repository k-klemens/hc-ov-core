package at.kk.msc.hcov.core.service.verificationtask.qualitycontrol;

import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.exception.QualityControlTaskCreationFailedException;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model.QualityControlTask;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import java.util.List;

public interface IQualityControlProvider {
  /**
   * Create a set of {@link QualityControlTask} objects from a given specification.
   *
   * @param verificationTaskSpecification of the verification.
   * @return a list of {@link QualityControlTask}
   * @throws QualityControlTaskCreationFailedException if something went wrong during creation of the quality control tasks.
   * @throws PluginLoadingError                        if a specified plugin could not be loaded.
   */
  List<QualityControlTask> createQualityControlTasks(
      VerificationTaskSpecification verificationTaskSpecification
  ) throws QualityControlTaskCreationFailedException, PluginLoadingError;

}
