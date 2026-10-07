package com.mbope.shop.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import com.mbope.shop.model.OrderRequest;
import com.mbope.shop.service.OrderService;

@SpringBootTest 
@RecordApplicationEvents 
class OrderEventPublisherTest {
    @Autowired 
    private OrderService orderService;

    @Autowired 
    private ApplicationEvents events;

    @Test 
    void createOrder_shouldPublishOrderCreatedEvent() {
        // Arrange
        var orderRequest = new OrderRequest("customer-1", "Item A", 2, 10.0);
        // Act
        orderService.createOrder(orderRequest);
        long count = events.stream(OrderCreatedEvent.class).count();
        // Assert
        assertThat(count).isEqualTo(1);
    }

    @Test
    void cancelOrder_shouldPublishOrderCancelledEvent() {
        // Arrange
        var orderRequest = new OrderRequest("customer-1", "Item A", 2, 10.0);
        var order = orderService.createOrder(orderRequest);
        // Act
        orderService.cancelOrder(order.getId());
        long count = events.stream(OrderCancelledEvent.class).count();
        // Assert
        assertThat(count).isEqualTo(1);
    }
}
