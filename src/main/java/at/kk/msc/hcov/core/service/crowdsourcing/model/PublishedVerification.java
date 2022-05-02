package at.kk.msc.hcov.core.service.crowdsourcing.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
/**
 * Class encapsulating information for verifications and tasks which are published.
 */
public class PublishedVerification {

  private List<PublishedVerificationTask> ontologyVerificationTasks;

  private List<PublishedQualityControlTask> qualityControlTasks;

}
