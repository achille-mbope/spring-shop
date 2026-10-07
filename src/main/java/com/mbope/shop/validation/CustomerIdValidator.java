package com.mbope.shop.validation;

import org.springframework.stereotype.Component;

import com.mbope.shop.model.OrderRequest;

@Component 
public class CustomerIdValidator implements OrderValidator {

    @Override
    public void validate(OrderRequest orderRequest) {
        if (orderRequest.getCustomerId() == null) {
            throw new IllegalArgumentException("Kunden-ID darf nicht null sein");
        }
        if (orderRequest.getCustomerId().isBlank()) {
            throw new IllegalArgumentException("Kunden-ID darf nicht leer sein");
        }
    }
    
}
