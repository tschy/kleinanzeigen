package classifieds_lifecycle

import classifieds_lifecycle.model.Item
import classifieds_lifecycle.model.SearchConfig
import org.jsoup.Jsoup

    class Paginator(val fetcherService: FetcherService,
                    val itemExtractor: ItemExtractor) {

        fun run(config: SearchConfig): MutableList<Item> {

        var n = 1
        val allItems = mutableListOf<Item>()

        do
        {
            val url = "https://www.kleinanzeigen.de/s-${config.category}/${config.art}/${config.plz}/seite:${n}/${config.searchTerm}/k0c217l3411r${config.radius}+${config.category}.art_s:${config.art}"
            println(url)

            // get HTML content for this page
            val body = fetcherService.fetch(url)
            val soup = Jsoup.parse(body)

            // test if pagination-next marker is present
            if (soup.select(".pagination-next").isNotEmpty()) {
                n += 1
                allItems.addAll(itemExtractor.extract(body))

                Thread.sleep(1000)
            }

        } while (soup.select(".pagination-next").isNotEmpty())
        return allItems
    }
}