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

    List<ReferredCandidate> findByExperienceGreaterThanEqual(int experience);

    List<ReferredCandidate> findByPreferredLocation(String preferredLocation);

    @Query("SELECT r FROM ReferredCandidate r WHERE r.noticePeriod <= :noticePeriod")
    List<ReferredCandidate> findByNoticePeriodLessThanOrEqual(int noticePeriod);


    @Query("SELECT new com.accolite.EmployeeReferralBackend.models.CandidateDetails(r.candidateName, r.primarySkill, r.interviewStatus, r.interviewedPosition, r.primarySkill, r.secondarySkills) FROM ReferredCandidate r")
    List<CandidateDetails> findAllCandidates();

  //  @Query("SELECT new com.accolite.EmployeeReferralBackend.models.CandidateDetails(r.candidateName, r.primarySkill, r.interviewStatus, r.interviewedPosition, r.primarySkill, r.secondarySkills) FROM ReferredCandidate r WHERE r.referrerEmail = :emailId")
    List<ReferredCandidate> findByReferrerEmail(String emailId);

}


