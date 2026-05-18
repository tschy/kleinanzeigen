package classifiedslifecycle

import org.springframework.context.annotation.Profile
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Component

@Component
@Profile("test")
class LocalSearchConfigLoader(
    override val searchConfigRepository: SearchConfigRepository
) : SearchConfigService {

    override fun getConfigsAsJsonStrings() : List<String> {
        val resolver = PathMatchingResourcePatternResolver()
        return resolver.getResources(
            "classpath:search-configs/*.json"
        ).map { it.inputStream.bufferedReader().readText() }
    }

}