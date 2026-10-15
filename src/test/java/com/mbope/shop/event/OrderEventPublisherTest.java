package com.mbope.shop.event;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import com.mbope.shop.model.OrderRequest;
import com.mbope.shop.service.OrderService;
import com.mbope.shop.model.OrderLineRequest;

@SpringBootTest 
@RecordApplicationEvents 
class OrderEventPublisherTest {
    @Autowired 
    private OrderService orderService;

    @Autowired 
    private ApplicationEvents events;

    private OrderRequest singleLine(String customerId) {
        return new OrderRequest(customerId,
                List.of(new OrderLineRequest("item-A", 1, 10.0)));
    }

    @Test 
    void createOrder_shouldPublishOrderCreatedEvent() {
        // Arrange
        var orderRequest = singleLine("customer-1");
        // Act
        orderService.createOrder(orderRequest);
        long count = events.stream(OrderCreatedEvent.class).count();
        // Assert
        assertThat(count).isEqualTo(1);
    }

    @Test
    void cancelOrder_shouldPublishOrderCancelledEvent() {
        // Arrange
        var orderRequest = singleLine("customer-1");
        var order = orderService.createOrder(orderRequest);
        // Act
        orderService.cancelOrder(order.getId());
        long count = events.stream(OrderCancelledEvent.class).count();
        // Assert
        assertThat(count).isEqualTo(1);
    }
}
