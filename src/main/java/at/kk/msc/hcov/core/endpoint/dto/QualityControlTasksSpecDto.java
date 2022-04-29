package at.kk.msc.hcov.core.endpoint.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Specification for creating quality control questions / tasks.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QualityControlTasksSpecDto {

  /**
   * List of the quality control tasks to be created.
   */
  List<QualityControlTaskDto> qualityControlTasks;

}
