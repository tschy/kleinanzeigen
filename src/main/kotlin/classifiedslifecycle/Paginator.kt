package classifiedslifecycle

import classifiedslifecycle.model.ScrapeItem
import classifiedslifecycle.model.SearchConfig
import org.jsoup.Jsoup
import java.io.File
import java.time.Instant


class Paginator(val fetcherService: FetcherService,
                    val itemExtractor: ItemExtractor) {

        fun run(config: SearchConfig): Set<ScrapeItem> {

        var n = 1
        val allItems = mutableSetOf<ScrapeItem>()

        do
        {
            val url = "https://www.kleinanzeigen.de/s-${config.category}/${config.art}/${config.plz}/seite:${n}/${config.searchTerm}/k0c217l3411r${config.radius}+${config.category}.art_s:${config.art}"
            println("paginator ${url}")



            // get HTML content for this page
            val body = fetcherService.fetch(url)
            val soup = Jsoup.parse(body)

            File("src/test/resources/${url.replace("/","_")}_${Instant.now()}.htm").writeText(body)

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