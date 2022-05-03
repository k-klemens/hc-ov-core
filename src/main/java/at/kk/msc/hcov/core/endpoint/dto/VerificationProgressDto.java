package at.kk.msc.hcov.core.endpoint.dto;

import at.kk.msc.hcov.core.service.crowdsourcing.model.VerificationProgress;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Describes the progress of the overall verification.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerificationProgressDto {
  /**
   * Name of the verification.
   */
  private String verificationName;

  /**
   * Date and time when the verification was created.
   */
  private LocalDateTime createdAt;

  /**
   * Status of all tasks of the verification.
   */
  private VerificationProgress.Status status;

  /**
   * Number of tasks published.
   */
  private long totalHits;

  /**
   * Number of tasks completed. Completed means all assignment (=redundant completion requests) are done.
   */
  private long completedHits;

  /**
   * Number of tasks not completed.
   */
  private long openHits;

  /**
   * List containing the progression status of each task.
   */
  private List<TaskProgressDetailDto> taskProgressDetails;

  public void incrementCompetedHits() {
    completedHits = completedHits + 1;
  }

  public void incrementOpenHits() {
    openHits = openHits + 1;
  }

  /**
   * Status describing the progression of the verification.
   */
  public enum Status {
    /**
     * The verification is still visible to workers and not all tasks are completed.
     */
    PUBLISHED,
    /**
     * All tasks of the verification are completed by the workers.
     */
    ALL_TASKS_COMPLETED
  }
}
