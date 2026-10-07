package com.mbope.shop.validation;

import org.springframework.stereotype.Component;

import com.mbope.shop.model.OrderRequest;

@Component 
public class ItemNameValidator implements OrderValidator {

    @Override
    public void validate(OrderRequest orderRequest) {
        if (orderRequest.getItemName() == null) {
            throw new IllegalArgumentException("Artikelname darf nicht null sein");
        }
        if (orderRequest.getItemName().isBlank()) {
            throw new IllegalArgumentException("Artikelname darf nicht leer sein");
        }
    }
}
