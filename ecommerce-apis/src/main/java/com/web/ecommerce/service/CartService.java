package com.web.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.web.ecommerce.model.Cart;
import com.web.ecommerce.repository.CartRepository;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Transactional
    public Cart addToCart(Long userId, Long productId, int quantity) {
        Optional<Cart> existingCart = cartRepository.findByUserIdAndProductId(userId, productId);
        
        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + quantity);
            return cartRepository.save(cart);
        } else {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setProductId(productId);
            newCart.setQuantity(quantity);
            return cartRepository.save(newCart);
        }
    }
    
    public List<Cart> getCartItemsByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Transactional
    public Cart updateCartQuantity(Long cartId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found with id: " + cartId));
        cart.setQuantity(quantity);
        return cartRepository.save(cart);
    }
    
    @Transactional
    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }
}