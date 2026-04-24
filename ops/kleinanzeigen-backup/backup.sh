#!/bin/bash

# 1. Get a temporary access token using the Refresh Token
echo "Refreshing Dropbox access token..."
RESPONSE=$(curl -s -X POST https://api.dropbox.com/oauth2/token \
    -u "$DROPBOX_APP_KEY:$DROPBOX_APP_SECRET" \
    -d grant_type=refresh_token \
    -d refresh_token="$DROPBOX_REFRESH_TOKEN")

ACCESS_TOKEN=$(echo $RESPONSE | grep -oP '"access_token":\s*"\K[^"]+')

if [ -z "$ACCESS_TOKEN" ]; then
    echo "Error: Could not retrieve access token. Response: $RESPONSE"
    exit 1
fi

# 2. Create timestamped filename
BACKUP_NAME="backup_$(date +%Y%m%d_%H%M%S).sql"

# 3. Execute database dump
echo "Starting database dump..."
pg_dump "$DATABASE_URL" > "$BACKUP_NAME"

# 4. Upload to Dropbox
API_ARG="{\"path\": \"/classifieds-backups/$BACKUP_NAME\",\"mode\": \"add\"}"

echo "Uploading to Dropbox..."
curl -X POST https://content.dropboxapi.com/2/files/upload \
    --header "Authorization: Bearer $ACCESS_TOKEN" \
    --header "Dropbox-API-Arg: $API_ARG" \
    --header "Content-Type: application/octet-stream" \
    --data-binary @"$BACKUP_NAME"

# 5. Local Cleanup
rm "$BACKUP_NAME"
echo "Process complete."