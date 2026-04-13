package classifiedslifecycle.shared

import jakarta.persistence.Embeddable
import java.io.Serializable
import java.time.Instant

@Embeddable
data class ListingId(
    val id : String = "",
    val firstScrape : Instant = Instant.MIN,
) : Serializable