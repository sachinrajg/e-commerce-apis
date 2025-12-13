package com.web.ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.ecommerce.model.Order;
import com.web.ecommerce.model.Product;
import com.web.ecommerce.repository.OrderRepository;
import com.web.ecommerce.repository.ProductRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order placeOrder(Long userId, Long productId, String userMobileNumber, String userAddress) {
        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(productId);
        order.setUserMobileNumber(userMobileNumber);
        order.setUserAddress(userAddress);
        order.setStatus("PENDING");
        return orderRepository.save(order);
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getSellerOrders(Long sellerId) {
        List<Product> products = productRepository.findByUserId(sellerId);
        List<Long> productIds = products.stream().map(Product::getProductId).collect(Collectors.toList());
        return orderRepository.findByProductIdIn(productIds);
    }

    public Order changeOrderStatus(Long orderId, String status) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status);
            return orderRepository.save(order);
        } else {
            throw new RuntimeException("Order not found");
        }
    }

    public void cancelOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
