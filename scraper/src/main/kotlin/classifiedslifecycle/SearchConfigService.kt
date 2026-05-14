package classifiedslifecycle

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Service

@Service
class SearchConfigService(
    val searchConfigRepository: SearchConfigRepository,
) {

    fun readConfigs(): List<SearchConfig> {
        val resolver = PathMatchingResourcePatternResolver()
        val resources = resolver.getResources(
            "classpath:search-configs/*.json")

        val configs = mutableListOf<SearchConfig>()

        resources.forEach { resource ->

            val mapper = jacksonObjectMapper()
            val configRead = mapper
                .readValue<SearchConfig>(resource.inputStream)

            val existing = searchConfigRepository
                .findByNameAndCategoryAndArtAndPlzAndSearchTermAndRadius(
                    configRead.name,
                    configRead.category,
                    configRead.art,
                    configRead.plz,
                    configRead.searchTerm,
                    configRead.radius
                )

            KotlinLogging.logger {}.info { " using config ${existing?.toDebugString()}" }

            val config = existing ?: searchConfigRepository
                .save(configRead)

            configs.add(config)
        }
        return configs
    }
}