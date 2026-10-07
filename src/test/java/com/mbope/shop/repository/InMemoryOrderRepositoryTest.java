package com.mbope.shop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.mbope.shop.model.Order;

class InMemoryOrderRepositoryTest {
    private OrderRepository repository;

    @BeforeEach 
    void setUp(){
        repository = new InMemoryOrderRepository();
    }

    @Test 
    void save_shouldAssignIdAndStoreOrder() {
        // Arrange
        Order order = new Order(null, "customer-1", "Item A", 2, 10.0);
        // Act
        Order savedOrder = repository.save(order);
        // Assert
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(repository.findById(savedOrder.getId())).contains(savedOrder);
    }

    @Test 
    void save_shouldReturnSameInstanceOnRepeatedFind() {
        // Arrange
        Order order = new Order("customer-1", "Item A", 2, 10.0);
        Order savedOrder = repository.save(order);
        // Act
        Optional<Order> found = repository.findById(savedOrder.getId());
        // Assert
        assertThat(found).contains(savedOrder);
    }

    @Test 
    void findById_shouldReturnEmptyWhenNotFound() {
        assertThat(repository.findById(999L)).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllSavedOrders() {
        // Arrange
        Order order1 = new Order("customer-1", "Item A", 2, 10.0);
        Order order2 = new Order("customer-2", "Item B", 1, 20.0);
        repository.save(order1);
        repository.save(order2);
        // Act
        List<Order> allOrders = repository.findAll();
        // Assert
        assertThat(allOrders).containsExactlyInAnyOrder(order1, order2);
    }

    void findAll_shouldReturnEmptyListWhenNoOrders() {
        assertThat(repository.findAll()).isEmpty();
    }
}
