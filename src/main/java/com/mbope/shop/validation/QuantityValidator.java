package com.mbope.shop.validation;

import org.springframework.stereotype.Component;

import com.mbope.shop.model.OrderRequest;

@Component 
public class QuantityValidator implements OrderValidator {

    @Override
    public void validate(OrderRequest orderRequest) {
        if (orderRequest.getQuantity() <= 0) {
            throw new IllegalArgumentException("Menge muss größer als 0 sein");
        }
    }
    
}
