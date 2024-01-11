package com.accolite.EmployeeReferralBackend.repository;

import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReferredCandidateRepository extends JpaRepository<ReferredCandidate, Integer> {
    Optional<ReferredCandidate> findByPanNumber(String panNumber);
    // You can add custom query methods if needed
}
