# Changelog

## Unreleased

### Schritt 5 – Testabdeckung und Validierung der Service-Logik
- Die Validierung in `OrderService` wurde so angepasst, dass alle bestehenden Testfälle die erwarteten Exceptions und Meldungen korrekt auslösen.
- `quantity <= 0` führt jetzt zu `IllegalArgumentException` mit der Meldung `Menge muss größer als 0 sein`.
- `unitPrice <= 0` führt jetzt zu `IllegalArgumentException` mit der Meldung `Einzelpreis muss größer als 0 sein`.
- Leere oder null `customerId`-Werte lösen jetzt die passende Validierungsfehlermeldung aus.
- Leere oder null `itemName`-Werte lösen jetzt die passende Validierungsfehlermeldung aus.
- `getOrdersByCustomerId(...)` behandelt `null`- und leere Suchkriterien nun sauber und liefert keine fehlerhaften Ergebnisse mehr.

### Schritt 4 – Verifikation
- Der Projekt-Testlauf wurde erfolgreich ausgeführt.
- Maven-Check: `./mvnw test -q` -> erfolgreich abgeschlossen.

### Schritt 3 – Geschäftslogik und Fehlerbehandlung
- `OrderService` wurde eingeführt.
- `createOrder(...)` validiert nun Pflichtfelder und Grenzwerte in der Reihenfolge der Tests.
- `getOrderById(...)` liefert bestehende Bestellungen zurück und wirft `OrderNotFoundException`, falls die Bestellung nicht existiert.
- `OrderNotFoundException` kapselt die Fehlernachricht mit dem erwarteten Format: `Bestellung nicht gefunden: <id>`.

### Schritt 2 – Bestellmodell und Request-Validierung
- `OrderRequest` wurde ergänzt, damit Bestellungen mit `customerId`, `itemName`, `quantity` und `unitPrice` sauber modelliert werden können.
- `Order` wurde erweitert, damit die Bestellung mit ID, Kundenbezug, Artikelinformationen und Gesamtpreis als Domänenobjekt sauber genutzt werden kann.
- Die Gleichheitslogik wurde ergänzt, damit Bestellobjekte in Tests zuverlässig verglichen werden können.

### Schritt 1 – Projektbasis und Anforderungen
- Die Spring Boot-Basis wurde im bestehenden Projekt konsistent erhalten.
- Die fachlichen Anforderungen aus `docs/Requirements.md` wurden als Grundlage für die Implementierung genutzt.
- Der Service- und Modellumfang wurde auf die in den Tests beschriebenen Bestell-Use-Cases eingegrenzt.
