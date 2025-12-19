package com.web.ecommerce.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.web.ecommerce.model.Cart;
import com.web.ecommerce.service.CartService;
import com.web.ecommerce.service.JwtUserDetailsService;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class CartController {

    @Autowired
    private CartService cartService;
    
    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping("/add")
    public Cart addToCart(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Long productId, @RequestParam int quantity) {
        Long userId = userDetailsService.getUserIdByUsername(userDetails.getUsername());
        return cartService.addToCart(userId, productId, quantity);
    }

    @GetMapping
    public List<Cart> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userDetailsService.getUserIdByUsername(userDetails.getUsername());
        return cartService.getCartItemsByUserId(userId);
    }

    @PutMapping("/update")
    public Cart updateCartQuantity(@RequestParam Long cartId, @RequestParam int quantity) {
        return cartService.updateCartQuantity(cartId, quantity);
    }
}