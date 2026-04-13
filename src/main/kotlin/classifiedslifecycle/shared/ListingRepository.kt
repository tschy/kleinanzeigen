package classifiedslifecycle.shared

import classifiedslifecycle.analysis.AggregatedItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface ListingRepository : JpaRepository<Item, ListingId> {

    fun findByIdId(id: String): List<Item>

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        """
    UPDATE Item i 
    SET i.scrapeCount = ?1, i.lastScrape = ?2 
    WHERE i.id.id = ?3 
      AND i.id.firstScrape = ?4
"""
    )
    fun updateScrapeCount(scrapeCount: Int, lastScrape: Instant, id: String, firstScrape: Instant)

    // all items that have changed
    //  SELECT * FROM listing WHERE id IN (SELECT id FROM listing GROUP BY id HAVING COUNT(id) > 1) ORDER BY id, first_scrape;
    @Query(
        """
    SELECT i FROM Item i 
    WHERE i.id.id IN (
        SELECT i2.id.id FROM Item i2 
        GROUP BY i2.id.id 
        HAVING COUNT(i2.id.id) > 1
    ) 
    ORDER BY i.id.id, i.id.firstScrape
"""
    )
    fun queryChangedItems(): List<Item>

    // changed listings with a new version in the last 24h
    // SELECT *  FROM listing GROUP BY id HAVING COUNT(id) > 1 AND MAX(first_scrape) > NOW() - INTERVAL '24 hours';
    @Query(
        """
    SELECT i FROM Item i
    WHERE i.id.id IN (
        SELECT i2.id.id FROM Item i2
        GROUP BY i2.id.id
        HAVING COUNT(i2.id.id) > 1 
           AND MAX(i2.id.firstScrape) > :cutoff
    )
"""
    )
    fun queryChangedItemsLast24hrs(cutoff: Instant): List<Item>

    // Listings whose **latest known version** was last scraped between 24h and 48h ago (i.e., potentially disappeared listings)
    // SELECT * FROM listing WHERE last_scrape < NOW() - INTERVAL '24 hours' AND last_scrape > NOW() - INTERVAL '48 hours';
    @Query(
        """
    SELECT i FROM Item i 
    WHERE i.lastScrape <= :twentyFourHoursAgo 
      AND i.lastScrape >= :fortyEightHoursAgo
      AND i.lastScrape = (
    SELECT MAX(i2.lastScrape)
    FROM Item i2
    WHERE i2.id.id = i.id.id
)
"""
    )
    fun queryItemsDisappearedBetween24And48Hours(
        twentyFourHoursAgo: Instant,
        fortyEightHoursAgo: Instant
    ): List<Item>


    // get lastGlobalScrape
    @Query("SELECT MAX(i.lastScrape) FROM Item i")
    fun queryGlobalLastScrape(): Instant?


    // changed listings with a new version in the last 24h
    // SELECT *  FROM listing GROUP BY id HAVING COUNT(id) > 1 AND MAX(first_scrape) > NOW() - INTERVAL '24 hours';
    @Query(
        """
    SELECT NEW classifiedslifecycle.analysis.AggregatedItem(
        i.id.id,
        MIN(i.id.firstScrape), 
        MAX(i.lastScrape), 
        SUM(i.scrapeCount), 
        MIN(i.created))
    FROM Item i
    GROUP BY i.id.id
"""
    )
    fun queryAggregateItems(): List<AggregatedItem>

}