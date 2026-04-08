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
    fun process(scrapeItems: Set<ScrapeItem>) {


        // sort list so the most recent listing is the first entry
        val sortedItems = scrapeItems
            .sortedByDescending { it.created } // null ends up at the end of the list
            .distinctBy { it.id } // removes duplicates based on the id property, relevant for TOP ads

        sortedItems.forEach { scrapeItem ->
//            println(" ------ $scrapeItem")

            // get the most recent listing of the ad
            val itemSaved = listingRepository
                .findByIdId(scrapeItem.id).maxByOrNull { it.id.firstScrape }

            val newItem = Item.fromScrapeItem(scrapeItem)


////////////////////////////////////
//            if (itemSaved != null && itemSaved.matches(scrapeItem)) {
//
//                listingRepository
//                    .updateScrapeCount(
//                        itemSaved.scrapeCount + 1,
//                        lastScrape = scrapeItem.scrapeTime,
//                        itemSaved.id.id,
//                        itemSaved.id.firstScrape,
//
//                    )
//
//            } else {
//                listingRepository.save(Item.fromScrapeItem(scrapeItem))
//            }
////////////////////////////////////

            if (itemSaved != null) {

                if (itemSaved.matches(scrapeItem)) {

                    listingRepository
                        .updateScrapeCount(
                            itemSaved.scrapeCount + 1,
                            lastScrape = scrapeItem.scrapeTime,
                            itemSaved.id.id,
                            itemSaved.id.firstScrape,

                        )
                    return@forEach
                } else {
                    println("db item: " + itemSaved.toDebugString())
                    println("scraped item: " + newItem.toDebugString())
                    println("---")
                }
            }
            listingRepository.save(newItem)
        }
    }


}



