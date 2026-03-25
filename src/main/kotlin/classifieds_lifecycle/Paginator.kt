package classifieds_lifecycle

import classifieds_lifecycle.model.SearchConfig
import org.jsoup.Jsoup

    class Paginator(val fetcherService: FetcherService,
                    val itemExtractor: ItemExtractor) {

        fun run(config: SearchConfig) {

        var n = 1

        do
        {
            val url = "https://www.kleinanzeigen.de/s-${config.category}/${config.art}/${config.plz}/seite:${n}/${config.searchTerm}/k0c217l3411r${config.radius}+${config.category}.art_s:${config.art}"

            // get HTML content for this page
            val body = fetcherService.fetch(url)
            val soup = Jsoup.parse(body)

            // test if pagination-next marker is present
            if (soup.select(".pagination-next").isNotEmpty()) {
                n += 1
                val items = itemExtractor.extract(body)
                items.forEach { println(it) }
                Thread.sleep(1000)
            }
        } while (soup.select(".pagination-next").isNotEmpty());
    }
}