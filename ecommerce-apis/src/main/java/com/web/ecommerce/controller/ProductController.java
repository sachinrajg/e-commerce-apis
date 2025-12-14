package com.web.ecommerce.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import com.web.ecommerce.model.Product;
import com.web.ecommerce.service.ProductService;
import com.web.ecommerce.util.JwtTokenUtil;

import io.jsonwebtoken.Claims;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping("/seller/products")
    public ResponseEntity<?> addProduct(@RequestBody Product product, Authentication authentication, HttpServletRequest request) {
        try {
            String token = extractToken(request);
            Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
            Long userId = claims.get("userId", Long.class);

            product.setUserId(userId);
            logger.info("Product saved: {}", product);

            productService.addProduct(product);

            return ResponseEntity.ok("Product added successfully");
        } catch (Exception e) {
            logger.error("Failed to add product: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to add product: " + e.getMessage());
        }
    }

    @GetMapping("/buyer/products")
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = productService.getAllProducts();
        System.out.println("Hello");
        logger.info("Hello - scahin");
        return ResponseEntity.ok(products);
    }

    @GetMapping("/seller/productsview")
    public ResponseEntity<?> getSellerProducts(HttpServletRequest request) {
        try {
            String token = extractToken(request);
            Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
            Long userId = claims.get("userId", Long.class);

            List<Product> products = productService.getProductsByUserId(userId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error("Failed to get products: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to get products: " + e.getMessage());
        }
    }

    @DeleteMapping("/seller/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, Authentication authentication, HttpServletRequest request) {
        try {
            String token = extractToken(request);
            if (token == null || token.isEmpty()) {
                throw new IllegalArgumentException("JWT Token cannot be null or empty");
            }

            Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
            Long userId = claims.get("userId", Long.class);
            logger.info("Authenticated user ID: {}", userId);

            Product product = productService.getProductById(id);
            logger.info("Product to be deleted: {}", product);

            if (product != null && product.getUserId().equals(userId)) {
                productService.deleteProduct(id);
                logger.info("Product deleted successfully");
                return ResponseEntity.ok("Product deleted successfully");
            } else {
                logger.warn("User does not have permission to delete this product");
                return ResponseEntity.status(403).body("You do not have permission to delete this product");
            }
        } catch (Exception e) {
            logger.error("Failed to delete product: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to delete product: " + e.getMessage());
        }
    }

    private String extractToken(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        logger.info("Authorization header: {}", requestTokenHeader);
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            return requestTokenHeader.substring(7);
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
            return null;
        }
    }
}
