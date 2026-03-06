# Kleinanzeigen Analyzer

## Purpose

Given specific search terms track how offers for those terms change over time on the site kleinanzeigen.de

## Constraints

There might be not an official API to query the site.
Request should be minimized to not overstrain the site's resources.
It might be necessary to circumvent scraping prevention mechanism.

## Architecture

To minimize admin work the database should be SaaS. 

To minimize cost, use a Cloud Cron service (such as Railway.com) to run the crawler only once per day and not pay any server cost beyond that time.
Crawler configuration will be a file, either YAML or a DSL in the Kotlin code itself.

Data exploration can then happen locally by setting up a VPN connection to the database directly.
Or probably better: regularly cloning the database locally. 
This avoids accidental corruption of the database. 
Oh wait, we can also achieve this by using a read-only user for database access.

## Data Model

Search term is an entity, for each search term there should be a search configuration and a search log.

The most important configuration item is, if the scraping is currently on or off.
The next important parameter is the scraping interval.
In the first version, start scraping interval should be fixed at once per 24 hrs and should not be changed.
(Potential improvement: The scraper should see how many changes there were since the last scrape and adjust the scraping interval such that minimal scraping is done and yet the number of missed items is minimized.)


## Crawling configuration

- For the first version, just a set of keywords. Items will be stored for each keyword separately.
- A meta table in the data base records all crawls for each keyword, so that they will all be available for analysis, even if removed from the crawling configuration.

## Incremental crawling

- Rejected idea: The crawler might use exsisting data base entries, especially the data or id number of the lastest result item for a keyword, to craft a query to get only newer items. Probably the most effecienty way to find disappeared items is to see that they are missing from the search results, instead of querying each item individually. In that case, we need to 

## Specs for the analysis interface

Show left side bar with all the search terms and intervals.
For the selected term and interval show the following data:
- average number of new offers per 24 hrs
- average age in days of disappeared offers (lifespan)
- average age of all offers
- average price of disappearing offers 
- average price of all offers
- average price of offers older than the average lifespan
- the same for half and double the lifespan

### offer a tabbed view for the following visualizations: 
- relationship between price and disappearance
- relationship between number of photos and price
- relationship between legnth of text and price

## Potential future data analysis

Download the scraped data including text content of the offers and find which keywords in the text have which influence on the price.












