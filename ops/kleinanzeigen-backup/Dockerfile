# Use a very small Linux base
FROM debian:bookworm-slim

# Install dependencies and the official Postgres 18 repository
RUN apt-get update && apt-get install -y curl gnupg2 lsb-release \
    && curl -fsSL https://www.postgresql.org/media/keys/ACCC4CF8.asc | gpg --dearmor -o /etc/apt/trusted.gpg.d/postgresql.gpg \
    && echo "deb http://apt.postgresql.org/pub/repos/apt/ $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list \
    && apt-get update \
    && apt-get install -y postgresql-client-18 \
    && rm -rf /var/lib/apt/lists/*
# Set the working directory inside the container
WORKDIR /app

# Copy your local script into the container's /app folder
COPY ops/kleinanzeigen-backup/backup.sh .

# Make the script executable
RUN chmod +x backup.sh

# Tell Docker to run your script when the container starts
CMD ["./backup.sh"]