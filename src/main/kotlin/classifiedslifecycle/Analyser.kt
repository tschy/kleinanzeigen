package classifiedslifecycle

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit



@Service
class Analyser(
    val listingRepository: ListingRepository


) {
    private val logger = KotlinLogging.logger {}

    fun showChangedItems() {

        println("Analysing $listingRepository")
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
        val lastGlobalScrape: Instant? = listingRepository.queryGlobalLastScrape()

        val aggregatedItems = listingRepository.queryAggregateItems()

        logger.info("Analysing ${aggregatedItems.size} items")
    }
}

// Select * from listing where last_scrape < '2026-04-07 14:20';