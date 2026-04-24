source image 
ghcr.io/tschy/classifieds-scraper:19


SPRING_DATASOURCE_PASSWORD 

SPRING_DATASOURCE_URL 
jdbc:postgresql://postgres.railway.internal:5432/railway

SPRING_DATASOURCE_USERNAME 
postgres


Deployed via Docker Image 
ghcr.io/tschy/classifieds-scraper:19


Restart policy 
never


status 
COMPLETED or ACTIVE, both work, crucial: wait for the first cron job to start, it will not start immediately

