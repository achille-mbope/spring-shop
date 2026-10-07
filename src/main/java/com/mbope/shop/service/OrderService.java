package com.mbope.shop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.mbope.shop.model.Order;
import com.mbope.shop.model.OrderRequest;

import com.mbope.shop.validation.CustomerIdValidator;
import com.mbope.shop.validation.ItemNameValidator;
import com.mbope.shop.validation.OrderValidator;
import com.mbope.shop.validation.OrderIdValidator;
import com.mbope.shop.validation.OrderRequestValidator;
import com.mbope.shop.validation.QuantityValidator;
import com.mbope.shop.validation.UnitPriceValidator;

public class OrderService {

    private final Map<Long, Order> orders = new HashMap<>();
    private final AtomicLong nextOrderId = new AtomicLong(1L);

    private final List<OrderValidator> validators = List.of(
        new OrderRequestValidator(),
        new CustomerIdValidator(),
        new ItemNameValidator(),
        new QuantityValidator(),
        new UnitPriceValidator()
    );

    private final OrderIdValidator orderIdValidator = new OrderIdValidator();

    public Order createOrder(OrderRequest request) {
        validators.forEach(validator -> validator.validate(request));

        Long orderId = nextOrderId.getAndIncrement();
        Order order = new Order(orderId, request.getCustomerId(), request.getItemName(), request.getQuantity(), request.getUnitPrice());
        orders.put(orderId, order);
        return order;
    }

    public Order getOrderById(Long orderId) {
        orderIdValidator.validate(orderId);
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
        order.cancel();
        return order;
    }
}
