package classifieds_lifecycle

import classifieds_lifecycle.model.Item
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.io.println
import kotlin.text.contains

// change: today was passed into the ItemExtractor but not used
class ItemExtractor(
    val today: LocalDate = LocalDate.now(), // change from  val today: LocalDate,
    // default value is actual today, under test conditions test date is being passed into the function
) {

    val dateRegex = Regex("\\d\\d.\\d\\d.\\d\\d\\d\\d")

    fun parseDate(s: String): LocalDate? {

        if (s.contains("Heute")) return today // change from return LocalDate.now
        else if (s.contains("Gestern")){
            return today.minusDays(1) // change from return LocalDate.now.minusDays
        }
        else {
            val dateStr = dateRegex.find(s)?.value ?: return null

            try {
                return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                // add DateTimeFormatter.ofPattern("dd.MM.yyyy")
            } catch (ex: Exception) {
                return null
            }
        }
    }

    val numberRegex = Regex("[\\d.,]+")

    fun extract(body: String): List<Item> {
        val soup = Jsoup.parse(body)
        val articles = soup.select("article.aditem")
        println("Found ${articles.size} articles")

        return articles.map { article: Element ->
            val id = article.attr("data-adid")
            val title = article.select("h2").text()
            val priceString = article.select(".aditem-main--middle--price-shipping").text()
            val price = numberRegex.find(priceString)?.value?.toDouble() ?: Double.NaN
            val negotiable = priceString.contains("VB")
            val created = article.select(".aditem-main--top--right").text()
            Item(id, title, price, negotiable, parseDate(created))
        }
    }




}

// fun fetchAndStore(url: String) {
//    val items = fetch(url)
// TODO: write to database
//}

