package at.kk.msc.hcov.core.persistence.metadata;

import at.kk.msc.hcov.core.persistence.metadata.exception.VerificationDoesNotExistException;
import at.kk.msc.hcov.core.persistence.model.VerificationMetaDataEntity;
import at.kk.msc.hcov.core.service.crowdsourcing.model.PublishedTaskIdMappings;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;

/**
 * Interface specifying operations to be supported for storing metadata of a published verification.
 */
public interface ICrowdsourcingMetadataStore {

  /**
   * Saves the metadata of a verification. Including general setup of the verification and a mapping between internally used IDs and
   * IDs provided by the crowdsourcing platform for the tasks. Populated question / task / hit templates are not persisted due to space
   * considerations.
   *
   * @param verificationTaskSpecification specification of the published verification tasks.
   * @param publishedTaskIdMappings       mappings between the extracted element ids and the ids provided by the crowdsourcing platform.
   * @return a the persisted meta-data of the verification.
   */
  VerificationMetaDataEntity saveMetaData(
      VerificationTaskSpecification verificationTaskSpecification, PublishedTaskIdMappings publishedTaskIdMappings
  );

  /**
   * Returns the {@link VerificationMetaDataEntity} from the persisted storage
   *
   * @param verificationName name of the verification
   * @return a {@link VerificationMetaDataEntity} object
   */
  VerificationMetaDataEntity getMetaData(String verificationName) throws VerificationDoesNotExistException;


}
