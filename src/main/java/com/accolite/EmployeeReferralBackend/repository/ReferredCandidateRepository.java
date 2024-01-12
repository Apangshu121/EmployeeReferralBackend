package com.accolite.EmployeeReferralBackend.repository;

import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferredCandidateRepository extends JpaRepository<ReferredCandidate,Integer> {
}
