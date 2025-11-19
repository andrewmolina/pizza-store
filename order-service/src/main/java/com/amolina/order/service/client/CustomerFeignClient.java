package com.amolina.order.service.client;

import com.amolina.order.service.client.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("customer-service")
public interface CustomerFeignClient {
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/customers/{customerId}",
            consumes = "application/json")
    CustomerDTO getCustomer(@PathVariable("customerId") Long customerId);
}

