package com.mbope.shop.service;

import com.mbope.shop.model.Order;
import com.mbope.shop.model.OrderLineRequest;
import com.mbope.shop.model.OrderRequest;
import com.mbope.shop.model.OrderStatus;
import com.mbope.shop.repository.InMemoryOrderRepository;
import com.mbope.shop.validation.CompositeOrderLineValidator;
import com.mbope.shop.validation.CustomerIdValidator;
import com.mbope.shop.validation.ItemNameValidator;
import com.mbope.shop.validation.OrderValidator;
import com.mbope.shop.validation.QuantityValidator;
import com.mbope.shop.validation.UnitPriceValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest {

    private final OrderService orderService = new OrderService(
            new InMemoryOrderRepository(),
            List.of(
                    new CustomerIdValidator(),
                    new CompositeOrderLineValidator(
                            List.of(new ItemNameValidator(),
                                    new QuantityValidator(),
                                    new UnitPriceValidator()))
            ),
            event -> { /* no-op */ }
    );

    private OrderRequest singleLine(String customerId, String itemName,
                                     int quantity, double unitPrice) {
        return new OrderRequest(customerId,
                List.of(new OrderLineRequest(itemName, quantity, unitPrice)));
    }

    @Test
    void createOrder_shouldReturnOrderWithCorrectTotal() {
        Order order = orderService.createOrder(singleLine("customer-1", "item-A", 2, 10.0));

        assertThat(order.getCustomerId()).isEqualTo("customer-1");
        assertThat(order.getLines()).hasSize(1);
        assertThat(order.getLines().get(0).getItemName()).isEqualTo("item-A");
        assertThat(order.getLines().get(0).getQuantity()).isEqualTo(2);
        assertThat(order.getLines().get(0).getUnitPrice()).isEqualTo(10.0);
        assertThat(order.getTotal()).isEqualTo(20.0);
    }

    @Test
    void createOrder_shouldSumMultipleLines() {
        OrderRequest request = new OrderRequest("customer-1", List.of(
                new OrderLineRequest("item-A", 2, 10.0),
                new OrderLineRequest("item-B", 1, 5.0)
        ));

        Order order = orderService.createOrder(request);

        assertThat(order.getLines()).hasSize(2);
        assertThat(order.getTotal()).isEqualTo(25.0);
    }

    @Test
    void createOrder_shouldThrowExceptionWhenQuantityIsZero() {
        assertThatThrownBy(() -> orderService.createOrder(
                singleLine("customer-1", "item-A", 0, 10.0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Menge muss größer als 0 sein");
    }

    @Test
    void createOrder_shouldThrowExceptionWhenQuantityIsNegative() {
        assertThatThrownBy(() -> orderService.createOrder(
                singleLine("customer-1", "item-A", -1, 10.0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Menge muss größer als 0 sein");
    }

    @Test
    void createOrder_shouldThrowExceptionWhenUnitPriceIsZero() {
        assertThatThrownBy(() -> orderService.createOrder(
                singleLine("customer-1", "item-A", 1, 0.0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preis muss größer als 0 sein");
    }

    @Test
    void createOrder_shouldThrowExceptionWhenUnitPriceIsNegative() {
        assertThatThrownBy(() -> orderService.createOrder(
                singleLine("customer-1", "item-A", 1, -5.0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preis muss größer als 0 sein");
    }

    @Test
    void createOrder_shouldThrowExceptionWhenCustomerIdIsBlank() {
        assertThatThrownBy(() -> orderService.createOrder(
                singleLine("   ", "item-A", 1, 10.0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Kunden-ID darf nicht leer sein");
    }

    @Test
    void createOrder_shouldThrowExceptionWhenItemNameIsBlank() {
        assertThatThrownBy(() -> orderService.createOrder(
                singleLine("customer-1", "", 1, 10.0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Artikelname darf nicht leer sein");
    }

    @Test
    void createOrder_shouldInitializeStatusAsNew() {
        Order order = orderService.createOrder(singleLine("customer-1", "item-A", 1, 10.0));

        assertThat(order.getStatus()).isEqualTo(OrderStatus.NEW);
    }

    @Test
    void getOrderById_shouldReturnOrder() {
        Order created = orderService.createOrder(singleLine("customer-1", "item-A", 1, 5.0));

        Order found = orderService.getOrderById(created.getId());

        assertThat(found).isEqualTo(created);
    }

    @Test
    void getOrderById_shouldThrowExceptionWhenNotFound() {
        assertThatThrownBy(() -> orderService.getOrderById(999L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage("Bestellung nicht gefunden: 999");
    }

    @Test
    void getAllOrders_shouldReturnAllCreatedOrders() {
        orderService.createOrder(singleLine("customer-1", "item-A", 1, 10.0));
        orderService.createOrder(singleLine("customer-2", "item-B", 2, 5.0));

        assertThat(orderService.getAllOrders()).hasSize(2);
    }

    @Test
    void getOrdersByCustomer_shouldReturnOnlyMatchingOrders() {
        orderService.createOrder(singleLine("customer-1", "item-A", 1, 10.0));
        orderService.createOrder(singleLine("customer-2", "item-B", 2, 5.0));
        orderService.createOrder(singleLine("customer-1", "item-C", 3, 1.0));

        var customerOrders = orderService.getOrdersByCustomer("customer-1");

        assertThat(customerOrders).hasSize(2);
        assertThat(customerOrders)
                .allMatch(o -> o.getCustomerId().equals("customer-1"));
    }

    @Test
    void cancelOrder_shouldSetStatusToCancelled() {
        Order created = orderService.createOrder(singleLine("customer-1", "item-A", 1, 10.0));

        Order cancelled = orderService.cancelOrder(created.getId());

        assertThat(cancelled.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    void cancelOrder_shouldThrowExceptionWhenAlreadyCancelled() {
        Order created = orderService.createOrder(singleLine("customer-1", "item-A", 1, 10.0));
        orderService.cancelOrder(created.getId());

        assertThatThrownBy(() -> orderService.cancelOrder(created.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Bestellung kann nicht storniert werden im Status: CANCELLED");
    }

    @Test
    void cancelOrder_shouldThrowExceptionWhenNotFound() {
        assertThatThrownBy(() -> orderService.cancelOrder(999L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage("Bestellung nicht gefunden: 999");
    }
}