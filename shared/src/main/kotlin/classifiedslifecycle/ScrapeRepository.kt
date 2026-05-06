package classifiedslifecycle

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface ScrapeRepository : JpaRepository<Scrape, Int> {

//[arrayOf(LocalDate.of(2026, 5, 1), 3L)]
//result[0][0] is the date, result[0][1] is the count


    // SELECT DATE(scrape_time) AS day, COUNT(*) AS scrape_count
//FROM scrape
//GROUP BY DATE(scrape_time)
//ORDER BY scrape_count ASC
//LIMIT 1;
    @Query(
        "SELECT DATE(s.scrape_time) AS day, COUNT(*) AS scrape_count " +
                "FROM scrape s " +
                "WHERE DATE(s.scrape_time) < CURRENT_DATE " +
                "GROUP BY DATE(s.scrape_time) " +
                "ORDER BY scrape_count ASC " +
                "LIMIT 1", nativeQuery = true
    )
    fun findDayWithMinScrapes(): List<Array<Any>>


    @Query(
        "SELECT COUNT(DISTINCT DATE(s.scrape_time)) " +
                "FROM scrape s " +
                "WHERE DATE(s.scrape_time) < CURRENT_DATE", nativeQuery = true
    )
    fun countDistinctDays(): Long


    @Query(
        "SELECT COUNT(*) FROM scrape s WHERE DATE(s.scrape_time) < CURRENT_DATE",
        nativeQuery = true
    )
    fun countExcludingToday(): Long
}