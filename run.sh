#!/bin/bash
docker exec kleinanzeigen-db-1 pg_dump -U postgres kleinanzeigen > /home/robert/kleinanzeigen/ops/backups/$(date +%Y-%m-%d_%H-%M-%S).sql
./gradlew bootRun