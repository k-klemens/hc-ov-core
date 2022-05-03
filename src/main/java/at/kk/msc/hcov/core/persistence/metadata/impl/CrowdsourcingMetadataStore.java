package at.kk.msc.hcov.core.persistence.metadata.impl;

import at.kk.msc.hcov.core.persistence.metadata.ICrowdsourcingMetadataStore;
import at.kk.msc.hcov.core.persistence.metadata.exception.VerificationDoesNotExistException;
import at.kk.msc.hcov.core.persistence.model.VerificationMetaDataEntity;
import at.kk.msc.hcov.core.persistence.model.creator.VerificationMetaDataCreator;
import at.kk.msc.hcov.core.persistence.repository.VerificationMetaDataRepository;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedTaskIdMappings;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CrowdsourcingMetadataStore implements ICrowdsourcingMetadataStore {

  private VerificationMetaDataRepository repository;
  private VerificationMetaDataCreator entityCreator;

  @Override
  public VerificationMetaDataEntity saveMetaData(
      VerificationTaskSpecification verificationTaskSpecification, PublishedTaskIdMappings publishedTaskIdMappings
  ) {
    VerificationMetaDataEntity entity = entityCreator.toEntity(verificationTaskSpecification, publishedTaskIdMappings);
    return repository.save(entity);
  }

  @Override
  public VerificationMetaDataEntity getMetaData(String verificationName) throws VerificationDoesNotExistException {
    return repository.findById(verificationName)
        .orElseThrow(() -> new VerificationDoesNotExistException(verificationName));
  }
}
