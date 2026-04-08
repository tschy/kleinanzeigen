
TODO:
- [x] Spring zurueck, ohne REST API/
- [x] orm einfuehren, Funktionalität testen. https://www.baeldung.com/spring-boot-hibernate
- [x] db loeschen, neu aufsetzen - refactoring abschliessen 
- [x] price parsing und test/fixed 2.600 being translated to 2,6/2.6(double)
- [x] turn application into Spring Boot Data Application
  - [x] create classes: Repository, Application, Service
- [x] implement that item information gets written into the db
- [x] db write test

- [x] mergen implementieren: datensatz lesen und entscheiden, ob ein neuer erstellt werden soll oder nur das scrapte datum updaten. in einer service klasse, die sich gut testen laesst. (+ code an db aenderungen anpassen)
    - [x] fix list.size == 1 in ItemService class
- [x] solve scrape_counter issues:
    - [x] Filter "TOP" ads, as they appear twice in the list, drop duplicates instead of writing them to the db  ->  .distinctBy { it.id } // removes duplicates based on the id property. in ItemService
    - [x] the items that get scraped twice into one go appear on two pages, find differences or find out why that happens
    - [x] investigate why the test with static htm files generates variable scrape counts > 1 / > other entries
    - [x] why do the test results in 3/1 scrape counts? aren't the database changes ephemeral? -> stale items that don't get cleaned up between different test runs
- [x] zweites datenbankschema, fuer echte und fuers Testen: public und test (recherchieren - wie man das macht, flyway konfigurieren fuer zweite db, oder zweiten Docker container)
- [x] ItemExtractor test, test for number of scraped items and scrape count == 1
- [x] add scraping of original price for items that have been reduced
- [x] FetcherService, Paginator, integrate into Spring Boot Framework as beans


- [ ] Testen echten scrape mit db write, 
- [ ] echter Test: mit verschwindenen Items/ ausfuehren, bis was verschwunden ist - oefter scrapen
- [ ] funktion die alle anzeigt, die verschwunden sind (als zweites main programm) - (laesst Spring Boot das zu? - ausrobieren, herausfinden)

FREITAG 10.4.26:
test code in src main - neue mainmethode in anderer datei als classifeslifecycleapplication
gradle: prod ausrufen
aus intellij immer testdatenbank

- [ ] // alle, deren last scrape aelter als 24h und juenger als 48 std ist - 4 und 8 std. am 8.4. 

- [ ] docker volume backup, produktionsdatenbank daten schuetzen
- [ ] manuelle tests nicht auf prod db -> test datenbank
- [ ] dritte db fuer manuelle tests
- [ ] Logging into files -> k logging oder anderes modernes framework verwenden (kotlin logging? -> roberts projekte nachschauen)

8.4./9.4.
- [ ] 7.4. abends einen scrape machen, mehrmals pro tag, 2 bis 3 mal
- [ ] logging
- [ ] dritte db aufsetzen
- [ ] sql queries aus analyser ausfuehren durch spring boot jpa, nach jedem scrape ausfuehren und gucken ob alles passt


sicherstellen: main methode aufrufen - default test datenbank aufrufen
tests: testdatenbank
 (main mit spez paramter) produ db
manuelle daten/tests dritte db

- [ ] konfiguration als yaml datei, und so, dass man mehrere Auftraege aufeinmal eintragen kann.


1. aus den Daten, die wir scrapen können, soviel Weisheit wie möglich zu extrahieren 
2. dabei links und rechts Beispiele anschauen, um ein intuitives Bild dafür zu bekommen, wie zuverlässig unsere Datenweisheit die echte Welt abbildet.