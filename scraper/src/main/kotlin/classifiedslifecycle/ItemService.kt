package classifiedslifecycle

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ItemService(
    val listingRepository: ListingRepository
) {
    private val logger = KotlinLogging.logger {}


    // TODO is that a good place for Transactional, we don't want to roll back 10000 items
    @Transactional
    fun process(scrapeItems: Set<ScrapeItem>) {

        // sort list so the most recent listing is the first entry
        val sortedItems = scrapeItems
            .sortedByDescending { it.created } // null ends up at the end of the list
            .distinctBy { it.id } // removes duplicates based on the id property, relevant for TOP ads

        sortedItems.forEach { scrapeItem ->



            // get the most recent listing of the ad
            val itemSaved = listingRepository
                .findByIdId(scrapeItem.id).maxByOrNull { it.id.firstScrape }

            val newItem = toItem(scrapeItem)


            if (itemSaved != null) {

                if (itemSaved.matches(scrapeItem)) {

//                    logger.info { "updateScrapeCount: "  }
                    logger.debug { "Item already exists" + itemSaved.toDebugString() }

                    listingRepository
                        .updateScrapeCount(
                            itemSaved.scrapeCount + 1,
                            lastScrape = scrapeItem.scrapeTime,
                            itemSaved.id.id,
                            itemSaved.id.firstScrape,

                            )
                    return@forEach
                } else {

                    logger.info { "db item: " + itemSaved.toDebugString() }
                    logger.info { "scraped item: " + newItem.toDebugString() }
                    logger.info { "---" }
                }
            }
            listingRepository.save<Item>(newItem)
        }
    }


}