- research Railway.com's database offers and pricing
  - research VPN access to the database (since it's essential to our solution architecture)
 
- decide on a database schema update tool (such as Flyway) and define the initial data model (already using AI assistant).
- investigate the kleinanzeigen APIs to see how it can be scraped (try manual or if too complicated, use agent)
- one-shot the scraper and run it locally; fix until it works. Maybe use a local database for this.
- maybe run it locally for a few days and then start working on the data analyzer / viewer part on the local database.
- maybe run it in the cloud in parallel, just to collect experience quicker

I think future steps depend on the learnings. 
Clearly, there is a constraint that we can test many things only after collecting some data with the scrapes. 
We should keep this in mind when choosing task order.
