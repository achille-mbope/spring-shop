package com.mbope.shop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.mbope.shop.model.Order;
import com.mbope.shop.model.OrderRequest;

public class OrderService {

    private final Map<Long, Order> orders = new HashMap<>();
    private final AtomicLong nextOrderId = new AtomicLong(1L);

    public Order createOrder(OrderRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("OrderRequest darf nicht null sein");
        }

        if (request.getCustomerId() == null) {
            throw new IllegalArgumentException("Kunden-ID darf nicht null sein");
        }
        if (request.getCustomerId().isBlank()) {
            throw new IllegalArgumentException("Kunden-ID darf nicht leer sein");
        }

        if (request.getItemName() == null) {
            throw new IllegalArgumentException("Artikelname darf nicht null sein");
        }
        if (request.getItemName().isBlank()) {
            throw new IllegalArgumentException("Artikelname darf nicht leer sein");
        }

        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Menge muss größer als 0 sein");
        }

        if (request.getUnitPrice() <= 0) {
            throw new IllegalArgumentException("Einzelpreis muss größer als 0 sein");
        }

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
}
