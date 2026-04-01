package classifiedslifecycle

import classifiedslifecycle.model.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.Optional

@Repository
interface ListingRepository : JpaRepository<Item, ListingId> {

    fun findByIdId(id: String): List<Item>

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Item i set i.scrapeCount = ?1 where i.id.id = ?2 and i.id.firstScrape = ?3")
    fun updateScrapeCount(scrapeCount: Int, id: String, firstScrape: Instant)
}
