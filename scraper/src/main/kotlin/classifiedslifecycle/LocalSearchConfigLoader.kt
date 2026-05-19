package classifiedslifecycle

import org.springframework.context.annotation.Profile
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Component

@Component
@Profile("test")
class LocalSearchConfigLoader(
    override val searchConfigRepository: SearchConfigRepository
) : SearchConfigLoader {


    val resolver = PathMatchingResourcePatternResolver()

    override fun getActiveConfigsString(): String {
        return resolver
            .getResource("classpath:search-configs/active-configs.json")
            .inputStream
            .bufferedReader()
            .readText()
    }

    override fun getJsonStrings(): List<String> {
        return resolver.getResources(
            "classpath:search-configs/*.json"
        )
            .filter { it.filename != "active-configs.json" }
            .map { it.inputStream.bufferedReader().readText() }
    }
}