package at.kk.msc.hcov.core.service.mapper;

import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskSpecificationRequestDto;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTaskSpecification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VerificationTaskSpecificationMapper {

  VerificationTaskSpecification toServiceObject(VerificationTaskSpecificationRequestDto dto);
  
}
