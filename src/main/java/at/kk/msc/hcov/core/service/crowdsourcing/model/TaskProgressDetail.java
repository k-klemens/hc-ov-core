package at.kk.msc.hcov.core.service.crowdsourcing.model;

import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.HitStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Describes the progress of one task/hit.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskProgressDetail {


  public TaskProgressDetail(UUID ontologyElementId, HitStatus hitStatus) {
    this.ontologyElementId = ontologyElementId;
    this.crowdsourcingId = hitStatus.getCrowdsourcingId();
    this.requestedCompletions = hitStatus.getMaxAssignments();
    this.numCompleted = hitStatus.getNumCompleted();
    this.numOpen = this.requestedCompletions - this.numCompleted;
  }


  /**
   * UUID of the extracted ontology element typically provided by an DataProvider.
   */
  private UUID ontologyElementId;

  /**
   * ID of the published task providded by the crowdsourcing platform.
   */
  private String crowdsourcingId;

  /**
   * Number of completions request for this task. Describes the redundancy of the HIT.
   */
  private int requestedCompletions;

  /**
   * Number of redundant tasks completed.
   */
  private int numCompleted;

  /**
   * Number of redundant tasks not completed.
   */
  private int numOpen;

}
