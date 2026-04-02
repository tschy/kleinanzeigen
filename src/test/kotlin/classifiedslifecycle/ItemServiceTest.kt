package classifiedslifecycle

import classifiedslifecycle.model.Item
import classifiedslifecycle.model.ScrapeItem
import io.mockk.confirmVerified
import io.mockk.every
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

class ItemServiceTest {

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
        false,
        LocalDate.of(2026, Month.MARCH, 19),
    )

    val oldItemDb2 = Item(

        ListingId(itemId, exampleInstant.minus(2, ChronoUnit.DAYS)),
        exampleInstant.minus(2, ChronoUnit.DAYS),
        1,
        "schickes Rennrad, fast wie neu",
        2.6,
        false,
        LocalDate.of(2026, Month.MARCH, 19),
    )

    // ein neues
    val newItem = ScrapeItem(
        itemId,
        exampleInstant,
        "super schickes Rennrad",
        2.6,
        false,
        LocalDate.of(2026, Month.MARCH, 19),
    )

    // TODO auch bei neuem Titel neuen Eintrag - erst Test, dann Feature implementieren bis der Test erfolgreich ist
    // TODO sorted testen, unklar welches Ergebnis zurueckgegeben wird

    @Test
    fun updateItem() {
        every { mockRepository.findByIdId(any()) } returns listOf(oldItemDb1, oldItemDb2)

        val itemService = ItemService(mockRepository)

        itemService.process(setOf(newItem))

        verify(exactly = 1) { mockRepository.save(any()) }
        confirmVerified(mockRepository)
    }

    @Test
    fun sortedListTest() {
        val list = listOf(1, 3, 2).sortedBy {it}
        assertThat(list.last()).isEqualTo(3)

        val list2 = listOf(
            exampleInstant,
            exampleInstant.minus(3, ChronoUnit.DAYS)
        ).sortedBy {it}

        assertThat(list2.last()).isEqualTo(exampleInstant)
    }
}
