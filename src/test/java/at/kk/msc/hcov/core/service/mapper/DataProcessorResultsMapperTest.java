package at.kk.msc.hcov.core.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import at.kk.msc.hcov.core.endpoint.dto.DataProcessorResultsDto;
import at.kk.msc.hcov.core.service.processing.model.DataProcessorResults;
import at.kk.msc.hcov.core.util.mockdata.ResultMockData;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class DataProcessorResultsMapperTest {

  DataProcessorResultsMapper target = Mappers.getMapper(DataProcessorResultsMapper.class);

  @Test
  public void testToDto() {
    // given
    List<DataProcessorResults> given = ResultMockData.EXPECTED_DATA_PROCESSOR_RESULTS_ONLY_RAW();

    // when
    List<DataProcessorResultsDto> actual = target.toDto(given);

    // then
    assertThat(actual).containsExactlyInAnyOrderElementsOf(ResultMockData.EXPECTED_DATA_PROCESSOR_RESULTS_ONLY_RAW_DTO());
  }
}
