package com.amolina.order.service;

import com.amolina.order.model.Order;
import com.amolina.order.model.dto.OrderResponseDTO;
import com.amolina.order.service.client.dto.CustomerDTO;
import com.amolina.order.service.client.dto.PizzaDTO;
import com.amolina.order.repository.OrderRepository;
import com.amolina.order.service.client.CustomerServiceClient;
import com.amolina.order.service.client.MenuServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuServiceClient menuServiceClient;

    @Autowired
    private CustomerServiceClient customerServiceClient;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<OrderResponseDTO> getOrderById(Long id) {
        logger.debug("Attempting to fetch order with id: {}", id);
        
        Optional<Order> orderOpt = orderRepository.findById(id);
        
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Order order = orderOpt.get();
        
        // Fetch pizza details from menu-service (protected by circuit breaker in separate service)
        PizzaDTO pizza = menuServiceClient.getPizza(order.getItemId());
        
        // Fetch customer details from customer-service (protected by circuit breaker in separate service)
        CustomerDTO customer = customerServiceClient.getCustomer(order.getCustomerId());
        
        // Build response with both pizza and customer details
        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(order.getOrderId());
        response.setItemId(order.getItemId());
        response.setPizzaName(pizza.getName());
        response.setPizzaPrice(pizza.getPrice());
        response.setSubtotal(order.getSubtotal());
        response.setTax(order.getTax());
        response.setTotal(order.getTotal());
        response.setCustomerId(order.getCustomerId());
        response.setCustomerFirstname(customer.getFirstname());
        response.setCustomerLastname(customer.getLastname());
        response.setCustomerEmail(customer.getEmail());
        response.setCustomerPhone(customer.getPhone());
        
        logger.debug("Successfully built order response for order id: {}", id);
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

