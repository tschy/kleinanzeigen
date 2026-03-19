package classifieds_lifecycle

import classifieds_lifecycle.model.Item
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.time.LocalDate
import kotlin.text.contains

class FetcherService(

) {
    val client = OkHttpClient()

    fun fetch(url: String): String {
        val request = Request.Builder().url(url).build();

        val body = client.newCall(request).execute().use { response ->
            response.body.string()
        }
        return body
    }

}

// fun fetchAndStore(url: String) {
//    val items = fetch(url)
// TODO: write to database
//}

