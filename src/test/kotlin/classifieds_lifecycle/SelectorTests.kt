package classifieds_lifecycle

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDate
import java.time.Month


class SelectorTests {

    val today = LocalDate.of(2026, Month.MARCH, 19)
    val yesterday = today.minusDays(1)
    val undertest = ItemExtractor(today)


    @Test
    fun testSelector() {

        val text = File("src/test/resources/rennraeder20260319.htm")
            .readText()
        //   ?: error("File not found!")
        // println(text)

        val results = undertest.extract(text)
        assertThat(results).hasSize(27)
        assertThat(results[2].created).isEqualTo(today)
        val yesterdaysItem = results.first { it.id == "3356665275" }
        assertThat(yesterdaysItem.created).isEqualTo(yesterday)
        val oldItem = results.first { it.id == "3356785624" }
        assertThat(oldItem.created).isEqualTo(LocalDate.of(2026, Month.MARCH, 17))
        val oldItem2 = results.first { it.id == "3356211495" }
        assertThat(oldItem2.created).isEqualTo(LocalDate.of(2025, Month.DECEMBER, 7))
    }
}