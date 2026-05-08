package classifiedslifecycle

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.io.ClassPathResource
import com.fasterxml.jackson.module.kotlin.readValue
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

            println("${f.name} ")
            val resource = ClassPathResource("search-configs/${f.name}")

            println("RAW CONTENT: ${resource.inputStream.bufferedReader().readText()}")

            val mapper = ObjectMapper()
            val configRead = mapper
                .readValue<SearchConfig>(resource.inputStream)

            println("READING FILE: ${resource.uri}")

            println(configRead.name)

            val existing = searchConfigRepository
                .findByName(configRead.name)

            println(existing?.toDebugString())

            val config = existing ?: searchConfigRepository
                .save(configRead)

            println(config.toDebugString())

            configs.add(config)
        }
        return configs
    }
}