package com.mbope.shop.event;

import java.time.Instant;

// Wir benutzen  Java Records weil sie eine kompakte Möglichkeit bieten, unveränderliche Datenstrukturen zu erstellen. 
// In diesem Fall repräsentiert das Record OrderCreatedEvent ein Ereignis, das auftritt, wenn eine Bestellung erstellt wird. 
// Es enthält die ID der Bestellung, die Kunden-ID, den Gesamtpreis und den Zeitpunkt des Ereignisses.
public record OrderCreatedEvent(Long orderId, String customerId, double totalPrice, Instant occuredAt) {

    public OrderCreatedEvent(Long orderId, String customerId, double totalPrice) {
        this(orderId, customerId, totalPrice, Instant.now());
    }
}
