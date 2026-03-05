# Kleinanzeigen Analyzer

## Purpose

Given specific search terms track how offers for those terms change over time on the site kleinanzeigen.de

## Constraints

There might be not an official API to query the site.
Request should be minimized to not overstrain the site's resources.
It might be necessary to circumvent scraping prevention mechanism.

## Architecture

To minimize costs service should run as a single instance.
To minimize admin work the database should be SaaS. 
Sythesized results should be available on a public website.
Scraping configuration should be available behind password protection (user: admin, password: to be written somewhere else).

## Data Model

Search term is an entity, for each search term there should be a search configuration and a search log.

The most important configuration item is, if the scraping is currently on or off.
The next important parameter is the scraping interval.
In the first version, start scraping interval should be fixed at once per 24 hrs and should not be changed.
(Potential improvement: The scraper should see how many changes there were since the last scrape and adjust the scraping interval such that minimal scraping is done and also the number of missed items is minimized.)


## Specs for the admin interface

- Add new search terms (and start scraping)
- Stop scraping for a search term
- Restart scraping for a search term

## Specs for the public interface

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












