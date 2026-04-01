package classifiedslifecycle

import classifiedslifecycle.model.Item
import classifiedslifecycle.model.ScrapeItem
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ItemService(
    val listingRepository: ListingRepository
) {
    @Transactional
    fun process(scrapeItems: MutableList<ScrapeItem>) {
        scrapeItems.forEach { scrapeItem ->
            println(" ------ ${scrapeItem}")

            val listForItem = listingRepository.findByIdId(scrapeItem.id)
            if (listForItem.isNotEmpty() && listForItem.size == 1) {
                val item = listForItem.first()
                listingRepository.updateScrapeCount((item.scrapeCount + 1), item.id.id, item.id.firstScrape)
            } else listingRepository.save(Item.fromScrapeItem(scrapeItem))
        }
    }
}




