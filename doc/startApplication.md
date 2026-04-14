 # Analysing and Scraping

## Analyser 
- run on each db (default/prod, test, test-m) with respective IntelliJ run configuration (profile set in "Active profiles" empty/test/test-m, and application.properties, application-test.properties and application-test-m.proerties in analysis/src/main/resources)

## Scraper
- Scraper - scrape into prod database: run kleinanzeigen/run.sh (makes a backup and runs ./gradlew :scraper:bootRun )
- Scrape into test db: comment out @Profile("!test & !test-m") in scraper.src.main...ScraperRunner and use the run configuration ScraperApplication test db from within IntelliJ, the same for writing into test_m, commands are: 

### scrape into test db + comment out @Profile annotation in ScrapeRunner
./gradlew :scraper:bootRun --args='--spring.profiles.active=test'

### scrape into test_m db + comment out @Profile annotation in ScrapeRunner
./gradlew :scraper:bootRun --args='--spring.profiles.active=test-m'

# TESTS

## scraper.src.test....ItemServiceTestSpringBoot 
- writes into the test db with the annotation @ActiveProfiles("test"), into the test_m db with the annotation @ActiveProfiles("test-m") and in the prod db when run without active profile. Execute via the run button in the gutter next to the class name. 

- The annotation @Profile("!test & !test-m") in scraper.src.main...ScraperRunner ensures that the scraper does not run when the tests run. If it is missing, the test and the scraper run.

## scraper.src.test...ItemServiceTestMockDb 
uses a Mock Database object and does not write into or read from any of the real db

## shared.src.test....ListingRepositoryTest 
- uses the Testcontainers library and creates and destroys the db object when being executed without writing in any of the real db





