package classifiedslifecycle.scraper

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class ItemExtractor(
    val today: LocalDate = LocalDate.now(), // change from  val today: LocalDate,
    // default value is actual today, under test conditions test date is being passed into the function
) {

    val dateRegex = Regex("\\d\\d.\\d\\d.\\d\\d\\d\\d")

    fun parseDate(s: String): LocalDate? {

        if (s.contains("Heute")) return today // change from return LocalDate.now
        else if (s.contains("Gestern")) {
            return today.minusDays(1) // change from return LocalDate.now.minusDays
        } else {
            val dateStr = dateRegex.find(s)?.value ?: return null

            try {
                return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                // add DateTimeFormatter.ofPattern("dd.MM.yyyy")
            } catch (ex: Exception) {
                return null
            }
        }
    }

    val priceRegex = Regex("[\\d.,]+")

    fun extract(body: String): List<ScrapeItem> {

        val soup = Jsoup.parse(body)
        val articles = soup.select("article.aditem")
        println("Found ${articles.size} articles")

        return articles.map { article: Element ->
            val id = article.attr("data-adid")
            val scrapeTime = Instant.now()
            val title = article.select("h2").text()
            val priceString = article.select(
                ".aditem-main--middle--price-shipping").text()
            val price = priceRegex.find(priceString)
                ?.value
                ?.replace(".", "")
                ?.replace(",", ".")
                ?.toDouble()
            val oldPriceString = article.select(
                ".aditem-main--middle--price-shipping--old-price").text()
            val oldPrice = priceRegex.find(oldPriceString)
                ?.value
                ?.replace(".", "")
                ?.replace(",", ".")
                ?.toDouble()
            val negotiable = priceString.contains("VB")
            val created = parseDate(article.select(
                ".aditem-main--top--right")
                .text())

            ScrapeItem(id, scrapeTime, title, price, oldPrice, negotiable, created)
        }
    }
}