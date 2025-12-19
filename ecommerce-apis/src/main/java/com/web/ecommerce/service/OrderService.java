package com.web.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.web.ecommerce.model.*;
import com.web.ecommerce.repository.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CartService cartService;

    @Transactional
    public List<Order> processCheckout(Long userId, List<OrderRequest> orderRequests) {
        List<Order> placedOrders = new ArrayList<>();
        
        for (OrderRequest request : orderRequests) {
            Order order = new Order();
            order.setUserId(userId);
            order.setProductId(request.getProductId());
            order.setUserMobileNumber(request.getUserMobileNumber());
            order.setUserAddress(request.getUserAddress());
            order.setStatus("PLACED_COD");
            
            placedOrders.add(orderRepository.save(order));
        }
        
        cartService.clearCart(userId);
        return placedOrders;
    }

    @Transactional
    public Order updateTrackingStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}