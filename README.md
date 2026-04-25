# Lakatos Tamás - Java Beadandó (S45FXF)

Ez a projekt egy Spring Boot MVC és InMemory adatbázis alapú REST API, amely a féléves egyetemi feladat követelményeit teljességgel megvalósítja (User, Post, Tag, Comment entitásokkal, köztük lévő 1:N és N:M kapcsolatokkal).

## Futtatás
A projekt futtatásához Java 17 szükséges. Semmilyen külső adatbázis-szerver (pl. MySQL) nem kell, hiszen a memória tárolja az adatokat (ConcurrentHashMap).

Linux / Mac terminálból:
```bash
./mvnw spring-boot:run
```

Windows parancssorból:
```cmd
mvnw.cmd spring-boot:run
```

## Tesztelés (Postman)
A repozitóriumban található a `postman_collection.json` fájl, ami tartalmazza a minimum kért 6 (összesen 8) végpontra vonatkozó teszteket.

1. Nyisd meg a Postman alkalmazást.
2. Kattints az **Import** gombra, és válaszd ki a `postman_collection.json` fájlt.
3. A szerver elindítása után futtasd a kéréseket 1-től 8-ig **sorrendben**. 
   *(Mivel egymásra épülnek: először usert és taget csinál, majd abból posztot, és végül teszteli a hibaüzeneteket dobó védelmeket).*

## Funkciók és Felépítés
* **Modellek (4 db):** `User`, `Post`, `Tag`, `Comment`. Ebből 4 alaptípus (`Long`, `String`, `Integer`, `Boolean`) és 1 Collection (`List`) mindegyikben jelen van.
* **Kapcsolatok (3 db):** User-Post (1:N), Post-Comment (1:N), Post-Tag (N:M).
* **Adatbázis és Szerviz:** Belső map alapú szálbiztos tárolás (`ConcurrentHashMap`, `AtomicLong`). A kapcsolatok konzisztenciáját (mikor egy poszt létrejön, bekerül a szerző listájába is) a Service réteg végzi el a háttérben.
* **Kontrollerek (4 db):** REST API végpontok (GET, POST, PUT, DELETE), beépített Java Stream szűrésekkel (pl. `?name=...` alapon) a követelmények szerint.
* **Hibakezelés (1 pont):** Globális `@ControllerAdvice` és egyedi kivételosztályok biztosítják a tetszetős 400 Bad Request és 404 Not Found hibaüzeneteket a szerver összeomlása (500) helyett.
* **Tervezési minta:** Szigorú DTO (Data Transfer Object) használattal, hogy elrejtsük a belső reprezentációt.
