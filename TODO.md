TODO:

- [x] Spring zurueck, ohne REST API/
- [x] orm einfuehren, Funktionalität testen. https://www.baeldung.com/spring-boot-hibernate
- [x] db loeschen, neu aufsetzen - refactoring abschliessen
- [x] price parsing und test/fixed 2.600 being translated to 2,6/2.6(double)
- [x] turn application into Spring Boot Data Application
    - [x] create classes: Repository, Application, Service
- [x] implement that item information gets written into the db
- [x] db write test

- [x] mergen implementieren: datensatz lesen und entscheiden, ob ein neuer erstellt werden soll oder nur das scrapte
  datum updaten. in einer service klasse, die sich gut testen laesst. (+ code an db aenderungen anpassen)
    - [x] fix list.size == 1 in ItemService class
- [x] solve scrape_counter issues:
    - [x] Filter "TOP" ads, as they appear twice in the list, drop duplicates instead of writing them to the db ->
      .distinctBy { it.id } // removes duplicates based on the id property. in ItemService
    - [x] the items that get scraped twice into one go appear on two pages, find differences or find out why that
      happens
    - [x] investigate why the test with static htm files generates variable scrape counts > 1 / > other entries
    - [x] why do the test results in 3/1 scrape counts? aren't the database changes ephemeral? -> stale items that don't
      get cleaned up between different test runs
- [x] zweites datenbankschema, fuer echte und fuers Testen: public und test (recherchieren - wie man das macht, flyway
  konfigurieren fuer zweite db, oder zweiten Docker container)
- [x] ItemExtractor test, test for number of scraped items and scrape count == 1
- [x] add scraping of original price for items that have been reduced
- [x] FetcherService, Paginator, integrate into Spring Boot Framework as beans

- [x] docker volume backup, produktionsdatenbank daten schuetzen
- [x] dritte db fuer manuelle tests - manuelle tests nicht auf prod db -> test datenbank
-[x] sicherstellen: main methode aufrufen - default test datenbank aufrufen, tests: testdatenbank (main mit spez
 paramter) produ db manuelle daten/tests dritte db
    - [x] test code in src main - neue mainmethode in anderer datei als classifeslifecycleapplication
    - [x] gradle: prod ausrufen
    - [x] aus intellij immer testdatenbank
- [x] Logging into files -> k logging oder anderes modernes framework verwenden (kotlin logging? -> roberts projekte
  nachschauen) https://www.baeldung.com/kotlin/kotlin-logging-library

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
- [ ] funktion die alle anzeigt, die verschwunden sind (als zweites main programm) - (laesst Spring Boot das zu? -
  ausrobieren, herausfinden)


- [ ] konfiguration als yaml datei, und so, dass man mehrere Auftraege aufeinmal eintragen kann.


1. aus den Daten, die wir scrapen können, soviel Weisheit wie möglich zu extrahieren
2. dabei links und rechts Beispiele anschauen, um ein intuitives Bild dafür zu bekommen, wie zuverlässig unsere
   Datenweisheit die echte Welt abbildet.


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
- [x] nicht machen: - spring boot console application - es muesste erst ein konzept gemacht werden, wie die app in
  verschieden. modi mit verschied. db ausfuehren
    - [x] repair that scraper and analyser can run on the db
    - [x] split gradle project into modules with two different main methods
- [x] fix areItemsOnline with tolerance window
- [x] verteilung age groups ausgeben - wie viele Anzeigen in jeder Gruppe, max 50 age groups
- [x] getrennt nach anzeigen online/nicht online (zu jeder age group zwei ausgeben)
- [x] Stichprobenartiger Test der Online/Offline-Logik Manuell prüfen ob isOnline() sinnvolle Ergebnisse liefert —
  einige Einträge direkt in der DB überprüfen. Fälle ignorieren, bei denen sich die Kleinanzeigen-ID geändert hat.
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

-------------------------------------------------

- [x] eine Abfrage schreiben, die für jede Anzeige den ältesten Preis (aus der Zeile mit dem frühesten firstScrape) und
  den neuesten Preis (aus der Zeile mit dem spätesten firstScrape) zurückgibt — mit Subqueries. Nicht MIN/MAX des
  Preises, sondern den Preis zum ältesten und neuesten Scrapezeitpunkt.
  mit SQL damit die Versionen nicht geladen werden muessen (sub Abfrage? aeltesten old price und neusten price
  ausgeben - felder oldest price und newest price - nicht min und max, sondern das aelteste und das neuste firstScrape
  finden - subqueries) - zurueckfragen nach dem nachdenken
    - [x] stichprobenartiger Test auf Korrektheit der Abfrage
      id | price | price  
      ------------+---------+---------
      3372945238 | 1250.00 | 1190.00
      3372945238 | 1250.00 | 1190.00
      (2 rows)
      '3372945238' : '1250.0' : '1190.0'
      kleinanzeigen_test=#
      kleinanzeigen_test=# select id, price from listing where id = '3318777714';
      kleinanzeigen_test=# select id, price from listing where id = '3372945238';
      id | price  
      ------------+---------
      3372945238 | 1250.00
      3372945238 | 1190.00
      (2 rows)
      '3372945238' : '1250.0' : '1190.0'
      kleinanzeigen_test=# select id, price, first_scrape, created from listing wh
      ere id = '337
      id | price | first_scrape | created   
      ------------+---------+-------------------------------+------------
      3372945238 | 1250.00 | 2026-04-07 13:50:43.210801+00 | 2026-04-05
      3372945238 | 1190.00 | 2026-04-13 12:14:16.65134+00 | 2026-04-05
      (2 rows)
      '3372945238' : '1250.0' : '1190.0'
      kleinanzeigen_test=# select id, price, first_scrape, created from listing where id = '3373079396';
      id | price | first_scrape | created   
      ------------+---------+-------------------------------+------------
      3373079396 | 1049.00 | 2026-04-07 13:50:43.189309+00 | 2026-04-05
      3373079396 | 1025.00 | 2026-04-13 04:41:35.461747+00 | 2026-04-05
      (2 rows)
      '3373079396' : '1049.0' : '1025.0'
      kleinanzeigen_test=# select id, price, first_scrape, created from listing where id = '3351835097';
      id | price | first_scrape | created   
      ------------+---------+-------------------------------+------------
      3351835097 | 1449.00 | 2026-04-07 13:50:43.763933+00 | 2026-03-14
      3351835097 | 1399.00 | 2026-04-13 04:41:35.81943+00 | 2026-03-14
      3351835097 | 1299.00 | 2026-04-13 12:14:16.823987+00 | 2026-03-14
      (3 rows)
      '3351835097' : '1449.0' : '1299.0'

            kleinanzeigen_test=# select id, price, first_scrape, created from listing where id = '3349216556';
                 id     |  price  |         first_scrape          |  created   
            ------------+---------+-------------------------------+------------
             3349216556 | 4550.00 | 2026-04-07 13:50:43.795419+00 | 2026-03-11
             3349216556 | 4499.00 | 2026-04-08 10:56:12.469166+00 | 2026-03-11
            (2 rows)
            '3349216556' : '4550.0' : '4499.0'
            
                 id     | price |         first_scrape          |  created   
            ------------+-------+-------------------------------+------------
             3373019454 |  5.00 | 2026-04-07 13:50:43.203287+00 | 2026-04-05
            (1 row)
            '3373019454' : '5.0' : '5.0'


- [x] run cron job on laptop to scrape data hourly
    - crontab -l to show the config file
    - crontab -e to edit and load the config file
    - less /var/mail/robert to see the output
    - pgrep cron to get the PID of the cron job


- [x] solve .jar executable problem -> do not try to execute the -plain.jar
-

/home/robert/Downloads/jdk-21_linux-x64_bin/jdk-21.0.10/bin/java -jar /home/robert/kleinanzeigen/scraper/build/libs/scraper.jar
- 

- Executable jars can be built using the bootJar task. The task is automatically created when the java plugin is applied
  and is an instance of BootJar. The assemble task is automatically configured to depend upon the bootJar task so
  running assemble (or build) will also run the bootJar
  task. https://docs.spring.io/spring-boot/docs/2.5.1/gradle-plugin/reference/htmlsingle/#packaging-executable.and-plain-archives

- Gemini:
  analysis.jar (The Executable Archive): This is the "Fat Jar." It contains your compiled code plus every library your
  app needs to run (Spring Framework, Hibernate, etc.). This is the one you run with java -jar.

  analysis-plain.jar (The Standard Archive): This is a "thin" jar. It contains only your compiled code and resources. It
  will not run on its own because it doesn't include its dependencies.

- https://stackoverflow.com/questions/67935064/difference-between-spring-boot-2-5-0-generated-jar-and-plain-jar/67935468]


-[x] Check data consistency for Analysis

ID OLDEST NEWEST ORIGINAL DISCOUNT AGE GROUP ONLINE      
3377491186 1950.0 1950.0 5 days false       
3378644032 595.0 595.0 5 days false       
3380489529 420.0 420.0 5 days false       
3382329252 250.0 250.0 5 days false       
3382794450 75.0 50.0 -25.0 5 days false       
3383073311 25.0 25.0 5 days false       
3383428393 1750.0 1750.0 5 days false       
3383572091 80.0 80.0 5 days false       
3383937818 50.0 50.0 5 days true        
3383955673 479.0 479.0 5 days true        
3383968475 349.0 349.0 5 days true        
3384051229 280.0 260.0 -20.0 5 days true        
3384056425 499.0 499.0 5 days true        
3384115721 50.0 50.0 5 days true        
3384130545 299.0 299.0 5 days true        
3384141138 1000.0 1000.0 5 days true        
3384142649 550.0 550.0 5 days true        
3384245879 260.0 260.0 5 days true        
3384308574 null null 5 days true        
3384343334 150.0 150.0 210.0 -60.0 5 days true        
3384395930 950.0 950.0 5 days true        
3384402787 850.0 850.0 1100.0 -250.0 5 days true        
3384433206 200.0 200.0 5 days true        
3384460249 399.0 399.0 5 days true        
3384502398 115.0 115.0 5 days true        
3384608912 280.0 280.0 5 days true        
3384610305 50.0 50.0 55.0 -5.0 5 days true

[1] Number of Items
[2] % of Discounted Items
[3] % of Online Items
[4] % of Offline Items
[5] % of Discounted Online Items
[6] % of Discounted Offline Items

Age Group     [1]        [2]    [3]    [4]    [5]    [6]
5 days 27 18.52 70.37 29.63 14.81 3.70
9 weeks 13 38.46 30.77 69.23 15.38 23.08

ID OLDEST NEWEST ORIGINAL DISCOUNT AGE GROUP ONLINE     
3323441286 250.0 250.0 9 weeks false       
3323775962 20.0 20.0 9 weeks false       
3323924730 1000.0 1000.0 1100.0 -100.0 9 weeks false       
3329913252 180.0 180.0 9 weeks true        
3321947078 60.0 60.0 9 weeks false       
3327919748 650.0 650.0 750.0 -100.0 9 weeks true        
3326634464 160.0 150.0 200.0 -50.0 9 weeks true        
3319898757 30.0 30.0 70.0 -40.0 9 weeks false       
3320346333 2800.0 2800.0 9 weeks false       
3318779170 690.0 690.0 9 weeks false       
3318329878 3000.0 3000.0 9 weeks false       
3318686970 1300.0 1300.0 1550.0 -250.0 9 weeks false       
3324612902 10.0 10.0 9 weeks true

-[x] built docker image
 gradle -v
 ./gradlew wrapper --gradle-version=8.13
 ./gradlew :scraper:jibDockerBuild --image=classifieds-scraper:v1
 docker images | grep classifieds-scraper
 docker run --network host classifieds-scraper -> in cron reinschreiben
 docker run --rm --network host classifieds-scraper



- [x] check all implemented functions are having correct output
- [x] discount: how do we deal with the oldPrice data. Do we trust it to have been an actual price? -> we do

    - [x] Discount rate per age group — what percentage of items had a price reduction (oldest price ≠ newest price),
      broken down by age group, and separately for online/offline
    - [x] Fix oldest price issue: take original price into account
    - [x] Online/offline distribution per age group — count and percentage of online vs offline items per age group 
    - [x] Data class for age group stats — a data class holding: count online, count offline, discount rate online,
      discount rate offline — grouped by age group (and optionally by online flag)
  
-[x] work on analyser/work on scraper
-[x] pause work on analyser, resume after scraper has been deployed
-[x] aeltester oldPrice

- [x] railway.com, was muesste man machen um die db und das tool zu deployen, notizen machen,
    - [x] versuchen dort eine db aufzusetzen
    - [x] eine db pro tag? wenn alles ins budget passt waere das gut (kostenlose version)

-[x] docker image, jedes mal ein neues erstellen wenn es eine neue version gibt
-[x] docker image auf railway laufen lassen -> vorher die db konfigurieren
-[x] docker build in der github action machen 
    -[x] build new image
-[x] in die github container registry hochladen (deployment koennte ein extra Schritt sein, oder auch nicht -> railway) ghcr.io/tsch/classifieds-scraper:17
-[x] push newly built docker image to github container registry
-[x] verstehen was das deployment model von railway ist und der unterschied von uns
  - gradle nicht auf produ, soll nur auf entwl servern laufen
  - benutzt railway gradle

#### Questions

- [x] Wo sind die Pull Request Review Kommentare?
- [x] implement code changes pull request -> whos doing the merging? -> kommt drauf an, mein projekt - git hub main gehoert mir, also mach ich den. wenn es ein team projekt ware dann gaebe es team regeln, dann muss einer approve druecken vielleicht. oder leute sind selbstverantwortlich, haben die changes befolgt/comments gefixed, dann machen die das alleine
- [x] Git History zusammen ansehen
    - [x] merge machen, main nach dev mergen


--------------------------------------------------

-[x] railway to pull the newly built docker image -> bessere Kontrolle mit Versionsnummern, leichtes roll back moeglich
- 
--------------------------------------------------
# Analysis

    - Correlation skipped — average discount vs age group correlation requires CSV/data science tooling, out of scope for now -> Kotlin Data Frame as well
    - Further analysis — think of other meaningful insights from the data
    - [] check for a correlation between discount and time online,

###  Ausgabe Analysis

- [] percent discounted

- [] besser lesbar, zweizeilig, oder abkuerzungen einfuehren

- [] grafik - website -> nebensaechlich, terraform ist sinnvoller


-------------------------------------------------
# Open Tasks

-[] find out which user agent string is used and think about which one we should use

-[] db auf railway laufen lassen kann und von lokal mit vpn zugreifen? 
    -[x] install railway cli, use railway connect to access db

-[] Configure a pre-deploy command to run database migrations before each deployment.

- [] docker checken/for test src files being exluded
- 
-------------------------------------------------

# Deployment

- [] terraform -> intro lesen
- [] von lokal aus auf die railway db zugreifen
- [] cron job schritte reproduzieren, neuen Service aufsetzen der auch erfolgreich scraped
- [] fix: no automatic deployment on pushing a new commit
- [] exclude src/test, should happen automatically, verify by checking the docker img

--------------------------------
# Scraper

- [] Spring Boot: sollte die Anwendung beenden, von alleine und von Spring gemanagt werden, der manuellen Code-Loesung ist das vorzuziehen
- ich überlege gerade, wie man geschickt das Datenmodell ergänzen kann, damit an den Einträgen ersichtlich ist, zu welcher SearchConfig sie gehören.
￼
- vielleicht in der SearchConfig Klasse noch ein Feld "key" hinzufügen und dieses dann in jedem ScrapeItem hinzufügen.

--------------------------------


## Git

- [] rebase - videos angucken, ist wichtig oder lesen, nicht jeder erklaert es auf die gleiche weise die zum eigenen passt

## TODO

- u. U. zu advanced: sicherheit von postgres verbessern, manuell zugriff bauen uber eigenen proxy

- [] terraform!






robert@hypatia:~/terraform$terraform import railway_project.classifieds-lifecycle classifieds-lifecycle
╷
│ Error: Missing API token
│ 
│   with provider["registry.terraform.io/terraform-community-providers/railway"],
│   on /home/robert/terraform/terraform.tf line 10, in provider "railway":
│   10: provider "railway" {
│ 
│ Required token could not be found. Please set the token using an input variable in the provider
│ configuration block or by using the `RAILWAY_TOKEN` environment variable.
╵

robert@hypatia:~/terraform$ 


nachschlagen in der railway doc, https://registry.terraform.io/providers/terraform-community-providers/railway/latest/docs/resources/project
oder sonstwo

