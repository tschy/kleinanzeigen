# DB

- only thing ever updated in the record of the listings table is the last scrape count and the date
- for any other change create a new entry with the current timestamp as first scrape


| DoSomethingClass | Data Class    |
|------------------|---------------|
| ItemExtractor    | ScrapeItem    |
| Repository       | Item          |
| XxxService       | connects both |

// service klasse macht das mapping zw scrapeitem und item