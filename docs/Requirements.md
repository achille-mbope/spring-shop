# Requirements klären
Eine saubere Anfordeungsanalyse ist die Grundlage jeder erfolgreichen API. Wir unterteilen die Anforderungen  in <b>funktionale</b>, <b>nicht-funcktionale</b>, <b>fachliche Regeln</b>, <b>Schnittstellen</b>, <b>Datenmodell</b>, <b>Fehlerfälle</b> und <b>Abgrenzungen</b>

## Projektkontext und Zielsetzung
<b>Ziel:</b> Entwicklung einer kleinen, aber strukturierten <b>Order-API</b>, die Bestellungen anlegen, abrufen und auflisten kann. Sie soll Refenrenzimplementierung für TDD, SOLID und Spring Boot diesen.

<b>Zielgruppe:</b>
- Interne Client-Anwedungen (Webshop-Frontend, Mobile App)
- Andere Microservices (z.B. Inventory-Service, Payment-Service)
- Test-Teams und Entwickler, die die API erweitern

<b>Erfolgskriterien:</b>
- Alle funcktionalen Anforderungen sind durch automatisierte Tests abgedeckt
- Testabdeckung (Line Coverage) >= 80%
- SOLID-Prinzipien sind nachvollziehbar umgesetzt
- Die API ist über HTTP/JSON erreichbar und dokumentiert (OpenAPI)

## Funktionale Anforderungen
1. <b>Bestellungen anlegen</b>
    - <b>Endpunkt:</b> `POST /api/v1/orders`
    - <b>Eingabe (JSON-Body):</b>
        - `customerId` (String, Pflicht, nicht leer)
        - `itemName` (String, Pflicht, nicht leer, max. 255 Zeichen)
        - `quantity` (Integer, Pflicht, >=1, <=1000)
        - `unitPrice` (Decimal, Pflicht, > 0, max. 2 Nachkommastellen)
        - `currency` (String, optional, Default `EUR`, ISO-4217)
    - <b>Verhalten:</b>
        - Bei gültiger Eingabe: Bestellung wird angelegt und mit <b>HTTP 201 Created</b> zurückgegeben
        - Die Response enthält zusätzlich : `id`, `total`, `createdAt`, `status`
        - Der Gesamtpreis wird als `quantity * unitPrice` berechnet
        - Der initiale Status ist `NEW`
    - <b>Idempotenz:</b> Nicht idempotent (mehrfacher Aufruf erzeugt mehrere Bestellungen)

2. <b>Bestellung abrufen</b>
    - <b>Endpunkt:</b> `GET /api/v1/orders/{id}`
    - <b>Eingabe:</b> Pfadparamter `id` (Long >=1)
    - <b>Verhalten:</b>
        - Vorhandende ID -> HTTP 200 mit Bestellung
        - Nicht vorhandene ID -> HTTP 404 mit Fehlermeldung
    - <b>Idempotenz:</b> Ja

3. <b>Alle Bestellungen abrufen</b>
    - <b>Endpunkt:</b> `GET /api/v1/orders/`
    - <b>Optionale Query-Parameter:</b>
        - `customerId` (Filter nach Kunden)
        - `status` (Filter nach Status, z.B `NEW`,`PAID`,`SHIPPED`)
        - `page`,`size` (Pagination, Default: page=0, size=20)
        - `sort` (z.B. `createdAt,desc`)
    - <b>Verhalten:</b>
        - HTTP 200 mit paginierter Liste
        - Response-Format: `{"content": [...], "page": 0, "size": 20, "totalElements": 42}`
4. <b>Bestellung stornieren</b>
    - <b>Endpunkt:</b> `DELETE /api/v1/orders/{id}`
    - <b>Verhalten:</b>
        - Nur Bestellungen im Status `NEW` können storniert werden
        - Status wechselt zu `CANCELLED`
        - Nicht mehr änderbar danach
        - HTTP 204 bei Erflog, HTTP 409 bei Konflikt (bereits versendet), HTTP 404 wenn nicht gefunden
5. <b>Bestellstatus aktualisieren</b>
    - <b>Endpunkt:</b> `PATCH /api/v1/orders/{id}/status`
    - <b>Erlaubte Übergänge:</b>
        - `NEW` -> `PAID` -> `SHIPPED` -> `DELIVERED`
        - `NEW` -> `CANCELLED`
        - `PAID` -> `CANCELLED` (mit Rückerstattungshinweis)
    - <b>Ungültige Übergänge:</b> HTTP 409 Conflict

## Nicht-funktionale Anforderungen
1. <b>Performance</b>
    - Antwortzeit im 95. Perzentil < 200 ms bei <= 1000 gleichzeitigen Nutzern
    - Durchstaz: mind. 500 Requests/Sekunde (lokale Testumgebung)
2. <b>Skalierbarkeit</b>
    - Stateless Design (horizontale Skalierung möglich)
    - Persistenz später austauschbar (H2 -> PostgreSQL -> MongoDB)
3. <b>Sicherheit:</b>
    - Authentifizierung via Bearer Token (JWT) - in dieser AUfbaustufe optional
    - Eingabevalidierung gegen XSS unf Injection
    - Keine sensiblen Daten in Logs
    - HTTPS-Pflicht in Produktion
4. <b>Fehlertoleranz:</b>
    - Zentrale Fehlerbehandlung via `@RestControllerAdvice`
    - Einheitliches Fehlerformat (`application/problem+json`, RFC 7807)
    - Keine Stacktraces in Responses
5. <b>Wartbarkeit</b>
    - SOLID-Prinzipien
    - Testabdeckung >= 80%
    - Klare Paketstruktur (domain, application, infrastucture)
    - JavaDoc auf öffentlichen interfaces
6. <b>Testbarkeit:</b>
    - Unit-Tests ohne Spring Context
    - Integrationstest mit `@SpringBootTest` und `MockMvc`
    - Testdaten über Builder-Pattern
7. <b>Beobachtbarkeit (Observability)</b>
    - Spring Boot Actuator mit `/health`, `/metrics`, `/info`
    - Strukturierte Logs (JSON) mit Correlation-ID
    - Optional: Micrometer + Prometheus
8. <b>Internationalisierung</b>
    - Fehlermeldungen über `MessageSource` austauschbar (DE/EN)

## Fachliche Regeln (Business Rules)
1. `quantity` muss >=1 und <=1000 sein
2. `unitPrice` muss > 0 sein
3. `customerId` darf nicht leer sein und muss dem Muster `^[A-Za-z0-9]{1,64}$` entsprechen
4. `itemName` darf max. 255 Zeichen enthalten
5. Gesamtpreis = `quantity * unitPrice`, kaufmännisch gerundet auf 2 Nachkommastellen
6. Eine Bestellung im Status `DELIVERED` oder `CANCELLED` ist unveränderlich
7. Statusübergänge nur gemäß definierter State Machine 
8. Währung muss gültiger ISO-4217-Code sein
9. Eine Bestellung kann nur einen Kunden zugeordnet sein
10. Doppelte Bestellungen (gleicher Kunde, gleicher Artikel, innerhalb 5 s) werden als Duplikat behandelt werden

## Datenmodell
<b>Order (Aggregate Root): </b>


| Field | Type | Pflicht | Beschreibung |
|---|---|---|---|
| id | Long | ja | EIndeitige ID (autogeneriert) |
| customerId | String | ja | Referenz auf Kunden |
| itemName | String | ja | Bezeichnung des Artikels |
| quantity | int | ja | Menge |
| unitPrice | BigDecimal | ja |Einzelpreis |
| total | BigDecimal | ja | Berechneter Preis |
| currency | String	| ja | ISO-4217 (Default: EUR) |
| status | Enum | ja | NEW, PAID, SHIPPED, DELIVERED, CANCELLED |
| createdAt	| Instant | ja | Erstellzeitpunkt |
| updatedAt	| Instant | ja | Letzte Änderung |

<b>OrderStatus (ENUM):</b> `NEW`, `PAID`, `SHIPPED`, `DELIVERED`, `CANCELLED`

## Schnittstellen & Integrationen
### Interne Schnittstellen:
- `OrderService` - Geschäftslogik
- `OrderRepository` - Persistenz (in dieser Ausbaustufe In-Memory, später JPA)
- `OrderValidator` - Validierungskette (Chain of Responsibility)

### Externe Schnittestellen (Ausblick):
- `CustomerService` - Prüfung, ob Kunde existiert
- `InventoryService` - Prüfung ob Artikel verfügbar
- `PaymentService` - Zahlungsabwicklung
- `NotificationService` - Bestellbestätigung per E-Mail

## Fehlerfälle & HTTP-Statuscodes

| Situation | HTTP-Status | Fehlercode |
| --- | --- | --- |
| Erfolgreiche Erstellung | 201 | - |
| Erfolgreicher Abruf | 200 | - |
| Validierungsfehler (z.B. quantity = 0) | 400 | `VALIDATION_ERROR` |
| Bestellung nicht gefunden | 404 | `ORDER_NOT_FOUND` |
| Statuswechsel nicht erlaubt | 409 | `INVALID_STATE_TRANSITION` |
| Interner Fehler | 500 | `INTERNAL_ERROR` |
| Nicht authentifiziert (später) | 401 | `UNAUTHORIZED` |

### Einheitliches Fehlerformat (RFC 7807):
```json
{
    "type": "ORDER_NOT_FOUND",
    "title": "Bestellung nicht gefunden",
    "status": 404,
    "detail": "Bestellung mit ID 42 existiert nicht",
    "instance": "/api/v1/orders/42"
}
```

## Abgrenzungen (Out of Scope)
Folgende Punkte sind <b>bewusst nicht</b> Teil dieser Ausbaustufe:
- Persistente Datenbank (zunächst In-Memory mit `ConcurrentHashMap`)
- Authentifizierung/Autorisierung (kommt in iteration 2)
- Mehrsprachigkeit
- Bestellpositionen (Aktuel: 1 Artikel pro Bestellung)
- Versand- und Zahlungsintegration
- Event-basierte Kommunikation (Kafka, RabbitMQ)
- Frontend / UI
- Deployment (Docker, Kubernetes) - separat zu betrachte

## Akzeptanzkriterien
Eine Anforderung gilt as erfüllt, wenn:
1. Alle funktionalen Anforderungen sind implementiert
2. Jede Business Rule ist durch mindestens einen Test abgedeckt
3. Alle Tests laufen grün (`mvn test`) 
4. Die API ist über `curl` oder Postman erreichbar
5. Fehlerantworten folgen RFC 7807
6. Testabdeckung >= 80%
7. Code-Review durchgeführt und SOLID-Prinzipien nachweisbar

## Offene Fragen (zu klären mit Stakeholdern)
1. Soll die Währung pro Bestellung oder global konfiguriert werden?
2. Sind Teillieferungen / mehrere Artikel pro Bestellung geplant?
3. Wie lange sollen stornierte Bestellungen aufbewahrt werden (Retention) ?
4. Soll es eine Obergrenze für den Gesamtpreis geben (Fraud Prevention) ?
5. Wie wird ein "Kunde" identifiziert (interne ID, E-Mail, externe Referenz)?
6. Sind Soft Deletes gewünscht?