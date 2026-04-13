package classifiedslifecycle

import classifiedslifecycle.scraper.ItemService
import classifiedslifecycle.shared.Item
import classifiedslifecycle.scraper.ScrapeItem
import classifiedslifecycle.shared.ListingId
import classifiedslifecycle.shared.ListingRepository
import io.mockk.Runs
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month
import java.time.temporal.ChronoUnit
import kotlin.time.ExperimentalTime
import kotlin.time.Instant.Companion.parse
import kotlin.time.toJavaInstant
import org.assertj.core.api.Assertions.assertThat

class ItemServiceTestMockDb {

    val mockRepository = mockk<ListingRepository>()

    @OptIn(ExperimentalTime::class)
    val exampleInstant = parse("2020-01-01T00:00:00.00Z").toJavaInstant()
    val itemId = "123"

    val oldItemDb1 = Item(
        ListingId(itemId, exampleInstant.minus(3, ChronoUnit.DAYS)),
        exampleInstant.minus(3, ChronoUnit.DAYS),
        1,
        "schickes Rennrad",
        2.6,
        null,
        false,
        LocalDate.of(2026, Month.MARCH, 19),
    )

    val oldItemDb2 = Item(

        ListingId(itemId, exampleInstant.minus(2, ChronoUnit.DAYS)),
        exampleInstant.minus(2, ChronoUnit.DAYS),
        1,
        "schickes Rennrad, fast wie neu",
        2.6,
        null,
        false,
        LocalDate.of(2026, Month.MARCH, 19),
    )

    // ein neues
    val newItem1 = ScrapeItem(
        itemId,
        exampleInstant,
        "super schickes Rennrad",
        2.6,
        null,
        false,
        LocalDate.of(2026, Month.MARCH, 19),
        )// ein neues

    val newItem2 = ScrapeItem(
        itemId,
        exampleInstant,
        "schickes Rennrad, fast wie neu",
        2.6,
        null,
        false,
        LocalDate.of(2026, Month.MARCH, 19),
    )

    @Test
    fun updateItem() {
        every { mockRepository.findByIdId(any()) } returns listOf(oldItemDb1, oldItemDb2)
        every { mockRepository.updateScrapeCount(any(), any(), any(), any()) } just Runs

        val itemService = ItemService(mockRepository)

        itemService.process(setOf(newItem2))

        verify(exactly = 1) { mockRepository.findByIdId(any()) }

        verify(exactly = 1) { mockRepository.updateScrapeCount(
            oldItemDb2.scrapeCount + 1,
            newItem2.scrapeTime,
            oldItemDb2.id.id,
            oldItemDb2.id.firstScrape
        ) }
        confirmVerified(mockRepository)
    }

    @Test
    fun saveNewItem() {
        every { mockRepository.findByIdId(any()) } returns listOf(oldItemDb1, oldItemDb2)
        every { mockRepository.save(any()) } answers { firstArg() }

        val itemService = ItemService(mockRepository)

        itemService.process(setOf(newItem1))

        verify(exactly = 1) { mockRepository.save(any()) }
        verify(exactly = 1) { mockRepository.findByIdId(any()) }

        verify(exactly = 1) {
            mockRepository.save(match {
                it.id.id == newItem1.id &&
                        it.title == newItem1.title &&
                        it.price == newItem1.price &&
                        it.negotiable == newItem1.negotiable &&
                        it.created == newItem1.created &&
                        it.scrapeCount == 1
            })
        }
        confirmVerified(mockRepository)
    }

    @Test
    fun sortedListTest() {
        val list = listOf(1, 3, 2).sortedBy { it }
        assertThat(list.last()).isEqualTo(3)

        val list2 = listOf(
            exampleInstant,
            exampleInstant.minus(3, ChronoUnit.DAYS)
        ).sortedBy { it }

        assertThat(list2.last()).isEqualTo(exampleInstant)
    }
}
