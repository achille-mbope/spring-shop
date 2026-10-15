package com.mbope.shop.repository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mbope.shop.model.Order;

class InMemoryOrderRepositoryTest {
    private OrderRepository repository;

    @BeforeEach 
    void setUp(){
        repository = new InMemoryOrderRepository();
    }

    private Order newOrder(String customerId){
        return new Order(customerId, List.of(new OrderLine("Item A",1, 10.0)));
    }

    @Test 
    void save_shouldAssignIdAndStoreOrder() {
        // Arrange
        Order order = newOrder("customer-1");
        // Act
        Order savedOrder = repository.save(order);
        // Assert
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(repository.findById(savedOrder.getId())).contains(savedOrder);
    }

    @Test 
    void save_shouldReturnSameInstanceOnRepeatedFind() {
        // Arrange & Act
        Order order = repository.save(newOrder("customer-1"));

        // Act
        Optional<Order> found = repository.findById(order.getId());
        // Assert
        assertThat(found).containsSame(order);
    }

    @Test 
    void findById_shouldReturnEmptyWhenNotFound() {
        assertThat(repository.findById(999L)).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllSavedOrders() {
        // Arrange
        Order order1 = new Order("customer-1");
        Order order2 = new Order("customer-2");
        repository.save(order1);
        repository.save(order2);
        // Act
        List<Order> allOrders = repository.findAll();
        // Assert
        assertThat(allOrders).containsExactlyInAnyOrder(order1, order2);
    }

    @Test 
    void findAll_shouldReturnEmptyListWhenNoOrders() {
        assertThat(repository.findAll()).isEmpty();
    }
}
