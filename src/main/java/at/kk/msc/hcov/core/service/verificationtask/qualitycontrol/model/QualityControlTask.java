package at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model;

import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QualityControlTask extends VerificationTask {

  /**
   * Correct answer to the given quality control task.
   */
  private String answer;

  public QualityControlTask(
      String verificationName, String ontologyName, UUID ontologyElementId, String taskHtml, String answer
  ) {
    super(verificationName, ontologyName, ontologyElementId, taskHtml);
    this.answer = answer;
  }

}
