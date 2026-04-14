
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

- [x] docker volume backup, produktionsdatenbank daten schuetzen
- [x] dritte db fuer manuelle tests - manuelle tests nicht auf prod db -> test datenbank
-[x] sicherstellen: main methode aufrufen - default test datenbank aufrufen, tests: testdatenbank (main mit spez paramter) produ db manuelle daten/tests dritte db
  - [x] test code in src main - neue mainmethode in anderer datei als classifeslifecycleapplication
  - [x] gradle: prod ausrufen
  - [x] aus intellij immer testdatenbank
- [x] Logging into files -> k logging oder anderes modernes framework verwenden (kotlin logging? -> roberts projekte nachschauen) https://www.baeldung.com/kotlin/kotlin-logging-library

- [x] alle, deren last scrape aelter als 24h und juenger als 48 std ist - 4 und 8 std. am 8.4.

8.4./9.4.
- [-] 7.4. abends einen scrape machen, mehrmals pro tag, 2 bis 3 mal
- [x] logging
- [x] dritte db aufsetzen
- [x] sql queries aus analyser ausfuehren durch spring boot jpa, nach jedem scrape ausfuehren
  - [ ]und gucken ob alles passt
- [x] Beispieltest fuer die dritte DB

- [ ] Testen echten scrape mit db write,
- [ ] echter Test: mit verschwindenen Items/ ausfuehren, bis was verschwunden ist - oefter scrapen
- [ ] funktion die alle anzeigt, die verschwunden sind (als zweites main programm) - (laesst Spring Boot das zu? - ausrobieren, herausfinden)


- [ ] konfiguration als yaml datei, und so, dass man mehrere Auftraege aufeinmal eintragen kann.


1. aus den Daten, die wir scrapen können, soviel Weisheit wie möglich zu extrahieren 
2. dabei links und rechts Beispiele anschauen, um ein intuitives Bild dafür zu bekommen, wie zuverlässig unsere Datenweisheit die echte Welt abbildet.



- [x] Analyse: Wie lange waren die Items online 
  - Buckets Zeit, 1 Tag, 1 Woche, 7 Wochen, 1 Jahr Anzahl Tage, Anzahl Wochen (besser) 
  - Definition "Alter" -> group by auf id.id - letztes (juengste) last Scrape datum - aeltestes created
- [x] integer fuer alter, boolean fuer stillOnline -> nur im Speicher waehrend der Analyse, direkt ausgeben
- [x] data class, aggregiertes objekt -> Item global, in db in verschd. versionen - nur noch id ohne comp key

  Wenn das juengste nicht das letzte ist -> Item ist verschwunden/nicht online
  globales letztes scrape datum festlegen (wenn ein Datensatz da nicht mehr drin ist, ist er nicht mehr online)

  Verteilung des Alters fuer die, die verschwunden sind, und die die nicht verschw sind
  alle Artikel die noch aktiv sind die Altersverteilung ausgeben (Tabelle)
  created nicht bis heute, sondern bis last scrape


-----------------------------------
- [x] log which db is being used, analyser and itemservice remove printout fun
- [x] nicht machen: - spring boot console application - es muesste erst ein konzept gemacht werden, wie die app in verschieden. modi mit verschied. db ausfuehren
  - [x] repair that scraper and analyser can run on the db
  - [x] split gradle project into modules with two different main methods
- [x] fix areItemsOnline with tolerance Window
- [x] verteilung age groups ausgeben - wie viele Anzeigen in jeder Gruppe, max 50 age groups
- [x] getrennt nach anzeigen online/nicht online (zu jeder age group zwei ausgeben)
- [x] Stichprobenartiger Test der Online/Offline-Logik Manuell prüfen ob isOnline() sinnvolle Ergebnisse liefert — einige Einträge direkt in der DB überprüfen. Fälle ignorieren, bei denen sich die Kleinanzeigen-ID geändert hat.
      **2026-04-13T15:01:16.093805Z**
      '3132479011' : '2026-04-07T13:50:44.158561Z' : '2026-04-13T15:01:13.259646Z' : '16': '2025-07-11' // true
      '3365959547' : '2026-04-07T13:50:43.420417Z' : '2026-04-13T15:00:52.871598Z' : '16': '2026-03-28' // true
      '3372937415' : '2026-04-07T13:50:43.212069Z' : '2026-04-07T14:19:38.745478Z' : '5': '2026-04-05' // false
      '3357998159' : '2026-04-07T13:50:43.642005Z' : '2026-04-13T15:00:57.901963Z' : '16': '2026-03-20' // true
      '3174562151' : '2026-04-07T13:50:44.132501Z' : '2026-04-13T15:01:13.259194Z' : '16': '2025-08-30' // true
      '3276376174' : '2026-04-07T13:50:44.073921Z' : '2026-04-09T05:24:20.374074Z' : '12': '2025-12-18' // false
      '3372825406' : '2026-04-07T13:50:43.217399Z' : '2026-04-09T05:23:51.728587Z' : '12': '2026-04-05' // false
      '3332653317' : '2026-04-07T13:50:43.998280Z' : '2026-04-13T15:01:08.266491Z' : '16': '2026-02-21' // true
      '3378064575' : '2026-04-13T04:41:35.190396Z' : '2026-04-13T15:00:40.445137Z' : '4': '2026-04-10' // true
      '3365127201' : '2026-04-07T13:50:43.437252Z' : '2026-04-09T05:23:58.590023Z' : '12': '2026-03-28' // false
      '3372221915' : '2026-04-07T13:50:43.232023Z' : '2026-04-13T15:00:47.820058Z' : '16': '2026-04-04' // true
      '3347111995' : '2026-04-07T13:50:43.835423Z' : '2026-04-13T15:01:03.379370Z' : '16': '2026-03-09' // true
      '3375023367' : '2026-04-07T13:50:43.088911Z' : '2026-04-09T05:23:47.757905Z' : '12': '2026-04-07' // false
      '3346750334' : '2026-04-07T13:50:43.841910Z' : '2026-04-13T15:01:03.379503Z' : '16': '2026-03-08' // true
      '3361781109' : '2026-04-07T13:50:43.576499Z' : '2026-04-13T15:00:55.274856Z' : '16': '2026-03-24' // true
      '3337686577' : '2026-04-07T13:50:43.965391Z' : '2026-04-09T05:24:16.487060Z' : '12': '2026-02-27' // false


- missing tests? : 
- Aggregation in Aggregate Item
- counting in ageDistribution and ageDistributionOnlineOffline
-------------------------------------------------
~~~~
 - eine Abfrage schreiben, die für jede Anzeige den ältesten Preis (aus der Zeile mit dem frühesten firstScrape) und den neuesten Preis (aus der Zeile mit dem spätesten firstScrape) zurückgibt — mit Subqueries. Nicht MIN/MAX des Preises, sondern den Preis zum ältesten und neuesten Scrapezeitpunkt.

- val discount - average discount/age korrelationen machen -> toolset waere csv und data science bibl -> fuehrt zu weit
- anteil derjenigen berechnen, bei denen oldest und newest price gleich geblieben ist, in den verschiede. age groups - rabattfaktor (anz der elem die rabattiert wurden)
- kotlin hat auch group by -> map oder so, auf der analyse gemacht werden kann - data class mit count online/count offline, discount rate online, dis rate offl fuer jede age group [group by age group und online flag - waere auch moeglich], auch fuer verhandelbar
- weitere sinnvolle analysen mit daten ueberlegen 
-----------------------
- railway.com, was muesste man machen um die db und das tool zu deployen, notizen machen, terraform -> intro lesen
  - versuchen dort eine db aufzusetzen und von lokal aus drauf zugreifen - wieviel kostet eine db pro tag? wenn alles ins budget passt waere das gut (kostenlose version)
