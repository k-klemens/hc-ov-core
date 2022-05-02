package at.kk.msc.hcov.core.persistence.repository;

import at.kk.msc.hcov.core.persistence.model.VerificationMetaDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationMetaDataRepository extends JpaRepository<VerificationMetaDataEntity, String> {

}
