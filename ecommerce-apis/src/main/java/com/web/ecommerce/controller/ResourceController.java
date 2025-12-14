package com.web.ecommerce.controller;

import javax.servlet.http.HttpServletRequest;


import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Claims;



import com.web.ecommerce.model.UserRegistrationRequest;
import com.web.ecommerce.model.User;
import com.web.ecommerce.util.JwtTokenUtil;
import com.web.ecommerce.service.UserService;



@RestController
public class ResourceController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private UserService userService;

    @PutMapping("/userservice/users/my-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UserRegistrationRequest registrationRequest, Authentication authentication, HttpServletRequest request){
        try{
            String token = extractToken(request);
            Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
            Long userId = claims.get("userId", Long.class);
            userService.updateUserProfile(userId, registrationRequest);
            return ResponseEntity.ok("Product added successfully");

        } catch(Exception e){
            logger.error("Failed to update user : {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to update user  : " + e.getMessage());
        }

    }

    @GetMapping("/userservice/users/my-profile")
    public ResponseEntity<User> getProfile(Authentication authentication, HttpServletRequest request){
        String token = extractToken(request);
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
        Long userId = claims.get("userId", Long.class);
        User user = userService.getUserDetails(userId);
        return ResponseEntity.ok(user);
    }

        private String extractToken(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            return requestTokenHeader.substring(7);
        }
        return null;
    }

    
}