package classifiedslifecycle

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.Instant

@Entity
@Table(name = "scrape")
open class Scrape(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val scrapeTime: Instant,

    @Column
    val foundItems: Int,

    @ManyToOne
    @JoinColumn(name = "search_config_id")
    val searchConfig: SearchConfig,


) {
    fun toDebugString() =
        "'$id' : '$scrapeTime' : '$foundItems : '$searchConfig"
}