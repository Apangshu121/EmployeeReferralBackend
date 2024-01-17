package com.accolite.EmployeeReferralBackend.repository;

import com.accolite.EmployeeReferralBackend.models.ReferredCandidateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferredCandidateHistoryRepository extends JpaRepository<ReferredCandidateHistory, Long> {
    // You can add custom query methods if needed
}

