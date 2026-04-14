package classifiedslifecycle

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.springframework.test.context.ActiveProfiles
import java.io.File

// Integration test (@SpringBootTest) using a separate test database specified in application-test.properties.

@SpringBootTest
//@ActiveProfiles("test-m") // determines into which db the data from this class is written
//@Transactional  // rollback changes
class ItemServiceTestSpringBoot {

    @Autowired
    lateinit var listingRepository: ListingRepository

    @Autowired
    lateinit var itemService: ItemService

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
            val text = File("../misc/html-debug/db_test_$i.htm")
                .readText()

            results.addAll(ItemExtractor().extract(text))
        }


        val savedItemListBeforeScrape = listingRepository.findAll()

        val map = HashMap<String, Int>()

        for (savedItemBeforeScrape in savedItemListBeforeScrape) {
            map[savedItemBeforeScrape.id.id] = savedItemBeforeScrape.scrapeCount
            println("db item: " + savedItemBeforeScrape.toDebugString())
        }


        // act
        itemService.process(results)

        val dedupedResults = results.sortedByDescending { it.created }.distinctBy { it.id }
        // assert
        for (scrapeItem in dedupedResults) {
            assertThat(listingRepository.findByIdId(scrapeItem.id)).isNotEmpty

            println("Processing id: ${scrapeItem.id}")

            val savedItemList = listingRepository.findByIdId(scrapeItem.id)
            for (savedItem in savedItemList) {
                println("${scrapeItem.id} -- ${savedItem.id} -- ${savedItem.scrapeCount}  -- ${savedItem.title}")
                println(map.getOrDefault(scrapeItem.id, 0))
                assertThat(scrapeItem.id).isEqualTo(savedItem.id.id)
                assertThat(savedItem.scrapeCount).isEqualTo(map.getOrDefault(scrapeItem.id, 0) + 1)
            }
        }
    }
}