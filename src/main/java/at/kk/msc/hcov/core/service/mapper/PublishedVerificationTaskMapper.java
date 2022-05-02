package at.kk.msc.hcov.core.service.mapper;

import at.kk.msc.hcov.core.endpoint.dto.PublishedVerificationTaskDto;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedVerificationTask;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublishedVerificationTaskMapper {

  PublishedVerificationTaskDto toDto(PublishedVerificationTask serviceObject);

}
