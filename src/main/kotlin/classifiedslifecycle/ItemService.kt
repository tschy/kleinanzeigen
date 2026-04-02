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

            // sorting order is descending


            // neustes finden und entscheiden ob merge oder neues machen
            val listForItem = listingRepository
                .findByIdId(scrapeItem.id)
                .sortedBy { it.id.firstScrape }

            if (listForItem.isNotEmpty() && listForItem.size == 1) {
                val item = listForItem.first()
                listingRepository
                    .updateScrapeCount(
                        (item.scrapeCount + 1),
                        item.id.id,
                        item.id.firstScrape
                    )
            } else listingRepository.save(Item.fromScrapeItem(scrapeItem))
        }
    }
}




