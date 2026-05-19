package classifiedslifecycle

import io.github.oshai.kotlinlogging.KotlinLogging
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import java.io.File
import java.time.Instant

@Component
class Paginator(
    val fetcherService: FetcherService,
    val itemExtractor: ItemExtractor
) {

    fun run(config: SearchConfig): Set<ScrapeItem> {

        var page = 1
        val allItems = mutableSetOf<ScrapeItem>()

        do {

            val url = buildUrl(config, page)

            KotlinLogging.logger {}.info { url }

            // get HTML content for this page
            val body = fetcherService.fetch(url)
            val soup = Jsoup.parse(body)

            File("misc/html-debug/${url.replace("/", "_")}_${Instant.now()}.htm")
                .also { it.parentFile.mkdirs() }
                .writeText(body)

            allItems.addAll(itemExtractor.extract(body, page))

            // test if pagination-next marker is present
            if (soup.select(".pagination-next").isNotEmpty()) {
                Thread.sleep(1000)
            }
            page += 1
        } while (soup.select(".pagination-next").isNotEmpty())
//        } while (page < 2)
        return allItems
    }

    private fun buildUrl(config: SearchConfig, page: Int): String {
        return "https://www.kleinanzeigen.de/s-" +
                "${config.category}/" +
                "${config.art}/" +
                "${config.plz}/seite:" +
                "${page}/" +
                "${config.searchTerm}/" +
                "${config.radius}+" +
                "${config.category}.art_s:" +
                config.art
    }
}

