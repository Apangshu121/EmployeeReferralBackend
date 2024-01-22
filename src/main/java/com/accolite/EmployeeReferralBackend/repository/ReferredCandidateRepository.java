package com.accolite.EmployeeReferralBackend.repository;

import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReferredCandidateRepository extends JpaRepository<ReferredCandidate, Integer>, JpaSpecificationExecutor<ReferredCandidate> {

    Optional<ReferredCandidate> findByPanNumber(String panNumber);

    List<ReferredCandidate> findByExperienceGreaterThanEqual(int experience);

    List<ReferredCandidate> findByPreferredLocation(String preferredLocation);

    @Query("SELECT r FROM ReferredCandidate r WHERE r.noticePeriodLeft <= :noticePeriodLeft")
    List<ReferredCandidate> findByNoticePeriodLeftLessThanOrEqual(int noticePeriodLeft);

    // List<ReferredCandidate> findByReferrerEmail(String emailId);

}


