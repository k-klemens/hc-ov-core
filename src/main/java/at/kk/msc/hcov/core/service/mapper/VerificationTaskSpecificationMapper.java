package at.kk.msc.hcov.core.service.mapper;

import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskSpecificationRequestDto;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class VerificationTaskSpecificationMapper {
  
  public abstract VerificationTaskSpecification toServiceObject(VerificationTaskSpecificationRequestDto dto);

}
