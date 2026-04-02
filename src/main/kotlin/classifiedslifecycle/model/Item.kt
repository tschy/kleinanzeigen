package classifiedslifecycle.model

import classifiedslifecycle.ListingId
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.Instant

@Entity
@Table(name = "listing")
data class Item(

    @EmbeddedId
    val id: ListingId,
    val lastScrape: Instant, // make sure they are always UTC? still relevant?
    val scrapeCount: Int,

    val title: String,
    val price: Double?,
    val negotiable: Boolean,
    val created: LocalDate?,
) {
    companion object {
        fun fromScrapeItem(scrapeItem: ScrapeItem): Item {
            val instant = Instant.now()
            return Item(
                ListingId(scrapeItem.id, instant),
                instant,
                1,
                scrapeItem.title,
                scrapeItem.price,
                scrapeItem.negotiable,
                scrapeItem.created
            )
        }
    }
    fun matches(scrapeItem: ScrapeItem): Boolean =
        title == scrapeItem.title &&
                price == scrapeItem.price &&
                negotiable == scrapeItem.negotiable &&
                created == scrapeItem.created
}


