package com.web.ecommerce.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.web.ecommerce.model.Order;
import com.web.ecommerce.model.OrderRequest;
import com.web.ecommerce.service.JwtUserDetailsService;
import com.web.ecommerce.service.OrderService;
import com.web.ecommerce.util.JwtTokenUtil;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(HttpServletRequest request, @RequestBody OrderRequest orderRequest) {
        try {
            String token = extractToken(request);
            if (token == null) {
                return ResponseEntity.status(401).body("Unauthorized: Missing or invalid token");
            }

            String username = jwtTokenUtil.getUsernameFromToken(token);
            Long userId = userDetailsService.getUserIdByUsername(username);

            Order order = orderService.placeOrder(userId,
                                                  orderRequest.getProductId(),
                                                  orderRequest.getUserMobileNumber(),
                                                  orderRequest.getUserAddress());
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error placing order: " + e.getMessage());
        }
    }

    private String extractToken(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            return requestTokenHeader.substring(7);
        }
        return null;
    }
}