package classifiedslifecycle

import classifiedslifecycle.model.ScrapeItem
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.springframework.test.context.ActiveProfiles
import java.io.File

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemServiceTestSpringBoot {

    @Autowired
    lateinit var listingRepository: ListingRepository

    val results = mutableSetOf<ScrapeItem>()


    // delete stale entries in the db from previous test runs
    @BeforeEach
    fun setup() {
        listingRepository.deleteAll()
    }


    @Test
    fun processTest() {

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
        for (scrapeItem in results) {
            assertThat(listingRepository.findByIdId(scrapeItem.id)).isNotEmpty

            //println("Processing id: ${scrapeItem.id}")

            val savedItemList = listingRepository.findByIdId(scrapeItem.id)
            for (savedItem in savedItemList) {
                println("${scrapeItem.id} -- ${savedItem.id} -- ${savedItem.scrapeCount}  -- ${savedItem.title}")
                assertThat(scrapeItem.id).isEqualTo(savedItem.id.id)
            }
        }
    }
}