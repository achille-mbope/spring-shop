package com.mbope.shop.service;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

import com.mbope.shop.model.Order;
import com.mbope.shop.model.OrderRequest;
import com.mbope.shop.model.OrderStatus;

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
                .hasMessageContaining("Menge muss größer als 0 sein");
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

    @Test
    void createOrder_shouldThrowExceptionForNegativeQuantity() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest("customer-1", "Item A", -1, 10.0);

        // Act & Assert
        assertThatThrownBy(() -> orderService.createOrder(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Menge muss größer als 0 sein");
    }

    @Test 
    void createOrder_shouldThrowExceptionForZeroQuantity() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest("customer-1", "Item A", 0, 10.0);

        // Act & Assert
        assertThatThrownBy(() -> orderService.createOrder(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Menge muss größer als 0 sein");
    }

    @Test 
    void createOrder_shouldThrowExceptionForNegativeUnitPrice() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest("customer-1", "Item A", 1, -10.0);

        // Act & Assert
        assertThatThrownBy(() -> orderService.createOrder(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Einzelpreis muss größer als 0 sein");
    }

    @Test 
    void createOrder_shouldThrowExceptionForZeroUnitPrice() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest("customer-1", "Item A", 1, 0.0);

        // Act & Assert
        assertThatThrownBy(() -> orderService.createOrder(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Einzelpreis muss größer als 0 sein");
    }

    @Test
    void createOrder_shouldThrowExceptionForNullCustomerId() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest(null, "Item A", 1, 10.0);

        // Act & Assert
        assertThatThrownBy(() -> orderService.createOrder(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Kunden-ID darf nicht null sein");
    }

    @Test
    void createOrder_shouldThrowExceptionForEmptyCustomerId() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest("", "Item A", 1, 10.0);

        // Act & Assert
        assertThatThrownBy(() -> orderService.createOrder(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Kunden-ID darf nicht leer sein");
    }

    @Test
    void createOrder_shouldThrowExceptionForNullItemName() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest("customer-1", null, 1, 10.0);

        // Act & Assert
        assertThatThrownBy(() -> orderService.createOrder(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Artikelname darf nicht null sein");
    }

    @Test 
    void createOrder_shouldThrowExceptionForEmptyItemName() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest("customer-1", "", 1, 10.0);

        // Act & Assert
        assertThatThrownBy(() -> orderService.createOrder(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Artikelname darf nicht leer sein");
    }

    @Test 
    void createOrder_shouldThrowExceptionForNullOrderRequest() {
        // Act & Assert
        assertThatThrownBy(() -> orderService.createOrder(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("OrderRequest darf nicht null sein");
    }

    @Test 
    void getOrderById_shouldThrowExceptionForNullOrderId() {
        // Act & Assert
        assertThatThrownBy(() -> orderService.getOrderById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Order ID darf nicht null sein");
    }

    @Test 
    void getOrderById_shouldThrowExceptionForNegativeOrderId() {
        // Act & Assert
        assertThatThrownBy(() -> orderService.getOrderById(-1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Order ID muss größer als 0 sein");
    }

    @Test 
    void getOrderById_shouldThrowExceptionForZeroOrderId() {
        // Act & Assert
        assertThatThrownBy(() -> orderService.getOrderById(0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Order ID muss größer als 0 sein");
    }

    @Test 
    void getAllOrders_shouldReturnAllCreatedOrders() {
        // Arrange
        OrderRequest orderRequest1 = new OrderRequest("customer-1", "Item A", 2, 10.0);
        OrderRequest orderRequest2 = new OrderRequest("customer-2", "Item B", 3, 15.0);
        OrderRequest orderRequest3 = new OrderRequest("customer-3", "Item A", 5, 10.0);
        Order createdOrder1 = orderService.createOrder(orderRequest1);
        Order createdOrder2 = orderService.createOrder(orderRequest2);
        Order createdOrder3 = orderService.createOrder(orderRequest3);

        // Act
        List<Order> allOrders = orderService.getAllOrders();

        // Assert
        assertThat(allOrders).containsExactlyInAnyOrder(createdOrder1, createdOrder2, createdOrder3);
    }

    @Test 
    void getAllOrders_shouldReturnEmptyListWhenNoOrdersCreated() {
        // Act
        List<Order> allOrders = orderService.getAllOrders();

        // Assert
        assertThat(allOrders).isEmpty();
    }

    @Test 
    void getOrdersByCustomerId_shouldReturnOrdersForGivenCustomerId() {
        // Arrange
        orderService.createOrder(new OrderRequest("customer-1", "Item A", 2, 10.0));
        orderService.createOrder(new OrderRequest("customer-2", "Item B", 3, 15.0));
        orderService.createOrder(new OrderRequest("customer-1", "Item C", 1, 20.0));
        // Act
        List<Order> customer1Orders = orderService.getOrdersByCustomerId("customer-1");
        // Assert
        assertThat(customer1Orders).hasSize(2);
        //assertThat(customer1Orders).containsExactlyInAnyOrder(createdOrder1, createdOrder3);
        assertThat(customer1Orders).allMatch(order -> order.getCustomerId().equals("customer-1"));
    }

    void createOrder_shouldInitialieStatusAsNew() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest("customer-1", "Item A", 2, 10.0);

        // Act
        Order order = orderService.createOrder(orderRequest);

        // Assert
        assertThat(order.getStatus()).isEqualTo(OrderStatus.NEW);
    }

    void cancelOrder_shouldChangeStatusToCancelled() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest("customer-1", "Item A", 2, 10.0);
        Order order = orderService.createOrder(orderRequest);

        // Act
        orderService.cancelOrder(order.getId());

        // Assert
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    void cancelOrder_shouldExceptionWhenNotFound() {
        // Act & Assert
        assertThatThrownBy(() -> orderService.cancelOrder(9999L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Bestellung nicht gefunden: 9999");
    }

}

