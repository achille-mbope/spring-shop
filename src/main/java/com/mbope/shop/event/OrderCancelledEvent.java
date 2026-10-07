package com.mbope.shop.event;

import java.time.Instant;

// Wir benutzen  Java Records weil sie eine kompakte Möglichkeit bieten, unveränderliche Datenstrukturen zu erstellen. 
// In diesem Fall repräsentiert das Record OrderCancelledEvent ein Ereignis, das auftritt, wenn eine Bestellung storniert wird. 
// Es enthält die ID der Bestellung, die Kunden-ID und den Zeitpunkt des Ereignisses.
public record OrderCancelledEvent(Long orderId, String customerId, Instant occuredAt) {
    public OrderCancelledEvent(Long orderId, String customerId) {
        this(orderId, customerId, Instant.now());
    }
}
