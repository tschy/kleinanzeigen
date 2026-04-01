#!/bin/bash

BASE_URL="https://www.kleinanzeigen.de/s-fahrraeder/herren/12309/seite:{PAGE}/rennrad/k0c217l3411r10+fahrraeder.art_s:herren"

for i in $(seq 1 28); do
    URL="${BASE_URL/\{PAGE\}/$i}"
    echo "Fetching page $i..."
    curl -s -o "db_test_$((i-1)).htm" "$URL"
    sleep 1
done
