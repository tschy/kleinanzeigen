package classifiedslifecycle

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

@Component
@Profile("!test")
class GitHubSearchConfigLoader(
    override val searchConfigRepository: SearchConfigRepository,
    val fetcherService: FetcherService
) : SearchConfigLoader {

    override fun getJsonStrings(): List<String> {

        val jsonString = fetcherService.fetchSearchConfigsFromGitHub(
            "https://api.github.com" +
                    "/repos" +
                    "/tschy" +
                    "/kleinanzeigen" +
                    "/contents" +
                    "/shared/src/main/resources/search-configs"
        )

        val mapper = jacksonObjectMapper()

        val metadataList: List<Map<String, Any>> =
            mapper.readValue(jsonString)

        val urlList = metadataList.mapNotNull { it["download_url"] as? String }

        val body = ArrayList<String>()

        urlList.forEach { url: String ->
            body.add(fetcherService.fetch(url))
        }
       return body
    }

    override fun getActiveConfigsString(): String {
        TODO("Not yet implemented")
    }
}