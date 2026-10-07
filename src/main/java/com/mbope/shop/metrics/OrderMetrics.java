package com.mbope.shop.metrics;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.mbope.shop.event.OrderCancelledEvent;
import com.mbope.shop.event.OrderCreatedEvent;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;

@Component 
public class OrderMetrics {
    private final Counter orderCreatedCounter;
    private final Counter orderCancelledCounter;
    private final DistributionSummary orderAmountSummary;

    
    public OrderMetrics(MeterRegistry meterRegistry) {
        this.orderCreatedCounter = Counter.builder("orders.created")
                .description("Anzahl der erstellten Bestellungen")
                .register(meterRegistry);

        this.orderCancelledCounter = Counter.builder("orders.cancelled")
                .description("Anzahl der stornierten Bestellungen")
                .register(meterRegistry);

        this.orderAmountSummary = DistributionSummary.builder("orders.amount")
                .description("Verteilung der Bestellungen")
                .baseUnit("EUR")
                .publishPercentiles(0.5,0.95,0.99)
                .register(meterRegistry);
    }

    @EventListener 
    public void onOrderCreated(OrderCreatedEvent event) {
        orderCreatedCounter.increment();
        orderAmountSummary.record(event.totalPrice());
    }

    @EventListener 
    public void onOrderCancelled(OrderCancelledEvent event) {
        orderCancelledCounter.increment();
    }
}
