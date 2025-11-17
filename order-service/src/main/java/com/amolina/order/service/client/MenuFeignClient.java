package com.amolina.order.service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.amolina.order.service.client.dto.PizzaDTO;

@FeignClient("menu-service")
public interface MenuFeignClient {
    @RequestMapping(
            method= RequestMethod.GET,
            value="/api/pizzas/{pizzaId}",
            consumes="application/json")
    PizzaDTO getPizza(@PathVariable("pizzaId") String pizzaId);
}
