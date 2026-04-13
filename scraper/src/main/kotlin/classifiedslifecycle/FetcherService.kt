package classifiedslifecycle

import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Service

@Service
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