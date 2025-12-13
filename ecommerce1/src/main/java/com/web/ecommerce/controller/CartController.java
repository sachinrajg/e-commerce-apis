package com.web.ecommerce.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import com.web.ecommerce.model.Cart;
import com.web.ecommerce.service.CartService;
import com.web.ecommerce.util.JwtTokenUtil;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class CartController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public Cart addToCart(@RequestParam Long productId, @RequestParam int quantity, HttpServletRequest request) {
        Cart cartItem = new Cart();
        String token = extractToken(request);
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
        Long userId1 = claims.get("userId", Long.class);
        String a = userId1.toString() + " " + productId.toString();
        logger.info(a);

        cartItem.setUserId(userId1);
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);
        return cartService.addToCart(cartItem);
    }

    @GetMapping("/{userId}")
    public List<Cart> getCart(@PathVariable Long userId) {
        String a = userId.toString();
        logger.info(a);
        return cartService.getCartItemsByUserId(userId);
    }

    @PutMapping("/update")
    public Cart updateCartQuantity(@RequestParam Long cartId, @RequestParam int quantity) {
        return cartService.updateCartQuantity(cartId, quantity);
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
