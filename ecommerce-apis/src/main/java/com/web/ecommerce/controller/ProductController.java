package com.web.ecommerce.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.web.ecommerce.model.Product;
import com.web.ecommerce.service.ProductService;
import com.web.ecommerce.service.JwtUserDetailsService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping("/seller/products")
    public ResponseEntity<?> addProduct(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Product product) {
        try {
            Long userId = userDetailsService.getUserIdByUsername(userDetails.getUsername());
            product.setUserId(userId);
            productService.addProduct(product);
            return ResponseEntity.ok("Product added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to add product: " + e.getMessage());
        }
    }

    @GetMapping("/buyer/products")
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/seller/productsview")
    public ResponseEntity<?> getSellerProducts(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userDetailsService.getUserIdByUsername(userDetails.getUsername());
        return ResponseEntity.ok(productService.getProductsByUserId(userId));
    }

    @DeleteMapping("/seller/products/{id}")
    public ResponseEntity<?> deleteProduct(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        Long userId = userDetailsService.getUserIdByUsername(userDetails.getUsername());
        Product product = productService.getProductById(id);

        if (product != null && product.getUserId().equals(userId)) {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully");
        }
        return ResponseEntity.status(403).body("Unauthorized delete attempt");
    }
}