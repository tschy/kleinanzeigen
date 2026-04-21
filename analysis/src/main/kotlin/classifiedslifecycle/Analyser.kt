package classifiedslifecycle

import io.github.oshai.kotlinlogging.KotlinLogging
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

    val itemsAgeGroupsDiscounted = mutableMapOf<String, Int>()
    val itemsAgeGroupOffline = mutableMapOf<String, Int>()
    val itemsAgeGroupOnline = mutableMapOf<String, Int>()



    fun printTableDiscountsOnlineAndOffline() {

        calculatePercentageDiscount()

        val columnWide = 10
        val columnNarrow = 6

        println()
        println("[1] Number of Items")
        println("[2] % of Discounted Items")
        println("[3] Number of Online Items")
        println("[4] Number of Offline Items")
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

        aggregatedItemsByAgeGroup.forEach { (ageGroup, items) ->

            val percentageDiscounted =
                "%.2f".format(
                    itemsAgeGroupsDiscounted[ageGroup]
                        ?.toDouble()
                        ?.div(items.size.toDouble())
                        ?.times(100.0)
                )

            val percentageDiscountedOnline =
                "%.2f".format(
                    itemsAgeGroupsDiscountedOnline[ageGroup]
                        ?.get(0)
                        ?.toDouble()
                        ?.div(items.size.toDouble())
                        ?.times(100.0)
                )

            val percentageDiscountedOffline =
                "%.2f".format(
                    itemsAgeGroupsDiscountedOnline[ageGroup]
                        ?.get(1)
                        ?.toDouble()
                        ?.div(items.size.toDouble())
                        ?.times(100.0)
                )

            val string = headerFormat.format(
                ageGroup,
                items.size,
                percentageDiscounted,
                itemsAgeGroupOnline[ageGroup]?.takeIf { it != 0 } ?: "",
                itemsAgeGroupOffline[ageGroup]?.takeIf { it != 0 } ?: "",
                percentageDiscountedOnline,
                percentageDiscountedOffline,
            ).replace("0.00", "    ")
                .replace(".00", "   ")
                .trimIndent()

            println(string)
        }
    }

    fun calculatePercentageDiscount() {

        aggregatedItemsByAgeGroup.forEach { (ageGroup, itemsAgeGroup) ->

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

            itemsAgeGroupsDiscountedOnline[ageGroup] = listOf(
                numberItemsDiscOnl,
                numberItemsDiscNotOnl,
                numberItemsNotDiscOnl,
                numberItemsNotDiscNotOnl
            )

            itemsAgeGroupsDiscounted[ageGroup] =
                numberItemsDiscOnl + numberItemsDiscNotOnl

            itemsAgeGroupOnline[ageGroup] =
                numberItemsDiscOnl + numberItemsNotDiscOnl

            itemsAgeGroupOffline[ageGroup] =
                numberItemsDiscNotOnl + numberItemsNotDiscNotOnl

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
                        item.discount.toString().padEnd(12)
                            .replace("0.0", "   ") +
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
