Make backup of database:
docker exec -it kleinanzeigen-db-1 pg_dump -U postgres kleinanzeigen > backup.sql

Drop the database inside the container:
docker exec -i kleinanzeigen-db-1 psql -U postgres -c "DROP DATABASE kleinanzeigen;"
docker exec -i kleinanzeigen-db-test-1 psql -U postgres -c "DROP DATABASE kleinanzeigen_test;"

Recreate the database:
docker exec -i kleinanzeigen-db-1 psql -U postgres -c "CREATE DATABASE kleinanzeigen;"
docker exec -i kleinanzeigen-db-test-1 psql -U postgres -c "CREATE DATABASE kleinanzeigen_test;"

Restore the backup:
docker exec -i kleinanzeigen-db-1 psql -U postgres -d kleinanzeigen < backup.sql
docker exec -i kleinanzeigen-db-test-1 psql -U postgres -d kleinanzeigen_test < backup.sql
docker exec -i kleinanzeigen-db-test-1 psql -U postgres -d kleinanzeigen_test < backups/2026-04-09_11-28-03.sql