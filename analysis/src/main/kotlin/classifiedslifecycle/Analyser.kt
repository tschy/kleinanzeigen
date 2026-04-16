package classifiedslifecycle

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.math.round

@Service
class Analyser(
    val listingRepository: ListingRepository,

    ) {
    private val logger = KotlinLogging.logger {}

    val lastGlobalScrape: Instant? = listingRepository.queryGlobalLastScrape()

    val aggregatedItems =
        listingRepository.queryAggregateItems().sortedBy { it.ageDays }

    val aggregatedItemsByAgeGroup = aggregatedItems.groupBy { it.ageGroup }

    fun printAgeGroupDistribution() {
        aggregatedItemsByAgeGroup.forEach { (ageGroup, items) ->
            print(ageGroup + " " + items.size)
            items.forEach { item -> print(" ${item.id},") }
            println()
        }
    }

    fun printIsItemOnline() {
        logger.info { "Analysing ${aggregatedItems.size} items" }
        println(lastGlobalScrape)
        aggregatedItems.forEach { item ->
            println(
                item.toDebugString() + " // " + item.isOnline(lastGlobalScrape)
            )
        }
    }

    fun determineAgeGroupDistributionForItemsOnlineAndOffline():
            MutableMap<String, Pair<Int, Int>> {
        logger.info { "Analysing ${aggregatedItems.size} items" }

        // getrennt nach anzeigen online/nicht online (zu jeder age group zwei ausgeben)
        val ageDistributionOnlineOffline = mutableMapOf<String, Pair<Int, Int>>()

        aggregatedItems.forEach { item ->
            val onlineCount = ageDistributionOnlineOffline[item.ageGroup]?.first ?: 0
            val offlineCount = ageDistributionOnlineOffline[item.ageGroup]?.second ?: 0

            ageDistributionOnlineOffline[item.ageGroup] =
                if (item.isOnline(lastGlobalScrape)) Pair(onlineCount + 1, offlineCount)
                else (Pair(onlineCount, offlineCount + 1));
        }
        return ageDistributionOnlineOffline
    }

    fun printAgeGroupDistributionForItemsOnlineAndOffline() {

        val ageDistributionOnlineOffline =
            determineAgeGroupDistributionForItemsOnlineAndOffline()
        println(ageDistributionOnlineOffline)
        println("%-12s %s %s".format("Age Group", "OnlineCount", "OfflineCount"))
        println("-".repeat(30))
        ageDistributionOnlineOffline.forEach { (group, counts) ->
            val (onlineCount, offlineCount) = counts
            println("%-12s %6d      %6d".format(group, onlineCount, offlineCount))
        }

        println("%-12s %s".format("Age Group", "Count", "% Discounted"))
        aggregatedItemsByAgeGroup.forEach { (string, items) ->
            println(string + " " + items.size + " ")
        }
    }

    fun platzhalter() {

        aggregatedItemsByAgeGroup.forEach { (ageGroup, items) ->
            println(ageGroup + " " + items.size)
            val itemsGroupedOnlineOrOffline =
                items.groupBy { it.isOnline(lastGlobalScrape) }

            itemsGroupedOnlineOrOffline.forEach { (isOnline, itemsOnlineOffline) ->

                print("$isOnline ")

                print(
                    "${itemsOnlineOffline.size}, " +
                            "${
                                round(
                                    itemsOnlineOffline.size.toDouble() /
                                            items.size * 100
                                )
                            }% "
                )
//                itemsOnlineOffline.forEach { print(it.id + ", ") }
                println()
            }
            println()
        }

        println("AGE GROUP".padEnd(15) +
                "ONLINE".padEnd(10) +
                "COUNT".padEnd(8) +
                "PERCENT".padEnd(10))
        println("-".repeat(43))
        aggregatedItemsByAgeGroup.forEach { (ageGroup, items) ->
            val itemsGroupedOnlineOrOffline = items.groupBy {
                it.isOnline(lastGlobalScrape) }
            itemsGroupedOnlineOrOffline.forEach { (isOnline, itemsOnlineOffline) ->
                println(
                    ageGroup.padEnd(15) +
                            isOnline.toString().padEnd(10) +
                            itemsOnlineOffline.size.toString().padEnd(8) +
                            "${round(
                                itemsOnlineOffline.size.toDouble() /
                                        items.size * 100)}%".padEnd(10)
                )
            }
            println()
        }

    }

    fun printAggregatedItems(
    ) {
        println(
            "ID".padEnd(12) +
                    "OLDEST".padEnd(12) +
                    "NEWEST".padEnd(12) +
                    "DISCOUNT".padEnd(8) +
                    "AGE GROUP".padEnd(12) +
                    "ONLINE".padEnd(12)
        )
        println("-".repeat(52))

        aggregatedItems.forEach { item ->
            println(
                item.id.padEnd(12) +
                        item.oldestPrice.toString().padEnd(12) +
                        item.newestPrice.toString().padEnd(12) +
                        item.discount.toString().padEnd(8)
                            .replace("0.0", "   ") +
                        item.ageGroup.padEnd(12) +
                        item.isOnline(lastGlobalScrape).toString().padEnd(12)
            )
        }
    }


    fun printChangedItems() {

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
            "---Listing items that changed and whose latest known version was last scraped between 24h and 48h ago i.e., potentially disappeared listings:"
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
}
