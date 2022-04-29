package at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model;

import at.kk.msc.hcov.core.service.templating.model.ResolvedVariablesWrapper;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class representing on quality control task.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityControlTasksSpecification {

  /**
   * List of the quality control tasks to be created.
   */
  private List<QualityControlTaskCreation> qualityControlTasks;

  public String getAnswerFor(UUID qualityControlModelElementsId) {
    return qualityControlTasks.stream()
        .filter(taskCreation -> taskCreation.getQualityControlModelElementId().equals(qualityControlModelElementsId))
        .map(QualityControlTaskCreation::getAnswer)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No task with uuid=" + qualityControlModelElementsId + " found!"));
  }

  public List<ResolvedVariablesWrapper> getResolvedVariablesWrappers() {
    return qualityControlTasks.stream()
        .map(
            qualityControlTaskCreation -> new ResolvedVariablesWrapper(
                qualityControlTaskCreation.getQualityControlModelElementId(),
                qualityControlTaskCreation.getResolvedVariables()
            )
        )
        .toList();
  }

}
