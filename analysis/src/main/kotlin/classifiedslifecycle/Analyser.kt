package classifiedslifecycle

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.sql.DataSource

@Service
class Analyser(
    val listingRepository: ListingRepository,

) {
    private val logger = KotlinLogging.logger {}

    val lastGlobalScrape: Instant? = listingRepository.queryGlobalLastScrape()

    fun showChangedItems() {

        println("---Listing changed items:")
        listingRepository.queryChangedItems()
            .forEach { listing -> println(listing.toDebugString()) }

        println("---Listing items that changed in the last 24 hours:")
        listingRepository.queryChangedItemsLast24hrs(
            Instant.now().minus(
                24, ChronoUnit.HOURS
            )
        ).forEach { listing -> println(listing.toDebugString()) }

        println(
            "---Listing items that changed and whose latest known version\n" +
                    "    was last scraped between 24h and 48h ago\n" +
                    "    (i.e., potentially disappeared listings):"
        )
        listingRepository.queryItemsDisappearedBetween24And48Hours(
            Instant.now().minus(
                24, ChronoUnit.HOURS
            ),
            Instant.now().minus(
                48, ChronoUnit.HOURS
            )
        ).forEach { listing -> println(listing.toDebugString()) }

    }

    fun analyse() {

        val aggregatedItems = listingRepository.queryAggregateItems()

        logger.info { "Analysing ${aggregatedItems.size} items" }


        println(lastGlobalScrape)
        aggregatedItems.forEach { item -> println(item.toDebugString() + " // "+ item.isOnline(lastGlobalScrape, 10, ChronoUnit.MINUTES)) }
    }
}
