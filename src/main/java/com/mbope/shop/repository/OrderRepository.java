package com.mbope.shop.repository;

import java.util.List;
import java.util.Optional;

import com.mbope.shop.model.Order;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findAll();
}
