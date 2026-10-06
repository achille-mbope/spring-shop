package com.mbope.shop.validation;

import com.mbope.shop.model.OrderRequest;

public class OrderRequestValidator implements OrderValidator {

    @Override
    public void validate(OrderRequest orderRequest) {
        if (orderRequest == null) {
            throw new IllegalArgumentException("OrderRequest darf nicht null sein");
        }
    }
}
