package classifiedslifecycle

import classifiedslifecycle.model.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface ListingRepository : JpaRepository<Item, ListingId>{

    fun findById_Id(id: String): List<Item>

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Item i set i.scrapeCount = ? where i.id.id = ? and i.id.firstScrape = ?")
    fun updateScrapeCount(scrapeCount: Int, id: String, firstScrape: Instant) {}
}
