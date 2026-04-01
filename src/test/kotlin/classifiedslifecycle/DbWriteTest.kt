package classifiedslifecycle

import classifiedslifecycle.model.Item
import classifiedslifecycle.model.ScrapeItem
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.springframework.test.context.ActiveProfiles
import java.io.File

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DbWriteTest {

    @Autowired
    lateinit var listingRepository: ListingRepository

    val results = mutableListOf<ScrapeItem>()

    @Test
    fun writeItemsToDB() {

        // arrange
        for (i in 0..27) {
            val text = File("src/test/resources/db_test_$i.htm")
                .readText()

            results.addAll(ItemExtractor().extract(text))
        }

        // act
        val itemService = ItemService(listingRepository)
        itemService.process(results)

        // assert

        for (item in results) {



            assertThat(listingRepository.findByIdId(item.id)).isNotEmpty
        }
    }
}