package classifiedslifecycle


import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.time.Month
import java.time.temporal.ChronoUnit
import kotlin.time.ExperimentalTime
import kotlin.time.Instant.Companion.parse
import kotlin.time.toJavaInstant

// Integration test (@SpringBootTest) using a separate test database specified in application-test.properties.

@SpringBootTest
@ActiveProfiles("test-m")
//@Transactional  // rollback changes
class AnalyserTestM {

    @Autowired
    lateinit var listingRepository: ListingRepository

    @OptIn(ExperimentalTime::class)
    val exampleInstant = parse("2020-01-01T00:00:00.00Z").toJavaInstant()
    val itemId = "123"

    val item1 = Item(
        ListingId(itemId, exampleInstant.minus(3, ChronoUnit.DAYS)),
        exampleInstant.minus(3, ChronoUnit.DAYS),
        1,
        "schickes Rennrad",
        2.6,
        null,
        false,
        LocalDate.of(2026, Month.MARCH, 19),
    )

    val item2 = Item(

        ListingId(itemId, exampleInstant.minus(2, ChronoUnit.DAYS)),
        exampleInstant.minus(2, ChronoUnit.DAYS),
        1,
        "schickes Rennrad, fast wie neu",
        2.6,
        null,
        false,
        LocalDate.of(2026, Month.MARCH, 19),
    )

    val item3 = Item(
        id = ListingId(itemId, exampleInstant),   // ListingId with firstScrape = exampleInstant
        lastScrape = exampleInstant,
        scrapeCount = 1,                           // first scrape
        title = "super schickes Rennrad",
        price = 2.6,
        oldPrice = null,
        negotiable = false,
        created = LocalDate.of(2026, Month.MARCH, 19)
    )

    val item4 = Item(
        id = ListingId(itemId, exampleInstant),   // same ListingId if same item
        lastScrape = exampleInstant,
        scrapeCount = 1,
        title = "schickes Rennrad, fast wie neu",
        price = 2.6,
        oldPrice = null,
        negotiable = true,
        created = LocalDate.of(2026, Month.MARCH, 19)
    )
    @Test
    fun analyseTest() {

        listingRepository.saveAll(listOf(item1, item2, item3, item4))

        val analyser = Analyser(listingRepository)
        analyser.analyse()
    }
}