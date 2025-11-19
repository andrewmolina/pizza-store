package com.amolina.order.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private Long itemId;
    private String pizzaName;
    private BigDecimal pizzaPrice;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;
    private Long customerId;
    private String customerFirstname;
    private String customerLastname;
    private String customerEmail;
    private String customerPhone;
}

