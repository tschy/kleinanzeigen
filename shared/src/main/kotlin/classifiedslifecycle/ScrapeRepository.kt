package classifiedslifecycle

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface ScrapeRepository : JpaRepository<Scrape, Int> {

//[arrayOf(LocalDate.of(2026, 5, 1), 3L)]
//result[0][0] is the date, result[0][1] is the count

    //SELECT DATE(scrape_time) AS day, COUNT(*) AS scrape_count
//FROM scrape
//GROUP BY DATE(scrape_time)
//ORDER BY scrape_count ASC
//LIMIT 1;
    @Query("SELECT CAST(s.scrapeTime AS LocalDate) AS day, " +
            "COUNT(s) AS qqscrapeCount FROM Scrape s " +
            "GROUP BY CAST(s.scrapeTime AS LocalDate) " +
            "ORDER BY scrapeCount ASC LIMIT 1")
    fun findDayWithMinScrapes(): List<Array<Any>>
}