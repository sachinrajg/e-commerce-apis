// CartService.java
package com.web.ecommerce.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.ecommerce.model.Cart;
import com.web.ecommerce.repository.CartRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public Cart addToCart(Cart cartItem) {
        Cart existingCartItem = cartRepository.findByUserIdAndProductId(cartItem.getUserId(), cartItem.getProductId());
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItem.getQuantity());
            return cartRepository.save(existingCartItem);
        } else {
            return cartRepository.save(cartItem);
        }
    }

    public List<Cart> getCartItemsByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public Cart updateCartQuantity(Long cartId, int quantity) {
        Cart cartItem = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItem.setQuantity(quantity);
        return cartRepository.save(cartItem);
    }
}
