package com.accolite.EmployeeReferralBackend.serviceImpl;

import com.accolite.EmployeeReferralBackend.models.User;
import com.accolite.EmployeeReferralBackend.repository.UserRepository;
import com.accolite.EmployeeReferralBackend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean modifyOrCreateUser(Long userId, User modifiedUser) {
        Optional<User> existingUserOptional = userRepository.findById(userId);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            // Update only the fields you want to allow modification
            existingUser.setEmail(modifiedUser.getEmail());
            existingUser.setRole(modifiedUser.getRole());
            existingUser.setTotalBonus(modifiedUser.getTotalBonus());

            // Save the modified user
            userRepository.save(existingUser);
        } else {
            // User not present, create a new user
            User newUser = new User();
            newUser.setId(userId);
            newUser.setEmail(modifiedUser.getEmail());
            newUser.setRole(modifiedUser.getRole());
            newUser.setTotalBonus(modifiedUser.getTotalBonus());

            // Save the new user
            userRepository.save(newUser);
        }

        return true;
    }


}