package classifieds_lifecycle

import org.jsoup.Jsoup

fun main(args: Array<String>) {
    val itemExtractor = ItemExtractor()
    val fetcherService = FetcherService()

    var n = 1

    do {
        val url = "https://www.kleinanzeigen.de/s-fahrraeder/herren/12309/seite:${n}/rennrad/k0c217l3411r10+fahrraeder.art_s:herren"

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
    }
        while (soup.select(".pagination-next").isNotEmpty());
}
