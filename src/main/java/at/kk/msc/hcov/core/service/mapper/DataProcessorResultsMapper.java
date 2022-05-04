package at.kk.msc.hcov.core.service.mapper;

import at.kk.msc.hcov.core.endpoint.dto.DataProcessorResultsDto;
import at.kk.msc.hcov.core.service.processing.model.DataProcessorResults;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DataProcessorResultsMapper {

  List<DataProcessorResultsDto> toDto(List<DataProcessorResults> serviceLayerObject);

}
