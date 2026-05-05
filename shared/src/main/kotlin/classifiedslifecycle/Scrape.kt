package classifiedslifecycle

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import jakarta.persistence.Id
import java.time.Instant

@Entity
@Table(name = "scrape")
open class Scrape(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val scrapeTime: Instant,
) {
    fun toDebugString() =
        "'$id' : '$scrapeTime' "
}