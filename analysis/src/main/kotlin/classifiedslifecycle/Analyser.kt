package classifiedslifecycle

import classifiedslifecycle.model.AgeGroupStats
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.collections.component1
import kotlin.collections.component2

@Service
class Analyser(
    val listingRepository: ListingRepository,
    val scrapeRepository: ScrapeRepository
) {

    private val logger = KotlinLogging.logger {}

    val firstGlobalScrape: Instant? = listingRepository.queryFirstLastScrape()

    val lastGlobalScrape: Instant? = listingRepository.queryGlobalLastScrape()

    val aggregatedItems =
        listingRepository.queryAggregateItems()
            .sortedBy { it.ageDays }

    val aggregatedItemsByAgeGroup = aggregatedItems
        .groupBy { it.ageGroup }

    val listAgeGroupStats = mutableListOf<AgeGroupStats>()


//- [] Before all the reports, print a status of the data that was used, such as:
//    - total scraping interval (very first and very last complete scrape)
//    - number of scrapes
//    - minimal scrapes per day


    fun printStats() {

        val numberOfScrapedDays = Duration.between(firstGlobalScrape, lastGlobalScrape).toDays()
        val numberOfMinimumScrapes = scrapeRepository.findDayWithMinScrapes()[0][1]

        println()
        println(
            "start scraping           $firstGlobalScrape \n" +
                    "last scrape              $lastGlobalScrape \n" +
                    "scarping interval of     $numberOfScrapedDays days \n" +
                    "total number of scrapes: ${scrapeRepository.count()} \n" +
                    "average scrapes per day: ${scrapeRepository.count() / numberOfScrapedDays}\n" +
                    "minimum scrapes per day: $numberOfMinimumScrapes"
        )
    }

    fun printTableDiscountsOnlineAndOffline() {

        calculatePercentageDiscount()

        val columnWide = 10
        val columnNarrow = 8
        val space = " ".repeat(5)

        println()
        println("[1] # Adverts")
        println("[2] % Discounted Ads")
        println("[3] % Online Ads")
        println("[4] % Offline Ads")
        println("[5] % Discounted Online Ads")
        println("[6] % Discounted Offline Ads")
        println()

        val headerAndRowFormat = "%-${columnWide}s " +
                "%${columnNarrow}s " +
                "%${columnWide}s " +
                "%-${columnNarrow}s " +
                "%-${columnNarrow}s " +
                "%-${columnWide}s " +
                "%-${columnWide}s"


        println(
            headerAndRowFormat
                .format(
                    "Age Group",
                    "[1]",
                    "[2]",
                    "${space}[3]",
                    "${space}[4]",
                    "${space}[5]",
                    "${space}[6]"
                )
        )

        println(
            (headerAndRowFormat)
                .format(
                    "",
                    "#Ad",
                    "%Disc",
                    "${space}%Onl",
                    "${space}%Off",
                    " %Disc Onl",
                    " %Disc Off"
                )
        )

        val numberFormatStringNarrow = "%${columnNarrow}.2f"
        val numberFormatStringWide = "%${columnWide}.2f"

        listAgeGroupStats.forEach { item ->

            val string = headerAndRowFormat.format(
                item.ageGroup,
                item.count,
                numberFormatStringNarrow.format(item.percentageDiscount),
                numberFormatStringWide.format(item.percentageDiscountOnline),
                numberFormatStringNarrow.format(item.percentageDiscountOffline),
                numberFormatStringNarrow.format(item.percentageOnline),
                numberFormatStringNarrow.format(item.percentageOffline),
            ).replace(" 0.00", "     ")
                .replace(".00", "   ")


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
