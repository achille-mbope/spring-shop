package com.mbope.shop.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component 
public class OrderEventListener {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);

    @EventListener 
    public void onOrderCreated(OrderCreatedEvent event) {
        logger.info("Order created: orderId={}, customerId={}, totalPrice={}", 
            event.orderId(), event.customerId(), event.totalPrice());
    }

    @EventListener 
    public void onOrderCancelled(OrderCancelledEvent event) {
        logger.info("Order cancelled: orderId={}, customerId={}", 
            event.orderId(), event.customerId());
    }
}
