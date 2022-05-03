package at.kk.msc.hcov.core.persistence.model;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class ConfigurationEntity {

  private String configurationKey;
  private String configurationValue;

}
