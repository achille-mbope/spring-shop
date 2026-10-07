package com.mbope.shop.metrics;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mbope.shop.event.OrderCancelledEvent;
import com.mbope.shop.event.OrderCreatedEvent;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

public class OrderMetricsTest {
    private MeterRegistry meterRegistry;
    private OrderMetrics orderMetrics;

    @BeforeEach 
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        orderMetrics = new OrderMetrics(meterRegistry);
    }

    @Test
    void onOrderCreated_shouldIncrementOrderCreatedCounter() {
        // Arrange
        double initialCount = meterRegistry.counter("orders.created").count();
        // Act
        orderMetrics.onOrderCreated(new OrderCreatedEvent(1L, "customer-1", 100.0));
        // Assert
        double newCount = meterRegistry.counter("orders.created").count();
        assertThat(newCount).isEqualTo(initialCount + 1);
    }

    @Test 
    void onOrderCreated_shouldRecordTotalAmount() {
        orderMetrics.onOrderCreated(new OrderCreatedEvent(1L, "c1", 42.5, Instant.now()));

        double total = meterRegistry.get("orders.amount")
                .summary().totalAmount();

        assertThat(total).isEqualTo(42.5);
    }

    @Test 
    void onOrderCancelled_shouldIncrementCounter() {
        orderMetrics.onOrderCancelled(new OrderCancelledEvent(1L, "c1", Instant.now()));

        double count = meterRegistry.get("orders.cancelled")
                .counter().count();

        assertThat(count).isEqualTo(1.0);
    }
}
