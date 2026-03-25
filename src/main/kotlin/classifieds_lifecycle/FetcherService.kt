package classifieds_lifecycle

import okhttp3.OkHttpClient
import okhttp3.Request

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

