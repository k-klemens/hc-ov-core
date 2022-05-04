package at.kk.msc.hcov.core.endpoint.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataProcessorResultsWrapperDto {

  private List<DataProcessorResultsDto> dataProcessorResultsDtos;

}
