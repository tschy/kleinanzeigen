package classifiedslifecycle.model

import java.time.Instant
import java.time.LocalDate

data class ScrapeItem(
    val id: String,
    val scrapeTime: Instant,

    val title: String,
    val price: Double?,
    val negotiable: Boolean,
    val created: LocalDate?,
    )