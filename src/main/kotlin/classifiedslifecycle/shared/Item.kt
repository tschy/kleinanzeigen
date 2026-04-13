package classifiedslifecycle.shared

import classifiedslifecycle.shared.ListingId
import classifiedslifecycle.scraper.ScrapeItem
import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(name = "listing")
open class Item(

    @EmbeddedId
    val id: ListingId,
    val lastScrape: Instant,
    val scrapeCount: Int,

    val title: String,

    @Column(columnDefinition = "numeric(10,2)")
    val price: Double?,

    @Column(columnDefinition = "numeric(10,2)")
    val oldPrice: Double?,

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
                scrapeItem.oldPrice,
                scrapeItem.negotiable,
                scrapeItem.created
            )
        }
    }

    fun matches(scrapeItem: ScrapeItem): Boolean =
        id.id == scrapeItem.id &&
                title == scrapeItem.title &&
                price == scrapeItem.price &&
                negotiable == scrapeItem.negotiable &&
                created == scrapeItem.created

    fun toDebugString() =
        "'${id.id}' : '${id.firstScrape}' : '${lastScrape}' : '${scrapeCount}': '${title}' : '${price}' : '${oldPrice}' : '${negotiable}' : '${created}' "

}