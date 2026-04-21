java main class wurde nicht gefunden, custom start command

java -cp "/app/resources:/app/classes:/app/libs/*" classifiedslifecycle.ScraperApplicationKt


app hat sich nicht beendet, quellcode angepasst

enable teardown

variables


docker image tag is hardcoded now

TODO
weiteren TaskWorker hinzufuegen und einstellungen/schritte dokumentieren
add: new deployment on pushing a new commit
routine to take out the data regulary and write it into the home db


----
spring boot sollte von alleine beenden/oder explizit sagen -> spring funktion, um auch die db vernuenftig beenden
-- gucken ob postgres dangling connections hat



abschaetzen wieviel byte ist ein datensatz, wieviele datensaetze. 


--> analyser index auf primary (in der datenbank, teil von flyway script, auf allen feldern mit denen gesucht wird einen index machen, -> einlesen)



ausblick: zugriff auf die cloud daten

