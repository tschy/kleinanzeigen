package classifiedslifecycle

import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(name = "listing")
open class Item(

    @EmbeddedId
    val id: ListingId,
    val lastScrape: Instant,
    val scrapeCount: Int,

    val title: String,

    @Column(columnDefinition = "numeric(10,2)")
    val price: Double?,

    @Column(columnDefinition = "numeric(10,2)")
    val oldPrice: Double?,

    val negotiable: Boolean,
    val created: LocalDate?,
) {

    fun toDebugString() =
        "'${id.id}' " +
                ": '${id.firstScrape}' " +
                ": '${lastScrape}'" +
                " : '${scrapeCount}'" +
                ": '${title}' " +
                ": '${price}' " +
                ": '${oldPrice}' " +
                ": '${negotiable}' " +
                ": '${created}' "

}