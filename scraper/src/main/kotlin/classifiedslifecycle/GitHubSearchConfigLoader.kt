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

    val mapper = jacksonObjectMapper()

    override fun getJsonStrings(): List<String> {

        val jsonString = fetcherService.fetchSearchConfigsFromGitHub(
            "https://api.github.com" +
                    "/repos" +
                    "/tschy" +
                    "/kleinanzeigen-configs" +
                    "/contents/"
        )

        val metadataList: List<Map<String, Any>> =
            mapper.readValue(jsonString)

        return metadataList
            .filter { it["name"] != "active-configs.json" }
            .mapNotNull { it["download_url"] as? String }
            .map { fetcherService.fetch(it) }
//            .mapNotNull { it["download_url"] as? String }

//        val body = ArrayList<String>()
//
//        urlList.forEach { url: String ->
//            body.add(fetcherService.fetch(jsonString))
//        }
//        return body
    }

    override fun getActiveConfigsString(): String {
        return fetcherService.fetchSearchConfigsFromGitHub(
            "https://api.github.com" +
                    "/repos" +
                    "/tschy" +
                    "/kleinanzeigen-configs" +
                    "/contents" +
                    "/active-configs.json"
        )

    }
}