package com.mbope.shop.service;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long orderId) {
        super("Bestellung nicht gefunden: " + orderId);
    }
}
