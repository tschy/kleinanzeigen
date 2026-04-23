
custom start command
java -cp "/app/resources:/app/classes:/app/libs/*" classifiedslifecycle.ScraperApplicationKt

source image 
ghcr.io/tschy/classifieds-scraper:17


SPRING_DATASOURCE_PASSWORD 

SPRING_DATASOURCE_URL 
jdbc:postgresql://postgres.railway.internal:5432/railway

SPRING_DATASOURCE_USERNAME 
postgres


Deployed via Docker Image 
ghcr.io/tschy/classifieds-scraper:17


Restart policy 
never


status 
COMPLETED or ACTIVE, both work, crucial: wait for the first cron job to start, it will not start immediately



spring boot exit thing