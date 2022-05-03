package at.kk.msc.hcov.core.service.mapper;

import at.kk.msc.hcov.core.endpoint.dto.VerificationProgressDto;
import at.kk.msc.hcov.core.service.crowdsourcing.model.VerificationProgress;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VerificationProgressMapper {

  VerificationProgressDto toDto(VerificationProgress verificationProgress);

}
