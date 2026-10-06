package com.mbope.shop.service;

import com.mbope.shop.model.Order;
import com.mbope.shop.model.OrderRequest;
import com.mbope.shop.service.OrderNotFoundException;
import com.mbope.shop.service.OrderService;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest {

    private final OrderService orderService = new OrderService();

    // Bestellung anlegen, Gesamtpreis berechnen und prüfen, ob der Gesamtpreis korrekt ist
    @Test
    void createOrder_shouldReturnOrderWithCorrectTotalPrice() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest("customer-1", "Item A", 2, 10.0);

        // Act
        Order order = orderService.createOrder(orderRequest);

        // Assert
        assertThat(order.getCustomerId()).isEqualTo("customer-1");
        assertThat(order.getItemName()).isEqualTo("Item A");
        assertThat(order.getQuantity()).isEqualTo(2);
        assertThat(order.getUnitPrice()).isEqualTo(10.0);
        assertThat(order.getTotalPrice()).isEqualTo(20.0);
    }

    @Test
    void createOrder_shouldThrowExceptionForInvalidQuantity() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest("customer-1", "Item A", 0, 10.0);

        // Act & Assert
        assertThatThrownBy(() -> orderService.createOrder(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity must be greater than zero");
    }

    @Test 
    void getOrderById_shouldReturnOrder() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest("customer-1", "Item A", 3, 10.0);
        Order createdOrder = orderService.createOrder(orderRequest);

        // Act
        Order retrievedOrder = orderService.getOrderById(createdOrder.getId());

        // Assert
        assertThat(retrievedOrder).isEqualTo(createdOrder);
    }

    @Test
    void getOrderById_shouldThrowExceptionForNonExistingOrder() {
        // Act & Assert
        assertThatThrownBy(() -> orderService.getOrderById(9999L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Bestellung nicht gefunden: 9999");
    }
}
