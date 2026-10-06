package com.mbope.shop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.mbope.shop.model.Order;
import com.mbope.shop.model.OrderRequest;
import com.mbope.shop.model.OrderStatus;

import com.mbope.shop.validation.CustomerIdValidator;
import com.mbope.shop.validation.ItemNameValidator;
import com.mbope.shop.validation.OrderValidator;
import com.mbope.shop.validation.QuantityValidator;
import com.mbope.shop.validation.UnitPriceValidator;

public class OrderService {

    private final Map<Long, Order> orders = new HashMap<>();
    private final AtomicLong nextOrderId = new AtomicLong(1L);

    private final List<OrderValidator> validators = List.of(
        new CustomerIdValidator(),
        new ItemNameValidator(),
        new QuantityValidator(),
        new UnitPriceValidator()
    );

    public Order createOrder(OrderRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("OrderRequest darf nicht null sein");
        }
        
        validators.forEach(validator -> validator.validate(request));

        Long orderId = nextOrderId.getAndIncrement();
        Order order = new Order(orderId, request.getCustomerId(), request.getItemName(), request.getQuantity(), request.getUnitPrice());
        orders.put(orderId, order);
        return order;
    }

    public Order getOrderById(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID darf nicht null sein");
        }
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID muss größer als 0 sein");
        }
        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        return order;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    public List<Order> getOrdersByCustomerId(String customerId) {
        if (customerId == null) {
            return new ArrayList<>();
        }

        List<Order> customerOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (customerId.equals(order.getCustomerId())) {
                customerOrders.add(order);
            }
        }
        return customerOrders;
    }

    public Order cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);
        
        if(order.getStatus() != OrderStatus.NEW) {
            throw new IllegalStateException("Bestellung kann nicht storniert werden im Status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);

        return order;
    }
}
