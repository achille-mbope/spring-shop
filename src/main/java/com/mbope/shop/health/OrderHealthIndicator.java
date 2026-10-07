package com.mbope.shop.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.mbope.shop.repository.OrderRepository;

@Component
public class OrderHealthIndicator implements HealthIndicator {

    private final OrderRepository repository;

    public OrderHealthIndicator(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Health health() {
        try {
            int orderCount = repository.findAll().size();
            return Health.up()
                    .withDetail("orderCount", orderCount)
                    .build();
        } catch (Exception ex) {
            return Health.down(ex).build();
        }
    }
}