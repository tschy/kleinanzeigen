# Classifieds Analyser

## Purpose

Track how listings for specific search terms change over time. The scraper periodically collects listings and stores them in a database. The analyser then provides statistics on pricing, discounts, listing lifespan, and online/offline distribution.

## Constraints

- No official API is available; the site is scraped directly using HTTP requests and HTML parsing
- Requests are minimized to avoid overloading the site's resources

---

## Architecture

The project is a Gradle multi-module Kotlin/Spring Boot project with the following modules:

- **scraper** — fetches listings and writes them to the database
- **analyser** — reads the database and prints statistical reports
- **shared** — common data model and database access, used by both scraper and analyser

### Railway Deployment

Three services run on [Railway](https://railway.com):

| Service | Description |
|---|---|
| **PostgreSQL** | Managed database with a persistent volume |
| **Scraper** | Cron job that runs the scraper on a schedule |
| **Backup** | Cron job that dumps the database and pushes it to Dropbox |

Deployment is triggered manually on Railway after a new Docker image has been pushed. The Docker image is built and published to the GitHub Container Registry (ghcr.io) automatically via GitHub Actions on every commit to `main`.

Infrastructure is managed with Terraform.

Database schema migrations are managed with Flyway and run automatically on scraper startup.

---

## Local Development

### Prerequisites

- JDK 21
- Docker (for running a local test database)
- IntelliJ IDEA (both modules are run directly from the IDE)
- Railway CLI (for accessing the production database)

### Running the Scraper

Run `ScraperApplicationKt` from IntelliJ with the `test` Spring profile active. This connects to a local PostgreSQL instance on port 5433.

### Running the Analyser

Run `AnalysisApplicationKt` from IntelliJ. This connects to the same local database and prints statistical reports to the console.

### Accessing the Production Database

Use the Railway CLI to open a tunnel to the production database:

```bash
railway connect
```

This allows read access to the live database without risking accidental writes, provided a read-only database user is configured.

---

## CI/CD

On every commit to `main`, a GitHub Actions workflow:

1. Builds a new Docker image of the scraper
2. Tags it with the build number (e.g. `ghcr.io/tschy/classifieds-scraper:42`)
3. Pushes it to the GitHub Container Registry

Railway is then updated manually to pull the new image. This gives explicit control over which version is running in production and makes rollbacks straightforward.

---

## Crawling Configuration

The scraping interval is fixed at once per hour in the current version.

---

## Data Model

Each listing is stored with:

- Listing ID, title, price, old price (if discounted), negotiable flag
- Created date (as reported by the site)
- First and last scrape timestamp
- Scrape count
- Online/offline flag

---

## Analysis Output

The analyser reports the following per age group:

- Number of listings
- Percentage discounted
- Online/offline distribution
- Discount rate broken down by online and offline listings

---

## Planned Analysis

- Average number of new listings per 24 hours
- Average listing lifespan
- Average price of disappearing vs. active listings
- Relationship between price and disappearance rate
- Relationship between number of photos and price
- Relationship between listing text length and price
- Keyword influence on price (NLP, future)

---

## Acknowledgements

Developed by Tamara Schymura, with mentoring by matey-jack and AI-assisted development.