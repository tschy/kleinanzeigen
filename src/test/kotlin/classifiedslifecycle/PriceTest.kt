package classifiedslifecycle

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDate
import java.time.Month


class PriceTest {

    val today = LocalDate.of(2026, Month.MARCH, 19)
    val undertest = ItemExtractor(today)


    @Test
    fun testSelector() {

        val text = File("src/test/resources/rennraeder20260319_parse_price.htm")
            .readText()
        //   ?: error("File not found!")
        // println(text)

        val results = undertest.extract(text)
        val yesterdaysItem = results.first { it.id == "3356665275" }
        assertThat(yesterdaysItem.price).isEqualTo(580.0)
        val oldItem = results.first { it.id == "3356685496" }
        assertThat(oldItem.price).isEqualTo(2650.0)
        val oldItem2 = results.first { it.id == "3356785624" }
        assertThat(oldItem2.price).isEqualTo(10.01)
    }
}