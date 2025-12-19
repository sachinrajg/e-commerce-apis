package com.web.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.web.ecommerce.model.*;
import com.web.ecommerce.service.*;
import com.web.ecommerce.util.JwtTokenUtil;
import java.util.Map;

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
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authRequest) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        
        final User user = userDetailsService.getUserDetails(authRequest.getUsername());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        
        final String token = jwtTokenUtil.generateToken(userDetails.getUsername(), user.getId(), user.getRoleId(), user.getStatus());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> regAdmin(@RequestBody UserRegistrationRequest r) {
        r.setRoleId(1);
        userService.registerUser(r);
        return ResponseEntity.ok(Map.of("status", true, "message", "Admin registered"));
    }

    @PostMapping("/register/seller")
    public ResponseEntity<?> regSeller(@RequestBody UserRegistrationRequest r) {
        r.setRoleId(2);
        userService.registerUser(r);
        return ResponseEntity.ok(Map.of("status", true, "message", "Seller registered"));
    }

    @PostMapping("/register/buyer")
    public ResponseEntity<?> regBuyer(@RequestBody UserRegistrationRequest r) {
        r.setRoleId(3);
        userService.registerUser(r);
        return ResponseEntity.ok(Map.of("status", true, "message", "Buyer registered"));
    }
}