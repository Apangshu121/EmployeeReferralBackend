package com.accolite.EmployeeReferralBackend.repository;

import com.accolite.EmployeeReferralBackend.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepo extends JpaRepository<Roles,Integer> {

}
