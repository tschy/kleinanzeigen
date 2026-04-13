package classifiedslifecycle

import classifiedslifecycle.scraper.ItemExtractor
import classifiedslifecycle.shared.Item
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class ItemExtractorTest {

    val itemExtractor = ItemExtractor()

    @Test
    fun `extracts items from htm and creates item objects`() {

        val pageText = File("src/test/resources/data/rennraeder20260319.htm")
            .readText()

        val itemsScraped = itemExtractor.extract(pageText)

        assertThat(itemsScraped).hasSize(27)


        val items = mutableListOf<Item>()

        for (scrapeItem in itemsScraped) {
            items.add(Item.fromScrapeItem(scrapeItem))
        }

        assertThat(items).hasSize(27)
        println(items[0])
        assertThat(items[0].title).isEqualTo("‼\uFE0FVintage Rennräder ab 350 Euro - VB‼\uFE0F Peugeot, Bianchi etc.")

        for (item in items) {
            assertThat(item.scrapeCount == 1)
        }
    }
}