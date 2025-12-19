package com.web.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.web.ecommerce.repository.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductRepository productRepo;
    
    @Autowired
    private UserRepository userRepo;

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
        return ResponseEntity.ok(java.util.Map.of("message", "Product deleted by Admin"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userRepo.deleteById(id);
        return ResponseEntity.ok(java.util.Map.of("message", "User deleted by Admin"));
    }
}