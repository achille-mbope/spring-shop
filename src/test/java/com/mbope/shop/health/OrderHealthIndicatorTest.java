package com.mbope.shop.health;

import com.mbope.shop.model.Order;
import com.mbope.shop.repository.InMemoryOrderRepository;
import com.mbope.shop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import static org.assertj.core.api.Assertions.assertThat;

class OrderHealthIndicatorTest {

    @Test
    void health_shouldBeUpWhenRepositoryResponds() {
        OrderRepository repository = new InMemoryOrderRepository();
        repository.save(new Order("c1", "item-A", 1, 10.0));
        OrderHealthIndicator indicator = new OrderHealthIndicator(repository);

        Health health = indicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsEntry("orderCount", 1);
    }

    @Test
    void health_shouldBeUpWithZeroOrders() {
        OrderRepository repository = new InMemoryOrderRepository();
        OrderHealthIndicator indicator = new OrderHealthIndicator(repository);

        Health health = indicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsEntry("orderCount", 0);
    }
}