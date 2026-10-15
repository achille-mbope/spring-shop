package com.mbope.shop.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

import java.util.List;

class OrderTest {
    private Order newOrder(){
        return new Order(1L, "customer-1", List.of(new OrderLine("Item A", 2, 10.0)));
    }

    @Test 
    void newOrder_shouldHaveStatusNew() {
        Order order = newOrder();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.NEW);
    }

    @Test
    void getTotal_shouldSumAllLines() {
        Order order = new Order(1L, "customer-1", List.of(
            new OrderLine("Item A", 2, 10.0),
            new OrderLine("Item B", 1, 5.0)
        ));

        assertThat(order.getTotal()).isEqualTo(25.0);
    }

    @Test
    void getTotal_shouldBeZeroForEmptyLines() {
        Order order = new Order(1L, "customer-1", List.of());

        assertThat(order.getTotal()).isEqualTo(0.0);
    }

    @Test 
    void cancel_shouldChangeStatusToCancelled() {
        // Arrange
        Order order = newOrder();
        // Act
        order.cancel();
        // Assert
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test 
    void cancel_shouldThrowExceptionWhenAlreadyCancelled() {
        // Arrange
        Order order = newOrder();
        order.cancel(); // cancel the order
        // Act & Assert
        assertThatThrownBy(order::cancel)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Bestellung kann nicht storniert werden im Status: CANCELLED");
    }
}
