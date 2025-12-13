package com.web.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.ecommerce.model.JwtRequest;
import com.web.ecommerce.model.JwtResponse;
import com.web.ecommerce.model.User.Role;
import com.web.ecommerce.model.UserRegistrationRequest;
import com.web.ecommerce.service.JwtUserDetailsService;
import com.web.ecommerce.service.UserService;
import com.web.ecommerce.util.JwtTokenUtil;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String role = userDetails.getAuthorities().iterator().next().getAuthority(); // Extract role
        final Long userId = userDetailsService.getUserIdByUsername(authenticationRequest.getUsername()); // Retrieve userId
        final Long role_id = userDetailsService.getUserIdByUsername()
        final String token = jwtTokenUtil.generateToken(userDetails.getUsername(), role, userId, role_id); // Generate token with role and userId
        return ResponseEntity.ok(new JwtResponse(token));
    }


    @GetMapping("/userId")
    public ResponseEntity<Long> getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userDetailsService.getUserIdByUsername(username);
        return ResponseEntity.ok(userId);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@RequestBody UserRegistrationRequest registrationRequest) {
        registrationRequest.setRole(Role.ADMIN);
        userService.registerUser(registrationRequest);
        return ResponseEntity.ok("Admin registered successfully");
    }

    @PostMapping("/buyer/register")
    public ResponseEntity<?> registerBuyer(@RequestBody UserRegistrationRequest registrationRequest) {
        registrationRequest.setRole(Role.BUYER);
        userService.registerUser(registrationRequest);
        return ResponseEntity.ok("Buyer registered successfully");
    }

    @PostMapping("/seller/register")
    public ResponseEntity<?> registerSeller(@RequestBody UserRegistrationRequest registrationRequest) {
        registrationRequest.setRole(Role.SELLER);
        userService.registerUser(registrationRequest);
        return ResponseEntity.ok("Seller registered successfully");
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
