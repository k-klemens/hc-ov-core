package at.kk.msc.hcov.core.service.crowdsourcing.model;

import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model.QualityControlTask;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
/**
 * Data class holding information of published quality control tasks.
 */
public class PublishedQualityControlTask extends QualityControlTask {

  public PublishedQualityControlTask(QualityControlTask qualityControlTask,
                                     String crowdsourcingId) {
    super(qualityControlTask.getVerificationName(), qualityControlTask.getOntologyName(), qualityControlTask.getOntologyElementId(),
        qualityControlTask.getTaskHtml(), qualityControlTask.getAnswer());
    this.crowdsourcingId = crowdsourcingId;
  }

  /**
   * ID of the published task provided by the crowdsourcing platform.
   */
  private String crowdsourcingId;

}
