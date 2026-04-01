
TODO:
- [x] Spring zurueck, ohne REST API/
- [x] orm einfuehren, Funktionalität testen. https://www.baeldung.com/spring-boot-hibernate
- [x] db loeschen, neu aufsetzen - refactoring abschliessen 
- [x] price parsing und test/fixed 2.600 being translated to 2,6/2.6(double)
- [x] turn application into Spring Boot Data Application
  - [x] create classes: Repository, Application, Service
- [x] implement that item information gets written into the db
- [x] db write test

- [ ] mergen implementieren: datensatz lesen und entscheiden ob ein neuer erstellt werden soll oder nur das scrapte datum updaten. in einer service klasse, die sich gut testen laesst. (+ code an db aenderungen anpassen)
  - [ ] fix list.size == 1 in ItemService class
- [ ] solve scrape_counter issues:
    - [ ] Filter "TOP" ads, as they appear twice in the list, drop them instead of writing them to the db
    - [ ] the items that get scraped twice into one go appear on two pages, find differences or find out why that happens
    - [ ] investigate why the test with static htm files generates variable scrape counts > 1 / > other entries
    - [ ] why do the test results in 3/1 scrape counts? aren't the database changes ephemeral?
- [ ] konfiguration als yaml datei, und so, dass man mehrere Auftraege aufeinmal eintragen kann.
- [ ] git rebase exercise, merge in robert/patch-1 into 8bbd7651

WONT DO:
- refactoring ListingRepository nicht abschliessen => das generiert uns spring-data!


