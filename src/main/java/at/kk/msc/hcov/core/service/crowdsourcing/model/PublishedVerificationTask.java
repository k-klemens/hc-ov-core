package at.kk.msc.hcov.core.service.crowdsourcing.model;

import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data class holding information of published verification tasks.
 */
@Data
@NoArgsConstructor
public class PublishedVerificationTask extends VerificationTask {

  public PublishedVerificationTask(String verificationName, String ontologyName, UUID ontologyElementId, String taskHtml,
                                   String crowdsourcingId) {
    super(verificationName, ontologyName, ontologyElementId, taskHtml);
    this.crowdsourcingId = crowdsourcingId;
  }

  public PublishedVerificationTask(VerificationTask verificationTask,
                                   String crowdsourcingId) {
    super(verificationTask.getVerificationName(), verificationTask.getOntologyName(),
        verificationTask.getOntologyElementId(), verificationTask.getTaskHtml());
    this.crowdsourcingId = crowdsourcingId;
  }

  /**
   * ID of the published task providded by the crowdsourcing platform.
   */
  private String crowdsourcingId;


}
