package classifiedslifecycle

import classifiedslifecycle.model.AgeGroupStats
import io.github.oshai.kotlinlogging.KotlinLogging
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.mutableMapOf
import kotlin.collections.set
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

    val itemsAgeGroupsDiscountedOnline = mutableMapOf<String, List<Int>>()

    val listAgeGroupStats = mutableListOf<AgeGroupStats>()


    fun printTableDiscountsOnlineAndOffline() {

        calculatePercentageDiscount()

        val columnWide = 10
        val columnNarrow = 6

        println()
        println("[1] Number of Items")
        println("[2] % of Discounted Items")
        println("[3] % of Online Items")
        println("[4] % of Offline Items")
        println("[5] % of Discounted Online Items")
        println("[6] % of Discounted Offline Items")
        println()

        val headerFormat = "%-${columnWide}s " +
                "%${columnNarrow}s " +
                "%${columnWide}s " +
                "%${columnNarrow}s " +
                "%${columnNarrow}s " +
                "%${columnNarrow}s " +
                "%${columnNarrow}s"
        println(
            (headerFormat)
                .format("Age Group", "[1]", "[2]", "[3]", "[4]", "[5]", "[6]")
        )

        listAgeGroupStats.forEach{ item ->

            val numberFormatString = "%6.2f"

            val percentageDiscounted =
                numberFormatString.format(item.percentageDiscount)

            val percentageDiscountedOnline =
                numberFormatString.format(item.percentageDiscountOnline)

            val percentageDiscountedOffline =
                numberFormatString.format(   item.percentageDiscountOffline)

            val percentageOnlineItems =
                numberFormatString.format(item.percentageOnline)
            val percentageOfflineItems =
                numberFormatString.format( item.percentageOffline)

            val string = headerFormat.format(
                item.ageGroup,
                item.count,
                percentageDiscounted,
                percentageOnlineItems,
                percentageOfflineItems,
                percentageDiscountedOnline,
                percentageDiscountedOffline,
            ).replace(" 0.00", "     ")
                .replace(".00", "   ")

                .trimIndent()
            println(string)
        }
        println()
    }

    fun calculatePercentageDiscount() {

        listAgeGroupStats.clear()

        aggregatedItemsByAgeGroup.forEach { (ageGroup, itemsAgeGroup) ->

            val countAgeGroup = itemsAgeGroup.size
            var numberItemsDiscOnl = 0
            var numberItemsDiscNotOnl = 0
            var numberItemsNotDiscOnl = 0
            var numberItemsNotDiscNotOnl = 0

            val itemsOnlineOrOffline =
                itemsAgeGroup.groupBy { it.isOnline(lastGlobalScrape) }

            itemsOnlineOrOffline.forEach { (online, itemsOnlineOffline) ->

                val itemsDiscNotDiscOnlNotOnl =
                    itemsOnlineOffline.groupBy { it.discount != 0.0 }

                itemsDiscNotDiscOnlNotOnl.forEach { (discount, itemsDiscNotDisc) ->

                    if (discount && online)
                        numberItemsDiscOnl = itemsDiscNotDisc.size
                    if (discount && !online)
                        numberItemsDiscNotOnl = itemsDiscNotDisc.size
                    if (!discount && online)
                        numberItemsNotDiscOnl = itemsDiscNotDisc.size
                    if (!discount && !online)
                        numberItemsNotDiscNotOnl = itemsDiscNotDisc.size
                }
            }


            listAgeGroupStats.add(
                AgeGroupStats(
                    ageGroup,
                    countAgeGroup,
                    (numberItemsDiscOnl + numberItemsNotDiscOnl),
                    (numberItemsDiscNotOnl + numberItemsNotDiscNotOnl),
                    (numberItemsDiscOnl + numberItemsDiscNotOnl)
                        .toDouble()
                        .div(countAgeGroup.toDouble())
                        .times(100.0),
                    numberItemsDiscOnl
                        .toDouble()
                        .div(countAgeGroup.toDouble())
                        .times(100.0),
                    numberItemsDiscNotOnl
                        .toDouble()
                        .div(countAgeGroup.toDouble())
                        .times(100.0),
                    (numberItemsDiscOnl + numberItemsNotDiscOnl)
                        .toDouble()
                        .div(countAgeGroup.toDouble())
                        .times(100.0),
                    (numberItemsDiscNotOnl + numberItemsNotDiscNotOnl)
                        .toDouble()
                        .div(countAgeGroup.toDouble())
                        .times(100.0)
                )
            )
        }
    }


    fun debugPrintChangedItems() {
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
            "---Listing items that changed and whose latest known version was last " +
                    "scraped between 24h and 48h ago i.e., " +
                    "potentially disappeared listings:"
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


    fun debugPrintAggregatedItems(
    ) {
        println(
            "ID".padEnd(12) +
                    "OLDEST".padEnd(12) +
                    "NEWEST".padEnd(12) +
                    "ORIGINAL".padEnd(12) +
                    "DISCOUNT".padEnd(12) +
                    "AGE GROUP".padEnd(12) +
                    "ONLINE".padEnd(12)
        )
        println("-".repeat(52))

        aggregatedItems.forEach { item ->
            println(
                item.id.padEnd(12) +
                        item.oldestPrice.toString().padEnd(12) +
                        item.newestPrice.toString().padEnd(12) +
                        item.originalPrice.toString().padEnd(12)
                            .replace("null", "    ") +
                        (item.discount.takeIf { it != 0.0 }?.toString() ?: "").padEnd(12) +
                        item.ageGroup.padEnd(12) +
                        item.isOnline(lastGlobalScrape).toString().padEnd(12)
            )
        }
    }


    fun debugPrintAgeGroupDistribution() {
        aggregatedItemsByAgeGroup.forEach { (ageGroup, items) ->
            print(ageGroup + " " + items.size)
            items.forEach { item -> print(" ${item.id},") }
            println()
        }
    }


}
