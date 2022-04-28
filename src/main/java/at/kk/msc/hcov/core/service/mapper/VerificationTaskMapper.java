package at.kk.msc.hcov.core.service.mapper;

import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskDto;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VerificationTaskMapper {

  List<VerificationTaskDto> toDto(List<VerificationTask> serviceObject);

}
