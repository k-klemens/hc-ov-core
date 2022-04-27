package at.kk.msc.hcov.core.service.verificationtask;

import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.verificationtask.exception.VerificationTaskCreationFailedException;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTaskSpecification;
import java.util.List;

public interface IVerificationTaskCreator {

  List<VerificationTask> createTasks(VerificationTaskSpecification specification)
      throws VerificationTaskCreationFailedException, PluginLoadingError;

}
