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
        aggregatedItems.forEach { item ->
            println(
                item.toDebugString() + " // " + item.isOnline(lastGlobalScrape)
            )
        }

        // print age distribution

        val sortedAggregateItem = aggregatedItems.sortedBy { it.ageDays }
        val ageDistribution = mutableMapOf<String, Int>()

        sortedAggregateItem.forEach { item ->
            ageDistribution[item.ageGroup] =
                ageDistribution.getOrDefault(item.ageGroup, 0) + 1
        }

        println(ageDistribution)
        println("%-12s %s".format("Age Group", "Count"))
        println("-".repeat(20))
        ageDistribution.forEach { (group, count) ->
            println("%-12s %d".format(group, count))
        }

        // getrennt nach anzeigen online/nicht online (zu jeder age group zwei ausgeben)
        val ageDistributionOnlineOffline = mutableMapOf<String, Pair<Int, Int>>()

        sortedAggregateItem.forEach { item ->
            val onlineCount = ageDistributionOnlineOffline[item.ageGroup]?.first ?: 0
            val offlineCount = ageDistributionOnlineOffline[item.ageGroup]?.second ?: 0

            ageDistributionOnlineOffline[item.ageGroup] =
                if (item.isOnline(lastGlobalScrape)) Pair(onlineCount + 1, offlineCount)
                else (Pair(onlineCount, offlineCount + 1));
        }


        println(ageDistributionOnlineOffline)
        println("%-12s %s %s".format("Age Group", "OnlineCount", "OfflineCount"))
        println("-".repeat(30))
        ageDistributionOnlineOffline.forEach { (group, counts) ->
            val (onlineCount, offlineCount) = counts
            println("%-12s %6d      %6d".format(group, onlineCount, offlineCount))
        }
    }
}
