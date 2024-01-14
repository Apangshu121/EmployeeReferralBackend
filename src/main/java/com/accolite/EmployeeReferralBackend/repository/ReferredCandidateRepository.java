package com.accolite.EmployeeReferralBackend.repository;

import com.accolite.EmployeeReferralBackend.models.CandidateDetails;
import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReferredCandidateRepository extends JpaRepository<ReferredCandidate, Integer> {
    Optional<ReferredCandidate> findByPanNumber(String panNumber);

    @Query("SELECT new com.accolite.EmployeeReferralBackend.models.CandidateDetails(r.candidateName, r.dateOfReferral, r.interviewStatus, r.interviewedPosition) FROM ReferredCandidate r")
    List<CandidateDetails> findAllCandidates();

    @Query("SELECT new com.accolite.EmployeeReferralBackend.models.CandidateDetails(r.candidateName, r.dateOfReferral, r.interviewStatus, r.interviewedPosition) FROM ReferredCandidate r WHERE r.referrerEmail = :emailId")
    List<CandidateDetails> findAllCandidatesOfReferrer(@Param("emailId") String emailId);
}


