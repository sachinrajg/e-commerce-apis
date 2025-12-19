package com.web.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.web.ecommerce.model.User;
import com.web.ecommerce.model.UserRegistrationRequest;
import com.web.ecommerce.repository.UserRepository;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(UserRegistrationRequest registrationRequest) {
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRoleId(registrationRequest.getRoleId());
        if (registrationRequest.getStatus() != null) {
        user.setStatus(registrationRequest.getStatus());
        } else {
            user.setStatus(true);
        }
        userRepository.save(user);
    }

    public void updateUserProfile(Long userId, UserRegistrationRequest updatedRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        user.setUsername(updatedRequest.getUsername());
        if (updatedRequest.getPassword() != null && !updatedRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedRequest.getPassword()));
        }
        if (updatedRequest.getRoleId() > 0) {
            user.setRoleId(updatedRequest.getRoleId());
        }
        if (StringUtils.hasText(updatedRequest.getAddressline1())) {
            user.setAddressline1(updatedRequest.getAddressline1());
        }
        if (StringUtils.hasText(updatedRequest.getAddressline2())) {
            user.setAddressline2(updatedRequest.getAddressline2());
        }
        if (StringUtils.hasText(updatedRequest.getCity())) {
            user.setCity(updatedRequest.getCity());
        }
        
        if (StringUtils.hasText(updatedRequest.getState())) {
            user.setState(updatedRequest.getState()); 
        }
        
        if (StringUtils.hasText(updatedRequest.getCountry())) {
            user.setCountry(updatedRequest.getCountry());
        }
        if (StringUtils.hasText(updatedRequest.getPhoneNumber())) {
            user.setPhoneNumber(updatedRequest.getPhoneNumber());
        }
        if (StringUtils.hasText(updatedRequest.getFirstName())) {
            user.setFirstName(updatedRequest.getFirstName());
        }
        if (StringUtils.hasText(updatedRequest.getLastName())) {
            user.setLastName(updatedRequest.getLastName());
        }
        if (StringUtils.hasText(updatedRequest.getFullname())) {
            user.setFullname(updatedRequest.getFullname());
        }

        userRepository.save(user);
    }

    public User getUserDetails(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }
}
