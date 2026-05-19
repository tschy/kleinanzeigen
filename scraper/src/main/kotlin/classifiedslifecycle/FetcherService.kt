package classifiedslifecycle

import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Service
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.beans.factory.annotation.Value

@Service
class FetcherService(

    @Value("\${github.token}")
    private var githubToken: String)
{
    val client = OkHttpClient()

    fun fetch(url: String): String {
        val request = Request.Builder().url(url).build();

        val body = client.newCall(request).execute().use { response ->
            response.body.string()
        }
        return body
    }

    fun fetchSearchConfigsFromGitHub(url: String): String {

        val logging = HttpLoggingInterceptor { message -> println(message) }
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val request = Request.Builder()
            .url(url)
            .header(
                "Accept",
                "application/vnd.github.raw+json"
            )            .header(
                "Authorization",
                "Bearer $githubToken"
            )
            .header(
                "X-GitHub-Api-Version",
                "2026-03-10"
            )
            .build();

        // response 200 is success https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status
        // https://docs.github.com/en/rest/using-the-rest-api/getting-started-with-the-rest-api?apiVersion=2026-03-10#http-method
        val body = client.newCall(request).execute().use { response ->
            response.body.string()
        }
        return body
    }

}