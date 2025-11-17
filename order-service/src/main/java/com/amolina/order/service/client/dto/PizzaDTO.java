package com.amolina.order.service.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PizzaDTO {
    private Long itemId;
    private String name;
    private String description;
    private BigDecimal price;
}

