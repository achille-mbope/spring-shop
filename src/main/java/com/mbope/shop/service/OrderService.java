package com.mbope.shop.service;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.mbope.shop.event.OrderCreatedEvent;
import com.mbope.shop.event.OrderCancelledEvent;
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
    
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository repository, List<OrderValidator> validators, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.validators = validators;
        this.eventPublisher = eventPublisher;
    }

    public Order createOrder(OrderRequest request) {
        validators.forEach(validator -> validator.validate(request));


        Order order = new Order(
            request.getCustomerId(), 
            request.getItemName(), 
            request.getQuantity(), 
            request.getUnitPrice()
        );

        Order savedOrder = repository.save(order);
        eventPublisher.publishEvent(new OrderCreatedEvent(
            savedOrder.getId(), 
            savedOrder.getCustomerId(), 
            savedOrder.getTotalPrice()
        ));

        return savedOrder;
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

        eventPublisher.publishEvent(new OrderCancelledEvent(
            order.getId(), 
            order.getCustomerId()
        ));
        
        return order;
    }
}
