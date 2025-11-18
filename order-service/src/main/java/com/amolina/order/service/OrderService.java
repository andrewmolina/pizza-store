package com.amolina.order.service;

import com.amolina.order.model.Order;
import com.amolina.order.model.dto.OrderResponseDTO;
import com.amolina.order.service.client.dto.PizzaDTO;
import com.amolina.order.repository.OrderRepository;
import com.amolina.order.service.client.MenuFeignClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuFeignClient menuFeignClient;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @CircuitBreaker(name = "menuService", fallbackMethod = "buildOrderWithDefaultPizza")
    @Retry(name = "menuService", fallbackMethod = "buildOrderWithDefaultPizza")
    @Bulkhead(name = "menuService", fallbackMethod = "buildOrderWithDefaultPizza")
    public Optional<OrderResponseDTO> getOrderById(Long id) {
        logger.debug("Attempting to fetch order with id: {}", id);
        
        Optional<Order> orderOpt = orderRepository.findById(id);
        
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Order order = orderOpt.get();
        
        // Fetch pizza details from menu-service (protected by resilience4j)
        logger.debug("Calling menu-service for pizza id: {}", order.getItemId());
        PizzaDTO pizza = menuFeignClient.getPizza(order.getItemId().toString());
        
        // Build response with pizza details
        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(order.getOrderId());
        response.setItemId(order.getItemId());
        response.setPizzaName(pizza.getName());
        response.setPizzaPrice(pizza.getPrice());
        response.setSubtotal(order.getSubtotal());
        response.setTax(order.getTax());
        response.setTotal(order.getTotal());
        response.setCustomerId(order.getCustomerId());
        
        logger.debug("Successfully built order response for order id: {}", id);
        return Optional.of(response);
    }

    /**
     * Fallback method when menu-service is unavailable or slow.
     * Returns order with default pizza information.
     */
    @SuppressWarnings("unused")
    private Optional<OrderResponseDTO> buildOrderWithDefaultPizza(Long id, Throwable throwable) {
        logger.warn("menu-service unavailable, using fallback for order id: {}. Error: {}", 
                    id, throwable.getMessage());
        
        Optional<Order> orderOpt = orderRepository.findById(id);
        
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Order order = orderOpt.get();
        
        // Build response with fallback pizza details
        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(order.getOrderId());
        response.setItemId(order.getItemId());
        response.setPizzaName("Pizza (Details Unavailable)");
        response.setPizzaPrice(BigDecimal.ZERO);
        response.setSubtotal(order.getSubtotal());
        response.setTax(order.getTax());
        response.setTotal(order.getTotal());
        response.setCustomerId(order.getCustomerId());
        
        logger.debug("Returning fallback order response for order id: {}", id);
        return Optional.of(response);
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        
        order.setItemId(orderDetails.getItemId());
        order.setSubtotal(orderDetails.getSubtotal());
        order.setTax(orderDetails.getTax());
        order.setTotal(orderDetails.getTotal());
        order.setCustomerId(orderDetails.getCustomerId());
        
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        orderRepository.delete(order);
    }
}

