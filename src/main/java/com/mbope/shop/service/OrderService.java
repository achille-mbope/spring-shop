package com.mbope.shop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.mbope.shop.model.Order;
import com.mbope.shop.model.OrderRequest;
import com.mbope.shop.repository.OrderRepository;
import com.mbope.shop.validation.CustomerIdValidator;
import com.mbope.shop.validation.ItemNameValidator;
import com.mbope.shop.validation.OrderValidator;
import com.mbope.shop.validation.OrderIdValidator;
import com.mbope.shop.validation.OrderRequestValidator;
import com.mbope.shop.validation.QuantityValidator;
import com.mbope.shop.validation.UnitPriceValidator;


public class OrderService {

    private final OrderRepository repository;

    private final List<OrderValidator> validators = List.of(
        new OrderRequestValidator(),
        new CustomerIdValidator(),
        new ItemNameValidator(),
        new QuantityValidator(),
        new UnitPriceValidator()
    );

    private final OrderIdValidator orderIdValidator = new OrderIdValidator();

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public Order createOrder(OrderRequest request) {
        validators.forEach(validator -> validator.validate(request));


        Order order = new Order(
            request.getCustomerId(), 
            request.getItemName(), 
            request.getQuantity(), 
            request.getUnitPrice()
        );
        
        return repository.save(order);
    }

    public Order getOrderById(Long orderId) {
        orderIdValidator.validate(orderId);
        return repository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    public List<Order> getOrdersByCustomerId(String customerId) {
        return repository.findAll().stream()
                .filter(order -> customerId.equals(order.getCustomerId()))
                .toList();

        
    }

    public Order cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);
        order.cancel();
        return order;
    }
}
