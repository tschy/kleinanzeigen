package classifiedslifecycle

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.io.ClassPathResource
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
class SearchConfigService(
    val searchConfigRepository: SearchConfigRepository,
) {

    fun readConfigs(): List<SearchConfig> {
        val files = ClassPathResource("search-configs")
            .file
            .listFiles { f -> f.extension == "json" }

        val configs = mutableListOf<SearchConfig>()

        files?.forEach { f ->
            val resource = ClassPathResource("search-configs/${f.name}")

            val mapper = ObjectMapper()
            val configRead = mapper
                .readValue<SearchConfig>(resource.inputStream)

            val existing = searchConfigRepository
                .findByName(configRead.name)

            KotlinLogging.logger {}.info { existing?.toDebugString() }

            val config = existing ?: searchConfigRepository
                .save(configRead)

            configs.add(config)
        }
        return configs
    }
}