package at.kk.msc.hcov.core.persistence.repository;

import at.kk.msc.hcov.core.persistence.model.VerificationTaskSpecificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTaskSpecificationRepository extends JpaRepository<VerificationTaskSpecificationEntity, String> {

}
