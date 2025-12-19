package com.web.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.web.ecommerce.model.Order;
import com.web.ecommerce.model.OrderRequest;
import com.web.ecommerce.service.JwtUserDetailsService;
import com.web.ecommerce.service.OrderService;
import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@AuthenticationPrincipal UserDetails userDetails, @RequestBody List<OrderRequest> orderRequests) {
        try {
            Long userId = userDetailsService.getUserIdByUsername(userDetails.getUsername());
            List<Order> orders = orderService.processCheckout(userId, orderRequests);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing checkout: " + e.getMessage());
        }
    }
}