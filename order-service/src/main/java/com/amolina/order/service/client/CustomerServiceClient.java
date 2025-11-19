package com.amolina.order.service.client;

import com.amolina.order.service.client.dto.CustomerDTO;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceClient.class);

    @Autowired
    private CustomerFeignClient customerFeignClient;

    @CircuitBreaker(name = "customerService", fallbackMethod = "getDefaultCustomer")
    @Retry(name = "customerService")
    @Bulkhead(name = "customerService")
    public CustomerDTO getCustomer(Long customerId) {
        logger.info("Calling customer-service for customer id: {}", customerId);
        CustomerDTO result = customerFeignClient.getCustomer(customerId);
        logger.info("Successfully fetched customer from customer-service");
        return result;
    }

    /**
     * Fallback method for customer-service failures.
     * Returns default customer information when customer-service is unavailable.
     */
    @SuppressWarnings("unused")
    private CustomerDTO getDefaultCustomer(Long customerId, Throwable throwable) {
        logger.warn("customer-service unavailable for customer id: {}. Error: {}. Using default customer.", 
                    customerId, throwable.getMessage());
        
        CustomerDTO defaultCustomer = new CustomerDTO();
        defaultCustomer.setCustomerId(customerId);
        defaultCustomer.setFirstname("Guest");
        defaultCustomer.setLastname("Customer");
        defaultCustomer.setEmail("unavailable@pizza-store.com");
        defaultCustomer.setPhone("N/A");
        
        return defaultCustomer;
    }
}

