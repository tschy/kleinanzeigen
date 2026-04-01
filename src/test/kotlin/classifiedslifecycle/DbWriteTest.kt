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

            val dbList = listingRepository.findByIdId(item.id)
            for (dbItem in dbList) {
                println("${item.id} -- ${dbItem.id} -- ${dbItem.scrapeCount}  -- ${dbItem.title}")
                assertThat(item.id).isEqualTo(dbItem.id.id)
            }


//            3276376174-- ListingId (id = 3276376174, firstScrape = 2026-04-01T14:25:18.423511Z)--1
//            3276369228-- ListingId (id = 3276369228, firstScrape = 2026-04-01T14:25:18.424064Z)--2
//            3276369228-- ListingId (id = 3276369228, firstScrape = 2026-04-01T14:25:18.424064Z)--2
//            3276189143-- ListingId (id = 3276189143, firstScrape = 2026-04-01T14:25:18.426006Z)--1
//            3275912940-- ListingId (id = 3275912940, firstScrape = 2026-04-01T14:25:18.426688Z)--1
//            3363871659-- ListingId (id = 3363871659, firstScrape = 2026-04-01T14:25:17.992720Z)--1
//            3148178158-- ListingId (id = 3148178158, firstScrape = 2026-04-01T14:25:17.768967Z)--2
//            3363654134-- ListingId (id = 3363654134, firstScrape = 2026-04-01T14:25:17.996416Z)--1

//kleinanzeigen=# select * from listing where scrape_count = 4;
//     id     |        first_scrape        |        last_scrape         | scrape_count |                             title                              | price | negotiable | created
//------------+----------------------------+----------------------------+--------------+----------------------------------------------------------------+-------+------------+---------
// 3148178158 | 2026-04-01 14:40:05.143453 | 2026-04-01 14:40:05.143453 |            4 | ‼️Vintage Rennräder ab 350 Euro - VB‼️ Peugeot, Bianchi etc.     |   350 | t          |
// 3200218098 | 2026-04-01 14:40:05.111367 | 2026-04-01 14:40:05.111367 |            4 | CUBE AGREE C:62 SLX, RH 58 cm liquidblue‘n‘blue Carbon Rennrad |  2600 | f          |
//(2 rows)

//            ------> appear as "TOP" ads twice in the list

//3338321253 -- ListingId(id=3338321253, firstScrape=2026-04-01T14:40:05.245364Z) -- 3  -- 28 er Rennrad mit 24 Gänge
//3367796743 -- ListingId(id=3367796743, firstScrape=2026-04-01T14:40:05.246973Z) -- 4  -- Specialized S-Works Crux 2025 Sram Red XPLR 13s 6.9kg
//3367796743 -- ListingId(id=3367796743, firstScrape=2026-04-01T14:40:05.246973Z) -- 4  -- Specialized S-Works Crux 2025 Sram Red XPLR 13s 6.9kg
//3367789962 -- ListingId(id=3367789962, firstScrape=2026-04-01T14:40:05.248425Z) -- 3  -- Rennrad retro

//2907740692 -- ListingId(id=2907740692, firstScrape=2026-04-01T14:40:05.900214Z) -- 3  -- Fondriest Over X Campagnolo Chorus SLX Vintage Rennrad 56
//2888023614 -- ListingId(id=2888023614, firstScrape=2026-04-01T14:40:05.900676Z) -- 3  -- Team Lotto Jumbo Langarmtrikot FILA Größe: S
//2870825275 -- ListingId(id=2870825275, firstScrape=2026-04-01T14:40:05.901134Z) -- 3  -- Rennrad Guerciotti - Aero Campagnolo Delta C Record 55cm Cinelli
//2847560878 -- ListingId(id=2847560878, firstScrape=2026-04-01T14:42:51.348643Z) -- 1  -- Rennrad Bridgestone Diamond Road Shimano 600 Arabesque 28''
//2812586537 -- ListingId(id=2812586537, firstScrape=2026-04-01T14:42:51.350574Z) -- 1  -- Team Rabobank Cool Max Radsocken L/XL weiß
//2790552117 -- ListingId(id=2790552117, firstScrape=2026-04-01T14:42:51.351901Z) -- 1  -- Fahrrad Retro Touring Lenker

//3087218547 -- ListingId(id=3087218547, firstScrape=2026-04-01T14:40:05.890299Z) -- 3  -- Rennrad Carbon Laufräder 28" 10f. Shimano wie neu inkl.Kassette
//3084464289 -- ListingId(id=3084464289, firstScrape=2026-04-01T14:40:05.890740Z) -- 4  -- Carbon rennrad
//3084464289 -- ListingId(id=3084464289, firstScrape=2026-04-01T14:40:05.890740Z) -- 4  -- Carbon rennrad
//3072179605 -- ListingId(id=3072179605, firstScrape=2026-04-01T14:40:05.891144Z) -- 3  -- Rennrad KS Cycling 28 Zoll


//3320346333 -- ListingId(id=3320346333, firstScrape=2026-04-01T14:40:05.797201Z) -- 3  -- BMC Roadmachine X TWO 2023 2024 Rival AXS eTap Redshift Stem
//3319898757 -- ListingId(id=3319898757, firstScrape=2026-04-01T14:42:51.216599Z) -- 1  -- Ritchey Comp Carbon Sattelstütze 31.6
//3319627944 -- ListingId(id=3319627944, firstScrape=2026-04-01T14:40:05.798210Z) -- 3  -- Willier Triestina GTR Team Rennrad
        }
    }
}