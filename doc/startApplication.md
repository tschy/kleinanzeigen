three different scenarios:

./gradlew bootRun
runs the application and writes data into the db -> use sh run.sh from the terminal to also create a backup of the data before the new scrape

./gradlew bootRun --args='--spring.profiles.active=test'
runs the application and writes data into the test db only if AppStartupRunner has the annotation when @Profile("!test") commented out

change active profile in AppStartupRunner.kt:
when @Profile("!test") is valid, only tests get executed,
without it tests get executed and a db scrape takes place with the values being written into the test db

From within IntelliJ: Environmental variable SPRING_PROFILES_ACTIVE=test is set in run configuration, default case: data gets written into the test db