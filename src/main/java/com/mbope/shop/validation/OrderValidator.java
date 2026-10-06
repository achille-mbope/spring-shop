package com.mbope.shop.validation;

import com.mbope.shop.model.OrderRequest;

public interface OrderValidator {
    void validate(OrderRequest orderRequest);
}
