package com.mbope.shop.validation;

import org.springframework.stereotype.Component;

import com.mbope.shop.model.OrderRequest;

@Component 
public class OrderRequestValidator implements OrderValidator {

    @Override
    public void validate(OrderRequest orderRequest) {
        if (orderRequest == null) {
            throw new IllegalArgumentException("OrderRequest darf nicht null sein");
        }
    }
}
