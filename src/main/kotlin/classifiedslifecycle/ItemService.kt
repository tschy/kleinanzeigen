package classifiedslifecycle

import classifiedslifecycle.model.Item
import classifiedslifecycle.model.ScrapeItem
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ItemService(
    val listingRepository: ListingRepository
) {
    @Transactional
    fun process(scrapeItems: List<ScrapeItem>) {
        scrapeItems.forEach { scrapeItem ->
            println(" ------ ${scrapeItem}")

            // get the most recent listing of the ad
            val itemSaved = listingRepository
                .findByIdId(scrapeItem.id).maxByOrNull { it.id.firstScrape }

            if (itemSaved != null && itemSaved.matches(scrapeItem)) {

                listingRepository
                    .updateScrapeCount(
                        (itemSaved.scrapeCount + 1),
                        itemSaved.id.id,
                        itemSaved.id.firstScrape
                    )
            }
            else { listingRepository.save(Item.fromScrapeItem(scrapeItem)) }
        }
    }
}



