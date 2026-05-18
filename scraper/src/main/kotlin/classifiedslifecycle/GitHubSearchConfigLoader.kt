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
) : SearchConfigService {

    override fun getConfigsAsJsonStrings(): List<String> {

        println(
            fetcherService.fetchWithGitHubHeader(
                "https://api.github.com" +
                        "/repos" +
                        "/tschy" +
                        "/kleinanzeigen" +
                        "/contents" +
                        "/shared/src/main/resources/search-configs"
            )
        )

        val jsonString = fetcherService.fetchWithGitHubHeader(
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

        val b = ArrayList<String>()

        urlList.forEach { url: String ->
            b.add(fetcherService.fetch(url))
        }

        b.forEach { it -> println(it) }

       return b
    }
}