package com.accolite.EmployeeReferralBackend.repository;

import com.accolite.EmployeeReferralBackend.models.SelectedCandidateDetails;
import com.accolite.EmployeeReferralBackend.models.SelectedReferredCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SelectedReferredCandidateRepository extends JpaRepository<SelectedReferredCandidate, Long> {

    Optional<SelectedReferredCandidate> findByPanNumber(String panNumber);

    @Query("SELECT new com.accolite.EmployeeReferralBackend.models.SelectedCandidateDetails(r.name, r.dateOfJoining, r.interviewedRole) FROM SelectedReferredCandidate r")
    List<SelectedCandidateDetails> findAllSelectedCandidates();
}
