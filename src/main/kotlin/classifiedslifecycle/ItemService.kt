package classifiedslifecycle

import classifiedslifecycle.model.Item
import classifiedslifecycle.model.ScrapeItem
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ItemService(
    val listingRepository: ListingRepository
) {

    fun process(scrapeItems: MutableList<ScrapeItem>) {
        // check db against scrapeItems:
        //      for each item:
        // does it exist in the database? - use listingId
        // if yes, increase scrapeCount in respective row
        // if not, create new db entry

        scrapeItems.forEach { scrapeItem ->
            println(" ------ ${scrapeItem}")

            val listForItem = listingRepository.findById_Id(scrapeItem.id)
            if (listForItem.isNotEmpty() && listForItem.size == 1) {
                val item = listForItem.first()
                listingRepository.updateScrapeCount((item.scrapeCount + 1), item.id.id, item.id.firstScrape)
            } else  listingRepository.save(Item.fromScrapeItem(scrapeItem))
        }
    }
}




