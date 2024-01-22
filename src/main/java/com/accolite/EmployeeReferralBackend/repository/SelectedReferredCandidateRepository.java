package com.accolite.EmployeeReferralBackend.repository;

import com.accolite.EmployeeReferralBackend.models.SelectedReferredCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SelectedReferredCandidateRepository extends JpaRepository<SelectedReferredCandidate, Long> {
    Optional<SelectedReferredCandidate> findByEmail(String email);
}
