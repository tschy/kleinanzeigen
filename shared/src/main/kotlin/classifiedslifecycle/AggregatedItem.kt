package classifiedslifecycle

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class AggregatedItem(

    val id: String,
    val firstScrape: Instant,
    val lastScrape: Instant,
    val scrapeCount: Long,
    val firstCreated: LocalDate?,

    ) {

    val ageDays = ChronoUnit.DAYS.between(
        firstCreated,
        lastScrape
            .atZone(
                ZoneId.of("Europe/Berlin")
            )
            .toLocalDate()
    )

    val ageGroup = if (ageDays <= 13) "$ageDays days"
    else if (ageDays <= 13 * 7) "${ageDays / 7} weeks"
    else "${ageDays / 30} months"

    //    fun isOnline(lastGlobalScrape: Instant?) = lastGlobalScrape == lastScrape
    fun isOnline(lastGlobalScrape: Instant?, tolerance: Long, timeFrame: ChronoUnit) =
        (lastScrape > lastGlobalScrape!!.minus(tolerance, timeFrame)
                && lastScrape < lastGlobalScrape.plus(tolerance, timeFrame))


    fun toDebugString() =
        "'${id}' : '${firstScrape}' : '${lastScrape}' : '${scrapeCount}': '${firstCreated}'"

}
