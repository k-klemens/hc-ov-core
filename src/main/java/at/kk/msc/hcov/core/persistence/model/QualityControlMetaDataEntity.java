package at.kk.msc.hcov.core.persistence.model;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityControlMetaDataEntity {

  @Id
  private UUID qualityControlModelElementId;

  private String crowdsourcingId;

  private String answer;
}
