package classifieds_lifecycle

import classifieds_lifecycle.model.Item
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.time.LocalDate
import kotlin.text.contains

class FetcherService() {
    val client = OkHttpClient()

    fun fetch(url: String): List<Item> {
        val request = Request.Builder().url(url).build();

        val body = client.newCall(request).execute().use { response ->
            response.body.string()
        }
        return extract(body)
    }

    val dateRegex = Regex("\\d\\d.\\d\\d.\\d\\d\\d\\d")
    fun parseDate(s: String): LocalDate? {
        if (s.contains("Heute")) return LocalDate.now()
        else if (s.contains("Gestern")) return LocalDate.now().minusDays(1)
        else {
            val dateStr = dateRegex.find(s)?.value ?: return null
            return LocalDate.parse(dateStr)
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

