package classifiedslifecycle

import io.github.oshai.kotlinlogging.KotlinLogging
import java.time.Instant

fun toItem(scrapeItem: ScrapeItem, searchConfig: SearchConfig): Item {
    val instant = Instant.now()
    return Item(
        ListingId(scrapeItem.id, instant),
        instant,
        1,
        scrapeItem.title,
        scrapeItem.price,
        scrapeItem.oldPrice,
        scrapeItem.negotiable,
        scrapeItem.created,
        searchConfig,
    )
}

fun Item.matches(scrapeItem: ScrapeItem): Boolean {
    val idMatch = id.id == scrapeItem.id
    val titleMatch = title == scrapeItem.title
    val priceMatch = price == scrapeItem.price
    val negotiableMatch = negotiable == scrapeItem.negotiable
    val createdMatch = created == scrapeItem.created
    if (!idMatch || !titleMatch || !priceMatch || !negotiableMatch || !createdMatch) {
        KotlinLogging.logger {}.info { "matches failed: id=$idMatch title=$titleMatch price=$priceMatch negotiable=$negotiableMatch created=$createdMatch" }
        KotlinLogging.logger {}.info { "  scraped: $scrapeItem" }
    }
    return idMatch && titleMatch && priceMatch && negotiableMatch && createdMatch
}