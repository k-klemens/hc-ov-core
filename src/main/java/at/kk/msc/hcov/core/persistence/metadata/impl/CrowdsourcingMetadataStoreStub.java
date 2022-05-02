package at.kk.msc.hcov.core.persistence.metadata.impl;

import at.kk.msc.hcov.core.persistence.metadata.ICrowdsourcingMetadataStore;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedTaskIdMappings;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CrowdsourcingMetadataStoreStub implements ICrowdsourcingMetadataStore {
  @Override
  public void saveMetaData(VerificationTaskSpecification verificationTaskSpecification,
                           PublishedTaskIdMappings publishedTaskIdMappings) {

  }
}
