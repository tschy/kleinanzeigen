package classifiedslifecycle

import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Service
import okhttp3.logging.HttpLoggingInterceptor

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

    fun fetchWithLogging(url: String): String {

        val logging = HttpLoggingInterceptor { message -> println(message) }
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val request = Request.Builder()
            .url(url)
            .header(
                "User-Agent",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (HTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"
            )
            .build();

        val body = client.newCall(request).execute().use { response ->
            response.body.string()
        }
        return body
    }

}