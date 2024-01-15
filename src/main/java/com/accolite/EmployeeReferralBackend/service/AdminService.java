package com.accolite.EmployeeReferralBackend.service;

import com.accolite.EmployeeReferralBackend.models.User;

import java.util.List;

public interface AdminService {

    List<User> getAllUsers();

    boolean modifyOrCreateUser(Long userId, User modifiedUser);


}
