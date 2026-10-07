package com.mbope.shop.validation;

import org.springframework.stereotype.Component;

@Component 
public class OrderIdValidator {

    public void validate(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID darf nicht null sein");
        }
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID muss größer als 0 sein");
        }
    }
}
