package com.amolina.order.service.client;

import com.amolina.order.service.client.dto.PizzaDTO;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MenuServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(MenuServiceClient.class);

    @Autowired
    private MenuFeignClient menuFeignClient;

    @CircuitBreaker(name = "menuService", fallbackMethod = "getDefaultPizza")
    @Retry(name = "menuService")
    @Bulkhead(name = "menuService")
    public PizzaDTO getPizza(Long pizzaId) {
        logger.info("Calling menu-service for pizza id: {}", pizzaId);
        PizzaDTO result = menuFeignClient.getPizza(pizzaId.toString());
        logger.info("Successfully fetched pizza from menu-service");
        return result;
    }

    /**
     * Fallback method for menu-service failures.
     * Returns default pizza information when menu-service is unavailable.
     */
    @SuppressWarnings("unused")
    private PizzaDTO getDefaultPizza(Long pizzaId, Throwable throwable) {
        logger.warn("menu-service unavailable for pizza id: {}. Error: {}. Using default pizza.", 
                    pizzaId, throwable.getMessage());
        
        PizzaDTO defaultPizza = new PizzaDTO();
        defaultPizza.setItemId(pizzaId);
        defaultPizza.setName("Pizza (Details Unavailable)");
        defaultPizza.setDescription("Service temporarily unavailable");
        defaultPizza.setPrice(BigDecimal.ZERO);
        
        return defaultPizza;
    }
}

