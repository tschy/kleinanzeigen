There are three docker containers (docker compose up) with three databases: production, test and manual tests:

docker exec -it kleinanzeigen-db-1 psql -U postgres -d kleinanzeigen

docker exec -it kleinanzeigen-db-test-1 psql -U postgres -d kleinanzeigen_test

docker exec -it  kleinanzeigen-db-test-m-1 psql -U postgres -d kleinanzeigen_test_m