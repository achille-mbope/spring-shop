package com.mbope.shop.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

class OrderTest {
    private Order newOrder(){
        return new Order(1L, "customer-1", "Item A", 2, 10.0);
    }

    @Test 
    void newOrder_shouldHaveStatusNew() {
        Order order = newOrder();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.NEW);
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
