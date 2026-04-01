
TODO:
- [x] Spring zurueck, ohne REST API/
- [x] orm einfuehren, Funktionalität testen. https://www.baeldung.com/spring-boot-hibernate
- [x] db loeschen, neu aufsetzen - refactoring abschliessen 
- [x] mergen implementieren: datensatz lesen und entscheiden ob ein neuer erstellt werden soll oder nur das scrapte datum updaten. in einer service klasse, die sich gut testen laesst. (Darin enthalten: code an db aenderungen anpassen):
  - [ ] turn application into Sprin
  - create classes: ScrapeItem, AppStartupRunner
- [x] price parsing und test/fixed 2.600 being translated to 2,6/2.6(double)
- [x] db write test/fixed scrape count not increasing
- [ ] konfiguration als yaml datei, und so, dass man mehrere Auftraege aufeinmal eintragen kann.
- solve scrape_counter issues:
    - [ ] Filter "TOP" ads, as they appear twice in the list, drop them instead of writing them to the db
    - [ ] investigate why the test with static htm files generates variable scrape counts > 1 / > other entries

WONT DO:
- refactoring ListingRepository nicht abschliessen => das generiert uns spring-data!


