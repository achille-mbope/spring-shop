package com.mbope.shop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mbope.shop.model.Order;
import com.mbope.shop.model.OrderRequest;
import com.mbope.shop.repository.OrderRepository;
import com.mbope.shop.validation.OrderValidator;
import com.mbope.shop.validation.OrderIdValidator;

@Service
public class OrderService {

    private final OrderRepository repository;

    private final List<OrderValidator> validators;

    private final OrderIdValidator orderIdValidator = new OrderIdValidator();

    public OrderService(OrderRepository repository, List<OrderValidator> validators) {
        this.repository = repository;
        this.validators = validators;
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
