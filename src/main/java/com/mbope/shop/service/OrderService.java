package com.mbope.shop.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.mbope.shop.model.Order;
import com.mbope.shop.model.OrderRequest;

public class OrderService {

    private final Map<Long, Order> orders = new HashMap<>();
    private final AtomicLong nextOrderId = new AtomicLong(1L);

    public Order createOrder(OrderRequest request) {
        if(request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }


        Long orderId = nextOrderId.getAndIncrement();
        Order order = new Order(orderId, request.getCustomerId(), request.getItemName(), request.getQuantity(), request.getUnitPrice());
        orders.put(orderId, order);
        return order;
    }

    public Order getOrderById(Long orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        return order;
    }

    
}
