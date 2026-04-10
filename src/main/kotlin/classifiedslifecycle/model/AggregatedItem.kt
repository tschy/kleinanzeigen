package classifiedslifecycle.model

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

    val ageGroup = if (ageDays < 14) "$ageDays days" else "${ageDays / 7} weeks"

    fun isOnline(lastGlobalScrape: Instant) = lastGlobalScrape == lastScrape
}
