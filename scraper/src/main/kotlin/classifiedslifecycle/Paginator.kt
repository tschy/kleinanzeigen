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
                Thread.sleep(3000)
            }
            page += 1
        } while (soup.select(".pagination-next").isNotEmpty())
//        } while (page < 2)
        return allItems
    }

    private fun buildUrl(config: SearchConfig, page: Int): String {
        var s = "https://www.kleinanzeigen.de/"

        if (config.category != "") s += "s-${config.category}/"
        if (config.art != "") s += "${config.art}/"
        if (config.plz != "") s += "${config.plz}/"
        s += "seite:${page}/"
        s += config.searchTerm
        config.radius?.let { s += "$it+" }
        if ((config.category != "") && (config.art != "")) s += "${config.category}.art_s:" + config.art

//        s = "https://www.kleinanzeigen.de/s-fahrraeder/herren/12309/seite:1/rennrad/k0c217l3411r10+fahrraeder.art_s:herren" // geht
//        s = "https://www.kleinanzeigen.de/s-fahrraeder/seite:1/tern-verge-s8i/k00+fahrraeder.art_s" // geht nicht
//        s = "https://www.kleinanzeigen.de/s-fahrraeder/seite:1/tern-verge-s8i/k0" // geht
//        s = "https://www.kleinanzeigen.de/s-fahrraeder/seite:1/tern-verge-s8i/k00+" // geht
        return s

    }

//        return "https://www.kleinanzeigen.de/s-" +
//                "${config.category}/" +
//                "${config.art}/" +
//                "${config.plz}/" +
//                "seite:${page}/" +
//                config.searchTerm +
//                "${config.radius}+" +
//                "${config.category}.art_s:" +
//                config.art
//    }
}

//https://www.kleinanzeigen.de/s-fahrraeder/s-tern-verge-s8i/k0
//https://www.kleinanzeigen.de/s-fahrraeder///seite:1/tern-verge-s8i/k0/0+fahrraeder.art_s

//https://www.kleinanzeigen.de/s-fahrraeder/herren/10115/seite:1/rennrad/k0c217l3411r/30+fahrraeder.art_s:herren