package com.mbope.shop.validation;

import org.springframework.stereotype.Component;

import com.mbope.shop.model.OrderRequest;

@Component 
public class UnitPriceValidator implements OrderValidator {

    @Override
    public void validate(OrderRequest orderRequest) {
        if (orderRequest.getUnitPrice() <= 0) {
            throw new IllegalArgumentException("Einzelpreis muss größer als 0 sein");
        }
    }
    
}
