package classifiedslifecycle

class Analyser {
// alle suchen, die sich in den letzten 24 std geandert haben



//    fix
//select id from listing group by id having count(id) > 1 AND MAX(firstScrape) > today - 24h;

    // alle, deren last scrape aelter als 24h und juenger als 48 std ist

    // verschwundene und geanderte anzeigen:
    // Select * from listing where last_scrape < '2026-04-07 14:20';q

// group by: gucken, ob es keine andere row gibt
}