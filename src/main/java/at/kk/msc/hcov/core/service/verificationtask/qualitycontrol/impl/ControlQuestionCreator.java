package at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.impl;

import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.templating.model.ResolvedVariablesWrapper;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.IQualityControlProvider;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.exception.QualityControlTaskCreationFailedException;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model.QualityControlTask;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model.QualityControlTasksSpecification;
import at.kk.msc.hcov.core.service.verificationtask.task.IVerificationTaskCreator;
import at.kk.msc.hcov.core.service.verificationtask.task.exception.VerificationTaskCreationFailedException;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Component to create control questions. Typically, those are seeded in to the normal set of questions and the workers are not aware of them.
 * This implementation facilitates a {@link IVerificationTaskCreator} and a {@link at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin} to create control questions with the same template
 * as a given verification task.
 */
@AllArgsConstructor
@Component
public class ControlQuestionCreator implements IQualityControlProvider {

  private IVerificationTaskCreator verificationTaskCreator;

  @Override
  public List<QualityControlTask> createQualityControlTasks(
      VerificationTaskSpecification verificationTaskSpecification
  ) throws QualityControlTaskCreationFailedException, PluginLoadingError {
    QualityControlTasksSpecification qualityControlTasksSpecification = verificationTaskSpecification.getQualityControlTasksSpecification();
    List<ResolvedVariablesWrapper> resolvedVariablesWrappers = qualityControlTasksSpecification.getQualityControlTasks().stream()
        .map(
            qualityControlTaskCreation -> new ResolvedVariablesWrapper(qualityControlTaskCreation.getQualityControlModelElementId(),
                qualityControlTaskCreation.getResolvedVariables())
        )
        .toList();

    List<VerificationTask> verificationTasks;
    try {
      verificationTasks =
          verificationTaskCreator.createTasksWithResolvedVariables(verificationTaskSpecification, resolvedVariablesWrappers);
    } catch (VerificationTaskCreationFailedException e) {
      throw new QualityControlTaskCreationFailedException(e);
    }

    return toQualityControlTasks(verificationTasks, qualityControlTasksSpecification);
  }

  private List<QualityControlTask> toQualityControlTasks(
      List<VerificationTask> verificationTasks, QualityControlTasksSpecification qualityControlTasksSpecification
  ) {
    return verificationTasks.stream().map(
        verificationTask -> new QualityControlTask(
            verificationTask.getVerificationName(),
            verificationTask.getOntologyName(),
            verificationTask.getOntologyElementId(),
            verificationTask.getTaskHtml(),
            qualityControlTasksSpecification.getAnswerFor(verificationTask.getOntologyElementId())
        )
    ).toList();
  }

}
