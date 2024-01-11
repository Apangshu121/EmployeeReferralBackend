package com.accolite.EmployeeReferralBackend.repository;

import com.accolite.EmployeeReferralBackend.models.SelectedCandidates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectedCandidatesRepo extends JpaRepository<SelectedCandidates, Integer> {
}
